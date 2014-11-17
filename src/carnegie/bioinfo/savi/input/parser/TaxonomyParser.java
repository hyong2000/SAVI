package carnegie.bioinfo.savi.input.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import carnegie.bioinfo.common.parser.AbstractParser;
import carnegie.bioinfo.common.parser.IParser;
import carnegie.bioinfo.common.util.Logger;
import carnegie.bioinfo.savi.input.parser.fileDB.TaxonomyDB;

public class TaxonomyParser extends AbstractParser{

	private int totalNumOfLine = 0;
	
	private HashMap<String,TaxonomyDB> taxonomyDBMap = new HashMap<String,TaxonomyDB>();


	/**
	 * @param args
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		TaxonomyParser parser = new TaxonomyParser("./input/" + "nodes.dmp");
		parser.parse();
		String currentNodeId = "7";
//		HashSet<String> childNodeSet = parser.getListOfChildNodes(currentNodeId);
//		System.out.println(childNodeSet);
//		System.out.println(childNodeSet.size());
		Boolean hasChild = parser.isInChildNode(currentNodeId,"335928");
		System.out.println(hasChild);
		
//		HashSet<String> parentNodeSet = parser.getListOfParentNodes(currentNodeId);
//		System.out.println(parentNodeSet);
//		System.out.println(parentNodeSet.size());
		Boolean hasParent = parser.isInParentNode(currentNodeId,"335928");
		System.out.println(hasParent);
	}
	
	public Boolean isInChildNode(String sourceNodeId, String targetNodeId) {
		// TODO Auto-generated method stub
		HashSet<String> childNodeSet = getListOfChildNodes(sourceNodeId);
		return childNodeSet.contains(targetNodeId);
	}
	
	public Boolean isInParentNode(String sourceNodeId, String targetNodeId) {
		// TODO Auto-generated method stub
		HashSet<String> parentNodeSet = getListOfParentNodes(sourceNodeId);
		return parentNodeSet.contains(targetNodeId);
	}

	private HashSet<String> getListOfChildNodes(String currentNodeId) {
		// TODO Auto-generated method stub
		HashSet<String> resultSet = new HashSet<String>();
		
		if(!taxonomyDBMap.containsKey(currentNodeId))
			return resultSet;
		
		doDepthFirstSearch(currentNodeId, resultSet);
		
		return resultSet;
	}
	
	private void doDepthFirstSearch(String currentNodeId,
			HashSet<String> resultSet) {
		// TODO Auto-generated method stub
		HashSet<String> currentChildrenList = taxonomyDBMap.get(currentNodeId).getChildrenNodeIdList();
		resultSet.addAll(currentChildrenList);
		Iterator<String> iterator = currentChildrenList.iterator();
		while(iterator.hasNext())
		{
			String childNodeId = iterator.next();
			doDepthFirstSearch(childNodeId, resultSet);
		}
	}

	private HashSet<String> getListOfParentNodes(String currentNodeId) {
		// TODO Auto-generated method stub
		HashSet<String> resultSet = new HashSet<String>();
		
		if(!taxonomyDBMap.containsKey(currentNodeId))
			return resultSet;
			
		while(!currentNodeId.equals("1"))
		{
			String parentNode = taxonomyDBMap.get(currentNodeId).getParentNodeId();
			resultSet.add(parentNode);
			currentNodeId = parentNode;
		}
		
		return resultSet;
	}
	
	public TaxonomyParser(String fileName)
	{
//		super.fileName = fileName;
		super(fileName);
	}
	
	@Override
	public void doLineParse(String line) {
		// TODO Auto-generated method stub
		totalNumOfLine++;
		
		StringTokenizer st = new StringTokenizer(line, "|");
		if (!st.hasMoreTokens())
			return;
		
		String nodeId = st.nextToken().trim();
		String parentNodeId = st.nextToken().trim();
		String rankName = st.nextToken().trim();
		
		//taxonomy info for current node
		TaxonomyDB taxonomyDB = null;
		if(!taxonomyDBMap.containsKey(nodeId))
			taxonomyDB = new TaxonomyDB();
		else
			taxonomyDB = taxonomyDBMap.get(nodeId);
		taxonomyDB.setNodeId(nodeId);
		taxonomyDB.setParentNodeId(parentNodeId);
		taxonomyDB.setRankName(rankName);
		taxonomyDBMap.put(nodeId, taxonomyDB);

		//taxonomy info for parent node
		if(!nodeId.equals(parentNodeId)){	// except the root node
			TaxonomyDB parentTaxonomyDB = null;
			if(!taxonomyDBMap.containsKey(parentNodeId))
				parentTaxonomyDB = new TaxonomyDB();
			else
				parentTaxonomyDB = taxonomyDBMap.get(parentNodeId);
			
			parentTaxonomyDB.setNodeId(parentNodeId);
			parentTaxonomyDB.addChild(nodeId);
			taxonomyDBMap.put(parentNodeId, parentTaxonomyDB);
		}
//		System.out.println(taxonomyDBMap);

	}

	@Override
	public void doAfterParse() {
		// TODO Auto-generated method stub
		System.out.println("Total Number of Taxanomy (TaxanomyParser): " + taxonomyDBMap.size());

		mapTaxonomyIdToName();
	}

	private void mapTaxonomyIdToName() {
		// TODO Auto-generated method stub
		IParser parser = new TaxonomyNameParser("./input/" + "names.dmp");
		try {
			parser.parse();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HashMap<String,String> taxonomyNameMap = parser.getResultHashMap();
		Iterator<String> taxonomyIterator = taxonomyDBMap.keySet().iterator();
		while(taxonomyIterator.hasNext())
		{
			String taxonomyId =  taxonomyIterator.next();
			TaxonomyDB taxonomyDB = taxonomyDBMap.get(taxonomyId);
			String scientificName = taxonomyNameMap.get(taxonomyId);
			taxonomyDB.setScientificName(scientificName);
//			System.out.println(taxonomyDB.getScientificName());
		}
	}

	@Override
	public HashMap getResultHashMap() {
		// TODO Auto-generated method stub
		return taxonomyDBMap;
	}

	@Override
	public ArrayList getResultCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTaxonomyName(String taxonomyID) {
		// TODO Auto-generated method stub
		if(taxonomyDBMap.containsKey(taxonomyID))
			return taxonomyDBMap.get(taxonomyID).getScientificName();
		Logger.println("TaxonomyName for taxonomyID(" + taxonomyID + ") is not found in nodes.dmp file!!");
		return "";
	}
	
}
