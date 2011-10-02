package kr.ac.snu.selab.soot.projects;

public class JHotDraw_5_3Project extends AbstractProject {
	
	public JHotDraw_5_3Project (String projectName) {
		super(projectName);
	}

	@Override
	public String getClassPath() {
		// TODO: Fix it!
		return "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar:"
				+ "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/jce.jar:"
				+ "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/ui.jar:"
				+ getSourceDirectory();
	}
}
