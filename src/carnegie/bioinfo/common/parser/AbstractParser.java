package carnegie.bioinfo.common.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import carnegie.bioinfo.common.util.Logger;


public abstract class AbstractParser implements IParser{
	protected String fileName = "";
	
	public AbstractParser(String fileName)
	{
		this.fileName = fileName;
	}

	public int parse() throws IOException
    {
		Logger.println("Start Parsing: " + fileName + " : " + this.getClass().getName());
		File checkfile = new File(fileName);
		if (checkfile.exists()) {
		    FileReader fileReader = new FileReader(fileName);
		    BufferedReader bfReader = new BufferedReader(fileReader);

		    String line = null;

		    while ((line = bfReader.readLine()) != null) {
			if (line.trim().equals(""))
			    continue;

				doLineParse(line);
		    }
		    bfReader.close();
		    fileReader.close();
		} else
		{
			System.err.println(fileName + " is not found!!");
			return -1;
		}
			
		doAfterParse();
		return 0;
    }
    
    abstract public void doLineParse(String line);
    abstract public void doAfterParse();
    
    abstract public HashMap getResultHashMap();
    abstract public ArrayList getResultCollection();
}
