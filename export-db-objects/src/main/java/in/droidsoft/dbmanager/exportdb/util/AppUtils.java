package in.droidsoft.dbmanager.exportdb.util;

import java.io.File;

public class AppUtils {

	/**
	 * Get the Resource File
	 * @param parentDirPath
	 * @param resourceFileName
	 * @return
	 */
	public static File getResourceFile(String parentDirPath, String resourceFileName) {
		if (parentDirPath == null || parentDirPath.trim().length() == 0) {
			throw new RuntimeException("Cannot get the Resource Directory.");
		}
		File dataDirectory = new File(parentDirPath);
		if (!dataDirectory.isDirectory()) {
			throw new RuntimeException("Resource Directory path is not available at " + dataDirectory.getAbsolutePath());
		}
		File resourceFile = new File(dataDirectory, resourceFileName);
		if (!resourceFile.isFile()) {
			throw new RuntimeException("Resource file " + resourceFileName + " is not available at " + resourceFile.getAbsolutePath());
		}
		return resourceFile;
	}
	
	public static void logMsg(String msg) {
		System.out.println("[INFO]  - " + msg);
	}
	
	public static void logErrorMsg(String msg) {
		System.out.println("[ERROR] - " + msg);
	}
}
