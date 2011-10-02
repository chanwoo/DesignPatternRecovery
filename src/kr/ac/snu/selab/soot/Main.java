package kr.ac.snu.selab.soot;

import kr.ac.snu.selab.soot.projects.*;

public class Main {
	public static void main(String[] args) {

		AbstractProject project = null;

//		project = new JHotDraw_5_3Project("JHotDraw_5_3");
		project = new StateSampleProject("StatePatternExample");
//		project = new StrategySampleProject("StrategyPatternExample");

		AnalyzerRunner.run(project);
	}
}
