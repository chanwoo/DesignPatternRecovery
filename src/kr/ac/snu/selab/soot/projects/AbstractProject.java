package kr.ac.snu.selab.soot.projects;

public abstract class AbstractProject {
	private String projectDirectory;
	private String projectName;
	private static String workSpaceDirectory = "/Users/chanwoo/Documents/workspace/";
	
	public AbstractProject(String aProjectName) {
		projectDirectory = workSpaceDirectory + aProjectName + "/";
		projectName = aProjectName;
	}
	
	public String getClassPath() {
		return "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar:"
				+ "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/jce.jar:"
				+ getSourceDirectory();
	}

	public String getJimpleDirectory() {
		return projectDirectory + "/jimple";
	}

	public String getSourceDirectory() {
		return projectDirectory + "/src";
	}

	public String getCallGraphPath() {
		return projectDirectory + "/callgraph/" + projectName + "_callgraph.txt";
	}
	
	public String getCallGraphXMLPath() {
		return projectDirectory + "/callgraph/" + projectName + "_callgraph.xml";
	}

	public String getOutputPath() {
		return projectDirectory + "/output/" + projectName + "_analysis.xml";
	}
	
	public String getCodeAnalysisOutputPath() {
		return projectDirectory + "/code_analysis_output/" + projectName + "_code_analysis.xml";
	}

}
