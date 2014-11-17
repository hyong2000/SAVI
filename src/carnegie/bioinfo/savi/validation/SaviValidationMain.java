package carnegie.bioinfo.savi.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import carnegie.bioinfo.common.ConstantsForCycDatParser;
import carnegie.bioinfo.common.ConstantsForEvidenceCode;
import carnegie.bioinfo.common.ConstantsForGeneralPurpose;
import carnegie.bioinfo.common.VersionParameters;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.common.util.Logger;
import carnegie.bioinfo.common.util.PrintUtil;
import carnegie.bioinfo.metacyc.parser.MetacycPathwayDatParser;
import carnegie.bioinfo.metacyc.parser.MetacycReactionDatParser;
import carnegie.bioinfo.metacyc.parser.MetacycSpeciesDatParser;
import carnegie.bioinfo.metacyc.parser.PathologicInputParser;
import carnegie.bioinfo.metacyc.parser.fileDB.PathologicDB;
import carnegie.bioinfo.metacyc.parser.fileDB.PathwayDB;
import carnegie.bioinfo.metacyc.parser.fileDB.ProteinDB;
import carnegie.bioinfo.metacyc.parser.fileDB.ReactionDB;
import carnegie.bioinfo.metacyc.parser.fileDB.SpeciesDB;
import carnegie.bioinfo.savi.SaviMain;
import carnegie.bioinfo.savi.input.parser.AippParser;
import carnegie.bioinfo.savi.input.parser.CappParser;
import carnegie.bioinfo.savi.input.parser.CvpParser;
import carnegie.bioinfo.savi.input.parser.ManualParser;
import carnegie.bioinfo.savi.input.parser.NppParser;
import carnegie.bioinfo.savi.input.parser.TaxonomyParser;
import carnegie.bioinfo.savi.input.parser.UppParser;
import carnegie.bioinfo.savi.input.parser.fileDB.AbUppCvpDB;
import carnegie.bioinfo.savi.input.parser.fileDB.AippDB;
import carnegie.bioinfo.savi.input.parser.fileDB.CappDB;
import carnegie.bioinfo.savi.input.parser.fileDB.ManualDB;


public class SaviValidationMain {

	/**
	 * @param args
	 */
	public static String outputFolderName = "./output/";
	
	public static String acceptedFileName = outputFolderName + "Accepted.txt";
	public static String rejectedFileName = outputFolderName + "Rejected.txt";
	public static String manualValidationFileName = outputFolderName + "Manual-to-validate.txt";
	public static String validationReportFileName = outputFolderName + "validationReport.txt";
	
	public StringBuffer resultSb = new StringBuffer(); 
	
	public HashSet<String> superpathwaySet = new HashSet<String>();
	public HashMap<String, PathwayDB> e2p2PathwayDBMap = null;
	public HashMap<String, PathwayDB> pgdbPathwayDBNotInE2p2Map = null;
	public HashMap<String, PathwayDB> initPgdbPathwayDBMap = null;

	public HashSet<String> accpetedPathwayIDSet = new HashSet<String>();
	public HashSet<String> rejectedPathwayIDSet = new HashSet<String>();
	public HashSet<String> manualPathwayIDSet = new HashSet<String>();

//	public double numOfAcceptedPathway = 0;
//	public double numOfRejectedPathway = 0;
//	public double numOfPGDBPathway = 0;
	
	public double numOfTruePositive = 0;
	public double numOfFalsePositive = 0;
	public double numOfFalseNegative = 0;
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		ConstantsForGeneralPurpose.LOGGING = false;
//		System.out.println("Last update date: 04/12/2013 (SAVI validation 1.0)");
//		System.out.println("Last update date: 05/07/2013 (SAVI validation 1.01)");
//		System.out.println("Last update: 06/28/2013 (SAVI validation 1.02)");
		System.out.println("Last update: 05/20/2014 (SAVI validation 1.03)");
		
		if (args.length != 2) {
			System.out.println("Run this program as following");
			System.out.println("java -classpath . carnegie.bioinfo.savi.SaviValidationMain inputFolder outputFolder");
			System.out.println("EX: java -classpath . carnegie.bioinfo.savi.SaviValidationMain ./input/. ./output/");
			System.exit(1);
		}
		
		if(args.length == 2){
			SaviMain.speciesInputFolderName = args[0];
			outputFolderName = args[1];
			System.out.println("Input folder name: " + SaviMain.speciesInputFolderName);
			System.out.println("Output folder name: " + outputFolderName);	
		}
		
		
		acceptedFileName = outputFolderName + "Accepted.txt";
		rejectedFileName = outputFolderName + "Rejected.txt";
		manualValidationFileName = outputFolderName + "Manual-to-validate.txt";
		validationReportFileName = outputFolderName + "validationReport.txt";
		
		SaviValidationMain saviValidationMain = new SaviValidationMain();
		saviValidationMain.initData();
		
