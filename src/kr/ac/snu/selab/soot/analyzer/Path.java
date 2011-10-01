package kr.ac.snu.selab.soot.analyzer;

import java.util.ArrayList;

import soot.SootMethod;

public class Path {
	public ArrayList<SootMethod> methods;

	public Path() {
		methods = new ArrayList<SootMethod>();
	}

	public Path copy() {
		Path p = new Path();
		p.methods.addAll(methods);
		return p;
	}

	public void add(SootMethod method) {
		methods.add(method);
	}

	public void addTop(SootMethod method) {
		methods.add(0, method);
	}

	public boolean contains(SootMethod aMethod) {
		return methods.contains(aMethod);
	}

	public SootMethod last() {
		return methods.get(methods.size() - 1);
	}

	public boolean isEmpty() {
		return methods.isEmpty();
	}

	@Override
	public String toString() {
		String result = "<Path>";
		for (SootMethod aMethod : methods) {
			result = result + sootMethodToXML(aMethod);
		}
		result = result + "</Path>";
		return result;
	}

	private String sootMethodToXML(SootMethod aMethod) {
		String result = "<Method>" + removeBracket(aMethod.toString())
				+ "</Method>";
		return result;
	}

	private String removeBracket(String aString) {
		return aString.toString().replaceAll("<|>", " ");
	}

	// public String toString1() {
	// int size = methods.size();
	//
	// StringBuffer buffer = new StringBuffer();
	// int i = 0;
	// for (i = 0; i < size - 1; i++) {
	// buffer.append(methods.get(i).toString());
	// buffer.append("->");
	// }
	// buffer.append(methods.get(i).toString());
	// return buffer.toString();
	// }
}
