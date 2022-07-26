/*******************************************************************************************************************************
DBObjectFileUtils.java

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

package in.droidsoft.dbmanager.exportdb.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
* Class DBObjectFileUtils
*/
public class DBObjectFileUtils {
	
	public static boolean createDBObjectFile(String parentDirectoryPath, String objFileName, String objScriptText) throws IOException {
		File parentDir = new File(parentDirectoryPath);
		boolean isDirCreated = parentDir.isDirectory();
		if (!isDirCreated) {
			isDirCreated = parentDir.mkdirs();
		}
		if (!isDirCreated) {
			throw new RuntimeException("Unable to create the directory " + parentDir.getAbsolutePath());
		}
		
		File objectFile = new File(parentDir, objFileName);
		writeToFile(objectFile, objScriptText.trim());
		return true;
	}
	
	private static void writeToFile(File file, String content) throws IOException {
		Path path = Paths.get(file.getAbsolutePath());
	    byte[] strToBytes = content.getBytes();
	    Files.write(path, strToBytes);
	}
}
