package carnegie.bioinfo.savi.input.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.ConstantsForGeneralPurpose;
import carnegie.bioinfo.common.VersionParameters;
import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.savi.input.parser.fileDB.CappDB;

public class CappParser extends AbstractParser{

	private int totalNumOfLine = 0;
	
	private HashMap<String,CappDB> cappDBMap = new HashMap<String,CappDB>();


	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new CappParser("./input/" + "CAPP.txt");
		parser.parse();
		
	}
	
	public CappParser(String fileName)
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
			VersionParameters.versionMap.put(VersionParameters.INPUT_CAPP, versionNumber);
			return;
		}
		
		if (totalNumOfLine == 2)	//remove header
			return;

		String pathwayID = st.nextToken().trim().toUpperCase();

		String taxonExcludeWithins = st.nextToken().trim();
		ArrayList<String> taxonExcludeWithinList = getListFromString(taxonExcludeWithins);
		String taxonExcludeOutsides = st.nextToken().trim();
		ArrayList<String> taxonExcludeOutsideList = getListFromString(taxonExcludeOutsides);
		String taxonIncludeWithins = st.nextToken().trim();
		ArrayList<String> taxonIncludeWithinList = getListFromString(taxonIncludeWithins);
		String rxnSafes = st.nextToken().trim().toUpperCase();
		ArrayList<String> rxnSafeList = getListFromString(rxnSafes);
		String rxnTroubles = st.nextToken().trim().toUpperCase();
		ArrayList<String> rxnTroubleList = getListFromString(rxnTroubles);
		String lowestCommonParent = st.nextToken().trim();
		
		String numOfSpecies = st.nextToken().trim();
		
		String evHinfRefs = st.nextToken().trim();
		ArrayList<String> evHinfRefList = getListFromString(evHinfRefs);

		String taxonWarningComment = st.nextToken().trim();
		boolean isTaxonWarning = false;
		if(taxonWarningComment.equalsIgnoreCase("YES"))
			isTaxonWarning = true;

		String taxonWarningFlagIfWithin = st.nextToken().trim();
		ArrayList<String> taxonWarningFlagIfWithinList = getListFromString(taxonWarningFlagIfWithin);
		String taxonWarningFlagIfOutsideOf = st.nextToken().trim();
		ArrayList<String> taxonWarningFlagIfOutsideOfList = getListFromString(taxonWarningFlagIfOutsideOf);

		String typeOfCriteria = st.nextToken().trim();


		
		CappDB cappDB = new CappDB();
		cappDB.setPathwayID(pathwayID);
		cappDB.setTaxonExcludeWithinList(taxonExcludeWithinList);
		cappDB.setTaxonExcludeOutsideList(taxonExcludeOutsideList);
		cappDB.setTaxonIncludeWithinList(taxonIncludeWithinList);
		cappDB.setRxnSafeList(rxnSafeList);
		cappDB.setRxnTroubleList(rxnTroubleList);
		cappDB.setLowestCommonParent(lowestCommonParent);
		cappDB.setNumOfSpecies(numOfSpecies);
		cappDB.setEvHinfRefList(evHinfRefList);
		cappDB.setTaxonWarning(isTaxonWarning);

		cappDB.setTaxonWarningFlagIfWithin(taxonWarningFlagIfWithinList);
		cappDB.setTaxonWarningFlagIfOutsideOf(taxonWarningFlagIfOutsideOfList);
		cappDB.setTypeOfCriteria(typeOfCriteria);
		

		if(cappDBMap.containsKey(pathwayID))
			System.err.println("Duplicated input for pathwayID: " + pathwayID + " in " + this.fileName);

		cappDBMap.put(pathwayID, cappDB);
	}

	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	private ArrayList<String> getListFromString(String evHinfRefs) {
		// TODO Auto-generated method stub
		ArrayList<String> resultList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(evHinfRefs, ConstantsForGeneralPurpose.DELIMITER_MULTI_VALUES);
		while(st.hasMoreTokens())
		{
			String evRef = st.nextToken().trim();
			if(evRef.equalsIgnoreCase("NA"))	//ignore the NA input
				continue;
			
			resultList.add(evRef);
		}
		return resultList;
	}

	@Override
	public void doAfterParse() {
		// TODO Auto-generated method stub
		System.out.println("Total Number of cappDB (CappParser): " + cappDBMap.size());
		
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return cappDBMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
