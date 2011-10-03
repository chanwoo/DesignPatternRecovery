package kr.ac.snu.selab.soot;

import kr.ac.snu.selab.soot.projects.*;

public class Main {
	public static void main(String[] args) {

		AbstractProject project = null;

//		project = new JHotDraw_5_3Project("JHotDraw_5_3");
//		project = new StatePatternExampleProject("StatePatternExample");
//		project = new StrategyPatternExampleProject("StrategyPatternExample");
		project = new StatePatternExample2Project("StatePatternExample2");

		AnalyzerRunner.run(project);
	}
}
