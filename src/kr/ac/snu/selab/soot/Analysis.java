package kr.ac.snu.selab.soot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import soot.Body;
import soot.Hierarchy;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;

public class Analysis {
	private List<SootClass> classList;
	private MyCallGraph callGraph;
	private HashMap<String, SootClass> classMap;
	private HashMap<String, SootMethod> methodMap;
	private Hierarchy hierarchy;
	
	public Analysis(List<SootClass> aClassList, Hierarchy aHierarchy, MyCallGraph aGraph) {
		classList = new ArrayList<SootClass>();
		classMap = new HashMap<String, SootClass>();
		methodMap = new HashMap<String, SootMethod>();
		
		classList = aClassList;
		for (SootClass aClass : classList) {
			classMap.put(aClass.getName(), aClass);
			for (SootMethod aMethod : aClass.getMethods()) {
				methodMap.put(aMethod.toString(), aMethod);
			}
		}
		callGraph = aGraph;
		hierarchy = aHierarchy;
	}
	
	public Analysis(List<SootClass> aClassList, Hierarchy aHierarchy) {
		classList = new ArrayList<SootClass>();
		classMap = new HashMap<String, SootClass>();
		methodMap = new HashMap<String, SootMethod>();
		
		classList = aClassList;
		for (SootClass aClass : classList) {
			classMap.put(aClass.getName(), aClass);
			for (SootMethod aMethod : aClass.getMethods()) {
				methodMap.put(aMethod.toString(), aMethod);
			}
		}
		callGraph = new MyCallGraph(classList, methodMap);
		hierarchy = aHierarchy;
	}
	
	private List<Unit> getUnits(SootMethod aMethod) {
		List<Unit> unitList = new ArrayList<Unit>();
		if (aMethod.hasActiveBody()) {
			Body body = aMethod.getActiveBody();
			unitList.addAll(body.getUnits());
		}
		return unitList;
	}
	
	private boolean isAbstractTypeClass(SootClass aClass) {
		boolean result = false;
		if (aClass.isInterface() || aClass.isAbstract()) {
			result = true;
		}
		return result;
	}
	
	public List<SootClass> getAbstractTypeClassList() {
		List<SootClass> result = new ArrayList<SootClass>();
		for (SootClass aClass : classList) {
			if (isAbstractTypeClass(aClass)) {
				result.add(aClass);
			}
		}
		return result;
	}
	
	// <JInvokeStmt> interfaceinvoke temp$0.<State: void changeSpeed(CeilingFan)>(this)
	// <UseBox> VB(temp$0)
	// <Type> State
	// <UseBox> VB(this)
	// <Type> CeilingFan
	// <UseBox> VB(interfaceinvoke temp$0.<State: void changeSpeed(CeilingFan)>(this))
	// <Type> void
	// <MethodRef> <State: void changeSpeed(CeilingFan)>
	public boolean isInvokeStatementOfReceiverType(Unit aUnit, SootClass aType) {
		boolean result = false;
		if (aUnit instanceof JInvokeStmt) {
			JInvokeStmt statement = (JInvokeStmt) aUnit;
			ValueBox receiver = (ValueBox)(statement.getUseBoxes().get(0));
			SootClass receiverClass = classMap.get(receiver.getValue()
					.getType().toString());
			if (receiverClass != null) {
				if (receiverClass instanceof SootClass) {
					if (receiverClass.equals(aType)) {
						result = true;
					}
				}
			}
		}
		return result;
	}
	
	private boolean isClassOfSubType(SootClass aClass, SootClass aType) {
		boolean result = false;
		if (aType.isInterface() && !(aClass.isInterface())) {
			List<SootClass> implementerList = hierarchy
					.getImplementersOf(aType);
			result = implementerList.contains(aClass);
		} else if (aType.isInterface() && aClass.isInterface()) {
			result = hierarchy.isInterfaceSubinterfaceOf(aClass, aType);
		} else if (!(aType.isInterface()) && !(aClass.isInterface())) {
			result = hierarchy.isClassSubclassOf(aClass, aType);
		}
		return result;
	}
	
	// <Creation Statement> temp$0 = new Off
	// <DefBox> VB(temp$0)
	// <UseBox> VB(new Off)
	public boolean isInstanceCreationStatementOfSubType(Unit aUnit, SootClass aType) {
		boolean result = false;
		if (aUnit instanceof JAssignStmt) {
			String rightSide = aUnit.getUseBoxes().get(0).getValue().toString();
			if (rightSide.matches("new .*")) {
				String className = rightSide.substring(4);
				SootClass createdClass = classMap.get(className);
				if (createdClass != null) {
					result = isClassOfSubType(createdClass, aType);
				}
			}
		}
		return result;
	}
	
	private boolean isFieldOfType(SootField aField, SootClass aType) {
		boolean result = false;
		if (aType.getName().equals(aField.getType().toString())) {
			result = true;
		}
		return result;
	}
	
	private MethodAnalysisResult analyzeMethod(SootMethod aMethod) {
		MethodAnalysisResult result = new MethodAnalysisResult();
		return result;
	}
	
	public List<AnalysisResult> getAnalysisResultList() {
		List<AnalysisResult> analysisResultList = new ArrayList<AnalysisResult>();
		List<SootClass> abstractTypeList = getAbstractTypeClassList();
		for (SootClass anAbstractType : abstractTypeList) {
			AnalysisResult anAnalysisResult = new AnalysisResult();
			anAnalysisResult.abstractType = anAbstractType;
			for (SootClass aClass : classList) {
				for (SootField aField : aClass.getFields()) {
					if (isFieldOfType(aField, anAbstractType)) {
						anAnalysisResult.storeList.add(new Store(aClass, aField));
					}
				}
				for (SootMethod aMethod : aClass.getMethods()) {
					for (Unit aUnit : getUnits(aMethod)) {
						if (isInstanceCreationStatementOfSubType(aUnit, anAbstractType)) {
							anAnalysisResult.createrList.add(new Creater(aClass, aMethod, aUnit));
						}
						if (isInvokeStatementOfReceiverType(aUnit, anAbstractType)) {
							anAnalysisResult.callerList.add(new Caller(aClass, aMethod, aUnit));
						}
					}
				}
			}
			analysisResultList.add(anAnalysisResult);
		}
		
		return analysisResultList;
	}
}
