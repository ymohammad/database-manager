/*******************************************************************************************************************************
StartupCheck.java

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

package in.droidsoft.dbmanager.exportdb.start;

import in.droidsoft.dbmanager.exportdb.util.AppUtils;

/**
* Class StartupCheck
*/
public class StartupCheck {
	
	public static void doStartupCheck(String appDataDirectory) {
		
	}
	
	public static void databaseCheck(String appDataDirectory) {
		logMsg("Reading DB Properties to establish Database Connection");
	}

	private static void logMsg(String msg) {
		AppUtils.logMsg(msg);
	}
}
