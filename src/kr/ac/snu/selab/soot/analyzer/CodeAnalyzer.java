package kr.ac.snu.selab.soot.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.ValueBox;
import soot.jimple.internal.JInvokeStmt;

public class CodeAnalyzer extends BodyTransformer {

	private static boolean touch = false;
	private String codeAnalysisOutputPath;

	public CodeAnalyzer(String aCodeAnalysisOutputPath) {
		this.codeAnalysisOutputPath = aCodeAnalysisOutputPath;
	}

	private List<Unit> getUnits(SootMethod aMethod) {
		List<Unit> unitList = new ArrayList<Unit>();
		if (aMethod.hasActiveBody()) {
			Body body = aMethod.getActiveBody();
			unitList.addAll(body.getUnits());
		}
		return unitList;
	}

	private String removeBracket(String aString) {
		return aString.toString().replaceAll("<|>", " ");
	}

	private void writeClass(SootClass aClass, PrintWriter writer) {
		writer.print("<Class>");
		writer.print("<ToString>");
		writer.print(removeBracket(aClass.toString()));
		writer.print("</ToString>");
		writer.print("<FieldList>");
		for (SootField aField : aClass.getFields()) {
			writer.print("<Field>");
			writer.print("<ToString>");
			writer.print(removeBracket(aField.toString()));
			writer.print("</ToString>");
			writer.print("<Type>");
			writer.print(removeBracket(aField.getType().toString()));
			writer.print("</Type>");
			writer.print("<Name>");
			writer.print(removeBracket(aField.getName()));
			writer.print("</Name>");
			writer.print("</Field>");
		}
		writer.print("</FieldList>");
		writer.print("<MethodList>");
		for (SootMethod aMethod : aClass.getMethods()) {
			writeMethod(aMethod, writer);
		}
		writer.print("</MethodList>");
		writer.print("</Class>");
	}
	
	private void writeMethod(SootMethod aMethod, PrintWriter writer) {
		writer.print("<Method>");
		writer.print("<ToString>");
		writer.print(removeBracket(aMethod.toString()));
		writer.print("</ToString>");
		writer.print("<ReturnType>");
		writer.print(removeBracket(aMethod.getReturnType().toString()));
		writer.print("</ReturnType>");
		writer.print("<ParameterList>");
		int i = 0;
		for (Object aType : aMethod.getParameterTypes()) {
			writer.print(String.format("<Parameter%d>", i));
			writer.print(removeBracket(aType.toString()));
			writer.print(String.format("</Parameter%d>", i));
		}
		writer.print("</ParameterList>");
		writer.print("<UnitList>");
		for (Unit aUnit : getUnits(aMethod)) {
			writeUnit(aUnit, writer);
		}
		writer.print("</UnitList>");
		writer.print("</Method>");
	}
	
	private void writeUnit(Unit aUnit, PrintWriter writer) {
		writer.print("<Unit>");
		writer.print("<ToString>");
		writer.print(removeBracket(aUnit.toString()));
		writer.print("</ToString>");
		writer.print("<UnitClass>");
		writer.print(removeBracket(aUnit.getClass().getName()));
		writer.print("</UnitClass>");
		for (UnitBox unitBox : aUnit.getBoxesPointingToThis()) {
			writer.print("<UnitBoxPointingToThis>");
			writer.print(removeBracket(unitBox.toString()));
			writer.print("</UnitBoxPointingToThis>");
		}
		int i = 0;
		for (ValueBox valueBox : aUnit.getDefBoxes()) {
			writer.print(String.format("<DefBox%d>", i));
			writer.print(removeBracket(valueBox.toString()));
			writer.print(String.format("</DefBox%d>", i));
			i = i + 1;
		}
		i = 0;
		for (ValueBox valueBox : aUnit.getUseBoxes()) {
			writer.print(String.format("<UseBox%d>", i));
			writer.print(removeBracket(valueBox.toString()));
			writer.print(String.format("</UseBox%d>", i));
			i = i + 1;
		}
		if (aUnit instanceof JInvokeStmt) {
			JInvokeStmt jInvokeStatement = (JInvokeStmt)aUnit;
			SootMethod invokeMethod = jInvokeStatement.getInvokeExpr().getMethod();
			writer.print("<InvokeMethod>");
			writer.print(removeBracket(invokeMethod.toString()));
			writer.print("</InvokeMethod>");
			writer.print("<ArgumentList>");
			for (Object invokeExpr : jInvokeStatement.getInvokeExpr().getArgs()) {
				writer.print("<Argument>");
				writer.print(removeBracket(invokeExpr.toString()));
				writer.print("</Argument>");
			}
			writer.print("</ArgumentList>");
		}
		writer.print("</Unit>");
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void internalTransform(Body arg0, String arg1, Map arg2) {
		if (touch)
			return;

		touch = true;
		List<SootClass> classList = new ArrayList<SootClass>();
		classList.addAll(Scene.v().getApplicationClasses());

		try {
			File outputFile = new File(codeAnalysisOutputPath);
			File dir = outputFile.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			PrintWriter writer = new PrintWriter(new FileWriter(codeAnalysisOutputPath));
			writer.print("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			writer.print("<ClassList>");
			for (SootClass aClass : classList) {
				writeClass(aClass, writer);
			}
			writer.print("</ClassList>");
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
