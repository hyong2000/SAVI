package carnegie.bioinfo.savi.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;


public class FirstTokenParser extends AbstractParser{

	private int totalNumOfLine = 0;
	private HashMap<String,String> lineStringMap = new HashMap<String,String>();
	private ArrayList<String> lineStringList = new ArrayList<String>();
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new FirstTokenParser(SaviValidationMain.acceptedFileName);
		parser.parse();
	}
	
	public FirstTokenParser(String fileName)
	{
		super(fileName);
	}

	@Override
	public void doLineParse(String line) {
		// TODO Auto-generated method stub
		totalNumOfLine++;
		if(line == null)
			return;
		
		if(totalNumOfLine == 1) 
			return;

		line = line.trim();

		if(line.length() == 0 )
			return;
		
		StringTokenizer st = new StringTokenizer(line, "\t"); 
		String firstToken = st.nextToken().trim().toUpperCase();
		lineStringMap.put(firstToken, firstToken);
		lineStringList.add(firstToken);
		
//		System.out.println(mutantPairKey);
		
	}


	@Override
	public void doAfterParse() {
		// TODO Auto-generated method stub
		System.out.println("Total Number of line (FirstTokenParser) in " + fileName + ": " + totalNumOfLine);

	}
	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return lineStringMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return lineStringList;
	}
}
