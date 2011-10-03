package kr.ac.snu.selab.soot;

import kr.ac.snu.selab.soot.analyzer.CodeAnalyzer;
import kr.ac.snu.selab.soot.analyzer.ExperimentAnalyzer;
import kr.ac.snu.selab.soot.projects.AbstractProject;
import soot.BodyTransformer;
import soot.PackManager;
import soot.Transform;
import kr.ac.snu.selab.soot.analyzer.*;

public class AnalyzerRunner {
	
	public static void run(AbstractProject project) {
		final String[] arguments = { "-cp", project.getClassPath(), "-f", "J", "-d",
				project.getJimpleDirectory(), "--process-dir",
				project.getSourceDirectory() };
		
//		BodyTransformer bodyTransformer = new CallGraphTXTCreater(project.getCallGraphPath());
//		BodyTransformer bodyTransformer = new CallGraphXMLCreater(project.getCallGraphPath(), project.getCallGraphXMLPath());
//		BodyTransformer bodyTransformer = new CodeAnalyzer(project.getCodeAnalysisOutputPath());
//		BodyTransformer bodyTransformer = new ExperimentAnalyzer(project.getOutputPath(), project.getCallGraphPath());
		BodyTransformer bodyTransformer = new RoleAnalyzer(project.getOutputPath());
		
		PackManager
				.v()
				.getPack("jtp")
				.add(new Transform("jtp.Experiment", bodyTransformer));

		soot.Main.main(arguments);
	}
}
