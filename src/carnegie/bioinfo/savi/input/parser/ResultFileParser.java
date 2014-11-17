package carnegie.bioinfo.savi.input.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;


public class ResultFileParser extends AbstractParser{

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
		IParser parser = new ResultFileParser("E:/hyong2000/projects/SequenceAnalysis/input/geneClusterForLee/loci.metabolic_genes.130319");
		parser.parse();
	}
	
	public ResultFileParser(String fileName)
	{
		super(fileName);
	}

	@Override
	public void doLineParse(String line) {
		// TODO Auto-generated method stub
	
		totalNumOfLine++;

		if(totalNumOfLine == 1)	//delete header
			return;

		if(line == null)
			return;

		line = line.trim();

		if(line.length() == 0 )
			return;
		
		StringTokenizer st = new StringTokenizer(line, "\t"); 
		String stringValue = st.nextToken().trim().toUpperCase(); 

		lineStringMap.put(stringValue, line);
		lineStringList.add(line);
		
//		System.out.println(mutantPairKey);
		
	}


	@Override
	public void doAfterParse() {
		// TODO Auto-generated method stub
		System.out.println("Total Number of lines in result file (" + fileName + "): " + totalNumOfLine);
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
