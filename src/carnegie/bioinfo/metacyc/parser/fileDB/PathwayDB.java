package carnegie.bioinfo.metacyc.parser.fileDB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class PathwayDB extends Object{
	
	private String uniqueID = null;
	private ArrayList<String> typeList = new ArrayList<String>();
	private HashSet<String> commonNameList = new HashSet<String>();
	private HashSet<String> reactionNameList = new HashSet<String>();
	private HashSet<String> citationList = new HashSet<String>();
	
	private HashSet<String> subPathwayList = new HashSet<String>();
	private HashSet<String> superPathwayList = new HashSet<String>();
	private HashSet<String> evidenceCodeList = new HashSet<String>();


	public HashSet<String> getCitationList() {
		return citationList;
	}

	public void setCitationList(HashSet<String> citationList) {
		this.citationList = citationList;
	}

	public void addCitation(String citation) {
		this.citationList.add(citation);
	}
	
	public HashSet<String> getSuperPathwayList() {
		return superPathwayList;
	}

	public void setSuperPathwayList(HashSet<String> superPathwayList) {
		this.superPathwayList = superPathwayList;
	}

	public void addSuperPathway(String superPathwayID)
	{
		superPathwayList.add(superPathwayID);
	}
	
	public HashSet<String> getEvidenceCodeList() {
		return evidenceCodeList;
	}

	public void setEvidenceCodeList(HashSet<String> evidenceCodeList) {
		this.evidenceCodeList = evidenceCodeList;
	}
	
	public void addEvidenceCode(String evidenceCode) {
		this.evidenceCodeList.add(evidenceCode);
	}

	public boolean isSynonym(String name) {
		Iterator<String> iterator = commonNameList.iterator();
		while(iterator.hasNext())
		{
			String curSynonym = ((String)iterator.next()).trim();
			if(curSynonym.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	public HashSet<String> getSubPathwayList() {
		return subPathwayList;
	}

	public void setSubPathwayList(HashSet<String> subPathwayList) {
		this.subPathwayList = subPathwayList;
	}
	
	public void addSubPathway(String subPathwayID)
	{
		subPathwayList.add(subPathwayID);
	}
	
	public ArrayList<String> getTypeList() {
		return typeList;
	}

	public void setTypeList(ArrayList<String> typeList) {
		this.typeList = typeList;
	}

	public void addType(String type) {
		this.typeList.add(type);
	}
	
	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public HashSet<String> getCommonNameList() {
		return commonNameList;
	}

	public void setCommonNameList(HashSet<String> synonymList) {
		this.commonNameList = synonymList;
	}

	public void addCommonName(String synonym) {
		// TODO Auto-generated method stub
		commonNameList.add(synonym);
	}

	public HashSet<String> getReactionIDList() {
		return reactionNameList;
	}

	public void setReactionNameList(HashSet<String> reactionList) {
		this.reactionNameList = reactionList;
	}
	
	public void addReactionName(String reaction) {
		// TODO Auto-generated method stub
		reactionNameList.add(reaction);
	}
	
}
