package carnegie.bioinfo.common;


public class ConstantsForCycDatParser {

    //for common identifier for header of dat file
	public static final String HEADER_SPECIES = "# Species:";
	public static final String HEADER_DATABASE = "# Database:";
	public static final String HEADER_VERSION = "# Version:";
	public static final String HEADER_FILE_NAME = "# File Name:";
	public static final String HEADER_DATE_TIME_GENERATED = "# Date and time generated:";

	
	//for common identifier for DAT file
	public static final String UNIQUE_ID = "UNIQUE-ID -";
    public static final String ITEM_END_DELIMITER = "//";
    public static final String LEFT_PREN  = "(";
    public static final String RIGHT_PREN  = ")";
    public static final String COMMON_NAME = "COMMON-NAME -";
    public static final String EC_NUMBER = "EC-NUMBER -";
    public static final String IN_PATHWAY = "IN-PATHWAY -";
    public static final String TAGS_TYPES = "TYPES -";
    public static final String CITATIONS = "CITATIONS -";

    
    //for Aracyc70ParserMain.java
    public static final String PATHWAYS_PRIMARIES  = "PRIMARIES -";
    public static final String PATHWAYS_REACTION_LAYOUT_DIRECTION_NIL  = "NIL";
    public static final String PATHWAYS_COMMON_NAME = "COMMON-NAME -";
    public static final String PATHWAYS_REACTION_LIST = "REACTION-LIST -";
    public static final String PATHWAYS_SUB_PATHWAYS = "SUB-PATHWAYS -";
    public static final String PATHWAYS_SUPER_PATHWAYS = "SUPER-PATHWAYS -";
    public static final String PATHWAYS_TYPES_SUPER_PATHWAY = "Super-Pathways";
    

    public static final String REACTIONS_LEFT_COMPOUND  = "LEFT -";
    public static final String REACTIONS_RIGHT_COMPOUND  = "RIGHT -";
    public static final String REACTIONS_ENZYMATIC_REACTION  = "ENZYMATIC-REACTION -";
    public static final String REACTIONS_DIRECTION_KEY  = "REACTION-DIRECTION -";
    public static final String REACTIONS_DIRECTION_LEFT_TO_RIGHT  = "LEFT-TO-RIGHT";
    public static final String REACTIONS_DIRECTION_RIGHT_TO_LEFT  = "RIGHT-TO-LEFT";
    
    public static final String COMPOUNDS_SYNONYM  = "SYNONYMS -";
    public static final String COMPOUNDS_UNKNOWN  = "Unknown";
    public static final String COMPOUNDS_TYPES_COMPOUNDS = "Compounds";

    public static final String GENE_PRODUCT = "PRODUCT -";

    public static final String PROTEIN_COMPONENT_OF = "COMPONENT-OF -";
    public static final String PROTEIN_COMPONENTS = "COMPONENTS -";
    public static final String PROTEIN_CATALYZES = "CATALYZES -";
     
    public static final String ENZRXNS_ENZYME_POSTFIX  = "-MONOMER";
    public static final String ENZRXNS_ENZYME_NAME  = "ENZYME -";
    public static final String ENZRXNS_REACTION_NAME  = "REACTION -";
    
	public static final String CLASSES_TYPES_FRAMES = "FRAMES";

	public static final String NCBI_TAXONOMY_ID = "NCBI-TAXONOMY-ID -";
    
    //for Aracyc70ParserByReactionLayoutMain.java
    public static final String PATHWAYS_REACTION_LAYOUT_DELIMITER  = "(:";
    public static final String PATHWAYS_REACTION_LAYOUT  = "REACTION-LAYOUT - ";
    public static final String PATHWAYS_REACTION_LAYOUT_PRIMARY  = "-PRIMARIES";
    public static final String PATHWAYS_REACTION_LAYOUT_VERTICAL  = "|";
    
    //for Aracyc70ParserByReactionLayoutMain.java 
    public static final String ME_SACS_LIST_DELIMETER  = "====";
    public static final String ME_SACS_LIST_PREFIX_MUTANT_NAME = "ME";
    public static final String ME_SACS_LIST_PREFIX_HEADER = "Cmpd_Name";
    public static final String ME_SACS_LIST_NA = "NA";
    
    //for PathologicInputParser
    public static final String PATHLOGIC_ID = "ID";
    public static final String PATHLOGIC_NAME = "NAME";
    public static final String PATHLOGIC_PRODUCT_TYPE = "PRODUCT-TYPE";
    public static final String PATHLOGIC_EC = "EC";
    public static final String PATHLOGIC_REACTION = "METACYC";


}
