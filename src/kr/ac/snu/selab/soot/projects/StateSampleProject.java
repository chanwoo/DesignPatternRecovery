package kr.ac.snu.selab.soot.projects;

public class StateSampleProject implements Project {

	@Override
	public String getClassPath() {
		// TODO: Fix it!
		return "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar:"
				+ "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/jce.jar:"
				+ getSourceDirectory();
	}

	@Override
	public String getJimpleDirectory() {
		// TODO: Fix it!
		return "/Users/chanwoo/Documents/workspace/StatePatternExample/jimple";
	}

	@Override
	public String getSourceDirectory() {
		// TODO: Fix it!
		return "/Users/chanwoo/Documents/workspace/StatePatternExample/src";
	}

	@Override
	public String getCallGraphPath() {
		// TODO: Fix it!
		return "/Users/chanwoo/Documents/workspace/StatePatternExample/jimple/state_analysis_cg.txt";
	}

	@Override
	public String getResultPath() {
		// TODO: Fix it!
		return "/Users/chanwoo/Documents/workspace/StatePatternExample/output/state_example_analysis.xml";
	}

}
