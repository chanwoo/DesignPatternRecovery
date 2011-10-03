package kr.ac.snu.selab.soot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class MyUtil {
	public static String removeBracket(String aString) {
		return aString.toString().replaceAll("<|>", " ");
	}

	public static void stringToFile(String aString, String aFilePath) {
		try {
			File outputFile = new File(aFilePath);
			File dir = outputFile.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			PrintWriter writer = new PrintWriter(new FileWriter(aFilePath));
			writer.print(aString);
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
