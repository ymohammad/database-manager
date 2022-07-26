/*******************************************************************************************************************************
ExportScriptTextOptions.java

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

package in.droidsoft.dbmanager.exportdb.rdbms.oracle;

/**
 * Class ExportScriptTextOptions
 */
public class ExportScriptTextOptions {

	private ExportScriptTextOptions() {
	}
	
	/**
	 * Follow properties to include or exclude the Table space info in the exported Object Script.
	 */
	public static final String TABLESPACE_INFO = "TABLESPACE"; 
	
	/**
	 * Follow properties to include or exclude the SCHEMA name in the exported Object Script.
	 */
	public static final String SCHEMA_INFO = "EMIT_SCHEMA";
	
	/**
	 * To make the exported script pretty.
	 */
	public static final String PRETTY_SCRIPT = "PRETTY";
	
	/**
	 * To exclude or include storage information
	 */
	public static final String STORAGE_INFO = "STORAGE";
	
	/**
	 * To exclude or include Segment information
	 */
	public static final String SEGMENT_INFO = "SEGMENT_ATTRIBUTES";
	
	/**
	 * To add semicolon at the end of the script.
	 */
	public static final String SQL_TERMINATOR = "SQLTERMINATOR";
	
	/**
	 * To exclude or include physical properties
	 */
	public static final String PHYSICAL_PROPS = "PHYSICAL_PROPERTIES";
	

	public static String getBlockCode(String property, boolean isEnabled) {
		String returnVal = "BEGIN DBMS_METADATA.SET_TRANSFORM_PARAM(DBMS_METADATA.SESSION_TRANSFORM, ";
		returnVal += "'" + property.toUpperCase() + "', ";
		
		if (isEnabled) {
			returnVal += "TRUE); END;";
		} else {
			returnVal += "FALSE); END;";
		}
		return returnVal;
	}
}
