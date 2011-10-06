package kr.ac.snu.selab.soot;

import kr.ac.snu.selab.soot.projects.AbstractProject;
import kr.ac.snu.selab.soot.projects.ProjectFileNotFoundException;
import kr.ac.snu.selab.soot.projects.ProjectManager;

public class Main {
	public static void main(String[] args) {

		ProjectManager projects = ProjectManager.getInstance();
		try {
			projects.loadProejcts();
		} catch (ProjectFileNotFoundException e) {
			System.err.println("Can not find projects.xml file.");
			return;
		}

		String projectName = "";

		// projectName = "JHotDraw_5_3";
		// projectName = "StatePatternExample";
		// projectName = "StrategyPatternExample";
		projectName = "StatePatternExample2";

		AbstractProject project = projects.getProject(projectName);
		if (project == null) {
			System.err.println("Can not find project information: "
					+ projectName);
			return;
		}

		AnalyzerRunner.run(project);
	}
}
