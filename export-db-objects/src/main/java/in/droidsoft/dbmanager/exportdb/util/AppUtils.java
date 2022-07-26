package in.droidsoft.dbmanager.exportdb.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;

public class AppUtils {

	/**
	 * Get the Resource File
	 * @param parentDirPath
	 * @param resourceFileName
	 * @return
	 */
	public static File getResourceFile(String parentDirPath, String resourceFileName) {
		if (isEmpty(parentDirPath)) {
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

	public static boolean isContainsStartsWith(String strToTest, String[] valueArray) {
		if (isEmpty(strToTest)) {
			return false;
		}
		if (valueArray == null || valueArray.length == 0) {
			return false;
		}
		for (String eachValue: valueArray) {
			if (strToTest.toUpperCase().trim().startsWith(eachValue)) {
				return true;
			}
		}
		return false;
	}
	public static boolean isEmpty(String str) {
		boolean res = str == null || str.trim().length() == 0;
		return res;
	}
	
	public static String convertClobToString(Clob clob) throws IOException, SQLException {
		StringBuffer sb = new StringBuffer();
		if (clob == null) {
			return sb.toString();
		}
		String line = null;
		BufferedReader buffReader = null;
		try {
			buffReader = new BufferedReader(clob.getCharacterStream());
			while ((line = buffReader.readLine()) != null) {
				sb.append(line).append("\r\n");
			}
		} finally {
			if (buffReader != null) {
				buffReader.close();
			}
		}
		return sb.toString();
	}

	/**
	 * Convert the given property if yes, "true" to true and otherwise false.
	 * @param property
	 * @return
	 */
	public static boolean convertToBoolean(String propVal) {
		//System.out.println("AppUtils.convertToBoolean()" + propVal);
		if (isEmpty(propVal)) return false;
		
		if (propVal.equalsIgnoreCase("yes") || propVal.equalsIgnoreCase("true")) return true;
		
		return false;
	}

	/**
	 * Separator to show on console when new Type export starts.
	 * @param size
	 * @return
	 */
	public static String getConsoleTypeSeparator(int size) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i<size; i++) {
			buff.append("-");
		}
		return buff.toString();
	}

	public static String getConsoleTopHeaderSeprator(int size) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i<size; i++) {
			buff.append("*");
		}
		return buff.toString();
	}
}
