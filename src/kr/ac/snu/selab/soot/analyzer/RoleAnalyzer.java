package kr.ac.snu.selab.soot.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kr.ac.snu.selab.soot.Analysis;
import kr.ac.snu.selab.soot.AnalysisResult;
import kr.ac.snu.selab.soot.MyUtil;

import soot.Body;
import soot.BodyTransformer;
import soot.Hierarchy;
import soot.Scene;
import soot.SootClass;

public class RoleAnalyzer extends BodyTransformer {
	private static boolean touch = false;
	private String outputPath;
	
	public RoleAnalyzer(String anOutputPath) {
		outputPath = anOutputPath;
	}
	
	@Override
	protected void internalTransform(Body b, String phaseName, Map options) {
		if (touch)
			return;

		touch = true;
		
		Hierarchy hierarchy = Scene.v().getActiveHierarchy();
		List<SootClass> classList = new ArrayList<SootClass>();
		classList.addAll(Scene.v().getApplicationClasses());
		Analysis analysis = new Analysis(classList, hierarchy);
		
		String result = "";
		result = result + "<AnalysisResultList>";
		for (AnalysisResult anAnalysisResult : analysis.getAnalysisResultList()) {
			result = result + anAnalysisResult.toXML();
		}
		result = result + "</AnalysisResultList>";
		
		MyUtil.stringToFile(result, outputPath);
	}

}
