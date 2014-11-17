package carnegie.bioinfo.savi.input.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;

public class TaxonomyNameParser extends AbstractParser{

	private int totalNumOfLine = 0;
	
	private HashMap<String,String> taxanomyNameMap = new HashMap<String,String>();


	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new TaxonomyNameParser("./input/" + "names.dmp");
		parser.parse();
		
	}
	
	public TaxonomyNameParser(String fileName)
	{
//		super.fileName = fileName;
		super(fileName);
	}
	
	@Override
	public void doLineParse(String line) {
		// TODO Auto-generated method stub
		totalNumOfLine++;
		
		StringTokenizer st = new StringTokenizer(line, "|");
		if (!st.hasMoreTokens())
			return;

		String taxonomyId = st.nextToken().trim();
		String taxonomyName = st.nextToken().trim();
		String uniqueName = st.nextToken().trim();
		String taxonomyClass = st.nextToken().trim();

		//put info for all taxanomyId (for the cast that taxonomyId does not have scientific name)
		if(!taxanomyNameMap.containsKey(taxonomyId))
			taxanomyNameMap.put(taxonomyId, taxonomyName);

		if(taxonomyClass.equals("scientific name"))
		{
			taxanomyNameMap.put(taxonomyId, taxonomyName);
//			System.out.println(taxonomyId + " : " +  taxonomyName);
		}
	}

	@Override
	public void doAfterParse() {
		// TODO Auto-generated method stub
		System.out.println("Total Number of taxanomyName (TaxonomyNameParser): " + taxanomyNameMap.size());
		
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return taxanomyNameMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
