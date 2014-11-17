package carnegie.bioinfo.common;

public class ConstantsForEvidenceCode {

	//Accpeted.txt (UPP and CVP)
    public static final String EV_HINF = "EV-HINF";
    public static final String EV_HINF_TAXON = "EV-HINF-TAXON";
    public static final String EV_HINF_REACTION = "EV-HINF-REACTION";
    public static final String EV_HINF_TAXON_REACTION = "EV-HINF-TAXON-REACTION";
    public static final String EV_IC = "EV-IC";
    public static final String EV_EXP = "EV-EXP";
    public static final String EV_EXP_SUB = "EV-EXP-SUB";
    public static final String REF_SUPERPATHWAY_CAPP = "Superpathway - CAPP";
    public static final String FLAG_NO_TAXON_WARNING = "NO TAXON-WARNING";
    public static final String FLAG_PASS_TAXON_WARNING = "Passes TAXON-WARNING";
    public static final String FLAG_FAIL_TAXON_WARNING = "Failed: TAXON-WARNING needed";
	
	//Rejected.txt (Flag)
    public static final String FLAG_NPP = "NPP";
    public static final String FLAG_TAXON_EXCLUDE_WITHIN = "TAXON-EXCLUDE-WITHIN";
    public static final String FLAG_TAXON_EXCLUDE_OUTSIDE = "TAXON-EXCLUDE-OUTSIDE";
    public static final String FLAG_TAXON_OR_RXN = "TAXON or RXN";
    public static final String FLAG_SUPERPATHWAY = "Superpathway";

    //Manual-to-validate.txt
    public static final String FLAG_MANUAL_LIST = "Manual List";
    public static final String FLAG_PROBLEM_PATHWAY = "Problem Pathway";
    public static final String FLAG_NEW_PATHWAY = "NEW PWY";

	//PreviousPGDB.txt (Flag)
    public static final String FLAG_AIPP = "AIPP";
    public static final String FLAG_CAPP = "CAPP";
}
