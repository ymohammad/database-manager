/*******************************************************************************************************************************
DatabaseScriptStore.java

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.droidsoft.dbmanager.exportdb.rdbms.model.SQLStatement;
import in.droidsoft.dbmanager.exportdb.rdbms.model.type.SQLStatementType;
import in.droidsoft.dbmanager.exportdb.util.AppUtils;

public class DatabaseScriptStore {

	private static final String DB_SCRIPT_FILE_NAME = "dbScript.sql";
	private String dataDirectoryPath = null;
	private ArrayList<SQLStatement> dbScriptList = null;
	
	public DatabaseScriptStore(String dataDirectoryPath) {
		this.dataDirectoryPath = dataDirectoryPath;
		this.dbScriptList = new ArrayList<SQLStatement>();
		this.readScriptFile();
	}

	@SuppressWarnings("unchecked")
	public List<SQLStatement> getDBScript() {
		List<SQLStatement> returnList =  (List<SQLStatement>) this.dbScriptList.clone();
		return returnList;
	}
	private void readScriptFile() {
		File dbScriptFile = this.getDbScriptFile();
		AppUtils.logMsg("DatabaseScriptStore.readScriptFile() reading script details..." + dbScriptFile.getAbsolutePath() );
		this.parseAndReadScriptFile(dbScriptFile);
	}

	private void parseAndReadScriptFile(File dbScriptFile) {

		BufferedReader bufferedReader = null;
		try {

			bufferedReader = new BufferedReader(new FileReader(dbScriptFile));
			String bufferLine = bufferedReader.readLine();
			String completeLine = null;
			
			while (bufferLine != null) {
				if (bufferLine.trim().length() == 0) {
					bufferLine = bufferedReader.readLine();
					continue;
				}

				if (!bufferLine.trim().startsWith("--") 
						&& !(bufferLine.trim().toUpperCase()).startsWith("PROMPT")
						&& !(bufferLine.trim().toUpperCase()).startsWith("SET ")
						&& !(bufferLine.trim().toUpperCase()).startsWith("SPOOL ")) {
					//AppUtils.logMsg("DatabaseScriptStore.parseAndReadScriptFile() Got a valid line ");
					if (bufferLine.trim().endsWith(";") || bufferLine.trim().endsWith("/")) {
						//AppUtils.logMsg("DatabaseScriptStore.parseAndReadScriptFile() Complete Statement in One Line.");
						bufferLine = removeSemicolon(bufferLine);
						bufferLine = removeComments(bufferLine);
						if (bufferLine.trim().length() > 0) {
							this.dbScriptList.add(this.getSQLStatement(bufferLine));
						}
					} else {
						completeLine = bufferLine;

						while (bufferLine != null && !bufferLine.trim().endsWith(";")) {
							bufferLine = bufferedReader.readLine();
							if (bufferLine != null) {
								bufferLine = removeComments(bufferLine.trim());
								if (bufferLine.trim().toUpperCase().startsWith("PROMPT")
										//|| bufferLine.trim().toUpperCase().startsWith("SET ")
										|| bufferLine.trim().toUpperCase().startsWith("SPOOL "))
									continue;

								completeLine += " " + bufferLine + " ";
							}
						}

						completeLine = removeSemicolon(completeLine);

						if (completeLine.trim().length() > 0) {
							this.dbScriptList.add(this.getSQLStatement(completeLine));
						}
					}
				} ///Valid Line.
				bufferLine = bufferedReader.readLine();
			}
			AppUtils.logMsg("Complete Statement :" + this.dbScriptList);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private SQLStatement getSQLStatement(String scriptLine) {
		SQLStatement statement = new SQLStatement();
		statement.setScriptLine(scriptLine);
		if (scriptLine.toUpperCase().trim().startsWith("INSERT") || scriptLine.toUpperCase().trim().startsWith("UPDATE")
				|| scriptLine.toUpperCase().trim().startsWith("DROP")
				|| scriptLine.toUpperCase().trim().startsWith("DELETE")) {
			statement.setStatementType(SQLStatementType.DML);
		} else {
			statement.setStatementType(SQLStatementType.DDL);
		}
		return statement;
	}

	private String removeComments(String bufferLine) {
		return bufferLine;
	}

	private String removeSemicolon(String scriptLine) {
		if (scriptLine == null) return scriptLine;
		return((scriptLine.trim()).substring(0,scriptLine.trim().length()-1));
	}
	private File getDbScriptFile() {
		return AppUtils.getResourceFile(dataDirectoryPath, DB_SCRIPT_FILE_NAME);
	}
}
