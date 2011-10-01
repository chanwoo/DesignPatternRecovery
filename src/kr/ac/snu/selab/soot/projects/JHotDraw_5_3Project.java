package kr.ac.snu.selab.soot.projects;

public class JHotDraw_5_3Project implements Project {

	@Override
	public String getClassPath() {
		// TODO: Fix it!
		return "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/classes.jar:"
				+ "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/jce.jar:"
				+ "/System/Library/Java/JavaVirtualMachines/1.6.0.jdk/Contents/Classes/ui.jar:"
				+ getSourceDirectory();
	}

	@Override
	public String getJimpleDirectory() {
		// TODO: Fix it!
		return "/Users/chanwoo/Documents/workspace/JHotDraw_5_3/jimple";
	}

	@Override
	public String getSourceDirectory() {
		// TODO: Fix it!
		return "/Users/chanwoo/Documents/workspace/JHotDraw_5_3/src";
	}

	@Override
	public String getCallGraphPath() {
		// TODO: Fix it!
		return "/Users/chanwoo/Documents/workspace/JHotDraw_5_3/callgraph/JHotDraw_5_3_cg.txt";
	}

	@Override
	public String getResultPath() {
		// TODO: Fix it!
		return "/Users/chanwoo/Documents/workspace/JHotDraw_5_3/output/JHotDraw_5_3_analysis.xml";
	}

}
