package carnegie.bioinfo.savi;

import java.io.File;
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
import carnegie.bioinfo.savi.input.parser.AippParser;
import carnegie.bioinfo.savi.input.parser.CappParser;
import carnegie.bioinfo.savi.input.parser.CvpParser;
import carnegie.bioinfo.savi.input.parser.ProblemPathwaysParser;
import carnegie.bioinfo.savi.input.parser.ResultFileParser;
import carnegie.bioinfo.savi.input.parser.ManualParser;
import carnegie.bioinfo.savi.input.parser.NppParser;
import carnegie.bioinfo.savi.input.parser.TaxonomyParser;
import carnegie.bioinfo.savi.input.parser.UppParser;
import carnegie.bioinfo.savi.input.parser.fileDB.AbUppCvpDB;
import carnegie.bioinfo.savi.input.parser.fileDB.AippDB;
import carnegie.bioinfo.savi.input.parser.fileDB.CappDB;
import carnegie.bioinfo.savi.input.parser.fileDB.ManualDB;
import carnegie.bioinfo.savi.input.parser.fileDB.ProblemPathwaysDB;


public class SaviMain {

	/**
	 * @param args
	 */
	public static String globalInputFolderName = "./input/";
	public static String speciesInputFolderName = "./input/dat/";
	public static String outputFolderName = "./output/";
	
	public String curSpecies = null;
	public String acceptedFileName = outputFolderName + "Accepted.txt";
	public String rejectedFileName = outputFolderName + "Rejected.txt";
	public String manualValidationFileName = outputFolderName + "Manual-to-validate.txt";
	public String validationReportFileName = outputFolderName + "validationReport.txt";
//	public String expFileName = "./output/EXP.txt";
	public String previousPgdbFileName = outputFolderName + "PreviousPGDB.txt";
	
	public TaxonomyParser taxonomyManager = null;

	public HashMap<String, AbUppCvpDB> abUppCvpDBMap = null;
	public HashMap<String, AbUppCvpDB> uppMap = null;
	public HashMap<String, AbUppCvpDB> cvpMap = null;
	public HashMap<String, String> nppMap = null;
	public HashMap<String, AippDB> aippMap = null;
	public HashMap<String, CappDB> cappMap = null;
	public HashMap<String, ManualDB> manualMap = null;
	public HashMap<String, ProblemPathwaysDB> problemPathwaysMap = null;
	
	public HashSet<String> superpathwaySet = new HashSet<String>();
	public HashMap<String, PathwayDB> pathwayDBMap = null;
	public HashMap<String, PathwayDB> pathwayPgdbDBMap = null;
//	public HashMap<String, ProteinDB> proteinDBMap = null;
	public HashMap<String, ReactionDB> reactionDBMap = null;
	public HashMap<String, SpeciesDB> speciesDBMap = null;
	public HashMap<String, PathologicDB> pathologicDBMap = null;

	public HashSet<String> reactionIdWithEnzymeSet = null;

	public HashSet<String> accpetedPathwayIDSet = new HashSet<String>();
	public HashSet<String> rejectedPathwayIDSet = new HashSet<String>();
	public HashSet<String> manualPathwayIDSet = new HashSet<String>();
	public HashSet<String> expPathwayIDSet = new HashSet<String>();
	public HashSet<String> expPathwayIDInSuperpathwaySet = new HashSet<String>();
	public HashSet<String> previousPgdbIDSet = new HashSet<String>();

	//for back up dat file info for different pathways.dat
	private String pathwayFileInfo = "";
	private String pathwayPgdbFileInfo = "";
	
	//for self result check
	public HashMap<String, String> accpetedResultMapFromFile = new HashMap<String, String>();
	public HashMap<String, String> rejectedResultMapFromFile = new HashMap<String, String>();
	public HashMap<String, String> manualResultMapFromFile = new HashMap<String, String>();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ConstantsForGeneralPurpose.LOGGING = false;
		System.out.println("Last update: 11/xx/2014 (SAVI 4.0) implemented by Taehyong Kim");
		
		if (args.length < 2) {
			System.out.println("Run this program as following");
			System.out.println("java -classpath . carnegie.bioinfo.savi.SaviMain inputDATfolder outputFolder");
			System.out.println("EX: java -classpath . carnegie.bioinfo.savi.SaviMain ./input/dat/ ./output/");
			System.exit(1);
		}
		
		if(args.length >= 2){ 
			speciesInputFolderName = args[0];
			outputFolderName = args[1];
			System.out.println("Input folder name: " + speciesInputFolderName);
			System.out.println("Output folder name: " + outputFolderName);			
		}
		
		if(args.length == 3) 
			ConstantsForGeneralPurpose.LOGGING = Boolean.parseBoolean(args[2]);
		
		SaviMain saviMain = new SaviMain();
		saviMain.initData();
		
		//step 1: is plant(Embryophyta) or above plant in taxonomy?
		saviMain.setUppOrCvpDataBySpecies();
		
		//validate pathways per each pathwayID from pathways_pgdb.dat (for previous PGDB)
		Iterator<String> keyPgdbIterator = saviMain.pathwayPgdbDBMap.keySet().iterator();
		while(keyPgdbIterator.hasNext())
		{
			String pathwayID = keyPgdbIterator.next();
			
			//step 1.1-1.10 per each pathway
			saviMain.validatePgdbPathway(pathwayID);
		}

		//validate pathways per each pathwayID from pathways.dat (identifying accepted or rejected pathways for new PGDB)
		Iterator<String> keyIterator = saviMain.pathwayDBMap.keySet().iterator();
		while(keyIterator.hasNext())
		{
			String pathwayID = keyIterator.next();
			
			//step 2-13 per each pathway
			saviMain.validatePathway(pathwayID);
		}
		
		//step 14
		saviMain.addNotAcceptedUppOrCvpIntoAccptedList();

		//step 15.1 - 15.5
		saviMain.addNonUppSuperpathwayIntoAccptedList();		

		//step 16 (Taxon warning check)  //last step before generating report
		saviMain.isTaxonWarning();		

		//step 17 (generate report)
		saviMain.generateReport();
		
		//step 18
		saviMain.selfResultCheck();
	}

	private void validatePgdbPathway(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		//step 1.1
		if(isExperimentallyValidated(pathwayID)) return;

		//step 1.2
		if(isSuperpathwayExperimentallyValidated(pathwayID)) return;
		
		//step 1.3
		if(ignoreInPathwaysDat(pathwayID)) return;
		
		//step 1.4
		if(ignoreInUppOrCvp(pathwayID)) return;
		
		//step 1.5
		if(ignoreInNPP(pathwayID)) return;
		
		//step 1.6
		if(isSuperPathwayInPreviousPgdb(pathwayID)) return;

		//step 1.7
		if(isAippInPreviousPgdb(pathwayID)) return;

		//step 1.8
		if(isManualInPreviousPgdb(pathwayID)) return;

		//step 1.9
		if(isCappInPreviousPgdb(pathwayID)) return;
		
		//step 1.10
		addRestOfPathwayIntoPreviousPgdb(pathwayID);
	}

	private void validatePathway(String pathwayID) throws IOException {
		// TODO Auto-generated method stub

		//for 2
		if(isInEXP(pathwayID)) return;

		//for 3
		if(isInEXPSuperpathway(pathwayID)) return;
		
		//step 4
		if(isInUppOrCvp(pathwayID)) return;
		
		//step 5
		if(isInNpp(pathwayID)) return;
		
		//step 6
		if(isInProblemPathways(pathwayID)) return;

		//step 7
		if(isSuperPathway(pathwayID)) return;

		//step 8
		if(isInAipp(pathwayID)) return;

		//step 9
		if(isInManual(pathwayID)) return;

		//step 10
		if(!isInCapp(pathwayID)) return;

		//step 11-13
		boolean isTaxonInclude = isInTaxonInclude(pathwayID, false);
		boolean isReactionSafe = isInReactionSafe(pathwayID, false);
		boolean isReactionTrouble = isInReactionTrouble(pathwayID, false);

		if(isAcceptedByTaxonOrRaction(pathwayID, isTaxonInclude, isReactionSafe, isReactionTrouble)) return;

		//step 14
		addRestOfPathwayIntoRejectionList(pathwayID);

	}

	private void generateReport() throws IOException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("Date of SAVI run: " + PrintUtil.getCurrentDate());
		sb.append("\r\n");
		sb.append("Time of SAVI run: " + PrintUtil.getCurrentTime());
		sb.append("\r\n");
		sb.append("Species of PGDB: " + taxonomyManager.getTaxonomyName(curSpecies));
		sb.append("\r\n");
		sb.append("ID for PGDB used: " + "nnnn");
		sb.append("\r\n");
		sb.append("UPP.txt version: " + VersionParameters.versionMap.get(VersionParameters.INPUT_UPP));
		sb.append("\r\n");
		sb.append("CVP.txt version: " + VersionParameters.versionMap.get(VersionParameters.INPUT_CVP));
		sb.append("\r\n");
		sb.append("NPP.txt version: " + VersionParameters.versionMap.get(VersionParameters.INPUT_NPP));
		sb.append("\r\n");
		sb.append("AIPP.txt version: " + VersionParameters.versionMap.get(VersionParameters.INPUT_AIPP));
		sb.append("\r\n");
		sb.append("Manual.txt version: " + VersionParameters.versionMap.get(VersionParameters.INPUT_MANUAL));
		sb.append("\r\n");
		sb.append("problem_pathways.txt version: " + VersionParameters.versionMap.get(VersionParameters.INPUT_PROBLEM_PATHWAYS));
		sb.append("\r\n");
		sb.append("CAPP.txt version: " + VersionParameters.versionMap.get(VersionParameters.INPUT_CAPP));
		sb.append("\r\n");
		sb.append("# PWYs in input PGDB (pathways.dat): " + pathwayDBMap.size());
		sb.append("\r\n");
		sb.append("# accepted PWYs: " + accpetedPathwayIDSet.size());
		sb.append("\r\n");
		sb.append("# Manual-to-validate PWYs: " + manualPathwayIDSet.size());
		sb.append("\r\n");
		sb.append("# Rejected PWYs: " + rejectedPathwayIDSet.size());
		sb.append("\r\n");
