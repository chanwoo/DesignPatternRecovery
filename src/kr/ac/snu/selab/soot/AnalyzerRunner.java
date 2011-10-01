package kr.ac.snu.selab.soot;

import kr.ac.snu.selab.soot.analyzer.ExperimentAnalyzer;
import kr.ac.snu.selab.soot.projects.Project;
import soot.PackManager;
import soot.Transform;

public class AnalyzerRunner {
	public static void run(Project project) {
		final String[] as = { "-cp", project.getClassPath(), "-f", "J", "-d",
				project.getJimpleDirectory(), "--process-dir",
				project.getSourceDirectory() };

		PackManager
				.v()
				.getPack("jtp")
				.add(new Transform("jtp.Experiment", new ExperimentAnalyzer(
						project.getResultPath(), project.getCallGraphPath())));

		soot.Main.main(as);
	}
}
