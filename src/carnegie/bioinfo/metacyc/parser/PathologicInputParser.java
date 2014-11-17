package carnegie.bioinfo.metacyc.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import carnegie.bioinfo.common.ConstantsForCycDatParser;
import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.common.util.PrintUtil;
import carnegie.bioinfo.metacyc.parser.fileDB.PathologicDB;

public class PathologicInputParser extends AbstractParser{

	private int totalNumOfProtein = 0;
	private HashMap<String,PathologicDB> pathologicDBMap = new HashMap<String,PathologicDB>();
	private PathologicDB pathologicDB = null;
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new PathologicInputParser("./input/dat/" + "pathologic.pf");
		parser.parse();
	}
	
	public PathologicInputParser(String fileName)
	{
//		super.fileName = fileName;
		super(fileName);
	}

	@Override
	public void doLineParse(String line) {
		// TODO Auto-generated method stub
		if(line.startsWith(ConstantsForCycDatParser.PATHLOGIC_ID))
		{
			String uniqueID = getUniqueID(line);
			createPathologicDB(uniqueID);
		}
		
		if(line.startsWith(ConstantsForCycDatParser.PATHLOGIC_NAME))
			addName(line);
		
		if(line.startsWith(ConstantsForCycDatParser.PATHLOGIC_PRODUCT_TYPE))
			addProductType(line);

		if(line.startsWith(ConstantsForCycDatParser.PATHLOGIC_EC))
			addECNumber(line);

		if(line.startsWith(ConstantsForCycDatParser.PATHLOGIC_REACTION))
			addReactionID(line);

		if(line.startsWith(ConstantsForCycDatParser.ITEM_END_DELIMITER))
			addProteinDBIntoMap();

	}
	
	private void addReactionID(String line) {
		// TODO Auto-generated method stub
		String reactionID = line.substring(ConstantsForCycDatParser.PATHLOGIC_REACTION.length()).trim();
		pathologicDB.addReactionID(reactionID);
	}
	
	private void addECNumber(String line) {
		// TODO Auto-generated method stub
		String ecNumber = line.substring(ConstantsForCycDatParser.PATHLOGIC_EC.length()).trim();
		pathologicDB.addEcNumber(ecNumber);
	}

	private void addProteinDBIntoMap() {
		// TODO Auto-generated method stub
		pathologicDBMap.put(pathologicDB.getUniqueID(), pathologicDB);
	}
	
	private void createPathologicDB(String uniqueID) {
		// TODO Auto-generated method stub
		pathologicDB = new PathologicDB(uniqueID);
		totalNumOfProtein++;
	}

	private void addName(String line) {
		// TODO Auto-generated method stub
		String name = line.substring(ConstantsForCycDatParser.PATHLOGIC_NAME.length()).trim();
		pathologicDB.setName(name);
	}

	private void addProductType(String line) {
		// TODO Auto-generated method stub
		String productType = line.substring(ConstantsForCycDatParser.PATHLOGIC_PRODUCT_TYPE.length()).trim();
		pathologicDB.setProductType(productType);
	}

	private String getUniqueID(String line) {
		// TODO Auto-generated method stub
		String uniqueID = line.substring(ConstantsForCycDatParser.PATHLOGIC_ID.length()).trim();
		
		//#############for remove subID(ATGxxxx.x) from Arabidopsis  //commented out 05/24/2013
//		int indexOfComma = uniqueID.lastIndexOf(".");
//		if(indexOfComma != -1)
//			uniqueID = uniqueID.substring(0, indexOfComma);

		return uniqueID;
	}

	@Override
	public void doAfterParse() {
		// TODO Auto-generated method stub
//		System.out.println("Number of class(compound) associated in a reaction: " + tempNodeMap.size());
		System.out.println("Total Number of Pathologic mapping data (PathologicInputParser): " + totalNumOfProtein);
		
		//test
//		printPathologicDBMap();
	}

	private void printPathologicDBMap() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		Iterator<PathologicDB> iterator = pathologicDBMap.values().iterator();
		while(iterator.hasNext())
		{
			PathologicDB pathologicDB = iterator.next();
			sb.append(pathologicDB.getUniqueID());
			sb.append("\t");
			sb.append(pathologicDB.getName());
			sb.append("\t");
			sb.append(pathologicDB.getProductType());
			sb.append("\t");
			sb.append(pathologicDB.getEcNumberSet());
			sb.append("\t");
			sb.append(pathologicDB.getReactionIDSet());
			sb.append("\r\n");
		}
		
		try {
			PrintUtil.saveStringIntoFile("pathologicInput_parsed.txt", sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return pathologicDBMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
}
