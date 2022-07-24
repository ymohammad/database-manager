/*******************************************************************************************************************************
ObjectScriptTextRepository.java

Copyright © 2022, Triniti Corporation. All rights reserved.
The Programs (which include both the software and documentation) contain proprietary information of Triniti Corporation;
they are provided under a license agreement containing restrictions on use and disclosure and are also protected by
copyright, patent and other intellectual and industrial property law. Reverse engineering, disassembly or de-compilation of
the programs is prohibited.
Program Documentation is licensed for use solely to support the deployment of the Programs and not for any other
purpose.
The information contained in this document is subject to change without notice. If you find any problems in the
documentation, please report them to us in writing. Triniti Corporation does not warrant that this document is error free.
Except as may be expressly permitted in your license agreement for these Programs, no part of these Programs may be
reproduced or transmitted in any form or by any means, electronic or mechanical, for any purpose, without the express
written permission of Triniti Corporation.

Author : ymohammad
Date   : Jul 24, 2022

Last modified by : ymohammad
Last modified on : Jul 24, 2022

*******************************************************************************************************************************/

package in.droidsoft.dbmanager.exportdb.rdbms.oracle.repository;

import java.io.IOException;
import java.sql.SQLException;

import in.droidsoft.dbmanager.exportdb.rdbms.model.ExportOptionsModel;

/**
* Class ObjectScriptTextRepository
*/
public interface ObjectScriptTextRepository {
	
	/**
	 * It sets the Settings for active session based on the given details like including STORAGE information in the
	 * exported SQL script. Similarly other settings.
	 */
	public void setExportProperties(ExportOptionsModel options);
	
	/**
	 * It exports the SQL Script Text of the given Object owner and name.
	 * 
	 * @param objectOwner
	 * @param objectType
	 * @param objectName
	 * @return
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public String getDBObjectScriptText(String objectOwner, String objectType, String objectName) throws IOException, SQLException;
}
