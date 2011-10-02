package kr.ac.snu.selab.soot.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.snu.selab.soot.MyCallGraph;
import kr.ac.snu.selab.soot.Util;

import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;

public class CallGraphXMLCreater extends BodyTransformer {
	private static boolean touch = false;
	private String outputPath;
	private String callGraphPath;
	
	public CallGraphXMLCreater(String callGraphPath, String callGraphXMLPath) {
		this.callGraphPath = callGraphPath;
		this.outputPath = callGraphXMLPath;
	}

	@Override
	protected void internalTransform(Body b, String phaseName, Map options) {
		// TODO Auto-generated method stub
		if (touch)
			return;
		touch = true;
		
		List<SootClass> classList = new ArrayList<SootClass>();
		classList.addAll(Scene.v().getApplicationClasses());
		HashMap<String, SootMethod> methodMap = new HashMap<String, SootMethod>();
		
		for (SootClass aClass : classList) {
			for (SootMethod aMethod : aClass.getMethods()) {
				methodMap.put(aMethod.toString(), aMethod);
			}
		}
		
		MyCallGraph cg = new MyCallGraph();
		cg = cg.load(callGraphPath, methodMap);
		Util.stringToFile(cg.toXML(), outputPath);
	}

}
