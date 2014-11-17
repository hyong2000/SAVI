package carnegie.bioinfo.savi.filegeneration;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import carnegie.bioinfo.common.ConstantsForGeneralPurpose;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.common.util.Logger;
import carnegie.bioinfo.common.util.PrintUtil;


public class SaviPgdbInputGenerationMain {

	/**
	 * @param args
	 */
	public static String outputFolder = "./output/";

	public static String acceptedFileName = outputFolder + "Accepted.txt";
	public static String rejectedFileName = outputFolder + "Rejected.txt";
	
	public HashMap<String, AcceptedListDB> acceptedListMap = new HashMap<String, AcceptedListDB>();
	public HashSet<String> rejectedIdSet = new HashSet<String>();
	
	public static void main(String[] args) throws Exception {
		ConstantsForGeneralPurpose.LOGGING = false;
//		System.out.println("Last updates: 06/28/2013 (SAVI PGDB input file generation 1.0)");
//		System.out.println("Last updates: 05/20/2014 (SAVI PGDB input file generation 1.01)");		
		System.out.println("Last updates: 08/15/2014 (SAVI PGDB input file generation 1.02)");		
		
		if (args.length != 1) {
			System.out.println("Run this program as following");
			System.out.println("java -classpath . carnegie.bioinfo.savi.SaviPgdbInputGenerationMain outputFolder");
			System.out.println("EX: java -classpath . carnegie.bioinfo.savi.SaviPgdbInputGenerationMain ./output/");
			System.exit(1);
		}
		
		if(args.length == 1){ 
			outputFolder = args[0];
			System.out.println("Output folder name: " + outputFolder);	
		}
		acceptedFileName = outputFolder + "Accepted.txt";
		rejectedFileName = outputFolder + "Rejected.txt";
		
		SaviPgdbInputGenerationMain saviValidationMain = new SaviPgdbInputGenerationMain();
		saviValidationMain.initData();
		saviValidationMain.createInputPgdbInputFiles();
	}

	private void createInputPgdbInputFiles() throws Exception {
		createAippFile();
		createIcFile();
		createCompUppFile();
		createCompCvpFile();
		createSuperFile();
		createCompRxnWarningFile();
		createCompTaxonFile();
		createCompRxnTaxonFile();
		createCompRxnFile();
		createRemoveFile();
		
		Logger.println("Number of pathways Not in any output files: " + acceptedListMap.size());
	}


	private void createAippFile() throws IOException {
		int numOfPathways = 0;
		StringBuffer sb = new StringBuffer();
		Iterator<AcceptedListDB> acceptedListDBIterator = acceptedListMap.values().iterator();
		while(acceptedListDBIterator.hasNext())
		{
			AcceptedListDB acceptedListDB = acceptedListDBIterator.next();
			if(acceptedListDB.getRef().contains("AIPP"))
			{
				sb.append(acceptedListDB.getPathwayId());
				sb.append("\r\n");
				numOfPathways++;
				acceptedListDBIterator.remove();
			}
		}
		System.out.println("Number of pathways in aipp.txt: " + numOfPathways);
		PrintUtil.saveStringIntoFile(outputFolder + "aipp.txt", sb.toString());
	}

	private void createIcFile() throws IOException {
		int numOfPathways = 0;
		StringBuffer sb = new StringBuffer();
		Iterator<AcceptedListDB> acceptedListDBIterator = acceptedListMap.values().iterator();
		while(acceptedListDBIterator.hasNext())
		{
			AcceptedListDB acceptedListDB = acceptedListDBIterator.next();
//revised for CVP-IC for non-plants
//			if(acceptedListDB.getRef().contains("UPP-IC"))
			if(acceptedListDB.getRef().contains("UPP-IC") || acceptedListDB.getRef().contains("CVP-IC"))
			{
				sb.append(acceptedListDB.getPathwayId());
				sb.append("\r\n");
				numOfPathways++;
				acceptedListDBIterator.remove();
			}
		}
		System.out.println("Number of pathways in ic.txt: " + numOfPathways);
		PrintUtil.saveStringIntoFile(outputFolder + "ic.txt", sb.toString());
	}

	private void createCompUppFile() throws IOException {
		int numOfPathways = 0;
		StringBuffer sb = new StringBuffer();
		Iterator<AcceptedListDB> acceptedListDBIterator = acceptedListMap.values().iterator();
		while(acceptedListDBIterator.hasNext())
		{
			AcceptedListDB acceptedListDB = acceptedListDBIterator.next();
			if(acceptedListDB.getRef().contains("UPP-HINF"))
			{
				sb.append(acceptedListDB.getPathwayId());
				sb.append("\r\n");
				numOfPathways++;
				acceptedListDBIterator.remove();
			}
		}
		System.out.println("Number of pathways in comp-upp.txt: " + numOfPathways);
		
		if(sb.length() != 0)
			PrintUtil.saveStringIntoFile(outputFolder + "comp-upp.txt", sb.toString());
	}

	//added for CVP-COMP for non-plants
	private void createCompCvpFile() throws IOException {
		int numOfPathways = 0;
		StringBuffer sb = new StringBuffer();
		Iterator<AcceptedListDB> acceptedListDBIterator = acceptedListMap.values().iterator();
		while(acceptedListDBIterator.hasNext())
		{
			AcceptedListDB acceptedListDB = acceptedListDBIterator.next();
			if(acceptedListDB.getRef().contains("CVP-COMP"))
			{
				sb.append(acceptedListDB.getPathwayId());
				sb.append("\r\n");
				numOfPathways++;
				acceptedListDBIterator.remove();
			}
		}
		System.out.println("Number of pathways in comp-cvp.txt: " + numOfPathways);
		
		if(sb.length() != 0)
			PrintUtil.saveStringIntoFile(outputFolder + "comp-cvp.txt", sb.toString());
	}

