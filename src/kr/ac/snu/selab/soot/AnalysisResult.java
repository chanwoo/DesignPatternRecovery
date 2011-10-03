package kr.ac.snu.selab.soot;

import java.util.ArrayList;
import java.util.List;

import soot.SootClass;

public class AnalysisResult {
	SootClass abstractType;
	List<Caller> callerList;
	List<Creater> createrList;
	List<Store> storeList;
	
	public AnalysisResult() {
		abstractType = null;
		callerList = new ArrayList<Caller>();
		createrList = new ArrayList<Creater>();
		storeList = new ArrayList<Store>();
	}
	
	public AnalysisResult(SootClass anAbstractType, List<Caller> aCallerList,
			List<Creater> aCreaterList, List<Store> aStoreList) {
		abstractType = anAbstractType;
		callerList = aCallerList;
		createrList = aCreaterList;
		storeList = aStoreList;
	}
	
	public String toXML() {
		String result = "";
		result = result + "<AnalysisResult>";
		result = result + "<AbstractType>";
		result = result + MyUtil.removeBracket(abstractType.toString());
		result = result + "</AbstractType>";
		result = result + "<CallerList>";
		for (Caller aCaller : callerList) {
			result = result + aCaller.toXML();
		}
		result = result + "</CallerList>";
		result = result + "<CreaterList>";
		for (Creater aCreater : createrList) {
			result = result + aCreater.toXML();
		}
		result = result + "</CreaterList>";
		result = result + "<StoreList>";
		for (Store aStore : storeList) {
			result = result + aStore.toXML();
		}
		result = result + "</StoreList>";
		result = result + "</AnalysisResult>";
		return result;
	}
}
