package kr.ac.snu.selab.soot;

import soot.SootClass;
import soot.SootField;

public class Store {
	private SootClass storeClass;
	private SootField storeField;

	public Store (SootClass aStoreClass, SootField aStoreField) {
		storeClass = aStoreClass;
		storeField = aStoreField;
	}
	
	public String toXML() {
		String result = "";
		result = result + "<Store>";
		result = result + "<StoreClass>";
		result = result + MyUtil.removeBracket(storeClass.toString());
		result = result + "</StoreClass>";
		result = result + "<StoreField>";
		result = result + MyUtil.removeBracket(storeField.toString());
		result = result + "</StoreField>";
		result = result + "</Store>";
		return result;
	}
}


