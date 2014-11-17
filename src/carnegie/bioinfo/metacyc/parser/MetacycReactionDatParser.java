package carnegie.bioinfo.metacyc.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import carnegie.bioinfo.common.ConstantsForCycDatParser;
import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.common.util.PrintUtil;
import carnegie.bioinfo.metacyc.parser.fileDB.ReactionDB;

public class MetacycReactionDatParser extends AbstractParser{

	public static String species = "";
	public static String database = "";
	public static String version = "";
	public static String fileName = "";
	public static String dateTimeGenerated = "";

	
	private int totalNumOfReactions = 0;
	private HashMap<String,ReactionDB> reactionDBMap = new HashMap<String,ReactionDB>();
	private ReactionDB reactionDB = null;
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new MetacycReactionDatParser("./input/dat/" + "reactions.dat");
		parser.parse();
	}
	
	public MetacycReactionDatParser(String fileName)
	{
//		super.fileName = fileName;
		super(fileName);
	}
	static public String getFileInfo()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n");
		sb.append("####################");
		sb.append("\r\n");
		sb.append("fileName: " + fileName);
		sb.append("\r\n");
		sb.append("species: " + species);
		sb.append("\r\n");
		sb.append("database: " + database);
		sb.append("\r\n");
		sb.append("version: " + version);
		sb.append("\r\n");
		sb.append("date and time generated: " + dateTimeGenerated);
		sb.append("\r\n");
		sb.append("####################");
		sb.append("\r\n");

		return sb.toString();
	}
	@Override
	public void doLineParse(String line) {
		// TODO Auto-generated method stub
		if(line.startsWith(ConstantsForCycDatParser.HEADER_SPECIES))
			species = getSpecies(line);
		if(line.startsWith(ConstantsForCycDatParser.HEADER_DATABASE))
			database = getDataBase(line);
		if(line.startsWith(ConstantsForCycDatParser.HEADER_VERSION))
			version = getVersion(line);
		if(line.startsWith(ConstantsForCycDatParser.HEADER_FILE_NAME))
			fileName = getFileName(line);
		if(line.startsWith(ConstantsForCycDatParser.HEADER_DATE_TIME_GENERATED))
			dateTimeGenerated = getDateTimeGenerated(line);

		
		if(line.startsWith(ConstantsForCycDatParser.UNIQUE_ID))
		{
			String uniqueID = getUniqueID(line);
			createReactoinDB(uniqueID);
		}
		
		if(line.startsWith(ConstantsForCycDatParser.COMMON_NAME))
			addCommonName(line);
		
		if(line.startsWith(ConstantsForCycDatParser.REACTIONS_ENZYMATIC_REACTION))
			addEnzrxnID(line);

		if(line.startsWith(ConstantsForCycDatParser.REACTIONS_LEFT_COMPOUND))
			addLeftCompound(line);
		
		if(line.startsWith(ConstantsForCycDatParser.REACTIONS_RIGHT_COMPOUND))
			addRightCompound(line);
		
		if(line.startsWith(ConstantsForCycDatParser.ITEM_END_DELIMITER))
			addReactionDBIntoMap();
	}
	
	
	private String getSpecies(String line) {
		// TODO Auto-generated method stub
		int tempStartIndex = line.indexOf(ConstantsForCycDatParser.HEADER_SPECIES);
		String value = line.substring(tempStartIndex+ConstantsForCycDatParser.HEADER_SPECIES.length()).trim();
		return value;
	}

	private String getDataBase(String line) {
		// TODO Auto-generated method stub
		int tempStartIndex = line.indexOf(ConstantsForCycDatParser.HEADER_DATABASE);
		String value = line.substring(tempStartIndex+ConstantsForCycDatParser.HEADER_DATABASE.length()).trim();
		return value;
	}

	private String getVersion(String line) {
		// TODO Auto-generated method stub
		int tempStartIndex = line.indexOf(ConstantsForCycDatParser.HEADER_VERSION);
		String value = line.substring(tempStartIndex+ConstantsForCycDatParser.HEADER_VERSION.length()).trim();
		return value;
	}

	private String getFileName(String line) {
		// TODO Auto-generated method stub
		int tempStartIndex = line.indexOf(ConstantsForCycDatParser.HEADER_FILE_NAME);
		String value = line.substring(tempStartIndex+ConstantsForCycDatParser.HEADER_FILE_NAME.length()).trim();
		return value;
	}

	private String getDateTimeGenerated(String line) {
		// TODO Auto-generated method stub
		int tempStartIndex = line.indexOf(ConstantsForCycDatParser.HEADER_DATE_TIME_GENERATED);
		String value = line.substring(tempStartIndex+ConstantsForCycDatParser.HEADER_DATE_TIME_GENERATED.length()).trim();
		return value;
	}

	
	private void addEnzrxnID(String line) {
		// TODO Auto-generated method stub
		String tempString = line.substring(ConstantsForCycDatParser.REACTIONS_ENZYMATIC_REACTION.length()).trim();
		reactionDB.addEnzymaticReactionID(tempString);
	}

	private void addLeftCompound(String line) {
		// TODO Auto-generated method stub
		String tempString = line.substring(ConstantsForCycDatParser.REACTIONS_LEFT_COMPOUND.length()).trim();
		reactionDB.addPrimaryCompoundName(tempString);
	}
	
	private void addRightCompound(String line) {
		// TODO Auto-generated method stub
		String tempString = line.substring(ConstantsForCycDatParser.REACTIONS_RIGHT_COMPOUND.length()).trim();
		reactionDB.addPrimaryCompoundName(tempString);
	}
	
	private void addCommonName(String line) {
		// TODO Auto-generated method stub
		String tempString = line.substring(ConstantsForCycDatParser.COMMON_NAME.length()).trim();
		reactionDB.setCommonName(tempString);
	}
	
	private void addReactionDBIntoMap() {
		// TODO Auto-generated method stub
		reactionDBMap.put(reactionDB.getUniqueID(), reactionDB);
	}
	
	private void createReactoinDB(String uniqueID) {
		// TODO Auto-generated method stub
		reactionDB = new ReactionDB(uniqueID);
		totalNumOfReactions++;
	}

	private String getUniqueID(String line) {
		// TODO Auto-generated method stub
		int tempStartIndex = line.indexOf(ConstantsForCycDatParser.UNIQUE_ID);
		String uniqueID = line.substring(tempStartIndex+ConstantsForCycDatParser.UNIQUE_ID.length()).trim();

		return uniqueID;
	}

	@Override
	public void doAfterParse() {
		// TODO Auto-generated method stub
//		System.out.println("Number of class(compound) associated in a reaction: " + tempNodeMap.size());
		System.out.println("Total Number of Reations (ReationsParser) : " + totalNumOfReactions);
		
		//test
//		printReactionDBMap();
	}

	private void printReactionDBMap() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		Iterator<ReactionDB> iterator = reactionDBMap.values().iterator();
		while(iterator.hasNext())
		{
			ReactionDB reactionDB = iterator.next();
			sb.append(reactionDB.getUniqueID());
			sb.append("\t");
			sb.append(reactionDB.getCommonName());
			sb.append("\t");
			sb.append(reactionDB.getPrimaryCompoundList());
			sb.append("\r\n");
		}
		
		try {
			PrintUtil.saveStringIntoFile("reactionDat_parsed.txt", sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return reactionDBMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
}
