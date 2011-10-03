package kr.ac.snu.selab.soot;

import soot.SootClass;
import soot.SootMethod;
import soot.Unit;

public class Creater {
	private SootClass createrClass;
	private SootMethod createrMethod;
	private Unit createrStatement;
//	private SootClass createdClass;


	public Creater(SootClass aCreaterClass, SootMethod aCreaterMethod, Unit aCreaterStatement) {
		createrClass = aCreaterClass;
		createrMethod = aCreaterMethod;
		createrStatement = aCreaterStatement;
//		createdClass = aCreatedClass;
	}	
	
	public String toXML() {
		String result = "";
		result = result + "<Creater>";
		result = result + "<CreaterClass>";
		result = result + MyUtil.removeBracket(createrClass.toString());
		result = result + "</CreaterClass>";
		result = result + "<CreaterMethod>";
		result = result + MyUtil.removeBracket(createrMethod.toString());
		result = result + "</CreaterMethod>";
		result = result + "<CreaterStatement>";
		result = result + MyUtil.removeBracket(createrStatement.toString());
		result = result + "</CreaterStatement>";
//		result = result + "<CreatedClass>";
//		result = result + Util.removeBracket(createdClass.toString());
//		result = result + "</CreatedClass>";
		result = result + "</Creater>";
		return result;
	}
}