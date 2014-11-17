package carnegie.bioinfo.savi.input.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.ConstantsForGeneralPurpose;
import carnegie.bioinfo.common.VersionParameters;
import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.savi.input.parser.fileDB.ProblemPathwaysDB;
import carnegie.bioinfo.savi.input.parser.fileDB.UppDB;

public class ProblemPathwaysParser extends AbstractParser{

	private int totalNumOfLine = 0;
	
	private HashMap<String,ProblemPathwaysDB> problemPathwaysDBMap = new HashMap<String,ProblemPathwaysDB>();


	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new ProblemPathwaysParser("./input/" + "problem_pathways.txt");
		parser.parse();
		
	}
	
	public ProblemPathwaysParser(String fileName)
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
			VersionParameters.versionMap.put(VersionParameters.INPUT_PROBLEM_PATHWAYS, versionNumber);
			return;
		}
		
		if (totalNumOfLine == 2)	//remove header
			return;

		String pathwayID = st.nextToken().trim().toUpperCase();
		String description = st.nextToken().trim();

		ProblemPathwaysDB problemPathwaysDB = new ProblemPathwaysDB();
		problemPathwaysDB.setPathwayID(pathwayID);
		problemPathwaysDB.setDescription(description);

		if(problemPathwaysDBMap.containsKey(pathwayID))
			System.err.println("Duplicated input for pathwayID: " + pathwayID + " in " + this.fileName);

		problemPathwaysDBMap.put(pathwayID, problemPathwaysDB);

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
		System.out.println("Total Number of problemPathwaysDB (ProblemPathwaysParser): " + problemPathwaysDBMap.size());
		
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return problemPathwaysDBMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
