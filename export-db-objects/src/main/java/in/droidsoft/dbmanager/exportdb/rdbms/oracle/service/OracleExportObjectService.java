/*******************************************************************************************************************************
OracleExportObjectService.java

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
Date   : Jul 21, 2022

Last modified by : ymohammad
Last modified on : Jul 21, 2022

*******************************************************************************************************************************/

package in.droidsoft.dbmanager.exportdb.rdbms.oracle.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.droidsoft.dbmanager.exportdb.rdbms.model.ExportObjectModel;
import in.droidsoft.dbmanager.exportdb.rdbms.model.ExportObjectOptionI;
import in.droidsoft.dbmanager.exportdb.rdbms.model.oracle.OracleExportOptionModel;
import in.droidsoft.dbmanager.exportdb.rdbms.oracle.repository.AllObjectRepsoitory;
import in.droidsoft.dbmanager.exportdb.rdbms.oracle.repository.ObjectScriptTextRepository;
import in.droidsoft.dbmanager.exportdb.service.ExportObjectService;
import in.droidsoft.dbmanager.exportdb.util.AppConstants;

/**
* Class OracleExportObjectService
*/
@Service
public class OracleExportObjectService extends ExportObjectService {
	
	@Autowired
	private AllObjectRepsoitory allObjRepo;
	
	@Autowired
	private ObjectScriptTextRepository objScriptRepo;
	
	public static final String SERVICE_TYPE = AppConstants.ORACLE_SERVICE_TYPE;

	@Override
	public List<ExportObjectModel> getExportObjectList(String sqlQuery) {
		List<Tuple> allObjectsEntity = this.allObjRepo.getAllObjectsEntity(sqlQuery);
		//List<ExportObjectModel> returnList = new ArrayList<ExportObjectModel>();
		
		if (allObjectsEntity == null || allObjectsEntity.size() == 0) {
			return null;
		}
		
		List<ExportObjectModel> returnList = allObjectsEntity.stream()
				.map(eachTuble -> {
					ExportObjectModel newEntity = new ExportObjectModel();
					newEntity.setObjectName(eachTuble.get("OBJECT_NAME", String.class));
					newEntity.setObjectType(eachTuble.get("OBJECT_TYPE", String.class));
					newEntity.setOwner(eachTuble.get("OWNER", String.class));
					return newEntity;
				})
				.collect(Collectors.toList());
		
//		for (Tuple eachEntity : allObjectsEntity) {
//			ExportObjectModel newEntity = new ExportObjectModel();
//			//BeanUtils.copyProperties(eachEntity, newEntity);
//			newEntity.setObjectName(eachEntity.getOBJECT_NAME());
//			newEntity.setObjectType(eachEntity.getOBJECT_TYPE());
//			newEntity.setOwner(eachEntity.getOWNER());
//			returnList.add(newEntity);
//		}
		return returnList;
	}

	@Override
	public String getServiceType() {
		return SERVICE_TYPE;
	}

	@Override
	public String getDBObjectDDLScriptText(String owner, String objectType, String objectName) {
		try {
			return this.objScriptRepo.getDBObjectScriptText(objectName, objectType, objectName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setExportObjectsProperties(ExportObjectOptionI options) {
		OracleExportOptionModel oraOption = (OracleExportOptionModel)options;
		this.objScriptRepo.setExportProperties(oraOption);
	}
}
