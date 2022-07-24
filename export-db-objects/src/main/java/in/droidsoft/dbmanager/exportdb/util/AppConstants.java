/*******************************************************************************************************************************
AppConstants.java

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
Date   : Jul 20, 2022

Last modified by : ymohammad
Last modified on : Jul 20, 2022

*******************************************************************************************************************************/

package in.droidsoft.dbmanager.exportdb.util;

/**
* Class AppConstants
*/
public class AppConstants {
	private AppConstants() {
	}
	
	public static final String DB_PROPERTIES_FILE_NAME = "dbdetails.properties";
	public static final String EXPORT_PROPERTIES_FILE_NAME = "export.properties";
	public static final String DB_STARTUP_SCRIPT_FILE_NAME = "startupDBScript.sql";
	public static final String EXPORT_OBJECTS_LIST_SELECT_QUERY_FILE_NAME="exportObjectsListSelectQuery.sql";
	public static final String DB_OBJECTS_OUTPUT_DIR_NAME = "DB_OBJECTS_OUTPUT";
	
	public static final String[] STANDARD_OBJECT_TYPES_ARR = {"SEQUENCE","PROCEDURE","PACKAGE","TRIGGER","VIEW", "FUNCTION","TABLE","TYPE", "PACKAGE BODY", 
			"TYPE BODY", "JAVA CLASS", "JAVA SOURCE", "MATERIALIZED VIEW"};
	
	public static final String[] DDL_STATEMENT_TYPE_ARRAY = {"CREATE", "DROP", "ALTER", "TRUNCATE", "COMMENT", "RENAME"};
	public static final String[] DQL_STATEMENT_TYPE_ARRAY = {"SELECT"};
	public static final String[] DML_STATEMENT_TYPE_ARRAY = {"INSERT", "UPDATE", "DELETE"};
	public static final String[] DCL_STATEMENT_TYPE_ARRAY = {"GRANT", "REVOKE"};
	
	public static final String ORACLE_SERVICE_TYPE = "ORACLE";
	
	public static final String DEFAULT_FILE_EXTENSION = "sql";
	
}
