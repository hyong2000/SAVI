package carnegie.bioinfo.savi.input.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.ConstantsForGeneralPurpose;
import carnegie.bioinfo.common.VersionParameters;
import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.savi.input.parser.fileDB.AippDB;

public class AippParser extends AbstractParser{

	private int totalNumOfLine = 0;
	
	private HashMap<String,AippDB> aippDBMap = new HashMap<String,AippDB>();


	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new AippParser("./input/" + "AIPP.txt");
		parser.parse();
		
	}
	
	public AippParser(String fileName)
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
			VersionParameters.versionMap.put(VersionParameters.INPUT_AIPP, versionNumber);
			return;
		}
		
		if (totalNumOfLine == 2)	//remove header
			return;

		String pathwayID = st.nextToken().trim().toUpperCase();
		String evHinfRefs = st.nextToken().trim();
		ArrayList<String> evHinfRefList = getListFromString(evHinfRefs);

		AippDB aippDB = new AippDB();
		aippDB.setPathwayID(pathwayID);
		aippDB.setEvHinfRefList(evHinfRefList);

		if(aippDBMap.containsKey(pathwayID))
			System.err.println("Duplicated input for pathwayID: " + pathwayID + " in " + this.fileName);
		
		aippDBMap.put(pathwayID, aippDB);

	}

	private ArrayList<String> getListFromString(String evHinfRefs) {
		// TODO Auto-generated method stub
		ArrayList<String> resultList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(evHinfRefs, ConstantsForGeneralPurpose.DELIMITER_MULTI_VALUES);
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
		System.out.println("Total Number of aippDB (AippParser): " + aippDBMap.size());
		
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return aippDBMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
