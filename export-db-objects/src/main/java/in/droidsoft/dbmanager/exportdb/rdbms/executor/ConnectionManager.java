/*******************************************************************************************************************************
ConnectionManager.java

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
Date   : Jul 19, 2022

Last modified by : ymohammad
Last modified on : Jul 19, 2022

*******************************************************************************************************************************/

package in.droidsoft.dbmanager.exportdb.rdbms.executor;

import java.sql.Connection;
import java.sql.DriverManager;

import in.droidsoft.dbmanager.exportdb.util.AppUtils;

import in.droidsoft.dbmanager.exportdb.rdbms.model.DBProps;

public class ConnectionManager {

	private Connection dbConnection;
	private DBProps dbProps;

	public ConnectionManager(DBProps dbProps) {
		this.dbProps = dbProps;
	}

	private boolean isValidConnection(Connection connection) {
		try {
			if (connection != null && !connection.isClosed())
				return true;
			else
				return false;
		} catch (Exception e) {
			return false;
		}
	}

	public Connection getConnection() {

		try {
			if (this.isValidConnection(dbConnection)) {
				return this.dbConnection;
			}
			Class.forName(this.dbProps.getDriverClassName());
			this.dbConnection = DriverManager.getConnection(this.dbProps.getDbUrl(), dbProps.getUserName(), dbProps.getPassword());
			return this.dbConnection;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void closeDbConnection() {
		try {
			if (this.dbConnection != null) {
				AppUtils.logMsg("Closing the DB connection.");
				dbConnection.close();
			} else {
				AppUtils.logMsg("Database connection already closed or not created.");
			}
		} catch (Exception e) {
			AppUtils.logErrorMsg("Error while closing db connection " + e.getMessage());
		}
	}
}
