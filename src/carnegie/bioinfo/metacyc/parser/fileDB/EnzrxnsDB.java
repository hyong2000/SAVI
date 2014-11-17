package carnegie.bioinfo.metacyc.parser.fileDB;


public class EnzrxnsDB {
	
	private String uniqueID = null;
	private String enzymeName = null;
	private String reactionName = null;
	
	public String getReactionName() {
		return reactionName;
	}

	public void setReactionName(String reactionName) {
		this.reactionName = reactionName;
	}

	public EnzrxnsDB(String uniqueID)
	{
		this.uniqueID = uniqueID;
	}
	
	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}


	public String getEnzymeName() {
		return enzymeName;
	}

	public void setEnzymeName(String enzymeName) {
		this.enzymeName = enzymeName;
	}

}
