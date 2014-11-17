package carnegie.bioinfo.metacyc.parser.fileDB;


public class SpeciesDB {
	
	private String uniqueID = "";
	private String type = "";
	private String commonName = "";
	private String ncbiTaxonomyID = "";
	
	public SpeciesDB(String uniqueID)
	{
		this.uniqueID = uniqueID;
	}
	
	public String getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCommonName() {
		return commonName;
	}
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	public String getNcbiTaxonomyID() {
		return ncbiTaxonomyID;
	}
	public void setNcbiTaxonomyID(String ncbiTaxonomyID) {
		this.ncbiTaxonomyID = ncbiTaxonomyID;
	}

	
}
