package kr.ac.snu.selab.soot.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.internal.JInvokeStmt;

public class CallGraphTXTCreater extends BodyTransformer {

	private static boolean touch = false;
	private String callGraphPath;

	public CallGraphTXTCreater(String callGraphPath) {
		this.callGraphPath = callGraphPath;
	}

	private List<Unit> getUnits(SootMethod aMethod) {
		List<Unit> unitList = new ArrayList<Unit>();
		if (aMethod.hasActiveBody()) {
			Body body = aMethod.getActiveBody();
			unitList.addAll(body.getUnits());
		}
		return unitList;
	}

	@Override
	protected void internalTransform(Body arg0, String arg1, Map arg2) {
		if (touch) {
			return;
		}
		touch = true;
		List<SootClass> classList = new ArrayList<SootClass>();
		classList.addAll(Scene.v().getApplicationClasses());
		
		PrintWriter writer = null;
		try {
			File outputFile = new File(callGraphPath);
			File dir = outputFile.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			writer = new PrintWriter(new FileWriter(callGraphPath));
			for (SootClass aClass : classList) {
				for (SootMethod aMethod : aClass.getMethods()) {
					for (Unit aUnit : getUnits(aMethod)) {
						if (aUnit instanceof JInvokeStmt) {
							JInvokeStmt jInvokeStatement = (JInvokeStmt)aUnit;
							writer.print(aMethod.toString());
							writer.print("\t");
							writer.println(jInvokeStatement.getInvokeExpr().getMethod().toString());
						}
					}
				}
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}
}