	private void createSuperFile() throws IOException {
		int numOfPathways = 0;
		StringBuffer sb = new StringBuffer();
		Iterator<AcceptedListDB> acceptedListDBIterator = acceptedListMap.values().iterator();
		while(acceptedListDBIterator.hasNext())
		{
			AcceptedListDB acceptedListDB = acceptedListDBIterator.next();
			if(acceptedListDB.getRef().contains("Superpathway - CAPP"))
			{
				sb.append(acceptedListDB.getPathwayId());
				sb.append("\r\n");
				numOfPathways++;
				acceptedListDBIterator.remove();
			}
		}
		System.out.println("Number of pathways in comp-super.txt: " + numOfPathways);
		PrintUtil.saveStringIntoFile(outputFolder + "comp-super.txt", sb.toString());
	}

	private void createCompRxnWarningFile() throws IOException {
		int numOfPathways = 0;
		StringBuffer sb = new StringBuffer();
		Iterator<AcceptedListDB> acceptedListDBIterator = acceptedListMap.values().iterator();
		while(acceptedListDBIterator.hasNext())
		{
			AcceptedListDB acceptedListDB = acceptedListDBIterator.next();
			if(acceptedListDB.getTaxonWarningComment().contains("Failed: TAXON-WARNING needed"))
			{
				sb.append(acceptedListDB.getPathwayId());
				sb.append("\r\n");
				numOfPathways++;
				acceptedListDBIterator.remove();
			}
		}
		System.out.println("Number of pathways in comp-rxn-warning.txt: " + numOfPathways);
		PrintUtil.saveStringIntoFile(outputFolder + "comp-rxn-warning.txt", sb.toString());
	}

	private void createCompTaxonFile() throws IOException {
		int numOfPathways = 0;
		StringBuffer sb = new StringBuffer();
		Iterator<AcceptedListDB> acceptedListDBIterator = acceptedListMap.values().iterator();
		while(acceptedListDBIterator.hasNext())
		{
			AcceptedListDB acceptedListDB = acceptedListDBIterator.next();
			if(acceptedListDB.getEvidencdCode().equals("EV-HINF-TAXON"))
			{
				sb.append(acceptedListDB.getPathwayId());
				sb.append("\r\n");
				numOfPathways++;
				acceptedListDBIterator.remove();
			}
		}
		System.out.println("Number of pathways in comp-taxon.txt: " + numOfPathways);
		PrintUtil.saveStringIntoFile(outputFolder + "comp-taxon.txt", sb.toString());
	}

	private void createCompRxnTaxonFile() throws IOException {
		int numOfPathways = 0;
		StringBuffer sb = new StringBuffer();
		Iterator<AcceptedListDB> acceptedListDBIterator = acceptedListMap.values().iterator();
		while(acceptedListDBIterator.hasNext())
		{
			AcceptedListDB acceptedListDB = acceptedListDBIterator.next();
			if(acceptedListDB.getEvidencdCode().equals("EV-HINF-TAXON-REACTION"))
			{
				sb.append(acceptedListDB.getPathwayId());
				sb.append("\r\n");
				numOfPathways++;
				acceptedListDBIterator.remove();
			}
		}
		System.out.println("Number of pathways in comp-rxn-taxon.txt: " + numOfPathways);
		PrintUtil.saveStringIntoFile(outputFolder + "comp-rxn-taxon.txt", sb.toString());
	}

	private void createCompRxnFile() throws IOException {
		int numOfPathways = 0;
		StringBuffer sb = new StringBuffer();
		Iterator<AcceptedListDB> acceptedListDBIterator = acceptedListMap.values().iterator();
		while(acceptedListDBIterator.hasNext())
		{
			AcceptedListDB acceptedListDB = acceptedListDBIterator.next();
			if(acceptedListDB.getEvidencdCode().equals("EV-HINF-REACTION"))
			{
				sb.append(acceptedListDB.getPathwayId());
				sb.append("\r\n");
				numOfPathways++;
				acceptedListDBIterator.remove();
			}
		}
		System.out.println("Number of pathways in comp-rxn.txt: " + numOfPathways);
		PrintUtil.saveStringIntoFile(outputFolder + "comp-rxn.txt", sb.toString());
	}

	private void createRemoveFile() throws IOException {
		int numOfPathways = 0;
		StringBuffer sb = new StringBuffer();
		Iterator<String> rejectedIdIterator = rejectedIdSet.iterator();
		while(rejectedIdIterator.hasNext())
		{
			String rejectedId = rejectedIdIterator.next();
			sb.append(rejectedId);
			sb.append("\r\n");
			numOfPathways++;
		}
		System.out.println("Number of pathways in remove.txt: " + numOfPathways);
		PrintUtil.saveStringIntoFile(outputFolder + "remove.txt", sb.toString());
	}

	private void initData() throws IOException {
		IParser accpetedPathwayIDParser = new AcceptedListParser(acceptedFileName);
		accpetedPathwayIDParser.parse();
		acceptedListMap = accpetedPathwayIDParser.getResultHashMap();
		
		IParser rejectedPathwayIDParser = new FirstTokenParser(rejectedFileName);
		rejectedPathwayIDParser.parse();
		rejectedIdSet.addAll(rejectedPathwayIDParser.getResultCollection());

	}
}
