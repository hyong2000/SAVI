package carnegie.bioinfo.savi.input.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.VersionParameters;
import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.savi.input.parser.fileDB.UppDB;

public class UppParser extends AbstractParser{

	private int totalNumOfLine = 0;
	
	private HashMap<String,UppDB> uppDBMap = new HashMap<String,UppDB>();


	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new UppParser("./input/" + "UPP.txt");
		parser.parse();
		
	}
	
	public UppParser(String fileName)
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
			VersionParameters.versionMap.put(VersionParameters.INPUT_UPP, versionNumber);
			return;
		}
		
		if (totalNumOfLine == 2)	//remove header
			return;

		String pathwayID = st.nextToken().trim().toUpperCase();
		String evHinfRefs = st.nextToken().trim();
		ArrayList<String> evHinfRefList = getListFromString(evHinfRefs);
		String evIcRefs = st.nextToken().trim();
		ArrayList<String> evIcRefList = getListFromString(evIcRefs);

		UppDB uppDB = new UppDB();
		uppDB.setPathwayID(pathwayID);
		uppDB.setEvHinfRefList(evHinfRefList);
		uppDB.setEvIcRefList(evIcRefList);

		if(uppDBMap.containsKey(pathwayID))
			System.err.println("Duplicated input for pathwayID: " + pathwayID + " in " + this.fileName);

		uppDBMap.put(pathwayID, uppDB);

	}

	private ArrayList<String> getListFromString(String evHinfRefs) {
		// TODO Auto-generated method stub
		ArrayList<String> resultList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(evHinfRefs, ",");
		while(st.hasMoreTokens())
		{
			String evRef = st.nextToken();
			resultList.add(evRef);
		}
		return resultList;
	}

	@Override
	public void doAfterParse() {
		// TODO Auto-generated method stub
		System.out.println("Total Number of uppDB (UppParser): " + uppDBMap.size());
		
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return uppDBMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
