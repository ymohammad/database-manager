/*******************************************************************************************************************************
DBExportManager.java

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

package in.droidsoft.dbmanager.exportdb.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.droidsoft.dbmanager.exportdb.config.AppContext;
import in.droidsoft.dbmanager.exportdb.io.DBObjectFileUtils;
import in.droidsoft.dbmanager.exportdb.rdbms.model.ExportObjectModel;
import in.droidsoft.dbmanager.exportdb.rdbms.model.ExportObjectTypeModel;
import in.droidsoft.dbmanager.exportdb.rdbms.model.ExportOptionsModel;
import in.droidsoft.dbmanager.exportdb.rdbms.model.SQLStatement;
import in.droidsoft.dbmanager.exportdb.rdbms.model.SchemaBasedExportObjectModel;
import in.droidsoft.dbmanager.exportdb.service.ExportObjectService;
import in.droidsoft.dbmanager.exportdb.store.ApplicationStore;
import in.droidsoft.dbmanager.exportdb.util.AppConstants;
import in.droidsoft.dbmanager.exportdb.util.AppUtils;

/**
* Class DBExportManager
*/
@Component
public class DBExportManager {

	@Autowired
	private AppContext appContext;
	
	private List<ExportObjectService> exportServiceList;
	
	@Autowired
	public DBExportManager(List<ExportObjectService> exportServiceList) {
		this.exportServiceList = exportServiceList;
	}
	
	public boolean startExportProcess() {
		ApplicationStore appStore = new ApplicationStore(appContext);
		
		String databaseType = appStore.getExportProps().getDatabaseType();
		logMsg("Exporting for Database Type :" + databaseType);
		if (AppUtils.isEmpty(databaseType)) {
			logErrorMsg("Database Type is required. Please add it in the property file " + appContext.getDataDirectoryPath() + File.separator + AppConstants.EXPORT_PROPERTIES_FILE_NAME);
			return false;
		}
		SQLStatement expAllObjStmt = appStore.getExportAllObjSqlStatemnt();
		if (expAllObjStmt == null || AppUtils.isEmpty(expAllObjStmt.getScriptLine())) {
			logErrorMsg("No Select Query is available to fetch Objects list to be exported from teh database. Please add the query in " + AppConstants.EXPORT_OBJECTS_LIST_SELECT_QUERY_FILE_NAME);
			return false;
		}
		if (exportServiceList == null || this.exportServiceList.size() == 0) {
			logErrorMsg("No Export Service is available to perform Export.");
			return false;
		}
		ExportObjectService exportService = this.getExportServiceForDatabaseType(databaseType);
		if (exportService == null) {
			logErrorMsg("No Export Service available for the Database Type " + databaseType);
			return false;
		}
		
		List<ExportObjectModel> exportObjectsList = this.getExportingObjectsList(exportService, expAllObjStmt);
		if (exportObjectsList == null || exportObjectsList.size() == 0) {
			logErrorMsg("No Objects found to be exported with the given Query. Nothing to export.");
			return false;
		}
		
		logMsg("Number of Objects to Export :" + exportObjectsList.size());
		List<SchemaBasedExportObjectModel> schemaBasedObjsList = exportService.prepareSchemaBasedExportModelList(exportObjectsList);
		boolean isContinue = this.printAndConfirmSchemaExport(schemaBasedObjsList);
		if (isContinue) {
			this.performDBObjectsExport(appStore, exportService, schemaBasedObjsList);
		}
		//System.out.println("DBExportManager.startExportProcess() schemaBasedObjsList:" + schemaBasedObjsList);
		return true;
	}
	

