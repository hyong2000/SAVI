package carnegie.bioinfo.savi.input.parser.fileDB;

abstract public class AbCommonDB {
	private double version = 0;
	private String pathwayID = "";
	
	public double getVersion() {
		return version;
	}
	public void setVersion(double version) {
		this.version = version;
	}
	public String getPathwayID() {
		return pathwayID;
	}
	public void setPathwayID(String pathwayID) {
		this.pathwayID = pathwayID;
	}

}
