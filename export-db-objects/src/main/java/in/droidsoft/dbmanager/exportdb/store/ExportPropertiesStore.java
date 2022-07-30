/*******************************************************************************************************************************
ExportPropertiesStore.java

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

package in.droidsoft.dbmanager.exportdb.store;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import in.droidsoft.dbmanager.exportdb.config.AppContext;
import in.droidsoft.dbmanager.exportdb.props.ExportProp;
import in.droidsoft.dbmanager.exportdb.rdbms.model.ExportOptionsModel;
import in.droidsoft.dbmanager.exportdb.util.AppConstants;
import in.droidsoft.dbmanager.exportdb.util.AppUtils;
import lombok.Getter;

/**
* Class ExportPropertiesStore
*/
@Getter
public class ExportPropertiesStore {
	private AppContext appContext;
	
	private ExportProp exportProps = null;
	
	public ExportPropertiesStore(AppContext appContext) {
		this.appContext = appContext;
		this.loadExportProps();
	}

	private void loadExportProps() {
		File exoprtPropFile = AppUtils.getResourceFile(this.appContext.getDataDirectoryPath(), AppConstants.EXPORT_PROPERTIES_FILE_NAME);
		FileReader reader = null;
		try {
			reader = new FileReader(exoprtPropFile);
			Properties props = new Properties();
			props.load(reader);
			
			String databaseType = props.getProperty("databaseType");
			ExportOptionsModel expoOptModel = this.prepareAndGetExpoOptModel(props);
			this.updateFileExtensionInfo(expoOptModel, props);
			this.updateObjectTypeAndDirectoryNameInfo(expoOptModel, props);
			this.exportProps = new ExportProp(databaseType, expoOptModel);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * Updates the File extension information in the map. It will be used while creation the extension files.
	 * @param expoOptModel
	 * @param props
	 */
	private void updateFileExtensionInfo(ExportOptionsModel expoOptModel, Properties props) {
		for (String objType: AppConstants.STANDARD_OBJECT_TYPES_ARR) {
			expoOptModel.addObjectExtension(objType, props.getProperty(expoOptModel.prepareAndgetExtensionMapKey(objType)));
		}
	}

	private void updateObjectTypeAndDirectoryNameInfo(ExportOptionsModel expoOptModel, Properties props) {
		for (String objType: AppConstants.STANDARD_OBJECT_TYPES_ARR) {
			String objTypeKey = expoOptModel.prepareAndgetExtensionMapKey(objType);
			String propValue = props.getProperty(AppConstants.DIR_NAME_PREFIX + objTypeKey);
			if (AppUtils.isEmpty(propValue)) {
				continue;
			}
			expoOptModel.addObjectTypeAndDirectoryName(objType, propValue);
		}
	}
	
	/**
	 * Prepares and get Export Options Model.
	 * @param props
	 * @return
	 */
	private ExportOptionsModel prepareAndGetExpoOptModel(Properties props) {
		ExportOptionsModel returnObj = new ExportOptionsModel();
		returnObj.setTableSpace(AppUtils.convertToBoolean(props.getProperty("INCLUDE_TABLE_SPACE")));
		returnObj.setEmitSchema(AppUtils.convertToBoolean(props.getProperty("INCLUDE_SCHEMA_NAME")));
		returnObj.setPhysicalProperties(AppUtils.convertToBoolean(props.getProperty("INCLUDE_PHYSICAL_PROPERTIES")));
		returnObj.setPretty(AppUtils.convertToBoolean(props.getProperty("EXPORT_SCRIPT_PRETTY")));
		returnObj.setSegmentAttribute(AppUtils.convertToBoolean(props.getProperty("INCLUDE_SEGMENT_ATTRIBUTES")));
		returnObj.setSqlTerminator(AppUtils.convertToBoolean(props.getProperty("END_SQL_SCRIPT_WITH_TERMINATOR")));
		returnObj.setStorage(AppUtils.convertToBoolean(props.getProperty("INCLUDE_STORAGE_INFO")));
		return returnObj;
	}
}
