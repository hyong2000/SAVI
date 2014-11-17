package carnegie.bioinfo.metacyc.parser.fileDB;

import java.util.Collection;
import java.util.HashSet;

public class ReactionDB {
	
	private String uniqueID = "";
	private String commonName = "";
	private HashSet<String> primaryCompoundList = new HashSet<String>();
	private HashSet<String> enzymeList = new HashSet<String>();
	private HashSet<String> pathwayCommonNameList = new HashSet<String>();
	private HashSet<String> pathwayIDList = new HashSet<String>();
	private HashSet<String> enzymaticReactionIDList = new HashSet<String>();
	
	public HashSet<String> getEnzymaticReactionIDList() {
		return enzymaticReactionIDList;
	}

	public void setEnzymaticReactionIDList(HashSet<String> enzymaticReactionIDList) {
		this.enzymaticReactionIDList = enzymaticReactionIDList;
	}
	
	public void addEnzymaticReactionID(String enarxnID)
	{
		enzymaticReactionIDList.add(enarxnID);
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	
	public HashSet<String> getPathwayIDList() {
		return pathwayIDList;
	}

	public void setPathwayIDList(HashSet<String> pathwayIDList) {
		this.pathwayIDList = pathwayIDList;
	}

	public HashSet<String> getPathwayCommonNameList() {
		return pathwayCommonNameList;
	}

	public void setPathwayCommonNameList(HashSet<String> pathwayList) {
		this.pathwayCommonNameList = pathwayList;
	}

	public HashSet<String> getEnzymeList() {
		return enzymeList;
	}

	public void setEnzymeList(HashSet<String> enzymeList) {
		this.enzymeList = enzymeList;
	}

	public ReactionDB(String uniqueID)
	{
		this.uniqueID = uniqueID;
	}
	
	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}
	
	public int getNumberOfPrimaryCompound()
	{
		return primaryCompoundList.size();
	}

	public HashSet<String> getPrimaryCompoundList()
	{
		HashSet<String> list = (HashSet<String>)primaryCompoundList.clone();
		return list;
	}

	public void addPrimaryCompoundName(String name)
	{
		primaryCompoundList.add(name);
	}
	
	public void addAllPrimaryCompoundName(Collection nameCollectoin)
	{
		primaryCompoundList.addAll(nameCollectoin);
	}
	
	public boolean isPrimaryCompound(String name)
	{
		return primaryCompoundList.contains(name);
	}

	public void addEnzymeName(String enzymeName) {
		// TODO Auto-generated method stub
		enzymeList.add(enzymeName);
	}
	
	public void addPathwayCommonName(String commonName)
	{
		pathwayCommonNameList.add(commonName);
	}

	public void addPathwayID(String uniqueID) {
		// TODO Auto-generated method stub
		pathwayIDList.add(uniqueID);
	}
}
