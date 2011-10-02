package kr.ac.snu.selab.soot.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import kr.ac.snu.selab.soot.MyCallGraph;
import soot.SootMethod;

public abstract class CallGraphPathCollector {
	protected HashMap<String, ArrayList<Path>> pathsMap;
	protected HashSet<String> hitSet;
	protected SootMethod startMethod;
	protected MyCallGraph graph;
	protected Map<String, SootMethod> methodMap;

	private static final int PATH_SET_SIZE_LIMIT = 10;

	public CallGraphPathCollector(SootMethod startMethod, MyCallGraph graph,
			Map<String, SootMethod> methodMap) {
		pathsMap = new HashMap<String, ArrayList<Path>>();
		hitSet = new HashSet<String>();

		this.startMethod = startMethod;
		this.graph = graph;
		this.methodMap = methodMap;
	}

	public ArrayList<Path> run() {
		System.out.println("Call path collecting start: "
				+ startMethod.toString());
		long tick1 = System.currentTimeMillis();

		pathsMap.clear();
		hitSet.clear();

		ArrayList<Path> paths = new ArrayList<Path>();
		int result = findPaths(startMethod, graph, methodMap, paths);
		if (result == CYCLE) {
			// throw an exception
			return paths;
		}

		long tick2 = System.currentTimeMillis();
		System.out.println("Call path collecting finished, " + (tick2 - tick1));

		return paths;
	}

	private static final int DONE = 0;
	private static final int CYCLE = 1;

	private int findPaths(SootMethod method, MyCallGraph graph,
			Map<String, SootMethod> methodMap, ArrayList<Path> output) {
		if (method == null)
			return DONE;

		String methodKey = method.toString();

		if (pathsMap.containsKey(methodKey)) {
			output.addAll(pathsMap.get(methodKey));
			return DONE;
		}

		if (isGoal(method)) {
			Path path = new Path();
			path.addTop(method);
			output.add(path);
			hitSet.add(methodKey);
			pathsMap.put(methodKey, output);
			return DONE;
		}

		if (hitSet.contains(methodKey)) {
			return CYCLE;
		} else {
			hitSet.add(methodKey);
		}

		HashSet<SootMethod> callers = graph.edgesInto(method);
		for (SootMethod m : callers) {
			if (m == null)
				continue;

			if (output.size() >= PATH_SET_SIZE_LIMIT) {
				// For performance
				break;
			}

			ArrayList<Path> pathSet = new ArrayList<Path>();
			int result = findPaths(m, graph, methodMap, pathSet);
			if (result == CYCLE)
				continue;

			for (Path p : pathSet) {
				Path p1 = p.copy();
				p1.addTop(method);
				output.add(p1);
			}
		}

		pathsMap.put(methodKey, output);
		return DONE;
	}

	protected abstract boolean isGoal(SootMethod method);
}
