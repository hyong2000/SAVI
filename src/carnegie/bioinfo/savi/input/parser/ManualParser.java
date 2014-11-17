package carnegie.bioinfo.savi.input.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.VersionParameters;
import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.savi.input.parser.fileDB.ManualDB;

public class ManualParser extends AbstractParser{

	private int totalNumOfLine = 0;
	
	private HashMap<String,ManualDB> manualDBMap = new HashMap<String,ManualDB>();


	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new ManualParser("./input/" + "MANUAL.txt");
		parser.parse();
		
	}
	
	public ManualParser(String fileName)
	{
//		super.fileName = fileName;
		super(fileName);
	}
	
	@Override
	public void doLineParse(String line) {
		// TODO Auto-generated method stub
		totalNumOfLine++;
		
		StringTokenizer st = new StringTokenizer(line, "\t");
		if (!st.hasMoreTokens())
			return;
		
		if (totalNumOfLine == 1)
		{
			String versionHeader = st.nextToken().trim();
			String versionNumber = st.nextToken().trim();
			VersionParameters.versionMap.put(VersionParameters.INPUT_MANUAL, versionNumber);
			return;
		}
		
		if (totalNumOfLine == 2)	//remove header
			return;

		String pathwayID = st.nextToken().trim().toUpperCase();
		String flag = "";
		if(st.hasMoreTokens())
			flag = st.nextToken().trim();

		ManualDB manualDB = new ManualDB();
		manualDB.setFlag(flag);

		if(manualDBMap.containsKey(pathwayID))
			System.err.println("Duplicated input for pathwayID: " + pathwayID + " in " + this.fileName);

		manualDBMap.put(pathwayID, manualDB);

	}

	@Override
	public void doAfterParse() {
		// TODO Auto-generated method stub
		System.out.println("Total Number of manualDB (ManualParser): " + manualDBMap.size());
		
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return manualDBMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