//		sb.append("Mutiply listed PWYS: " + "PWY-xxxx");
//		sb.append("\r\n");
		sb.append("# PWYs in pathways_pgdb.dat: " + pathwayPgdbDBMap.size());
		sb.append("\r\n");
//		sb.append("# PWYs in EXP.dat: " + expPathwayIDSet.size());
//		sb.append("\r\n");
		sb.append("# PWYs in PreviousPGDB.txt: " + previousPgdbIDSet.size());
		sb.append("\r\n");
		
		sb.append(MetacycSpeciesDatParser.getFileInfo());
		sb.append(pathwayFileInfo);
		sb.append(pathwayPgdbFileInfo);
		sb.append(MetacycReactionDatParser.getFileInfo());
		
		PrintUtil.saveStringIntoFile(validationReportFileName, sb.toString());
		System.out.println(VersionParameters.versionMap);
	}

	private boolean isAcceptedByTaxonOrRaction(String pathwayID, boolean isTaxonInclude,
			boolean isReactionSafe, boolean isReactionTrouble) throws IOException {
		// TODO Auto-generated method stub
		boolean isAccepted = false;
		CappDB cappDB = cappMap.get(pathwayID);
		PathwayDB pathwayDB = pathwayDBMap.get(pathwayID);

		StringBuffer sb = new StringBuffer();
		sb.append(pathwayID);
		sb.append("\t");
		sb.append(pathwayDB.getCommonNameList());
		sb.append("\t");
		
		if(isTaxonInclude && (isReactionSafe || isReactionTrouble)){
			//EV-HINF-RXN+TAXON
			isAccepted = true;
			sb.append(ConstantsForEvidenceCode.EV_HINF_TAXON_REACTION);
		}else if(isTaxonInclude){
			//EV-HINF-TAXON
			isAccepted = true;
			sb.append(ConstantsForEvidenceCode.EV_HINF_TAXON);
		}else if(isReactionSafe || isReactionTrouble){
			//EV-HINF-RXN
			isAccepted = true;
			sb.append(ConstantsForEvidenceCode.EV_HINF_REACTION);
		}
		
		sb.append("\t");
		sb.append(cappDB.getEvHinfRefList());
		sb.append("\t");
		sb.append(curSpecies);
		sb.append("\t");
		sb.append(getCitationsFromPGDB(pathwayID));
		sb.append("\t");

		sb.append(getTaxonomyNameList(cappDB.getTaxonWarningFlagIfWithin()));
		sb.append("\t");
		sb.append(getTaxonomyNameList(cappDB.getTaxonWarningFlagIfOutsideOf()));
		sb.append("\r\n");
		
		if(isAccepted){
			addAcceptedPathway(pathwayID, sb.toString());
			return true;
		}
		else
			return false;
	}

	private String getTaxonomyNameList(
			ArrayList<String> taxonomyIdList) {
		// TODO Auto-generated method stub
		ArrayList<String> taxonomyNameList = new ArrayList<String>();
		Iterator<String> taxonomyIdIterator = taxonomyIdList.iterator();
		while(taxonomyIdIterator.hasNext())
		{
			String taxonomyID = taxonomyIdIterator.next();
			String taxonomyName = taxonomyManager.getTaxonomyName(taxonomyID);
			taxonomyNameList.add(taxonomyName);
		}

		if(taxonomyNameList.size() != 0)
			return taxonomyNameList.toString();
		else
			return "NA";

	}

	private void isTaxonWarning() throws IOException {
		// TODO Auto-generated method stub
		getAcceptedFilesForTaxonCheck();
		
		StringBuffer sb = new StringBuffer();
		sb.append("PWY-ID	Pathway Common Name	Evidence code	REF	Species	Citations	TAXON-WARNING-FLAG-IF-WITHIN	TAXON-WARNING-FLAG-IF-OUTSIDE-OF	Taxon warning comment\r\n");

		Iterator<String> keyIterator = accpetedResultMapFromFile.keySet().iterator();
		while(keyIterator.hasNext())
		{
			String pathwayID = keyIterator.next();
			String line = accpetedResultMapFromFile.get(pathwayID);
			sb.append(line);
			sb.append("\t");
			if(cappMap.containsKey(pathwayID) && cappMap.get(pathwayID).isTaxonWarning()){
				if(isPassedTaxonExcludeAndInclude(pathwayID)){	//passed Taxon warning
					sb.append(ConstantsForEvidenceCode.FLAG_PASS_TAXON_WARNING);
				}else{									//failed Taxon warning
					sb.append(ConstantsForEvidenceCode.FLAG_FAIL_TAXON_WARNING);
				}
			} else		//not in CAPP.txt file or 'No' on CAPP
			{
				sb.append(ConstantsForEvidenceCode.FLAG_NO_TAXON_WARNING);
			}
			sb.append("\r\n");
		}
		
		PrintUtil.saveStringIntoFile(acceptedFileName, sb.toString());
	}

	private boolean isPassedTaxonExcludeAndInclude(String pathwayID) {
		// TODO Auto-generated method stub
		CappDB cappDB = cappMap.get(pathwayID);
		
		int numOfTaxonWarningIfWithin = cappDB.getTaxonWarningFlagIfWithin().size();
		int numOfTaxonWarningIfOutsideOf = cappDB.getTaxonWarningFlagIfOutsideOf().size();

		if(numOfTaxonWarningIfWithin==0 && numOfTaxonWarningIfOutsideOf==0)	//NAs in both TaxonWarning
		{
			System.err.println("No taxon warning information is found even though taxon warning flag is YES");
			return false;
		}

		boolean isDaughterOfTaxonWarningFlagIfWithin = IsDaughterOfParentList(cappDB.getTaxonWarningFlagIfWithin());
		boolean isDaughterOfTaxonWarningFlagIfOutsideOf = IsDaughterOfParentList(cappDB.getTaxonWarningFlagIfOutsideOf());
		
		if(numOfTaxonWarningIfWithin!=0 && numOfTaxonWarningIfOutsideOf!=0)	//no NAs in both TaxonWarning
		{
			if(isDaughterOfTaxonWarningFlagIfWithin || !isDaughterOfTaxonWarningFlagIfOutsideOf)	//rejected
				return false;
			else
				return true;
		}
		else if(numOfTaxonWarningIfWithin==0 && numOfTaxonWarningIfOutsideOf!=0)	//NAs in TaxonWarningIfWithin
		{
			if(!isDaughterOfTaxonWarningFlagIfOutsideOf)	//rejected
				return false;
			else
				return true;
		}
		else if(numOfTaxonWarningIfWithin!=0 && numOfTaxonWarningIfOutsideOf==0)	//NAs in TaxonWarningIfOutsideOf
		{
			if(isDaughterOfTaxonWarningFlagIfWithin)	//rejected
				return false;
			else
				return true;
		}
		
		return false;
	}
	
