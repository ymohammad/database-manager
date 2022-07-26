/*******************************************************************************************************************************
DatabasePropsStore.java

Copyright � 2022, DroidSoft. All rights reserved.
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
Date   : Jul 19, 2022

Last modified by : ymohammad
Last modified on : Jul 19, 2022

*******************************************************************************************************************************/

package in.droidsoft.dbmanager.exportdb.store;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;

import in.droidsoft.dbmanager.exportdb.config.AppContext;
import in.droidsoft.dbmanager.exportdb.rdbms.model.DBProps;
import in.droidsoft.dbmanager.exportdb.util.AppConstants;
import in.droidsoft.dbmanager.exportdb.util.AppUtils;

public class DatabasePropsStore {

	private DBProps dbProps = null;
	
	@Autowired
	private AppContext appContext = null;
	
	public DatabasePropsStore() {
		this.loadDBProperties();
	}
	
	public DBProps getDatabaseProps() {
		DBProps clonedCopy = new DBProps(this.dbProps);
		return clonedCopy;
	}
	private void loadDBProperties() {
		File dbPropsFile = AppUtils.getResourceFile(this.appContext.getDataDirectoryPath(), AppConstants.DB_PROPERTIES_FILE_NAME);
		FileReader reader = null;
		try {
			reader = new FileReader(dbPropsFile);
			Properties props = new Properties();
			props.load(reader);
			
			String driverClassName = props.getProperty("source.driverClassName");
			String dbUrl = props.getProperty("source.jdbcUrl");
			String userName = props.getProperty("source.username");
			String password = props.getProperty("source.password");
			
			this.dbProps = new DBProps(userName, password, dbUrl, driverClassName);
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
}
