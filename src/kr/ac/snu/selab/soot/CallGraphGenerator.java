package kr.ac.snu.selab.soot;

import kr.ac.snu.selab.soot.projects.Project;
import soot.PackManager;
import soot.Transform;

public class CallGraphGenerator {
	public static void run(Project project) {
		String[] as = { "-w", "-cp", project.getClassPath(), "-f", "J", "-d",
				project.getJimpleDirectory(), "--process-dir",
				project.getSourceDirectory() };

		PackManager
				.v()
				.getPack("wjtp")
				.add(new Transform("wjtp.CallGraphWriter", new CallGraphWriter(
						project.getCallGraphPath())));

		soot.Main.main(as);
	}
}