//	private boolean isPassedTaxonExcludeAndInclude(String pathwayID) {
//		// TODO Auto-generated method stub
//		CappDB cappDB = cappMap.get(pathwayID);
//		boolean isDaughterOfTaxonExclude = IsDaughterOfParentList(cappDB.getTaxonExcludeWithinList());
//		boolean isDaughterOfTaxonInclude = IsDaughterOfParentList(cappDB.getTaxonIncludeWithinList());
//		
//		if(isDaughterOfTaxonExclude || !isDaughterOfTaxonInclude)	//rejected
//			return false;
//		else
//			return true;
//	}

//	private boolean hasNAinTaxonWarning(
//			ArrayList<String> taxonParentList) {
//		Iterator<String> iterator = taxonParentList.iterator();
//		while(iterator.hasNext())
//		{
//			String parentSpeciesId = iterator.next().trim();
//			if(parentSpeciesId.equalsIgnoreCase("NA"))
//				return true;
//		}
//		return false;
//	}

	private boolean isInEXP(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		if(expPathwayIDSet.contains(pathwayID))
			return true;
		return false;
	}
	
	private boolean isInEXPSuperpathway(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		if(expPathwayIDInSuperpathwaySet.contains(pathwayID))
			return true;
		return false;
	}

	private void addPreviousPgdbPathway(String pathwayID, String string) throws IOException {
		// TODO Auto-generated method stub
		PrintUtil.appendStringIntoFile(previousPgdbFileName, string);
		previousPgdbIDSet.add(pathwayID);
	}

	private void addRestOfPathwayIntoPreviousPgdb(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		PathwayDB pgdbPathwayDB = pathwayPgdbDBMap.get(pathwayID);
		StringBuffer sb = new StringBuffer();
		sb.append(pathwayID);
		sb.append("\t");
		sb.append(pgdbPathwayDB.getCommonNameList());
		sb.append("\t");
		sb.append(getCitationsFromPGDB(pathwayID));
		sb.append("\t");
		sb.append(ConstantsForEvidenceCode.FLAG_NEW_PATHWAY);
		sb.append("\r\n");
		
		addPreviousPgdbPathway(pathwayID, sb.toString());
	}
	
	private boolean isCappInPreviousPgdb(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		PathwayDB pgdbPathwayDB = pathwayPgdbDBMap.get(pathwayID);
		boolean isCappInPreviousPgdb = cappMap.containsKey(pathwayID);
		if(isCappInPreviousPgdb)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pgdbPathwayDB.getCommonNameList());
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.FLAG_CAPP);
			sb.append("\r\n");
			
			addPreviousPgdbPathway(pathwayID, sb.toString());
		}
		
		return isCappInPreviousPgdb;
	}

	private boolean isManualInPreviousPgdb(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		PathwayDB pgdbPathwayDB = pathwayPgdbDBMap.get(pathwayID);
		boolean isManualInPreviousPgdb = manualMap.containsKey(pathwayID);
		if(isManualInPreviousPgdb)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pgdbPathwayDB.getCommonNameList());
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.FLAG_MANUAL_LIST);
			sb.append("\r\n");
			
			addPreviousPgdbPathway(pathwayID, sb.toString());
		}
		
		return isManualInPreviousPgdb;
	}

	private boolean isAippInPreviousPgdb(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		PathwayDB pgdbPathwayDB = pathwayPgdbDBMap.get(pathwayID);
		boolean isAippInPreviousPgdb = aippMap.containsKey(pathwayID);
		if(isAippInPreviousPgdb)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pgdbPathwayDB.getCommonNameList());
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.FLAG_AIPP);
			sb.append("\r\n");
			
			addPreviousPgdbPathway(pathwayID, sb.toString());
		}
		
		return isAippInPreviousPgdb;
	}

	private boolean isSuperPathwayInPreviousPgdb(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		PathwayDB pgdbPathwayDB = pathwayPgdbDBMap.get(pathwayID);
		boolean isSuperPathway = pgdbPathwayDB.getTypeList().contains(ConstantsForCycDatParser.PATHWAYS_TYPES_SUPER_PATHWAY);
		if(isSuperPathway)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pgdbPathwayDB.getCommonNameList());
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.FLAG_SUPERPATHWAY);
			sb.append("\r\n");
			
			addPreviousPgdbPathway(pathwayID, sb.toString());
		}
		
		return isSuperPathway;
	}
	
	private boolean ignoreInNPP(String pathwayID) {
		// TODO Auto-generated method stub
		return nppMap.containsKey(pathwayID);
	}

	private boolean ignoreInUppOrCvp(String pathwayID) {
		// TODO Auto-generated method stub
		return abUppCvpDBMap.containsKey(pathwayID);
	}

	private boolean ignoreInPathwaysDat(String pathwayID) {
		// TODO Auto-generated method stub
		return pathwayDBMap.containsKey(pathwayID);
	}

	private boolean isSuperpathwayExperimentallyValidated(
			String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		if(checkSuperpathwayExperimentallyValidated(pathwayID))
		{
//			addExp(pathwayID);
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pathwayPgdbDBMap.get(pathwayID).getCommonNameList());
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.EV_EXP_SUB);
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append("NA");
			sb.append("\r\n");
			addAcceptedPathway(pathwayID, sb.toString());
			
			expPathwayIDInSuperpathwaySet.add(pathwayID);
			return true;
		}
		return false;
	}
	
	private boolean checkSuperpathwayExperimentallyValidated(
			String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		PathwayDB pathwayPgdbDB = pathwayPgdbDBMap.get(pathwayID);
		Iterator<String> iterator = pathwayPgdbDB.getSuperPathwayList().iterator();
		while(iterator.hasNext())
		{ 
			String superpathwayId = iterator.next();
			boolean isExperimentallyValidated = checkExperimentallyValidated(superpathwayId);

			//recursively check whether a superpathway is experimentally validated or not 
			if(isExperimentallyValidated || checkSuperpathwayExperimentallyValidated(superpathwayId))
				return true;
		}
		return false;
	}

	private boolean isExperimentallyValidated(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		if(checkExperimentallyValidated(pathwayID)){
//			addExp(pathwayID);
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pathwayPgdbDBMap.get(pathwayID).getCommonNameList());
			sb.append("\t");	
			sb.append(ConstantsForEvidenceCode.EV_EXP);
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append("NA");
			sb.append("\r\n");
			addAcceptedPathway(pathwayID, sb.toString());
			
			expPathwayIDSet.add(pathwayID);
			return true;
		}
		return false;
	}
	
	private boolean checkExperimentallyValidated(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		PathwayDB pathwayPgdbDB = pathwayPgdbDBMap.get(pathwayID);

		Iterator<String> evidenceIterator = pathwayPgdbDB.getEvidenceCodeList().iterator();
		while(evidenceIterator.hasNext())
		{
			String evidenceCode = evidenceIterator.next();
			if(evidenceCode.startsWith("EV-EXP") || evidenceCode.startsWith("EV-AS")){
				return true;
			}
		}
		return false;
	}

