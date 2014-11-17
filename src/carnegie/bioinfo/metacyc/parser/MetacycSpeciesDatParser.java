package carnegie.bioinfo.metacyc.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import carnegie.bioinfo.common.ConstantsForCycDatParser;
import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.common.util.PrintUtil;
import carnegie.bioinfo.metacyc.parser.fileDB.SpeciesDB;

public class MetacycSpeciesDatParser extends AbstractParser{

	public static String species = "";
	public static String database = "";
	public static String version = "";
	public static String fileName = "";
	public static String dateTimeGenerated = "";

	private int totalNumOfSpecies = 0;
	private HashMap<String,SpeciesDB> speciesDBMap = new HashMap<String,SpeciesDB>();
	private SpeciesDB speciesDB = null;
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		IParser parser = new MetacycSpeciesDatParser("./input/dat/" + "species.dat");
		parser.parse();
	}
	
	public MetacycSpeciesDatParser(String fileName)
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
			createSpeciesDB(uniqueID);
		}
		
		if(line.startsWith(ConstantsForCycDatParser.COMMON_NAME))
			addCommonName(line);
		
		if(line.startsWith(ConstantsForCycDatParser.TAGS_TYPES))
			addTypes(line);

		if(line.startsWith(ConstantsForCycDatParser.NCBI_TAXONOMY_ID))
		{
			addTaxonomyID(line);
			addSpeciesDBIntoMap();
		}
	
		//if there is no end delimeter, it will be problem
//		if(line.startsWith(ConstantsForCycDatParser.ITEM_END_DELIMITER))
//			addSpeciesDBIntoMap();
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

	
	
	private void addTaxonomyID(String line) {
		// TODO Auto-generated method stub
		String taxonomyID = line.substring(ConstantsForCycDatParser.NCBI_TAXONOMY_ID.length()).trim();
		speciesDB.setNcbiTaxonomyID(taxonomyID);
	}

	private void addSpeciesDBIntoMap() {
		// TODO Auto-generated method stub
		speciesDBMap.put(speciesDB.getUniqueID(), speciesDB);
	}
	
	private void addTypes(String line) {
		// TODO Auto-generated method stub
		String type = line.substring(ConstantsForCycDatParser.TAGS_TYPES.length()).trim();
		speciesDB.setType(type);
	}
	
	private void addCommonName(String line) {
		// TODO Auto-generated method stub
		String tempString = line.substring(ConstantsForCycDatParser.COMMON_NAME.length()).trim();
		speciesDB.setCommonName(tempString);
	}
	
	private void createSpeciesDB(String uniqueID) {
		// TODO Auto-generated method stub
		speciesDB = new SpeciesDB(uniqueID);
		totalNumOfSpecies++;
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
		System.out.println("Total Number of Species (MetacycSpeciesDatParser): " + totalNumOfSpecies);
		
		//test
//		printClassesDBMap();
	}

	private void printClassesDBMap() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		Iterator<SpeciesDB> iterator = speciesDBMap.values().iterator();
		while(iterator.hasNext())
		{
			SpeciesDB speciesDB = iterator.next();
			sb.append(speciesDB.getUniqueID());
			sb.append("\t");
			sb.append(speciesDB.getType());
			sb.append("\t");
			sb.append(speciesDB.getCommonName());
			sb.append("\t");
			sb.append(speciesDB.getNcbiTaxonomyID());
			sb.append("\r\n");
		}
		
		try {
			PrintUtil.saveStringIntoFile("speciesDat_parsed.txt", sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return speciesDBMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}
}
