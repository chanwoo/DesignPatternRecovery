package kr.ac.snu.selab.soot;

import kr.ac.snu.selab.soot.projects.JHotDraw_5_3Project;
import kr.ac.snu.selab.soot.projects.Project;
import kr.ac.snu.selab.soot.projects.*;

public class Main {
	public static void main(String[] args) {
		boolean load = true;
		if (args != null && args.length > 0) {
			if (args[0].equals("w")) {
				load = false;
			}
		}
		
		// set load to false if you want to generate a call graph
//		load = false;

		Project project = null;

		project = new JHotDraw_5_3Project();
//		project = new StateSampleProject();

		if (load) {
			AnalyzerRunner.run(project);
		} else {
			CallGraphGenerator.run(project);
		}
	}
}