//	private void addExp(String pathwayID) throws IOException {
//		// TODO Auto-generated method stub
//		PathwayDB pathwayPgdbDB = pathwayPgdbDBMap.get(pathwayID);
//		
//		StringBuffer sb = new StringBuffer();
//		
//		sb.append(pathwayID);
//		sb.append("\t");
//		sb.append(pathwayPgdbDB.getCommonNameList());
//		sb.append("\t");
//		sb.append(pathwayPgdbDB.getCitationList());
//		sb.append("\r\n");
//		
//		PrintUtil.appendStringIntoFile(expFileName, sb.toString());
//	}

	private void selfResultCheck() throws IOException {
		// TODO Auto-generated method stub
		getReadFilesForSelfResultCheck();
		
		if(testPathwaysExistOnceInResults())
			System.out.println("Self validation was passed!: All pathways in Pathway.dat exist and exist only once across result files.");
		
		if(testAllUppCppPathwaysInResults())
			System.out.println("Self validation was passed!: All UPP pathways exist in Accepted.txt.");

		testDupulicatedPathwaysInInputs();
	}

	private void testDupulicatedPathwaysInInputs() {
	// TODO Auto-generated method stub
		Iterator<String> uppCvpIterator = abUppCvpDBMap.keySet().iterator();
		while(uppCvpIterator.hasNext())
		{
			String pathwayId = uppCvpIterator.next();
			if(nppMap.containsKey(pathwayId))
				System.err.println(pathwayId + " exists in both UPP(CVP) and NPP files)");
				
			if(aippMap.containsKey(pathwayId))
				System.err.println(pathwayId + " exists in both UPP(CVP) and AIPP files)");
				
			if(cappMap.containsKey(pathwayId))
				System.err.println(pathwayId + " exists in both UPP(CVP) and CAPP files)");
			
			if(manualMap.containsKey(pathwayId))
				System.err.println(pathwayId + " exists in both UPP(CVP) and manual files)");
		}
			
		Iterator<String> nppIterator = nppMap.keySet().iterator();
		while(nppIterator.hasNext())
		{
			String pathwayId = nppIterator.next();
			if(aippMap.containsKey(pathwayId))
				System.err.println(pathwayId + " exists in both NPP and AIPP files)");
				
			if(cappMap.containsKey(pathwayId))
				System.err.println(pathwayId + " exists in both NPP and CAPP files)");
			
			if(manualMap.containsKey(pathwayId))
				System.err.println(pathwayId + " exists in both NPP and manual files)");
		}
		
		Iterator<String> aippIterator = aippMap.keySet().iterator();
		while(aippIterator.hasNext())
		{
			String pathwayId = aippIterator.next();
			if(cappMap.containsKey(pathwayId))
				System.err.println(pathwayId + " exists in both AIPP and CAPP files)");
			
			if(manualMap.containsKey(pathwayId))
				System.err.println(pathwayId + " exists in both AIPP and manual files)");
		}
		
		Iterator<String> cappIterator = cappMap.keySet().iterator();
		while(cappIterator.hasNext())
		{
			String pathwayId = cappIterator.next();
			if(manualMap.containsKey(pathwayId))
				System.err.println(pathwayId + " exists in both CAPP and manual files)");
		}
	}

	private boolean testAllUppCppPathwaysInResults() {
		// TODO Auto-generated method stub
		boolean isPassed = true;
		Iterator<String> pathwayIDIterator = abUppCvpDBMap.keySet().iterator();
		while(pathwayIDIterator.hasNext())
		{
			int numOfExist = 0;
			String pathwayID = pathwayIDIterator.next();
			if(accpetedResultMapFromFile.containsKey(pathwayID))
				numOfExist++;

			if(numOfExist == 0){
				isPassed = false;
				System.err.println(pathwayID + " in UPP.txt is not found in any result files!!");
			}
		}
		return isPassed;
	}

	private boolean testPathwaysExistOnceInResults() {
		// TODO Auto-generated method stub
		boolean isPassed = true;
		Iterator<String> pathwayIDIterator = pathwayDBMap.keySet().iterator();
		while(pathwayIDIterator.hasNext())
		{
			int numOfExist = 0;
			String pathwayID = pathwayIDIterator.next();
			if(accpetedResultMapFromFile.containsKey(pathwayID))
				numOfExist++;
			
			if(rejectedResultMapFromFile.containsKey(pathwayID))
				numOfExist++;

			if(manualResultMapFromFile.containsKey(pathwayID))
				numOfExist++;
			
			if(numOfExist == 0){
				isPassed = false;
				System.err.println(pathwayID + " in pathways.dat is not found in any result files!!");
			}else if(numOfExist > 1){
				isPassed = false;
				System.err.println(pathwayID + " in pathways.dat is found more than once in result files!!");
			}
		}
		return isPassed;
	}

	private void addNonUppSuperpathwayIntoAccptedList() throws IOException {
		// TODO Auto-generated method stub
//		System.out.println("Total Number of NonUppsuperpathways: " + superpathwaySet.size());

		int curDepth = 1;
		for(;;curDepth++){
			Iterator<String> pathwayIDIterator = superpathwaySet.iterator();
			while(pathwayIDIterator.hasNext())
			{
				String pathwayID = pathwayIDIterator.next();
				
				// if a superpathway is already accepted, skip
				if (accpetedPathwayIDSet.contains(pathwayID)){
					pathwayIDIterator.remove();
					continue;
				}
				
				HashSet<String> tempSet = new HashSet<String>();
				tempSet.add(pathwayID);
				int depth = getDepthOfSuperPathway(tempSet);
				
				//to check superpathways with shorter depth
				if (depth > curDepth)
					continue;

				Logger.println("Depth of superpathway (" + pathwayID + "): " + depth);

				// ## reject
				// if all subpathways of a superpathway are not included in accepted list, put it into rejected list
				if (!isAllSubPathwaysAccepted(pathwayID)) {
					StringBuffer sb = new StringBuffer();
					sb.append(pathwayID);
					sb.append("\t");
					sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
					sb.append("\t");
					sb.append(ConstantsForEvidenceCode.FLAG_SUPERPATHWAY);
					sb.append("\t");
					sb.append(getLowestCommonParentTaxID(pathwayID));
					sb.append("\t");
					sb.append(getLowestCommonParentTaxName(pathwayID));
					sb.append("\t");
					sb.append(isDaughterOfParent(getLowestCommonParentTaxID(pathwayID)));
					sb.append("\t");
					sb.append(getNumberOfSpecies(pathwayID));
					sb.append("\t");
					sb.append(curSpecies);
					sb.append("\t");
					sb.append(getRxnSafeList(pathwayID));
					sb.append("\t");
					sb.append(getRxnTroubleList(pathwayID));
					sb.append("\t");
					sb.append(getTaxonIncluldWithinList(pathwayID));
					sb.append("\t");
					sb.append(pathwayDBMap.get(pathwayID).getSubPathwayList());
					sb.append("\t");
					sb.append(getCitationsFromPGDB(pathwayID));
					sb.append("\r\n");
					
					addRejectedPathway(pathwayID, sb.toString());
					pathwayIDIterator.remove();
					continue;
				}

				// ## conditionally accepted by taxon and reaction information
				//step 15.1 - 15.5
				goThroghTaxonAndRxnValidation(pathwayID);
				pathwayIDIterator.remove();
			}
		
			if(superpathwaySet.size() == 0)
				break;
		}
	}

