package kr.ac.snu.selab.soot.old;

public class SampleProject implements ExperimentProject {

	@Override
	public String getOutputDirectory() {
		return "/Users/chanwoo/Documents/workspace/StrategyPatternExample/sootOutput";
	}

	@Override
	public String getSourceDirectory() {
		return "/Users/chanwoo/Documents/workspace/StrategyPatternExample/src";
	}

	@Override
	public String getClassPath() {
		return "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar:"
				+ "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/jce.jar:"
				+ getSourceDirectory();
	}

}
