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

import java.time.LocalDateTime;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import in.droidsoft.dbmanager.exportdb.config.AppContext;
import in.droidsoft.dbmanager.exportdb.manager.DBExportManager;
import in.droidsoft.dbmanager.exportdb.util.AppUtils;

/**
* Class ApplicationStart
*/
@Component
public class ApplicationStart implements ApplicationListener<ContextRefreshedEvent> {

	@Value("${application_data_directory}")
	private String appDataDir;
	
	@Autowired
	private AppContext appContext;
	
	@Autowired
	private DBExportManager exportManager;
	
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("******************************************************************************");
		System.out.println("\t\tSTARTING EXPORT PROCESS");
		System.out.println("******************************************************************************");
		//System.out.println(event);
		start();
	}
	private void start() {
		logMsg("Process started at " + LocalDateTime.now());
		String dataDirectory = !AppUtils.isEmpty(this.appDataDir) ? this.appDataDir : this.getDataDirectory();
		if (AppUtils.isEmpty(dataDirectory)) {
			logErrorMsg("Data Directory required to proceed futher. Terminating the program.");
			System.exit(0);
		}
		startApplicationProcess(dataDirectory);
		logMsg("Execution Completes - " + LocalDateTime.now());
	}
	private String getDataDirectory() {
		Scanner scan = null;
		String dataDirectory = null;
		try {
			scan = new Scanner(System.in);
			userInput("Enter the Data Directory :");
			dataDirectory = scan.nextLine();
		} finally {
			if (scan != null) {
				scan.close();
			}
		}
		return dataDirectory;
	}
	private void userInput(String msg) {
		System.out.println(msg);
	}
	private void startApplicationProcess(String dataDirectory) {
		try {
			logMsg("Given data directory path :" + dataDirectory);
			StartupCheck.doStartupCheck(dataDirectory);
			
			this.appContext.setDataDirectoryPath(dataDirectory);
			this.exportManager.startExportProcess();
		} catch (Exception e) {
			e.printStackTrace();
			logErrorMsg(e);
		} finally {
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