//	private void addNonUppSuperpathwayIntoAccptedList() throws IOException {
//		// TODO Auto-generated method stub
////		System.out.println("Total Number of NonUppsuperpathways: " + superpathwaySet.size());
//
//		int curDepth = 1;
//		for(;;curDepth++){
//			Iterator<String> pathwayIDIterator = superpathwaySet.iterator();
//			while(pathwayIDIterator.hasNext())
//			{
//				String pathwayID = pathwayIDIterator.next();
//				
//				// if a superpathway is already accepted, skip
//				if (accpetedPathwayIDSet.contains(pathwayID)){
//					pathwayIDIterator.remove();
//					continue;
//				}
//				
//				HashSet<String> tempSet = new HashSet<String>();
//				tempSet.add(pathwayID);
//				int depth = getDepthOfSuperPathway(tempSet);
//				
//				//to check superpathways with shorter depth
//				if (depth > curDepth)
//					continue;
//
//				Logger.println("Depth of superpathway (" + pathwayID + "): " + depth);
//
//				// if subpathways of a superpathway are not included in accepted list, put it into rejected list
//				if (!isAllSubPathwaysAccepted(pathwayID)) {
//					StringBuffer sb = new StringBuffer();
//					sb.append(pathwayID);
//					sb.append("\t");
//					sb.append(ConstantsForEvidenceCode.FLAG_SUPERPATHWAY);
//					sb.append("\t");
//					sb.append(getLowestCommonParentTaxID(pathwayID));
//					sb.append("\t");
//					sb.append(getLowestCommonParentTaxName(pathwayID));
//					sb.append("\t");
//					sb.append(isDaughterOfParent(getLowestCommonParentTaxID(pathwayID)));
//					sb.append("\t");
//					sb.append(getNumberOfSpecies(pathwayID));
//					sb.append("\t");
//					sb.append(curSpecies);
//					sb.append("\t");
//					sb.append(getRxnTroubleList(pathwayID));
//					sb.append("\t");
//					sb.append(pathwayDBMap.get(pathwayID).getSubPathwayList());
//					sb.append("\r\n");
//					
//					addRejectedPathway(pathwayID, sb.toString());
//					pathwayIDIterator.remove();
//					continue;
//				}
//
//				StringBuffer sb = new StringBuffer();
//				sb.append(pathwayID);
//				sb.append("\t");
//				sb.append(ConstantsForEvidenceCode.EV_HINF);
//				sb.append("\t");
//				sb.append(ConstantsForEvidenceCode.FLAG_SUPERPATHWAY);
//				sb.append("\t");
//				sb.append(curSpecies);
//				sb.append("\r\n");
//				
//				addAcceptedPathway(pathwayID, sb.toString());
//				pathwayIDIterator.remove();
//			}
//		
//			if(superpathwaySet.size() == 0)
//				break;
//		}
//	}
	
	private void goThroghTaxonAndRxnValidation(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		
		//step 15.1
		if(!isSuperpathwayInCapp(pathwayID)) return;

		//step 15.2
		if(isInTaxonInclude(pathwayID, true)) return;

		//step 15.3
		if(isInReactionSafe(pathwayID, true)) return;

		//step 15.4
		if(isInReactionTrouble(pathwayID, true)) return;
		
		//step 15.5
		addRestOfPathwayIntoRejectionList(pathwayID);
	}

	private boolean isAllSubPathwaysAccepted(String pathwayID) {
		// TODO Auto-generated method stub
		PathwayDB pathwayDB = pathwayDBMap.get(pathwayID);
		
		if(pathwayDB.getSubPathwayList().size() == 0){
			System.err.println("Superpathway (" + pathwayID + ") does not have any sub-pathway in pathway.dat");
			return false;
		}
			
		Iterator<String> subPathwayIterator = pathwayDB.getSubPathwayList().iterator();
		while(subPathwayIterator.hasNext())
		{
			String subPathwayID = subPathwayIterator.next();
			if(!accpetedPathwayIDSet.contains(subPathwayID))
				return false;
		}
		
		return true;
	}
	
	private int getDepthOfSuperPathway(HashSet<String> pathwayIDSet) {
		// TODO Auto-generated method stub
		boolean includeSuperPathway = false;
		HashSet<String> subPathwaySet = new HashSet<String>();
		Iterator<String> pathwayIterator = pathwayIDSet.iterator();
		while(pathwayIterator.hasNext())
		{
			String pathwayID = pathwayIterator.next();
			PathwayDB pathwayDB = pathwayDBMap.get(pathwayID);
			subPathwaySet.addAll(pathwayDB.getSubPathwayList());
			
			if(!includeSuperPathway)
				includeSuperPathway = doesIncludeSuperPathway(pathwayDB.getSubPathwayList());
		}
		
		if(subPathwaySet.size() != 0 && includeSuperPathway)
			return getDepthOfSuperPathway(subPathwaySet) + 1;
		
		return 1; 
	}

	private boolean doesIncludeSuperPathway(HashSet<String> pathwayIDList) {
		// TODO Auto-generated method stub
		Iterator<String> pathwayIDIterator = pathwayIDList.iterator();
		while(pathwayIDIterator.hasNext())
		{
			String pathwayID = pathwayIDIterator.next();
			PathwayDB pathwayDB = pathwayDBMap.get(pathwayID);
			if(pathwayDB.getTypeList().contains(ConstantsForCycDatParser.PATHWAYS_TYPES_SUPER_PATHWAY))
				return true;
		}
		return false;
	}
	
	private void addNotAcceptedUppOrCvpIntoAccptedList() throws IOException {
		// TODO Auto-generated method stub
//		System.out.println("Total Number of accepted pathways (before NotAcceptedUppOrCvp): " + accpetedPathwayIDSet.size());
		
//		Iterator<String> keyIterator = abNotAccpetedUppCvpDBMap.keySet().iterator();
		Iterator<String> keyIterator = abUppCvpDBMap.keySet().iterator();
		while(keyIterator.hasNext())
		{
			String pathwayID = keyIterator.next();
			if(accpetedPathwayIDSet.contains(pathwayID))	//if a pathway is already accepted, then skip;
				continue;
			
			AbUppCvpDB abUppCvpDB= abUppCvpDBMap.get(pathwayID);
			
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");	
			sb.append(ConstantsForEvidenceCode.EV_IC);
			sb.append("\t");
			sb.append(abUppCvpDB.getEvIcRefList());
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append("NA");
			sb.append("\r\n");
			
			addAcceptedPathway(pathwayID, sb.toString());
		}
//		System.out.println("Total Number of accepted pathways (after NotAcceptedUppOrCvp): " + accpetedPathwayIDSet.size());
	}


	private void addRestOfPathwayIntoRejectionList(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append(pathwayID);
		sb.append("\t");
		sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
		sb.append("\t");
		sb.append(ConstantsForEvidenceCode.FLAG_TAXON_OR_RXN);
		sb.append("\t");
		sb.append(getLowestCommonParentTaxID(pathwayID));
		sb.append("\t");
		sb.append(getLowestCommonParentTaxName(pathwayID));
		sb.append("\t");
		sb.append(isDaughterOfParent(getLowestCommonParentTaxID(pathwayID)));
		sb.append("\t");
		sb.append(getNumberOfSpecies(pathwayID));
		sb.append("\t");
		sb.append(curSpecies);
		sb.append("\t");
		sb.append(getRxnSafeList(pathwayID));
		sb.append("\t");
		sb.append(getRxnTroubleList(pathwayID));
		sb.append("\t");
		sb.append(getTaxonIncluldWithinList(pathwayID));
		sb.append("\t");
		sb.append("NA");
		sb.append("\t");
		sb.append(getCitationsFromPGDB(pathwayID));
		sb.append("\r\n");
		
		addRejectedPathway(pathwayID, sb.toString());
	}

	private boolean isInReactionTrouble(String pathwayID, boolean isSaveIntoAcceptedList) throws IOException {
		// TODO Auto-generated method stub
//		if(pathwayID.equals("PWY-5153")) 
//			System.out.println("test");
		
		if(cappMap.containsKey(pathwayID))
		{
			CappDB cappDB = cappMap.get(pathwayID);
			ArrayList<String> rxnTroubleList = cappDB.getRxnTroubleList();
			boolean isEnzymeInReactions = isEnzymeIdentifiedForReactionsAndHavingRxnIDInPFfile(rxnTroubleList);
			
			if(isEnzymeInReactions && isSaveIntoAcceptedList)
			{
				StringBuffer sb = new StringBuffer();
				sb.append(pathwayID);
				sb.append("\t");
				sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
				sb.append("\t");
				sb.append(ConstantsForEvidenceCode.EV_HINF);
				sb.append("\t");
				sb.append(cappDB.getEvHinfRefList());
				sb.append("\t");
				sb.append(curSpecies);
				sb.append("\t");
				sb.append(getCitationsFromPGDB(pathwayID));
				sb.append("\t");	

				sb.append(getTaxonomyNameList(cappDB.getTaxonWarningFlagIfWithin()));
				sb.append("\t");
				sb.append(getTaxonomyNameList(cappDB.getTaxonWarningFlagIfOutsideOf()));
				sb.append("\r\n");
				
				addAcceptedPathway(pathwayID, sb.toString());
			}
			
			return isEnzymeInReactions;

		} else
			return false;
	}

