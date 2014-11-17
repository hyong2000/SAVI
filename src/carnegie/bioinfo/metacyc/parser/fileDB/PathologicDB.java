package carnegie.bioinfo.metacyc.parser.fileDB;

import java.util.HashSet;

public class PathologicDB {
	
	private String uniqueID = "";
	private String name = "";
	private String productType = "";
	private HashSet<String> ecNumberSet = new HashSet<String>();
	private HashSet<String> reactionIDSet = new HashSet<String>();
	
	public String getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public HashSet<String> getEcNumberSet() {
		return ecNumberSet;
	}
	public void setEcNumberSet(HashSet<String> ecNumberSet) {
		this.ecNumberSet = ecNumberSet;
	}
	public void addEcNumber(String ecNumber) {
		this.ecNumberSet.add(ecNumber);
	}

	public HashSet<String> getReactionIDSet() {
		return reactionIDSet;
	}
	public void setReactionIDSet(HashSet<String> reactionIDSet) {
		this.reactionIDSet = reactionIDSet;
	}

	public void addReactionID(String reactionID) {
		this.reactionIDSet.add(reactionID);
	}

	public PathologicDB(String uniqueID)
	{
		this.uniqueID = uniqueID;
	}
}
