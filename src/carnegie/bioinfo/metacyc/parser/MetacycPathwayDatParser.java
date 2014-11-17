package carnegie.bioinfo.metacyc.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.ConstantsForCycDatParser;
import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.common.util.PrintUtil;
import carnegie.bioinfo.metacyc.parser.fileDB.PathwayDB;

public class MetacycPathwayDatParser extends AbstractParser{

	private String outputFolder = "";

	private String species = "";
	private String database = "";
	private String version = "";
//	private String fileName = "";
	private String dateTimeGenerated = "";

	
	public boolean isPrintPathwayID = false;
	public String PathwayListFileName = "";

	private int totalNumOfPathway = 0;
	
	private HashMap<String,PathwayDB> pathwayDBMap = new HashMap<String,PathwayDB>();
	private PathwayDB pathwayDB = null;

	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
//		IParser parser = new MetacycPathwayDatParser("./input/dat/" + "pathways.dat");
		IParser parser = new MetacycPathwayDatParser("./input/validation/" + "pathways_pgdb.dat");
		parser.parse();
		
	}
	
	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}
	
	public MetacycPathwayDatParser(String fileName)
	{
//		super.fileName = fileName;
		super(fileName);
	}
	
	public String getFileInfo()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n");
		sb.append("####################");
		sb.append("\r\n");
		sb.append("fileName: " + this.fileName);
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
//		if(line.startsWith(ConstantsForCycDatParser.HEADER_FILE_NAME))
//			fileName = getFileName(line);
		if(line.startsWith(ConstantsForCycDatParser.HEADER_DATE_TIME_GENERATED))
			dateTimeGenerated = getDateTimeGenerated(line);
		
		
		if(line.startsWith(ConstantsForCycDatParser.UNIQUE_ID))
		{
			String uniqueID = getUniqueID(line);
			createPathwayDB(uniqueID);
		}
		
		if(line.startsWith(ConstantsForCycDatParser.PATHWAYS_COMMON_NAME))
			addCommonName(line);
		
		if(line.startsWith(ConstantsForCycDatParser.CITATIONS)){
			addEvidenceCode(line);
			addCitations(line);
		}
		
		if(line.startsWith(ConstantsForCycDatParser.TAGS_TYPES))
			addTypes(line);
		
		if(line.startsWith(ConstantsForCycDatParser.PATHWAYS_REACTION_LIST))
			addReactionList(line);

		if(line.startsWith(ConstantsForCycDatParser.PATHWAYS_SUPER_PATHWAYS))
			addSuperPathwayList(line);

		if(line.startsWith(ConstantsForCycDatParser.PATHWAYS_SUB_PATHWAYS))
			addSubPathwayList(line);

		if(line.startsWith(ConstantsForCycDatParser.ITEM_END_DELIMITER))
			addPathwayDBIntoMap();

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
	
	private void addCitations(String line) {
		// TODO Auto-generated method stub
		String citations = line.substring(ConstantsForCycDatParser.CITATIONS.length()).trim();
		pathwayDB.addCitation(citations);
	}

	private void addEvidenceCode(String line) {
		// TODO Auto-generated method stub
		int tempStartIndex = line.indexOf(ConstantsForCycDatParser.CITATIONS);
		String citation = line.substring(tempStartIndex+ConstantsForCycDatParser.CITATIONS.length()).trim();
		StringTokenizer st = new StringTokenizer(citation, ":");
		String reference = st.nextToken().trim();
		if (reference.startsWith("EV-"))	//for the case of empty reference 
			pathwayDB.addEvidenceCode(reference);
		else if(st.hasMoreTokens()){
			String evidenceCode = st.nextToken().trim();
			pathwayDB.addEvidenceCode(evidenceCode);
		}
//		System.out.println(pathwayDB.getEvidenceCodeList());
	}

	private void addTypes(String line) {
		// TODO Auto-generated method stub
		String type = line.substring(ConstantsForCycDatParser.TAGS_TYPES.length()).trim();
		pathwayDB.addType(type);
	}

	private void addPathwayDBIntoMap() {
		// TODO Auto-generated method stub
		pathwayDBMap.put(pathwayDB.getUniqueID(), pathwayDB);
	}

	private void createPathwayDB(String uniqueID) {
		// TODO Auto-generated method stub
		totalNumOfPathway++;
		pathwayDB = new PathwayDB();
		pathwayDB.setUniqueID(uniqueID);
	}

	private void addReactionList(String line) {
		// TODO Auto-generated method stub
		String tempString = line.substring(ConstantsForCycDatParser.PATHWAYS_REACTION_LIST.length()).trim();
		pathwayDB.addReactionName(tempString.trim());
	}
	
	private void addSuperPathwayList(String line) {
		// TODO Auto-generated method stub
		String tempString = line.substring(ConstantsForCycDatParser.PATHWAYS_SUPER_PATHWAYS.length()).trim();
		pathwayDB.addSuperPathway(tempString.trim());
	}
	
	private void addSubPathwayList(String line) {
		// TODO Auto-generated method stub
		String tempString = line.substring(ConstantsForCycDatParser.PATHWAYS_SUB_PATHWAYS.length()).trim();
		pathwayDB.addSubPathway(tempString.trim());
	}

	private void addCommonName(String line) {
		// TODO Auto-generated method stub
		String tempString = line.substring(ConstantsForCycDatParser.PATHWAYS_COMMON_NAME.length()).trim();
		pathwayDB.addCommonName(tempString.trim());
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
		System.out.println("Total Number of Pathway (PathwayParser): in " + fileName + ": " + totalNumOfPathway);
		
		//test
//		printPathwayDBMap();
		if(isPrintPathwayID)
			printPathwayID();
	}

	private void printPathwayDBMap() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		Iterator<PathwayDB> iterator = pathwayDBMap.values().iterator();
		while(iterator.hasNext())
		{
			PathwayDB pathwayDB = (PathwayDB)iterator.next();
			
			Iterator<String> compoundIterator = pathwayDB.getReactionIDList().iterator();
			while(compoundIterator.hasNext())
			{
				String compoundName = (String)compoundIterator.next();
				sb.append(pathwayDB.getUniqueID() + ": (" + compoundName + ")");
				sb.append("\r\n");
				if(pathwayDB.getCommonNameList().size() > 1)
					System.err.println("Num of common Name: " + pathwayDB.getCommonNameList().size());
				else if (pathwayDB.getCommonNameList().size() == 0)
					System.err.println("No common name: " + pathwayDB.getUniqueID());
			}
		}
		
		try {
			PrintUtil.saveStringIntoFile("pathwaysDat_parsed.txt", sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void printPathwayID() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("PathwayID");
		sb.append("\t");
		sb.append("Pathway Name");
		sb.append("\r\n");
		Iterator<PathwayDB> iterator = pathwayDBMap.values().iterator();
		while(iterator.hasNext())
		{
			PathwayDB pathwayDB = (PathwayDB)iterator.next();
			sb.append(pathwayDB.getUniqueID());
			sb.append("\t");
			sb.append(pathwayDB.getCommonNameList());
			sb.append("\r\n");
		}
		
		try {
			PrintUtil.saveStringIntoFile(outputFolder + PathwayListFileName, sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return pathwayDBMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
