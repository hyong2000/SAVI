package carnegie.bioinfo.savi.input.parser.fileDB;


public class ManualDB extends AbCommonDB{

	private String flag = "";
	final static public String MANUAL_LIST = "Manual List";
	final static public String NEW_PATHWAY = "New Pathway";
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
