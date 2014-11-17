package carnegie.bioinfo.savi.input.parser.fileDB;

import java.util.HashSet;

public class TaxonomyDB{

	private String nodeId = "";
	private String parentNodeId = "";
	private HashSet<String> childrenNodeIdList = new HashSet<String>();
	private String rankName = "";
	private String scientificName = "";
	
	public String getScientificName() {
		return scientificName;
	}
	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public String getParentNodeId() {
		return parentNodeId;
	}
	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}
	public HashSet<String> getChildrenNodeIdList() {
		return childrenNodeIdList;
	}
	public void setChildrenNodeIdList(HashSet<String> childrenNodeIdList) {
		this.childrenNodeIdList = childrenNodeIdList;
	}

	public void addChild(String childNodeId) {
		this.childrenNodeIdList.add(childNodeId);
	}

	public String getRankName() {
		return rankName;
	}
	public void setRankName(String rankName) {
		this.rankName = rankName;
	}
		
}
