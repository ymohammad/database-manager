/*******************************************************************************************************************************
ExportOptionsModel.java

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
Date   : Jul 24, 2022

Last modified by : ymohammad
Last modified on : Jul 24, 2022

*******************************************************************************************************************************/

package in.droidsoft.dbmanager.exportdb.rdbms.model;

import java.util.HashMap;
import java.util.Map;

import in.droidsoft.dbmanager.exportdb.util.AppConstants;
import in.droidsoft.dbmanager.exportdb.util.AppUtils;
import lombok.Data;

/**
* Class ExportOptionsModel
*/
@Data
public class ExportOptionsModel {
	public ExportOptionsModel() {
		this.loadValidObjectTypeData();
	}
	private boolean tableSpace;
	private boolean emitSchema;
	private boolean pretty;
	private boolean storage;
	private boolean segmentAttribute;
	private boolean sqlTerminator;
	private boolean physicalProperties;
	
	private static Map<String, String> fileExtensionMap = new HashMap<String, String>();
	private static Map<String, String> validObjectType = new HashMap<String, String>();
	private static Map<String, String> objectTypeAndDirectoryNameMap = new HashMap<String, String>();
	
	public void addObjectExtension(String objectType, String extension) {
		if (AppUtils.isEmpty(objectType) || AppUtils.isEmpty(extension)) {
			return;
		}
		String mapKey = this.prepareAndgetExtensionMapKey(objectType);
		
		if (fileExtensionMap == null) {
			fileExtensionMap = new HashMap<String, String>();
		}
		fileExtensionMap.put(mapKey, extension);
	}
	
	public String getObjectExtension(String objectType) {
		if (AppUtils.isEmpty(objectType) || fileExtensionMap == null || fileExtensionMap.size() == 0) {
			return AppConstants.DEFAULT_FILE_EXTENSION;
		}
		String key = this.prepareAndgetExtensionMapKey(objectType);
		String fileExt = fileExtensionMap.get(key);
		if (!AppUtils.isEmpty(fileExt)) {
			return fileExt;
		}
				
		return AppConstants.DEFAULT_FILE_EXTENSION;
	}
	public String getValidObjectType(String objectType) {
		if (AppUtils.isEmpty(objectType) || validObjectType == null || validObjectType.size() == 0) {
			return objectType;
		}
		String valObjType = validObjectType.get(objectType);
		if (!AppUtils.isEmpty(valObjType)) {
			return valObjType;
		}
				
		return objectType;
	}
	public String prepareAndgetExtensionMapKey(String objectType) {
		return objectType.trim().replaceAll(" ", "_").toUpperCase();
	}
	private void loadValidObjectTypeData() {
		if (AppConstants.VALID_OBJECT_TYPES_NAMES_ARR != null && AppConstants.VALID_OBJECT_TYPES_NAMES_ARR.length > 0) {
			for (String eachType : AppConstants.VALID_OBJECT_TYPES_NAMES_ARR) {
				if (eachType.indexOf("=") != -1 ) {
					String[] keyValArr = eachType.split("=");
					if (keyValArr.length != 2) continue;
					
					validObjectType.put(keyValArr[0], keyValArr[1]);
				}
			}
		}
	}

	public String getObjectTypeAndDirectoryName(String objectType) {
		if (AppUtils.isEmpty(objectType)) {
			return "NOT_EXIST";
		}
		if (objectTypeAndDirectoryNameMap == null || objectTypeAndDirectoryNameMap.size() == 0) {
			return this.getDefaultFileName(objectType);
		}
		String directoryName = objectTypeAndDirectoryNameMap.get(objectType);
		if (!AppUtils.isEmpty(directoryName)) {
			return directoryName;
		}
				
		return this.getDefaultFileName(objectType);
	}
	
	private String getDefaultFileName(String objectType) {
		return objectType.trim().replaceAll(" ", "_").toUpperCase();
	}

	public void addObjectTypeAndDirectoryName(String objectType, String propValue) {
		if (AppUtils.isEmpty(objectType) || AppUtils.isEmpty(propValue)) {
			return;
		}
		
		if (objectTypeAndDirectoryNameMap == null) {
			objectTypeAndDirectoryNameMap = new HashMap<String, String>();
		}
		objectTypeAndDirectoryNameMap.put(objectType, propValue);
	}
}
