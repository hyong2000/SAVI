package carnegie.bioinfo.common.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PrintUtil {

    public static void saveStringIntoFile(String fileName, String contents) throws IOException {
	// TODO Auto-generated method stub
	FileWriter file = new FileWriter(fileName, false);
	BufferedWriter bw = new BufferedWriter(file);
	
	bw.write(contents);

	bw.close();
	file.close();
    }

    public static void appendStringIntoFile(String fileName, String contents) throws IOException {
    	// TODO Auto-generated method stub
    	FileWriter file = new FileWriter(fileName, true);
    	BufferedWriter bw = new BufferedWriter(file);
    	
    	bw.write(contents);

    	bw.close();
    	file.close();
    }

	public static String getCurrentDate() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static String getCurrentTime() {
		// TODO Auto-generated method stub
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