		//validate pathways per each pathwayID
		Iterator<String> keyIterator = saviValidationMain.e2p2PathwayDBMap.keySet().iterator();
		while(keyIterator.hasNext())
		{
			String pathwayID = keyIterator.next();
			
			saviValidationMain.printPathway(pathwayID);

			if(saviValidationMain.pgdbPathwayDBNotInE2p2Map.containsKey(pathwayID))
				saviValidationMain.pgdbPathwayDBNotInE2p2Map.remove(pathwayID);
		}
		
		saviValidationMain.addLeftoverPGDBList();
		saviValidationMain.generateSummaryReport();
		saviValidationMain.saveResult();
	}

	private void addLeftoverPGDBList() {
		// TODO Auto-generated method stub
		Iterator<PathwayDB> pathwayIterator = pgdbPathwayDBNotInE2p2Map.values().iterator();
		while(pathwayIterator.hasNext()){
			PathwayDB pathwayDB = pathwayIterator.next();
			resultSb.append("");
			resultSb.append("\t");
			resultSb.append(pathwayDB.getUniqueID());
			resultSb.append("\t");
			resultSb.append(pathwayDB.getCommonNameList());
			resultSb.append("\t");
			resultSb.append(pathwayDB.getEvidenceCodeList());
			resultSb.append("\t");
			resultSb.append(isInAcceptedList(pathwayDB.getUniqueID()));
			resultSb.append("\t");
			resultSb.append(isInRejectedList(pathwayDB.getUniqueID()));
			resultSb.append("\t");
			resultSb.append(isInManualList(pathwayDB.getUniqueID()));
			resultSb.append("\t");
			resultSb.append(isTruePositive(pathwayDB.getUniqueID()));
			resultSb.append("\t");
			resultSb.append(isFalsePositive(pathwayDB.getUniqueID()));
			resultSb.append("\t");
			resultSb.append(isFalseNegative(pathwayDB.getUniqueID()));
			resultSb.append("\r\n");
		}
	}

	private void saveResult() throws Exception {
		// TODO Auto-generated method stub
		PrintUtil.saveStringIntoFile(outputFolderName + "compareResultWithPGDB.txt", resultSb.toString());
	}

	private void printPathway(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		resultSb.append(pathwayID);
		resultSb.append("\t");
		resultSb.append(getPGDBPathwayID(pathwayID));
		resultSb.append("\t");
		resultSb.append(getPGDBPathwayCommonName(pathwayID));
		resultSb.append("\t");
		resultSb.append(getPGDBPathwayEvidenceCodeList(pathwayID));
		resultSb.append("\t");
		resultSb.append(isInAcceptedList(pathwayID));
		resultSb.append("\t");
		resultSb.append(isInRejectedList(pathwayID));
		resultSb.append("\t");
		resultSb.append(isInManualList(pathwayID));
		resultSb.append("\t");
		resultSb.append(isTruePositive(pathwayID));
		resultSb.append("\t");
		resultSb.append(isFalsePositive(pathwayID));
		resultSb.append("\t");
		resultSb.append(isFalseNegative(pathwayID));
		resultSb.append("\r\n");

	}

	private boolean isFalseNegative(String pathwayID) {
		// TODO Auto-generated method stub
//		if((getPGDBPathwayID(pathwayID).length() != 0 && !isInPredictedPathwayList(pathwayID))
//			|| (getPGDBPathwayID(pathwayID).length() != 0 && isInPredictedPathwayList(pathwayID) && isInRejectedList(pathwayID))){
		if(initPgdbPathwayDBMap.containsKey(pathwayID) && !isInAcceptedList(pathwayID)){
			numOfFalseNegative++;
			return true;
		} else
			return false;
	}

	private boolean isFalsePositive(String pathwayID) {
		// TODO Auto-generated method stub
//		if(getPGDBPathwayID(pathwayID).length() == 0 && isInAcceptedList(pathwayID)){
//		if(getPGDBPathwayID(pathwayID).length() == 0 && isInPredictedPathwayList(pathwayID) && isInAcceptedList(pathwayID)){
		if(!initPgdbPathwayDBMap.containsKey(pathwayID) && isInAcceptedList(pathwayID)){
			numOfFalsePositive++;
			return true;
		} else
			return false;
	}

	private boolean isTruePositive(String pathwayID) {
		// TODO Auto-generated method stub
//		if(getPGDBPathwayID(pathwayID).length() != 0 && isInAcceptedList(pathwayID)){
//		if(getPGDBPathwayID(pathwayID).length() != 0 && isInPredictedPathwayList(pathwayID) && isInAcceptedList(pathwayID)){
		if(initPgdbPathwayDBMap.containsKey(pathwayID) && isInAcceptedList(pathwayID)){
			numOfTruePositive++;
			return true;
		} else
			return false;
	}

	private boolean isInManualList(String pathwayID) {
		// TODO Auto-generated method stub
		return manualPathwayIDSet.contains(pathwayID);
	}

	private boolean isInRejectedList(String pathwayID) {
		// TODO Auto-generated method stub
		if(rejectedPathwayIDSet.contains(pathwayID)){
//			numOfRejectedPathway++;
			return true;
		} else
			return false;
	}

	private boolean isInAcceptedList(String pathwayID) {
		// TODO Auto-generated method stub
		if(accpetedPathwayIDSet.contains(pathwayID)){
//			numOfAcceptedPathway++;
			return true;
		} else
			return false;
	}
	
	private boolean isInPredictedPathwayList(String pathwayID) {
		// TODO Auto-generated method stub
		if(e2p2PathwayDBMap.containsKey(pathwayID)){
			return true;
		} else
			return false;
	}

	private Object getPGDBPathwayEvidenceCodeList(String pathwayID) {
		// TODO Auto-generated method stub
		if(pgdbPathwayDBNotInE2p2Map.containsKey(pathwayID))
			return pgdbPathwayDBNotInE2p2Map.get(pathwayID).getEvidenceCodeList();
		return "";
	}

	private Object getPGDBPathwayCommonName(String pathwayID) {
		// TODO Auto-generated method stub
		if(pgdbPathwayDBNotInE2p2Map.containsKey(pathwayID))
			return pgdbPathwayDBNotInE2p2Map.get(pathwayID).getCommonNameList();
		return "";
	}

	private String getPGDBPathwayID(String pathwayID) {
		// TODO Auto-generated method stub
		if(pgdbPathwayDBNotInE2p2Map.containsKey(pathwayID))
			return pathwayID;
		return "";
	}

	private void generateSummaryReport() throws IOException {
		// TODO Auto-generated method stub
		resultSb.append("\r\n");
		resultSb.append("\r\n");
		resultSb.append("SAVI-assessment-stats.txt	(report as \" x / y\")	(report as \" x / y\")");
		resultSb.append("\r\n");
		resultSb.append("\"True positive\" predicted pathways (# of true positive/# of predicted pathways)\t" + numOfTruePositive + "/" + e2p2PathwayDBMap.size() + "\t" + numOfTruePositive/e2p2PathwayDBMap.size());
		resultSb.append("\r\n");
		resultSb.append("\"False positive\" predicted pathways(# of false positive/# of predicted pathways)\t" + numOfFalsePositive + "/" + e2p2PathwayDBMap.size() + "\t" + numOfFalsePositive/e2p2PathwayDBMap.size());
		resultSb.append("\r\n");
		resultSb.append("\"False negative\" predicted pathways(# of false negative/# of predicted pathways)\t" + numOfFalseNegative + "/" + e2p2PathwayDBMap.size() + "\t" + numOfFalseNegative/e2p2PathwayDBMap.size());
		resultSb.append("\r\n");
		resultSb.append("Percentage of \"True positive\" predicted pathways in the released version\t" + numOfTruePositive + "/" + initPgdbPathwayDBMap.size() + "\t" + numOfTruePositive/initPgdbPathwayDBMap.size());
		resultSb.append("\r\n");
	}

	private void initData() throws IOException {
		// TODO Auto-generated method stub
		IParser accpetedPathwayIDParser = new FirstTokenParser(acceptedFileName);
		accpetedPathwayIDParser.parse();
		accpetedPathwayIDSet.addAll(accpetedPathwayIDParser.getResultCollection());

		IParser rejectedPathwayIDParser = new FirstTokenParser(rejectedFileName);
		rejectedPathwayIDParser.parse();
		rejectedPathwayIDSet.addAll(rejectedPathwayIDParser.getResultCollection());

		IParser manualPathwayIDParser = new FirstTokenParser(manualValidationFileName);
		manualPathwayIDParser.parse();
		manualPathwayIDSet.addAll(manualPathwayIDParser.getResultCollection());

		IParser e2p2MetacycPathwayDatParser = new MetacycPathwayDatParser(SaviMain.speciesInputFolderName + "pathways.dat");
		e2p2MetacycPathwayDatParser.parse();
		e2p2PathwayDBMap = e2p2MetacycPathwayDatParser.getResultHashMap();

//		IParser pgdbMetacycPathwayDatParser = new MetacycPathwayDatParser("./input/validation/" + "pathways_pgdb.dat");
		IParser pgdbMetacycPathwayDatParser = new MetacycPathwayDatParser(SaviMain.speciesInputFolderName + "pathways_pgdb.dat");
		pgdbMetacycPathwayDatParser.parse();
		pgdbPathwayDBNotInE2p2Map = pgdbMetacycPathwayDatParser.getResultHashMap();
		initPgdbPathwayDBMap = (HashMap<String, PathwayDB>)pgdbPathwayDBNotInE2p2Map.clone();
		
		resultSb.append("PWY-ID in pathways.dat	present in previously released PGDB?	Common name	Evidence code in released PGDB	in accepted.txt	in rejected.txt	in manual.txt	True positive	False positive	False negative\r\n");

	}
}
