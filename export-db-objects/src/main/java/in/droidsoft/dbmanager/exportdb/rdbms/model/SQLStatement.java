/*******************************************************************************************************************************
SQLStatement.java

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

package in.droidsoft.dbmanager.exportdb.rdbms.model;

import in.droidsoft.dbmanager.exportdb.rdbms.model.type.SQLStatementType;

public class SQLStatement {
	private String scriptLine;
	private SQLStatementType statementType;
	
	public SQLStatement() {
		super();
	}
	
	public SQLStatement(String scriptLine, SQLStatementType statementType) {
		super();
		this.scriptLine = scriptLine;
		this.statementType = statementType;
	}

	public String getScriptLine() {
		return scriptLine;
	}
	public void setScriptLine(String scriptLine) {
		this.scriptLine = scriptLine;
	}
	public SQLStatementType getStatementType() {
		return statementType;
	}
	public void setStatementType(SQLStatementType statementType) {
		this.statementType = statementType;
	}
	@Override
	public String toString() {
		return "SQLStatement [scriptLine=" + scriptLine + ", statementType=" + statementType + "]";
	}
}