package kr.ac.snu.selab.soot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import soot.Body;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.internal.JInvokeStmt;

public class MyCallGraph {

	private HashMap<String, HashSet<SootMethod>> sourceToTargetSetMap;
	private HashMap<String, HashSet<SootMethod>> targetToSourceSetMap;

	public MyCallGraph() {
		sourceToTargetSetMap = new HashMap<String, HashSet<SootMethod>>();
		targetToSourceSetMap = new HashMap<String, HashSet<SootMethod>>();		
	}
	
	public MyCallGraph(List<SootClass> aClassList, HashMap<String, SootMethod> methodMap) {
		sourceToTargetSetMap = new HashMap<String, HashSet<SootMethod>>();
		targetToSourceSetMap = new HashMap<String, HashSet<SootMethod>>();
		
		for (SootClass aClass : aClassList) {
			for (SootMethod aMethod : aClass.getMethods()) {
				List<Unit> unitList = new ArrayList<Unit>();
				if (aMethod.hasActiveBody()) {
					Body body = aMethod.getActiveBody();
					unitList.addAll(body.getUnits());
				}
				for (Unit aUnit : unitList) {
					if (aUnit instanceof JInvokeStmt) {
						JInvokeStmt jInvokeStatement = (JInvokeStmt)aUnit;
						addEdge(aMethod.toString(), jInvokeStatement.getInvokeExpr().getMethod().toString(), methodMap);
					}
				}
			}
		}
	}
	
	public String toXML() {
		String result = "";
		result = result + "<CallGraph>";
		result = result + "<SourceToTargetSetList>";
		for (Entry<String, HashSet<SootMethod>> anEntry : sourceToTargetSetMap.entrySet()) {
			result = result + "<SourceToTargetSet>";
			result = result + "<Source>" + MyUtil.removeBracket(anEntry.getKey()) + "</Source>";
			result = result + "<TargetSet>";
			for (SootMethod aMethod : anEntry.getValue()) {
				result = result + "<Target>" + MyUtil.removeBracket(aMethod.toString()) + "</Target>";
			}
			result = result + "</TargetSet>";
			result = result + "</SourceToTargetSet>";
		}
		result = result + "</SourceToTargetSetList>";
		result = result + "<TargetToSourceSetList>";
		for (Entry<String, HashSet<SootMethod>> anEntry : targetToSourceSetMap.entrySet()) {
			result = result + "<TargetToSourceSet>";
			result = result + "<Target>" + MyUtil.removeBracket(anEntry.getKey()) + "</Target>";
			result = result + "<SourceSet>";
			for (SootMethod aMethod : anEntry.getValue()) {
				result = result + "<Source>" + MyUtil.removeBracket(aMethod.toString()) + "</Source>";
			}
			result = result + "</SourceSet>";
			result = result + "</TargetToSourceSet>";
		}
		result = result + "</TargetToSourceSetList>";
		result = result + "</CallGraph>";
		return result;
	}
	
	public MyCallGraph load (String filePath, Map<String, SootMethod> methodMap) {
		MyCallGraph g = new MyCallGraph();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("\t");
				if (tokens == null || tokens.length != 2)
					continue;

				g.addEdge(tokens[0], tokens[1], methodMap);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}
		return g;
	}

	public HashSet<SootMethod> edgesInto(SootMethod aMethod) {
		String key = aMethod.toString();
		if (targetToSourceSetMap.containsKey(key)) {
			return targetToSourceSetMap.get(key);
		}
		else {
			return new HashSet<SootMethod>();
		}
	}
	
	public HashSet<SootMethod> edgesOutOf(SootMethod aMethod) {
		String key = aMethod.toString();
		if (sourceToTargetSetMap.containsKey(key)) {
			return sourceToTargetSetMap.get(key);
		}
		else {
			return new HashSet<SootMethod>();
		}
	}

	public void addEdge(String source, String target,
			Map<String, SootMethod> methodMap) {
		if (!methodMap.containsKey(source) || !methodMap.containsKey(target))
			return;

		if (!targetToSourceSetMap.containsKey(target)) {
			HashSet<SootMethod> sourceSet = new HashSet<SootMethod>();
			targetToSourceSetMap.put(target, sourceSet);
		}
		
		HashSet<SootMethod> sourceSet = targetToSourceSetMap.get(target);
		sourceSet.add(methodMap.get(source));
		
		if (!sourceToTargetSetMap.containsKey(source)) {
			HashSet<SootMethod> targetSet = new HashSet<SootMethod>();
			sourceToTargetSetMap.put(source, targetSet);
		}
		
		HashSet<SootMethod> targetSet = sourceToTargetSetMap.get(source);
		targetSet.add(methodMap.get(target));
	}
}
