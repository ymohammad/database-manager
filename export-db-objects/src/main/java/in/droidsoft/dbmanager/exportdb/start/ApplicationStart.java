/*******************************************************************************************************************************
ApplicationStart.java

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

package in.droidsoft.dbmanager.exportdb.start;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import in.droidsoft.dbmanager.exportdb.config.AppContext;
import in.droidsoft.dbmanager.exportdb.rdbms.executor.ConnectionManager;
import in.droidsoft.dbmanager.exportdb.rdbms.executor.DBScriptExecutor;
import in.droidsoft.dbmanager.exportdb.rdbms.model.SQLStatement;
import in.droidsoft.dbmanager.exportdb.store.DatabasePropsStore;
import in.droidsoft.dbmanager.exportdb.store.DatabaseScriptStore;
import in.droidsoft.dbmanager.exportdb.store.sqlscript.FileSourceSQLScriptStore;
import in.droidsoft.dbmanager.exportdb.util.AppConstants;
import in.droidsoft.dbmanager.exportdb.util.AppUtils;

/**
* Class ApplicationStart
*/
@Component
public class ApplicationStart implements ApplicationListener<ContextRefreshedEvent> {

	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("******************************************************************************");
		System.out.println("\t\tAPPLICATION IS STARTING");
		System.out.println("******************************************************************************");
		//System.out.println(event);
		start();
	}
	private void start() {
		logMsg("Starting the Program Execution - " + LocalDateTime.now());
		Scanner scan = null;
		try {
			scan = new Scanner(System.in);
			userInput("Enter the Data Directory :");
			String dataDirectory = scan.nextLine();
			if (dataDirectory == null || dataDirectory.trim().length() == 0) {
				logErrorMsg("Data Directory required to proceed futher. Terminating the program.");
				System.exit(0);
			}
			startApplicationProcess(dataDirectory);
			logMsg("Execution Completes - " + LocalDateTime.now());
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
	}
	private void userInput(String msg) {
		System.out.println(msg);
	}
	private void startApplicationProcess(String dataDirectory) {
		ConnectionManager connManager = null;
		try {
			logMsg("Given data directory path :" + dataDirectory);
			StartupCheck.doStartupCheck(dataDirectory);
			
			AppContext appContext = AppContext.getInstance();
			appContext.setDataDirectoryPath(dataDirectory);
			DatabasePropsStore propStore = new DatabasePropsStore();
			connManager = new ConnectionManager(propStore.getDatabaseProps());
			Connection dbConnection = connManager.getConnection();
			if (dbConnection == null) {
				logErrorMsg("Unable to establish DB Connection");
				return;
			}
			logMsg("Database Connection Establish Successfully.");
			logMsg("Loading the DB Script to be executed...Please wait.");
			DatabaseScriptStore dbStore = new FileSourceSQLScriptStore(AppConstants.DB_STARTUP_SCRIPT_FILE_NAME);
			List<SQLStatement> dbScript = dbStore.getDBScript();
			if (dbScript == null || dbScript.size() == 0) {
				logErrorMsg("No Script Lines to execute.");
				return;
			}
			logMsg("Total SQL Statements loaded :" + dbScript.size());
			logMsg("Start executing the SQL Script.");
			DBScriptExecutor executor = new DBScriptExecutor();
			executor.executeDBScript(dbConnection, dbScript);
		} catch (SQLException e) {
			e.printStackTrace();
			logErrorMsg(e);
		} finally {
			if (connManager != null) {
				connManager.closeDbConnection();
			}
		}
	}

	private void logErrorMsg(Exception e) {
		String msg = e.getMessage() != null ? e.getMessage() : e.toString();
		AppUtils.logErrorMsg(msg);
	}

	private void logErrorMsg(String msg) {
		AppUtils.logErrorMsg(msg);
	}

	private void logMsg(String msg) {
		AppUtils.logMsg(msg);
	}
}
