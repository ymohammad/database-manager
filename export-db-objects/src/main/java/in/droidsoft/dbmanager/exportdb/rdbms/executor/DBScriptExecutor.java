/*******************************************************************************************************************************
DBScriptExecutor.java

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

package in.droidsoft.dbmanager.exportdb.rdbms.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import in.droidsoft.dbmanager.exportdb.rdbms.model.SQLStatement;
import in.droidsoft.dbmanager.exportdb.rdbms.model.type.SQLStatementType;
import in.droidsoft.dbmanager.exportdb.util.AppUtils;

public class DBScriptExecutor {

	public void executeDBScript(Connection dbConnection, List<SQLStatement> sqlScript) throws SQLException {
		if (sqlScript == null || sqlScript.size() == 0) {
			System.out.println("[ERROR] No Script lines to execute.");
		}
		dbConnection.setAutoCommit(false);
		for (SQLStatement statement : sqlScript) {
			boolean res = executeSQLStatement(dbConnection, statement);
			this.writeToExecutionLog((res == true ? "Success" : "Failure" ) + " :" + statement.getScriptLine() );
		}
		dbConnection.commit();
	}
	
	private boolean executeSQLStatement(Connection dbConnection, SQLStatement sqlStatement) {
		PreparedStatement preStmt = null;
		try {
			preStmt = dbConnection.prepareStatement(sqlStatement.getScriptLine());
			int executeUpdate = preStmt.executeUpdate();
			if (sqlStatement.getStatementType() == SQLStatementType.DDL) {
				return true;
			}
			if (sqlStatement.getStatementType() == SQLStatementType.DML) {
				if (executeUpdate > 0) {
					return true;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (preStmt != null) {
				try {
					preStmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	private void writeToExecutionLog(String msg) {
		AppUtils.logMsg(msg);
	}
}
