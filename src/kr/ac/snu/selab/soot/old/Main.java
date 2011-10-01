package kr.ac.snu.selab.soot.old;

import soot.PackManager;
import soot.Transform;

public class Main {
	public static void main(String[] args) {

		ExperimentProject project = null;
		// project = new SampleProject();
		project = new JHotDraw_5_3Project();
		// project = new JHotDraw_6_0_b1Project();

		String[] as = { "-w", "-cp", project.getClassPath(), "-f", "J", "-d",
				project.getOutputDirectory(), "--process-dir",
				project.getSourceDirectory() };

		 PackManager.v().getPack("wjtp")
		 .add(new Transform("wjtp.Experiment", new Experiment()));

		soot.Main.main(as);
	}
}
