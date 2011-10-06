package kr.ac.snu.selab.soot.projects;

public abstract class AbstractProject {
	private String projectName;

	public AbstractProject(String aProjectName) {
		projectName = aProjectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public abstract String getClassPath();

	public abstract String getJimpleDirectory();

	public abstract String getSourceDirectory();

	public abstract String getCallGraphPath();

	public abstract String getCallGraphXMLPath();

	public abstract String getOutputPath();

	public abstract String getCodeAnalysisOutputPath();
}