//	private boolean isEnzymeIdentifiedForReactionsAndHavingRxnIDInPFfile(
//			ArrayList<String> rxnTroubleList) {
//		// TODO Auto-generated method stub
//		Iterator<String> rxnTroubleIterator = rxnTroubleList.iterator();
//		while(rxnTroubleIterator.hasNext()) //rxn-trouble list
//		{
//			String reactionID = rxnTroubleIterator.next();
//			ReactionDB reactionDB = reactionDBMap.get(reactionID);
//			
//			if(reactionDB == null)
//				continue;
//			
//			Iterator<String> enzrxnIDIterator = reactionDB.getEnzymaticReactionIDList().iterator();
//			while(enzrxnIDIterator.hasNext())	//reaction.dat
//			{
//				String enzrxnID = enzrxnIDIterator.next();
//				Iterator<ProteinDB> proteinDBIterator = proteinDBMap.values().iterator();
//				while(proteinDBIterator.hasNext())	//protein.dat
//				{
//					ProteinDB proteinDB = proteinDBIterator.next();
//					if (proteinDB.getCatalyzerSet().contains(enzrxnID))
//					{
//						String pfID = proteinDB.getCommonName();
//						PathologicDB pathologicDB = pathologicDBMap.get(pfID);
//						if(pathologicDB != null && pathologicDB.getReactionIDSet().contains(reactionID))	//pf file
//							return true;
//					}
//				}
//			}
//		}
//		return false;
//	}

	private boolean isEnzymeIdentifiedForReactionsAndHavingRxnIDInPFfile(
			ArrayList<String> rxnTroubleList) {
		// TODO Auto-generated method stub
		Iterator<String> rxnTroubleIterator = rxnTroubleList.iterator();
		while(rxnTroubleIterator.hasNext()) //rxn-trouble list
		{
			String reactionID = rxnTroubleIterator.next();
			Iterator<PathologicDB> pathologicIterator = pathologicDBMap.values().iterator();
			while(pathologicIterator.hasNext())	//pathologic list
			{
				PathologicDB pathlogicDB = pathologicIterator.next();
				
				if(pathlogicDB.getReactionIDSet().contains(reactionID)){
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInReactionSafe(String pathwayID, boolean isSaveIntoAcceptedList) throws IOException {
		// TODO Auto-generated method stub
		if(cappMap.containsKey(pathwayID))
		{
			CappDB cappDB = cappMap.get(pathwayID);
			ArrayList<String> rxnSafeList = cappDB.getRxnSafeList();
			boolean isEnzymeInReactions = isEnzymeIdentifiedForReactions(rxnSafeList);
			
			if(isEnzymeInReactions && isSaveIntoAcceptedList)
			{
				StringBuffer sb = new StringBuffer();
				sb.append(pathwayID);
				sb.append("\t");
				sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
				sb.append("\t");
				sb.append(ConstantsForEvidenceCode.EV_HINF);
				sb.append("\t");
				sb.append(cappDB.getEvHinfRefList());
				sb.append("\t");
				sb.append(curSpecies);
				sb.append("\t");
				sb.append(getCitationsFromPGDB(pathwayID));
				sb.append("\t");	

				sb.append(getTaxonomyNameList(cappDB.getTaxonWarningFlagIfWithin()));
				sb.append("\t");
				sb.append(getTaxonomyNameList(cappDB.getTaxonWarningFlagIfOutsideOf()));
				sb.append("\r\n");
				
				addAcceptedPathway(pathwayID, sb.toString());
			}
			
			return isEnzymeInReactions;

		} else
			return false;
	}


	private boolean isEnzymeIdentifiedForReactions(ArrayList<String> rxnSafeList) {
		// TODO Auto-generated method stub
		Iterator<String> rxnSafeIterator = rxnSafeList.iterator();
		while(rxnSafeIterator.hasNext())
		{
			String reactionID = rxnSafeIterator.next();
			ReactionDB reactionDB = reactionDBMap.get(reactionID);
			if(reactionDB != null && reactionDB.getEnzymaticReactionIDList().size() != 0)
				return true;
		}
		return false;
	}


	private boolean isInTaxonInclude(String pathwayID, boolean isSaveIntoAcceptedList) throws IOException {
		// TODO Auto-generated method stub
		if(cappMap.containsKey(pathwayID))
		{
			CappDB cappDB = cappMap.get(pathwayID);
			boolean isInTaxonInclude = IsDaughterOfParentList(cappDB.getTaxonIncludeWithinList());
			if(isInTaxonInclude & isSaveIntoAcceptedList)
			{
				StringBuffer sb = new StringBuffer();
				sb.append(pathwayID);
				sb.append("\t");
				sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
				sb.append("\t");
				sb.append(ConstantsForEvidenceCode.EV_HINF);
				sb.append("\t");
				sb.append(cappDB.getEvHinfRefList());
				sb.append("\t");
				sb.append(curSpecies);
				sb.append("\t");
				sb.append(getCitationsFromPGDB(pathwayID));
				sb.append("\t");	

				sb.append(getTaxonomyNameList(cappDB.getTaxonWarningFlagIfWithin()));
				sb.append("\t");
				sb.append(getTaxonomyNameList(cappDB.getTaxonWarningFlagIfOutsideOf()));
				sb.append("\r\n");
				
				addAcceptedPathway(pathwayID, sb.toString());
			}
			
			return isInTaxonInclude;
		} else
			return false;
	}

	private boolean isInTaxonExcludeOutside(String pathwayID) {
		// TODO Auto-generated method stub
		if(cappMap.containsKey(pathwayID))
		{
			CappDB cappDB = cappMap.get(pathwayID);
			if(cappDB.getTaxonExcludeOutsideList().size() == 0)	//if no entry, go to the next step
				return false;
			else 
				return true;
		} else	//if no entry, go to the next step
			return false;
	}
	
	private boolean isDaughterOfTaxonExcludeOutside(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		CappDB cappDB = cappMap.get(pathwayID);
		boolean isInTaxonExcludeOutside = IsDaughterOfParentList(cappDB.getTaxonExcludeOutsideList());
		if(!isInTaxonExcludeOutside)	//if curSpeciese is not a daughter of an exclude outside list, add it to reject list
		{
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.FLAG_TAXON_EXCLUDE_OUTSIDE);
			sb.append("\t");
			sb.append(getLowestCommonParentTaxID(pathwayID));
			sb.append("\t");
			sb.append(getLowestCommonParentTaxName(pathwayID));
			sb.append("\t");
			sb.append(isDaughterOfParent(getLowestCommonParentTaxID(pathwayID)));
			sb.append("\t");
			sb.append(getNumberOfSpecies(pathwayID));
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getRxnSafeList(pathwayID));
			sb.append("\t");
			sb.append(getRxnTroubleList(pathwayID));
			sb.append("\t");
			sb.append(getTaxonIncluldWithinList(pathwayID));
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\r\n");
			
			addRejectedPathway(pathwayID, sb.toString());
			return false;
		}
		return true;
	}

	private boolean isInTaxonExcludeWithin(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		if(cappMap.containsKey(pathwayID))
		{
			CappDB cappDB = cappMap.get(pathwayID);
			
			//#########need to change
//			boolean isInTaxonExcludeWithin = cappDB.getTaxonExcludeWithinList().contains(curSpecies);
			boolean isInTaxonExcludeWithin = IsDaughterOfParentList(cappDB.getTaxonExcludeWithinList());
			if(isInTaxonExcludeWithin)
			{
				StringBuffer sb = new StringBuffer();
				sb.append(pathwayID);
				sb.append("\t");
				sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
				sb.append("\t");
				sb.append(ConstantsForEvidenceCode.FLAG_TAXON_EXCLUDE_WITHIN);
				sb.append("\t");
				sb.append(getLowestCommonParentTaxID(pathwayID));
				sb.append("\t");
				sb.append(getLowestCommonParentTaxName(pathwayID));
				sb.append("\t");
				sb.append(isDaughterOfParent(getLowestCommonParentTaxID(pathwayID)));
				sb.append("\t");
				sb.append(getNumberOfSpecies(pathwayID));
				sb.append("\t");
				sb.append(curSpecies);
				sb.append("\t");
				sb.append(getRxnSafeList(pathwayID));
				sb.append("\t");
				sb.append(getRxnTroubleList(pathwayID));
				sb.append("\t");
				sb.append(getTaxonIncluldWithinList(pathwayID));
				sb.append("\t");
				sb.append("NA");
				sb.append("\t");
				sb.append(getCitationsFromPGDB(pathwayID));
				sb.append("\r\n");
				
				addRejectedPathway(pathwayID, sb.toString());
			}
			
			return isInTaxonExcludeWithin;
		} else
			return false;
	}


	private boolean IsDaughterOfParentList(
			ArrayList<String> taxonParentList) {
		Iterator<String> iterator = taxonParentList.iterator();
		while(iterator.hasNext())
		{
			String parentSpeciesId = iterator.next();
			if(isDaughterOfParent(parentSpeciesId))
				return true;
		}
		return false;
	}


	private boolean isDaughterOfParent(String parentSpeciesId) {
		// TODO Auto-generated method stub
		return taxonomyManager.isInChildNode(parentSpeciesId, curSpecies);
	}


	private boolean isInCapp(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		boolean isInCapp = cappMap.containsKey(pathwayID);
		if(!isInCapp)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.FLAG_NEW_PATHWAY);
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\r\n");
			
			addManualValidationPathway(pathwayID, sb.toString());
			return false;
		} else
			return true;
	}

	private Object getCitationsFromPGDB(String pathwayID) {
		// TODO Auto-generated method stub
		String result = "NA";
		if(pathwayPgdbDBMap.containsKey(pathwayID))
			result = pathwayPgdbDBMap.get(pathwayID).getCitationList().toString();
		return result;
	}

	private boolean isSuperpathwayInCapp(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		boolean isSuperpathwayInCapp = cappMap.containsKey(pathwayID);
		if(!isSuperpathwayInCapp)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.EV_HINF);
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.REF_SUPERPATHWAY_CAPP);
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append("NA");
			sb.append("\r\n");
			
			addAcceptedPathway(pathwayID, sb.toString());
			return false;
		} else
			return true;
	}

	private boolean isInManual(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		boolean isInManual = manualMap.containsKey(pathwayID);
		if(isInManual)
		{
			ManualDB manualDB = manualMap.get(pathwayID);

			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.FLAG_MANUAL_LIST);
			sb.append("\t");
			sb.append(manualDB.getFlag());
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\r\n");
			
			addManualValidationPathway(pathwayID, sb.toString());
			return true;
		} else
			return false;
	}

	private boolean isInProblemPathways(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		boolean isInProblemPathways = problemPathwaysMap.containsKey(pathwayID);
		if(isInProblemPathways)
		{
			ProblemPathwaysDB problemPathwaysDB = problemPathwaysMap.get(pathwayID);

			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.FLAG_PROBLEM_PATHWAY);
			sb.append("\t");
			sb.append(problemPathwaysDB.getDescription());
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\r\n");
			
			addManualValidationPathway(pathwayID, sb.toString());
			return true;
		} else
			return false;
	}

	private boolean isInAipp(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		boolean isInAipp = aippMap.containsKey(pathwayID);
		if(isInAipp)
		{
			AippDB aippDB = aippMap.get(pathwayID);

			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.EV_HINF);
			sb.append("\t");
			sb.append(aippDB.getEvHinfRefList());
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append("NA");
			sb.append("\r\n");
			
			addAcceptedPathway(pathwayID, sb.toString());

			return true;
		} else
			return false;
	}


	private boolean isSuperPathway(String pathwayID) {
		// TODO Auto-generated method stub
		PathwayDB pathwayDB = pathwayDBMap.get(pathwayID);
		boolean isSuperPathway = pathwayDB.getTypeList().contains(ConstantsForCycDatParser.PATHWAYS_TYPES_SUPER_PATHWAY);
		if(isSuperPathway)
			superpathwaySet.add(pathwayID);
		
		return isSuperPathway;
	}


	private boolean isInNpp(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		boolean isInNpp = nppMap.containsKey(pathwayID);
		if(isInNpp)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.FLAG_NPP);
			sb.append("\t");
			sb.append(getLowestCommonParentTaxID(pathwayID));
			sb.append("\t");
			sb.append(getLowestCommonParentTaxName(pathwayID));
			sb.append("\t");
			sb.append(isDaughterOfParent(getLowestCommonParentTaxID(pathwayID)));
			sb.append("\t");
			sb.append(getNumberOfSpecies(pathwayID));
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getRxnSafeList(pathwayID));
			sb.append("\t");
			sb.append(getRxnTroubleList(pathwayID));
			sb.append("\t");
			sb.append(getTaxonIncluldWithinList(pathwayID));
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\r\n");
			
			addRejectedPathway(pathwayID, sb.toString());
			return true;
		} else
			return false;
	}


	private boolean isInUppOrCvp(String pathwayID) throws IOException {
		// TODO Auto-generated method stub
		boolean isInUppOrCvp = abUppCvpDBMap.containsKey(pathwayID);
		if(isInUppOrCvp)
		{
			AbUppCvpDB abUppCvpDB = abUppCvpDBMap.get(pathwayID);

			StringBuffer sb = new StringBuffer();
			sb.append(pathwayID);
			sb.append("\t");
			sb.append(pathwayDBMap.get(pathwayID).getCommonNameList());
			sb.append("\t");
			sb.append(ConstantsForEvidenceCode.EV_HINF);
			sb.append("\t");
			sb.append(abUppCvpDB.getEvHinfRefList());
			sb.append("\t");
			sb.append(curSpecies);
			sb.append("\t");
			sb.append(getCitationsFromPGDB(pathwayID));
			sb.append("\t");
			sb.append("NA");
			sb.append("\t");
			sb.append("NA");
			sb.append("\r\n");
			
			addAcceptedPathway(pathwayID, sb.toString());
			
			return true;
		} else
			return false;
	}


	private void addAcceptedPathway(String pathwayID, String string) throws IOException {
		// TODO Auto-generated method stub
		PrintUtil.appendStringIntoFile(acceptedFileName, string);
		accpetedPathwayIDSet.add(pathwayID);

		//to find which UPP or CVP are not in the predicted pathway list
//		if(abNotAccpetedUppCvpDBMap.containsKey(pathwayID))
//			abNotAccpetedUppCvpDBMap.remove(pathwayID);
	}


	private void addRejectedPathway(String pathwayID, String string) throws IOException {
		// TODO Auto-generated method stub
		PrintUtil.appendStringIntoFile(rejectedFileName, string);
		rejectedPathwayIDSet.add(pathwayID);
	}
	
	private void addManualValidationPathway(String pathwayID, String string) throws IOException {
		// TODO Auto-generated method stub
		PrintUtil.appendStringIntoFile(manualValidationFileName, string);
		manualPathwayIDSet.add(pathwayID);
	}

	private void setUppOrCvpDataBySpecies() {
		// TODO Auto-generated method stub
		boolean isPlant = isSpecisesWithinEmbryophyta();
		if(isPlant)
			abUppCvpDBMap = uppMap;
		else
			abUppCvpDBMap = cvpMap;
		
//		abNotAccpetedUppCvpDBMap = (HashMap<String, AbUppCvpDB>)abUppCvpDBMap.clone();
	}


	private boolean isSpecisesWithinEmbryophyta() {
		// TODO Auto-generated method stub
		SpeciesDB speciesDB  = speciesDBMap.values().iterator().next();
		String ncbiTaxonomyID = speciesDB.getNcbiTaxonomyID();
		
		curSpecies = ncbiTaxonomyID;
		
		boolean isChild = taxonomyManager.isInChildNode("3193", ncbiTaxonomyID);
		return isChild;
	}


	private void initData() throws IOException {
		// TODO Auto-generated method stub
		mkdir(outputFolderName);
		
		PrintUtil.saveStringIntoFile(acceptedFileName, "PWY-ID	Pathway Common Name	Evidence code	REF	Species	Citations	TAXON-WARNING-FLAG-IF-WITHIN	TAXON-WARNING-FLAG-IF-OUTSIDE-OF	Taxon warning comment\r\n");
		PrintUtil.saveStringIntoFile(rejectedFileName, "PWY-ID	Pathway Common Name	FLAG	LCP TAX-ID	LCP-Name	Daughter of LCP?	# species	Species of PGDB	RXN-SAFE	RXN-TROUBLE	TAXON-INCLUDE-WITHIN	SUB-PATHWAY	Citations\r\n");
		PrintUtil.saveStringIntoFile(manualValidationFileName, "PWY-ID	Pathway Common Name	FLAG	FLAG comment	Species	Citations\r\n");
//		PrintUtil.saveStringIntoFile(expFileName, "PWY-ID	Pathway common name	Citations\r\n");
		PrintUtil.saveStringIntoFile(previousPgdbFileName, "PWY-ID	Pathway common name	Citations	FLAG\r\n");

		IParser uppParser = new UppParser(globalInputFolderName + "UPP.txt");
		uppParser.parse();
		uppMap = uppParser.getResultHashMap();		
		
		IParser cvpParser = new CvpParser(globalInputFolderName + "CVP.txt");
		cvpParser.parse();
		cvpMap = cvpParser.getResultHashMap();		

		IParser nppParser = new NppParser(globalInputFolderName + "NPP.txt");
		nppParser.parse();
		nppMap = nppParser.getResultHashMap();		

		IParser aippParser = new AippParser(globalInputFolderName + "AIPP.txt");
		aippParser.parse();
		aippMap = aippParser.getResultHashMap();		

		IParser cappParser = new CappParser(globalInputFolderName + "CAPP.txt");
		cappParser.parse();
		cappMap = cappParser.getResultHashMap();		
		
		IParser manualParser = new ManualParser(globalInputFolderName + "MANUAL.txt");
		manualParser.parse();
		manualMap = manualParser.getResultHashMap();		

		IParser problemPathwaysParser = new ProblemPathwaysParser(globalInputFolderName + "problem_pathways.txt");
		problemPathwaysParser.parse();
		problemPathwaysMap = problemPathwaysParser.getResultHashMap();		

		taxonomyManager = new TaxonomyParser(globalInputFolderName + "nodes.dmp");
		taxonomyManager.parse();
		
//		IParser metacycProteinDatParser = new MetacycProteinDatParser(speciesInputFolderName + "proteins.dat");
//		metacycProteinDatParser.parse();
//		proteinDBMap = metacycProteinDatParser.getResultHashMap();

		MetacycPathwayDatParser metacycPathwayDatParser = new MetacycPathwayDatParser(speciesInputFolderName + "pathways.dat");
		metacycPathwayDatParser.isPrintPathwayID = true;
		metacycPathwayDatParser.PathwayListFileName = "inputPathwayList.txt";
		metacycPathwayDatParser.setOutputFolder(outputFolderName);
		metacycPathwayDatParser.parse();
		pathwayFileInfo = metacycPathwayDatParser.getFileInfo();
		pathwayDBMap = metacycPathwayDatParser.getResultHashMap();

		MetacycPathwayDatParser metacycPathwayPgdbDatParser = new MetacycPathwayDatParser(speciesInputFolderName + "pathways_pgdb.dat");
		metacycPathwayPgdbDatParser.isPrintPathwayID = true;
		metacycPathwayPgdbDatParser.PathwayListFileName = "all_pathways_previous_pgdb.txt";
		metacycPathwayPgdbDatParser.setOutputFolder(outputFolderName);
		metacycPathwayPgdbDatParser.parse();

		pathwayPgdbFileInfo = metacycPathwayPgdbDatParser.getFileInfo();
		pathwayPgdbDBMap = metacycPathwayPgdbDatParser.getResultHashMap();

		IParser metacycReactionDatParser = new MetacycReactionDatParser(speciesInputFolderName + "reactions.dat");
		metacycReactionDatParser.parse();
		reactionDBMap = metacycReactionDatParser.getResultHashMap();
		
		IParser metacycSpeciesDatParser = new MetacycSpeciesDatParser(speciesInputFolderName + "species.dat");
		metacycSpeciesDatParser.parse();
		speciesDBMap = metacycSpeciesDatParser.getResultHashMap();

		String pathologicFileName = findPathologicFileName(speciesInputFolderName);
		IParser pathologicInputParser = new PathologicInputParser(speciesInputFolderName + pathologicFileName);
		pathologicInputParser.parse();
		pathologicDBMap = pathologicInputParser.getResultHashMap();
	}

	private String findPathologicFileName(String folderName) {
		// TODO Auto-generated method stub
		String pathlogicFileName = "";

		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().toLowerCase().endsWith(".pf")) {
				pathlogicFileName = listOfFiles[i].getName();
				Logger.println("pathlogicFileName: " + pathlogicFileName);
			}
		}
		return pathlogicFileName;
	}

	private void getReadFilesForSelfResultCheck() throws IOException {
		// TODO Auto-generated method stub
		IParser acceptedParser = new ResultFileParser(outputFolderName + "Accepted.txt");
		acceptedParser.parse();
		accpetedResultMapFromFile = acceptedParser.getResultHashMap();
		
		IParser rejectedParser = new ResultFileParser(outputFolderName + "Rejected.txt");
		rejectedParser.parse();
		rejectedResultMapFromFile = rejectedParser.getResultHashMap();

		IParser manualParser = new ResultFileParser(outputFolderName + "Manual-to-validate.txt");
		manualParser.parse();
		manualResultMapFromFile = manualParser.getResultHashMap();
	}

	private void getAcceptedFilesForTaxonCheck() throws IOException {
		// TODO Auto-generated method stub
		IParser acceptedParser = new ResultFileParser(outputFolderName + "Accepted.txt");
		acceptedParser.parse();
		accpetedResultMapFromFile = acceptedParser.getResultHashMap();
	}

	private String getRxnTroubleList(String pathwayID) {
		// TODO Auto-generated method stub
		if(cappMap.containsKey(pathwayID) && cappMap.get(pathwayID).getRxnTroubleList().size() != 0)
			return cappMap.get(pathwayID).getRxnTroubleList().toString();
		else
			return "NA";
	}
	
	private String getRxnSafeList(String pathwayID) {
		// TODO Auto-generated method stub
		if(cappMap.containsKey(pathwayID) && cappMap.get(pathwayID).getRxnSafeList().size() != 0)
			return cappMap.get(pathwayID).getRxnSafeList().toString();
		else
			return "NA";
	}

	private String getTaxonIncluldWithinList(String pathwayID) {
		// TODO Auto-generated method stub
		if(cappMap.containsKey(pathwayID) && cappMap.get(pathwayID).getTaxonIncludeWithinList().size() != 0)
			return cappMap.get(pathwayID).getTaxonIncludeWithinList().toString();
		else
			return "NA";
	}

	private String getNumberOfSpecies(String pathwayID) {
		// TODO Auto-generated method stub
		if(cappMap.containsKey(pathwayID))
			return cappMap.get(pathwayID).getNumOfSpecies();
//			return String.valueOf(cappMap.get(pathwayID).getNumOfSpecies());
		else
			return "NA";
	}

	private String getLowestCommonParentTaxName(String pathwayID) {
		// TODO Auto-generated method stub
		String lowestCommonParentID = getLowestCommonParentTaxID(pathwayID);
		String lowestCommonParentName = taxonomyManager.getTaxonomyName(lowestCommonParentID);
		if(lowestCommonParentName.length() != 0)
			return lowestCommonParentName;
		else
			return "NA";
	}

	private String getLowestCommonParentTaxID(String pathwayID) {
		// TODO Auto-generated method stub
		if(cappMap.containsKey(pathwayID) 
			&& cappMap.get(pathwayID).getLowestCommonParent().length() != 0
			&& !cappMap.get(pathwayID).getLowestCommonParent().equals("NA"))
			return cappMap.get(pathwayID).getLowestCommonParent();
		else
			return "NA";
	}
	
	private void mkdir(String folderName) {
//		folderName = folderName.substring(0,folderName.length()-1);
		File theDir = new File(folderName);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + folderName);
			boolean result = theDir.mkdir();

			if (result) {
				System.out.println("DIR created");
			} else
			{
				System.err.println("Creating DIR failed!!");
			}
		}
	}
}
