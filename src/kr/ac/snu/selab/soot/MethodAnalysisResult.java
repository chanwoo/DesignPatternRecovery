package kr.ac.snu.selab.soot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MethodAnalysisResult {
	String methodName;
	List<FlowIn> FlowInList;
	List<FlowOut> FlowOutList;
	HashMap<FlowIn, FlowOut> flowInToFlowOutMap;
	HashMap<FlowOut, FlowIn> flowOutToFlowInMap;
	
	public MethodAnalysisResult() {
		FlowInList = new ArrayList<FlowIn>();
		FlowOutList = new ArrayList<FlowOut>();
	}
}
