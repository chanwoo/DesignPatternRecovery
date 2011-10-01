package kr.ac.snu.selab.soot.old;

public class JHotDraw_6_0_b1Project implements ExperimentProject {

	@Override
	public String getOutputDirectory() {
		return "/Users/chanwoo/Documents/workspace/StrategyPatternExample/sootOutput";
	}

	@Override
	public String getSourceDirectory() {
		return "/Users/chanwoo/Documents/workspace/JHotDraw60b1/src";
	}

	@Override
	public String getClassPath() {
		return "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar:"
				+ "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/jce.jar:"
				+ "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/ui.jar:"
				+ "/Users/chanwoo/Documents/workspace/JHotDraw60b1/lib/jdo2-api-2.0.jar:"
				+ getSourceDirectory();
	}

}
