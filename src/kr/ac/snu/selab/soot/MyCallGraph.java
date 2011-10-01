package kr.ac.snu.selab.soot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import soot.SootMethod;

public class MyCallGraph {

	private HashMap<String, HashSet<SootMethod>> map;

	public MyCallGraph() {
		map = new HashMap<String, HashSet<SootMethod>>();
	}

	public List<SootMethod> edgesInto(SootMethod aMethod,
			Map<String, SootMethod> methodMap) {
		ArrayList<SootMethod> srcs = new ArrayList<SootMethod>();
		String key = aMethod.toString();

		HashSet<SootMethod> set = map.get(key);
		if (set == null) {
			return srcs;
		}

		for (SootMethod k : set) {
			if (k != null)
				srcs.add(k);
		}
		return srcs;
	}

	public void addEdge(String src, String tgt,
			Map<String, SootMethod> methodMap) {
		if (!methodMap.containsKey(src) || !methodMap.containsKey(tgt))
			return;

		if (!map.containsKey(tgt)) {
			HashSet<SootMethod> set = new HashSet<SootMethod>();
			map.put(tgt, set);
		}

		HashSet<SootMethod> set = map.get(tgt);
		set.add(methodMap.get(src));
	}
}