	private void performDBObjectsExport(ApplicationStore appStore, ExportObjectService exportService, List<SchemaBasedExportObjectModel> schemaBasedObjsList) {
		logMsg("Starting the Export operation. ");
		String messagePad = "\t";
		List<ExportObjectModel> exportFailedList = new ArrayList<ExportObjectModel>();
		try {
			ExportOptionsModel exportOption = appStore.getExportProps().getExportOptions();
			exportService.setExportObjectsProperties(exportOption);
			
			for (SchemaBasedExportObjectModel schemaObj : schemaBasedObjsList) {
				String ownerName = schemaObj.getOwner().toUpperCase();
				List<ExportObjectTypeModel> exportObjectTypeList = schemaObj.getExportObjectTypeList();
				logMsg("Exporting objects of Owner - " + ownerName);
				
				if (exportObjectTypeList == null || exportObjectTypeList.size() == 0 ) {
					logMsg("No Objects available to export for " + ownerName);
					continue;
				}
				logMsg("Number of Object types to export " + exportObjectTypeList.size());
				
				for (ExportObjectTypeModel eachObjType : exportObjectTypeList) {
					String objectTypeName = eachObjType.getObjectType();
					List<ExportObjectModel> objectList = eachObjType.getObjectList();
					if (AppUtils.isEmpty(objectTypeName)) {
						continue;
					}
					logMsg("Exporting type " + objectTypeName + ", Total Objects :" + (objectList != null ? objectList.size() : 0));
					
					if (objectList != null) {
						for (ExportObjectModel eachModel: objectList) {
							String objName = eachModel.getObjectName();
							if (AppUtils.isEmpty(objName)) {
								continue;
							}
							objName = objName.toUpperCase();
							objectTypeName = objectTypeName.toUpperCase();
							logMsg("Exporting Object :" + objName);
							
							String dbScript = exportService.getDBObjectDDLScriptText(ownerName, objectTypeName, objName);
							if (AppUtils.isEmpty(dbScript)) {
								logErrorMsg(messagePad + "Failed to fetch the DDL Script");
								exportFailedList.add(eachModel);
								continue;
							}
							
							logMsg(messagePad + "DDL Script fetched successfully. Number of characters in the Script - " + dbScript.length());
							String objParentDirpath = this.prepareObjectParentDirPath(ownerName, objectTypeName);
							String objFileName = objName + "." + exportOption.getObjectExtension(objectTypeName);
							logMsg(messagePad + "Exporting the DDL to file - " + objParentDirpath + File.separator + objFileName);
							boolean isExported = DBObjectFileUtils.createDBObjectFile(objParentDirpath, objFileName, dbScript);
							if (isExported) {
								logMsg(messagePad + "Exporting the DDL to file SUCCESSFUL.");
							} else {
								exportFailedList.add(eachModel);
								logErrorMsg(messagePad + "Exporting the DDL to file FAILED.");
							}
						}
					}
				}
				
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	private String prepareObjectParentDirPath(String ownerName, String objectType) {
		String outputDirPath = this.appContext.getDataDirectoryPath() + File.separator + AppConstants.DB_OBJECTS_OUTPUT_DIR_NAME;
		outputDirPath += File.separator + ownerName.toUpperCase();
		outputDirPath += File.separator + objectType.toUpperCase();
		return outputDirPath;
	}
	private boolean printAndConfirmSchemaExport(List<SchemaBasedExportObjectModel> schemaBasedObjsList) {
		return true;
	}
	
	private ExportObjectService getExportServiceForDatabaseType(String databaseType) {
		for (ExportObjectService eachService: this.exportServiceList) {
			if (eachService.getServiceType() != null && eachService.getServiceType().equalsIgnoreCase(databaseType)) {
				return eachService;
			}
		}
		return null;
	}

	private List<ExportObjectModel> getExportingObjectsList(ExportObjectService exportService,
			SQLStatement expAllObjStmt) {
		String sqlQuery = expAllObjStmt.getScriptLine();
		return exportService.getExportObjectList(sqlQuery);
	}

	private void logErrorMsg(String msg) {
		AppUtils.logErrorMsg(msg);
	}

	private void logMsg(String msg) {
		AppUtils.logMsg(msg);
	}
	
}
