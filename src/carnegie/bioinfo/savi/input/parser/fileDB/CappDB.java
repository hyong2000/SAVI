package carnegie.bioinfo.savi.input.parser.fileDB;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class CappDB extends AbCommonDB{

	private ArrayList<String> taxonExcludeWithinList = new ArrayList<String>();
	private ArrayList<String> taxonExcludeOutsideList = new ArrayList<String>();
	private ArrayList<String> taxonIncludeWithinList = new ArrayList<String>();
	private ArrayList<String> rxnSafeList = new ArrayList<String>();
	private ArrayList<String> rxnTroubleList = new ArrayList<String>();
	private String lowestCommonParent = "";
	private String numOfSpecies = "";
	private ArrayList<String> evHinfRefList = new ArrayList<String>();
	private boolean isTaxonWarning = false;
	
//	private String taxonWarningComment = "";
//	private String taxonWarningFlagIfWithinComment = "";
//	private String taxonWarningFlagIfOutsideOfComment = "";
	private String typeOfCriteria = "";

	private ArrayList<String> taxonWarningFlagIfWithin = new ArrayList<String>();
	private ArrayList<String> taxonWarningFlagIfOutsideOf = new ArrayList<String>();

	public ArrayList<String> getTaxonWarningFlagIfWithin() {
		return taxonWarningFlagIfWithin;
	}
	public void setTaxonWarningFlagIfWithin(
			ArrayList<String> taxonWarningFlagIfWithin) {
		this.taxonWarningFlagIfWithin = taxonWarningFlagIfWithin;
	}
	public ArrayList<String> getTaxonWarningFlagIfOutsideOf() {
		return taxonWarningFlagIfOutsideOf;
	}
	public void setTaxonWarningFlagIfOutsideOf(
			ArrayList<String> taxonWarningFlagIfOutsideOf) {
		this.taxonWarningFlagIfOutsideOf = taxonWarningFlagIfOutsideOf;
	}

//	public String getTaxonWarningComment() {
//		return taxonWarningComment;
//	}
//	public void setTaxonWarningComment(String taxonWarningComment) {
//		this.taxonWarningComment = taxonWarningComment;
//	}
//	public String getTaxonWarningFlagIfWithinComment() {
//		return taxonWarningFlagIfWithinComment;
//	}
//	public void setTaxonWarningFlagIfWithinComment(
//			String taxonWarningFlagIfWithinComment) {
//		this.taxonWarningFlagIfWithinComment = taxonWarningFlagIfWithinComment;
//	}
//	public String getTaxonWarningFlagIfOutsideOfComment() {
//		return taxonWarningFlagIfOutsideOfComment;
//	}
//	public void setTaxonWarningFlagIfOutsideOfComment(
//			String taxonWarningFlagIfOutsideOfComment) {
//		this.taxonWarningFlagIfOutsideOfComment = taxonWarningFlagIfOutsideOfComment;
//	}
	
	
	public String getTypeOfCriteria() {
		return typeOfCriteria;
	}
	public void setTypeOfCriteria(String typeOfCriteria) {
		this.typeOfCriteria = typeOfCriteria;
	}
	
	
	
	public boolean isTaxonWarning() {
		return isTaxonWarning;
	}
	public void setTaxonWarning(boolean isTaxonWarning) {
		this.isTaxonWarning = isTaxonWarning;
	}
	public ArrayList<String> getTaxonExcludeWithinList() {
		return taxonExcludeWithinList;
	}
	public void setTaxonExcludeWithinList(ArrayList<String> taxonExcludeWithinList) {
		this.taxonExcludeWithinList = taxonExcludeWithinList;
	}
	public ArrayList<String> getTaxonExcludeOutsideList() {
		return taxonExcludeOutsideList;
	}
	public void setTaxonExcludeOutsideList(ArrayList<String> taxonExcludeOutsideList) {
		this.taxonExcludeOutsideList = taxonExcludeOutsideList;
	}
	public ArrayList<String> getTaxonIncludeWithinList() {
		return taxonIncludeWithinList;
	}
	public void setTaxonIncludeWithinList(ArrayList<String> taxonIncludeWithinList) {
		this.taxonIncludeWithinList = taxonIncludeWithinList;
	}
	public ArrayList<String> getRxnSafeList() {
		return rxnSafeList;
	}
	public void setRxnSafeList(ArrayList<String> rxnSafeList) {
		this.rxnSafeList = rxnSafeList;
	}
	public ArrayList<String> getRxnTroubleList() {
		return rxnTroubleList;
	}
	public void setRxnTroubleList(ArrayList<String> rxnTroubleList) {
		this.rxnTroubleList = rxnTroubleList;
	}
	public String getLowestCommonParent() {
		return lowestCommonParent;
	}
	public void setLowestCommonParent(String lowestCommonParent) {
		this.lowestCommonParent = lowestCommonParent;
	}
	public String getNumOfSpecies() {
		return numOfSpecies;
	}
	public void setNumOfSpecies(String numOfSpecies) {
		this.numOfSpecies = numOfSpecies;
	}
	public ArrayList<String> getEvHinfRefList() {
		return evHinfRefList;
	}
	public void setEvHinfRefList(ArrayList<String> evHinfRefList) {
		this.evHinfRefList = evHinfRefList;
	}
	
	
	
}
