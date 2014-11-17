package carnegie.bioinfo.metacyc.parser.fileDB;

import java.util.HashSet;

public class ProteinDB {
	
	private String uniqueID = "";
	private String commonName = "";
	private String componentOf = "";
	private HashSet<String> componentSet = new HashSet<String>();
	private HashSet<String> catalyzerSet = new HashSet<String>();

	
	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public HashSet<String> getCatalyzerSet() {
		return catalyzerSet;
	}

	public void setCatalyzerSet(HashSet<String> catalyzerSet) {
		this.catalyzerSet = catalyzerSet;
	}

	public void addCatalyzer(String catalyzer) {
		this.catalyzerSet.add(catalyzer);
	}
	
	public HashSet<String> getComponentSet() {
		return componentSet;
	}

	public void setComponentSet(HashSet<String> componentList) {
		this.componentSet = componentList;
	}

	public void addComponent(String componentName) {
		this.componentSet.add(componentName);
	}

	public String getComponentOf() {
		return componentOf;
	}

	public void setComponentOf(String componentOf) {
		this.componentOf = componentOf;
	}

	public ProteinDB(String uniqueID)
	{
		this.uniqueID = uniqueID;
	}
	
	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

}
