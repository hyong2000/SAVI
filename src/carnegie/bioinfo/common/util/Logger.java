package carnegie.bioinfo.common.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import carnegie.bioinfo.common.ConstantsForGeneralPurpose;


public class Logger {

	static StringBuffer sb = new StringBuffer();
	static int loggingType = ConstantsForGeneralPurpose.LOGGING_TYPE_CONSOLE;
	private static final String loggerPrefix = "####";

	public static void println(Object obj)
	{
		loggingln(obj);
	}

	public static void print(Object obj)
	{
		logging(obj);
	}

	public static void println(int obj)
	{
		loggingln(Integer.toString(obj));
	}

	public static void print(int obj)
	{
		logging(Integer.toString(obj));
	}
	
	private static void loggingln(Object obj)
	{
		if(ConstantsForGeneralPurpose.LOGGING)
		{
//			String temp = "__" + obj;
			if(loggingType == ConstantsForGeneralPurpose.LOGGING_TYPE_FILE)
			{
				sb.append(loggerPrefix);
				sb.append(obj +  "\r\n");
			}
			else if(loggingType == ConstantsForGeneralPurpose.LOGGING_TYPE_CONSOLE)
			{
				System.out.print(loggerPrefix);
				System.out.println(obj);
			}
		}
	}
	
	private static void logging(Object obj)
	{
		if(ConstantsForGeneralPurpose.LOGGING)
		{
//			String temp = "__" + obj;
			if(loggingType == ConstantsForGeneralPurpose.LOGGING_TYPE_FILE)
			{
				sb.append(loggerPrefix);
				sb.append(obj);
			}
			else if(loggingType == ConstantsForGeneralPurpose.LOGGING_TYPE_CONSOLE)
			{
				System.out.print(loggerPrefix);
				System.out.print(obj);
			}
		}
	}
	
	public static void writeIntoFile(String fileName) throws IOException
	{
	    if(loggingType == ConstantsForGeneralPurpose.LOGGING_TYPE_FILE)
	    {
		String outFileName = fileName;
		FileWriter file = new FileWriter(outFileName, false);
		BufferedWriter bw = new BufferedWriter(file);
		
		bw.write(sb.toString());
		
		bw.close();
		file.close();
	    }
	}

	public static int getLoggingType() {
		return loggingType;
	}

	public static void setLoggingType(int loggingType) {
		Logger.loggingType = loggingType;
	}

}

