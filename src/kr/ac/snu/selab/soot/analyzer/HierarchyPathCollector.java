package kr.ac.snu.selab.soot.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.snu.selab.soot.MyCallGraph;
import soot.Body;
import soot.Hierarchy;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.internal.JAssignStmt;

public class HierarchyPathCollector extends CallGraphPathCollector {
	private Hierarchy hierarchy;
	private SootClass abstractTypeClass;
	private HashMap<String, SootClass> classMap;
	private HashMap<String, Boolean> subTypeMap;

	public HierarchyPathCollector(SootMethod callee,
			SootClass abstractTypeClass, Hierarchy hierarchy,
			MyCallGraph graph, HashMap<String, SootClass> classMap,
			Map<String, SootMethod> methodMap) {
		super(callee, graph, methodMap);
		this.abstractTypeClass = abstractTypeClass;
		this.classMap = classMap;
		this.hierarchy = hierarchy;
		subTypeMap = new HashMap<String, Boolean>();
	}

	@Override
	protected boolean isGoal(SootMethod method) {
		if (method == null)
			return false;

		String key = method.toString();
		if (subTypeMap.containsKey(key)) {
			return subTypeMap.get(key);
		}
		
		boolean b = isInstanceCreationMethodOfSubType(method,
				abstractTypeClass, classMap, hierarchy);
		subTypeMap.put(key, b);

		return b;
	}

	private boolean isInstanceCreationMethodOfSubType(SootMethod aMethod,
			SootClass abstractTypeClass, HashMap<String, SootClass> classMap,
			Hierarchy aHierarchy) {
		boolean result = false;
		for (Unit aUnit : getUnits(aMethod)) {
			if (isInstanceCreationStatementOfSubType(aUnit, abstractTypeClass,
					classMap, aHierarchy)) {
				result = true;
			}
		}
		return result;
	}

	// <Creation Statement> temp$0 = new Off
	// <DefBox> VB(temp$0)
	// <UseBox> VB(new Off)
	private boolean isInstanceCreationStatementOfSubType(Unit aUnit,
			SootClass anAbstractTypeClass, HashMap<String, SootClass> classMap,
			Hierarchy aHierarchy) {
		boolean result = false;
		if (aUnit instanceof JAssignStmt) {
			String rightSide = aUnit.getUseBoxes().get(0).getValue().toString();
			if (rightSide.matches("new .*")) {
				String className = rightSide.substring(4);
				SootClass createdClass = classMap.get(className);
				if (createdClass != null)
					result = isClassOfSubType(createdClass,
							anAbstractTypeClass, aHierarchy);
			}
		}
		return result;
	}

	private boolean isClassOfSubType(SootClass aClass, SootClass aType,
			Hierarchy aHierarchy) {
		boolean result = false;
		if (aType.isInterface() && !(aClass.isInterface())) {
			List<SootClass> implementerList = aHierarchy
					.getImplementersOf(aType);
			result = implementerList.contains(aClass);
		} else if (aType.isInterface() && aClass.isInterface()) {
			result = aHierarchy.isInterfaceSubinterfaceOf(aClass, aType);
		} else if (!(aType.isInterface()) && !(aClass.isInterface())) {
			result = aHierarchy.isClassSubclassOf(aClass, aType);
		}
		return result;
	}

	private List<Unit> getUnits(SootMethod aMethod) {
		List<Unit> unitList = new ArrayList<Unit>();
		if (aMethod.hasActiveBody()) {
			Body body = aMethod.getActiveBody();
			unitList.addAll(body.getUnits());
		}
		return unitList;
	}
}
