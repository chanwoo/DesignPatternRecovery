package kr.ac.snu.selab.soot;

import soot.SootClass;
import soot.SootMethod;
import soot.Unit;

public class Caller {
	private SootClass callerClass;
	private SootMethod callerMethod;
	private Unit callerStatement;
//	private SootClass receiverClass;
	
	public Caller(SootClass aCallerClass, SootMethod aCallerMethod, Unit aCallerStatement) {
		callerClass = aCallerClass;
		callerMethod = aCallerMethod;
		callerStatement = aCallerStatement;
//		receiverClass = aReceiverClass;
	}
	
	public String toXML() {
		String result = "";
		result = result + "<Caller>";
		result = result + "<CallerClass>";
		result = result + MyUtil.removeBracket(callerClass.toString());
		result = result + "</CallerClass>";
		result = result + "<CallerMethod>";
		result = result + MyUtil.removeBracket(callerMethod.toString());
		result = result + "</CallerMethod>";
		result = result + "<CallerStatement>";
		result = result + MyUtil.removeBracket(callerStatement.toString());
		result = result + "</CallerStatement>";
//		result = result + "<ReceiverClass>";
//		result = result + Util.removeBracket(receiverClass.toString());
//		result = result + "</ReceiverClass>";
		result = result + "</Caller>";
		return result;
	}
}
