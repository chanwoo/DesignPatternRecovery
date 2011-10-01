package kr.ac.snu.selab.soot;

import java.io.File;
import java.util.Map;

import soot.Scene;
import soot.SceneTransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.CallGraphUtil;

public class CallGraphWriter extends SceneTransformer {
	private String outputPath;

	public CallGraphWriter(String outputPath) {
		this.outputPath = outputPath;
		File file = new File(outputPath);
		File parentFile = file.getParentFile();
		if (!parentFile.exists()) {
			parentFile.mkdirs();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void internalTransform(String arg0, Map arg1) {
		CallGraph cg = Scene.v().getCallGraph();
		CallGraphUtil.write(cg, outputPath);
	}
}
