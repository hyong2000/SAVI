package carnegie.bioinfo.savi.filegeneration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.common.util.Logger;


public class AcceptedListParser extends AbstractParser{

	private int totalNumOfLine = 0;
	private HashMap<String,AcceptedListDB> acceptedListMap = new HashMap<String,AcceptedListDB>();
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new AcceptedListParser(SaviPgdbInputGenerationMain.acceptedFileName);
		parser.parse();
	}
	
	public AcceptedListParser(String fileName)
	{
		super(fileName);
	}

	@Override
	public void doLineParse(String line) {
		// TODO Auto-generated method stub
		totalNumOfLine++;
		line = line.trim();
		if(line.length() == 0 )
			return;
		
		//remove header
		if(totalNumOfLine == 1) 
			return;

		AcceptedListDB acceptedListDB = new AcceptedListDB();
		StringTokenizer st = new StringTokenizer(line, "\t"); 
		
		acceptedListDB.setPathwayId(st.nextToken().trim());
		acceptedListDB.setPathwayCommonName(st.nextToken().trim());
		acceptedListDB.setEvidencdCode(st.nextToken().trim());
		acceptedListDB.setRef(st.nextToken().trim());
		acceptedListDB.setSpecies(st.nextToken().trim());
		acceptedListDB.setCiatations(st.nextToken().trim());
		acceptedListDB.setTaxonWarningFlagIfWithin(st.nextToken().trim());
		acceptedListDB.setTaxonWarningFlagIfOutsideOf(st.nextToken().trim());
		acceptedListDB.setTaxonWarningComment(st.nextToken().trim());
		
		acceptedListMap.put(acceptedListDB.getPathwayId(), acceptedListDB);
//		System.out.println(mutantPairKey);
		
	}


	@Override
	public void doAfterParse() {
		// TODO Auto-generated method stub
		Logger.println("Total Number of acceptedListDB (AcceptedListParser) in " + fileName + ": " + acceptedListMap.size());

	}
	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return acceptedListMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
}
