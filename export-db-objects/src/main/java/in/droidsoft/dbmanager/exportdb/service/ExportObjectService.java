/*******************************************************************************************************************************
ExportObjectService.java

Copyright © 2022, DroidSoft. All rights reserved.
The Programs (which include both the software and documentation) contain proprietary information of DroidSoft;
they are provided under a license agreement containing restrictions on use and disclosure and are also protected by
copyright, patent and other intellectual and industrial property law. Reverse engineering, disassembly or de-compilation of
the programs is prohibited.
Program Documentation is licensed for use solely to support the deployment of the Programs and not for any other
purpose.
The information contained in this document is subject to change without notice. If you find any problems in the
documentation, please report them to us in writing. DroidSoft does not warrant that this document is error free.
Except as may be expressly permitted in your license agreement for these Programs, no part of these Programs may be
reproduced or transmitted in any form or by any means, electronic or mechanical, for any purpose, without the express
written permission of DroidSoft.

Author : ymohammad
Date   : Jul 22, 2022

Last modified by : ymohammad
Last modified on : Jul 22, 2022

*******************************************************************************************************************************/

package in.droidsoft.dbmanager.exportdb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.droidsoft.dbmanager.exportdb.rdbms.model.ExportObjectModel;
import in.droidsoft.dbmanager.exportdb.rdbms.model.ExportObjectTypeModel;
import in.droidsoft.dbmanager.exportdb.rdbms.model.SchemaBasedExportObjectModel;

/**
* Class ExportObjectService
*/
public abstract class ExportObjectService {
	/**
	 * Service TYpe implementation.
	 * @return
	 */
	public abstract String getServiceType();
	
	/**
	 * Gives the list of Exporting Objects.
	 * @param sqlQuery
	 * @return
	 */
	public abstract List<ExportObjectModel> getExportObjectList(String sqlQuery);
	
	public List<SchemaBasedExportObjectModel> prepareSchemaBasedExportModelList(List<ExportObjectModel> expObjList) {
		if (expObjList == null | expObjList.size() == 0) return null;
		
		HashMap<String, SchemaBasedExportObjectModel> schemaMap = new HashMap<String, SchemaBasedExportObjectModel>();
		expObjList.forEach(eom -> {
			String owner = eom.getOwner();
			String objectType = eom.getObjectType();
			
			SchemaBasedExportObjectModel eachSchema = schemaMap.get(owner) != null ? schemaMap.get(owner) : new SchemaBasedExportObjectModel();
			eachSchema.setOwner(owner);
			
			List<ExportObjectTypeModel> exportObjTypeList = eachSchema.getExportObjectTypeList() != null ? eachSchema.getExportObjectTypeList() : new ArrayList<ExportObjectTypeModel>();
			ExportObjectTypeModel objTypeModel = null;
			
			for(ExportObjectTypeModel et : exportObjTypeList) {
				if (et.getObjectType().equalsIgnoreCase(objectType)) {
					objTypeModel = et;
					break;
				}
			}
			if (objTypeModel == null) {
				objTypeModel = new ExportObjectTypeModel();
				objTypeModel.setObjectType(objectType);
				objTypeModel.setObjectList(new ArrayList<ExportObjectModel>());
				exportObjTypeList.add(objTypeModel);
			}
			
			objTypeModel.getObjectList().add(eom);
			
			eachSchema.setExportObjectTypeList(exportObjTypeList);
			schemaMap.put(owner, eachSchema);
		});
		List<SchemaBasedExportObjectModel> resultList = new ArrayList<SchemaBasedExportObjectModel>(schemaMap.values());
		return resultList;
	}
}

