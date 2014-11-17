package carnegie.bioinfo.savi.input.parser.fileDB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class AippDB extends AbCommonDB{

	private ArrayList<String> evHinfRefList = new ArrayList<String>();
	
	public ArrayList<String> getEvHinfRefList() {
		return evHinfRefList;
	}
	public void setEvHinfRefList(ArrayList<String> evHinfRefList) {
		this.evHinfRefList = evHinfRefList;
	}
		
}
