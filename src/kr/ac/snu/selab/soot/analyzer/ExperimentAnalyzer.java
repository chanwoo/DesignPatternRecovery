package kr.ac.snu.selab.soot.analyzer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import kr.ac.snu.selab.soot.MyCallGraph;
import kr.ac.snu.selab.soot.Util;

import soot.Body;
import soot.BodyTransformer;
import soot.Hierarchy;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JInvokeStmt;

public class ExperimentAnalyzer extends BodyTransformer {

	private static boolean touch = false;
	private Map<String, SootMethod> methodMap;

	private String output;
	private String callGraphPath;

	public ExperimentAnalyzer(String output, String callGraphPath) {
		this.output = output;
		this.callGraphPath = callGraphPath;
	}

	@SuppressWarnings("unchecked")
	private boolean isAbstractTypeClass(SootClass aClass, Hierarchy aHierarchy) {
		boolean result = false;
		if (aClass.isInterface()) {
			result = true;
		} else {
			List<SootClass> list = aHierarchy.getDirectSubclassesOf(aClass);
			if (list.size() > 0) {
				result = true;
			}
		}
		return result;
	}

	private List<SootClass> getAbstractTypeClassList(List<SootClass> classList,
			Hierarchy aHierarchy) {
		List<SootClass> result = new ArrayList<SootClass>();
		for (SootClass aClass : classList) {
			if (isAbstractTypeClass(aClass, aHierarchy)) {
				result.add(aClass);
			}
		}
		return result;
	}

	private List<SootClass> getConcreteClassListOfType(
			List<SootClass> classList, Hierarchy aHierarchy,
			SootClass anAbstractTypeClass) {
		List<SootClass> result = new ArrayList<SootClass>();
		for (SootClass aClass : classList) {
			if (isClassOfSubType(aClass, anAbstractTypeClass, aHierarchy)) {
				result.add(aClass);
			}
		}
		return result;
	}

	private List<Unit> getUnits(SootMethod aMethod) {
		List<Unit> unitList = new ArrayList<Unit>();
		if (aMethod.hasActiveBody()) {
			Body body = aMethod.getActiveBody();
			unitList.addAll(body.getUnits());
		}
		return unitList;
	}

	// <Creation Statement> temp$0 = new Off
	// <DefBox> VB(temp$0)
	// <UseBox> VB(new Off)
	private boolean isInstanceCreationStatementOfSubType(Unit aUnit,
			SootClass anAbstractTypeClass, HashMap<String, SootClass> classMap,
			Hierarchy aHierarchy) {
		boolean result = false;
		if (aUnit instanceof JAssignStmt) {
			String rightSide = aUnit.getUseBoxes().get(0).getValue().toString();
			if (rightSide.matches("new .*")) {
				String className = rightSide.substring(4);
				SootClass createdClass = classMap.get(className);
				if (createdClass != null) {
					result = isClassOfSubType(createdClass,
							anAbstractTypeClass, aHierarchy);
				}
			}
		}
		return result;
	}

	private boolean isClassOfSubType(SootClass aClass, SootClass aType,
			Hierarchy aHierarchy) {
		boolean result = false;
		if (aType.isInterface() && !(aClass.isInterface())) {
			List<SootClass> implementerList = aHierarchy
					.getImplementersOf(aType);
			result = implementerList.contains(aClass);
		} else if (aType.isInterface() && aClass.isInterface()) {
			result = aHierarchy.isInterfaceSubinterfaceOf(aClass, aType);
		} else if (!(aType.isInterface()) && !(aClass.isInterface())) {
			result = aHierarchy.isClassSubclassOf(aClass, aType);
		}
		return result;
	}

	private boolean isInstanceCreationMethodOfSubType(SootMethod aMethod,
			SootClass abstractTypeClass, HashMap<String, SootClass> classMap,
			Hierarchy aHierarchy) {
		boolean result = false;
		for (Unit aUnit : getUnits(aMethod)) {
			if (isInstanceCreationStatementOfSubType(aUnit, abstractTypeClass,
					classMap, aHierarchy)) {
				result = true;
			}
		}
		return result;
	}

	// <Field> <CeilingFan: State current_state>
	// <FieldSet Statement> this.<CeilingFan: State current_state> = temp$0
	// <DefBox> VB(this.<CeilingFan: State current_state>)
	// <UseBox> VB(this)
	// <UseBox> VB(temp$0)
	private boolean isFieldSetStatement(Unit aUnit, SootField aField) {
		boolean result = false;
		if (aUnit instanceof JAssignStmt) {
			if (aUnit.getDefBoxes().get(0).getValue().toString()
					.equals("this." + aField.toString())) {
				result = true;
			}
		}
		return result;
	}

	// To find 'setter' aka 'reference injector'
	private boolean isFieldSetMethod(SootMethod aMethod, SootField aField) {
		boolean result = false;
		for (Unit aUnit : getUnits(aMethod)) {
			if (isFieldSetStatement(aUnit, aField)) {
				result = true;
			}
		}
		return result;
	}

	// To find 'Caller' class
	//
	// <JInvokeStmt> interfaceinvoke temp$0.<State: void
	// changeSpeed(CeilingFan)>(this)
	// <UseBox> VB(temp$0)
	// <Type> State
	// <UseBox> VB(this)
	// <Type> CeilingFan
	// <UseBox> VB(interfaceinvoke temp$0.<State: void
	// changeSpeed(CeilingFan)>(this))
	// <Type> void
	// <MethodRef> <State: void changeSpeed(CeilingFan)>
	private boolean isAbstractOperationInvokeStatement(Unit aUnit,
			SootClass anAbstractTypeClass, HashMap<String, SootClass> aClassMap) {
		boolean result = false;
		if (aUnit instanceof JInvokeStmt) {
			JInvokeStmt statement = (JInvokeStmt) aUnit;
			ValueBox receiver = (ValueBox) (statement.getUseBoxes().get(0));
			SootClass receiverClass = aClassMap.get(receiver.getValue()
					.getType().toString());
			// if (receiverClass != null) {
			if (receiverClass instanceof SootClass) {
				if (receiverClass.equals(anAbstractTypeClass)) {
					result = true;
				}
			}
		}
		return result;
	}

	// To find 'Store' class
	private boolean isAbstractTypeField(SootField aField,
			SootClass abstractTypeClass) {
		boolean result = false;
		if (abstractTypeClass.getName().equals(aField.getType().toString())) {
			result = true;
		}
		return result;
	}

	private String sootMethodToXML(SootMethod aMethod) {
		String result = "<Method>" + removeBracket(aMethod.toString())
				+ "</Method>";
		return result;
	}

	private String removeBracket(String aString) {
		return aString.toString().replaceAll("<|>", " ");
	}

	private String statementToXML(Unit aUnit) {
		String result = "<Statement>" + removeBracket(aUnit.toString())
				+ "</Statement>";
		return result;
	}

	// private HashSet<Path> traverse(SootMethod aMethod, MyCallGraph cg,
	// Hierarchy aHierarchy, SootClass anAbstractTypeClass,
	// HashMap<String, SootClass> aClassMap) {
	// HashSet<Path> set = new HashSet<Path>();
	// trav(set, new Path(), aMethod, cg, aHierarchy, anAbstractTypeClass,
	// aClassMap);
	// return set;
	// }

	// private void trav(HashSet<Path> aResult, Path aPath, SootMethod aMethod,
	// MyCallGraph cg, Hierarchy aHierarchy,
	// SootClass anAbstractTypeClass, HashMap<String, SootClass> aClassMap) {
	// // prevent infinite traversing of cyclic call graph
	// if (aPath.contains(aMethod)) {
	// return;
	// } else if (isInstanceCreationMethodOfSubType(aMethod,
	// anAbstractTypeClass, aClassMap, aHierarchy)) {
	// aResult.add(aPath.add(aMethod));
	// return;
	// } else {
	// aPath.add(aMethod);
	// for (SootMethod aCallerMethod : getCallerMethods(aMethod, cg)) {
	// trav(aResult, aPath.copy(), aCallerMethod, cg, aHierarchy,
	// anAbstractTypeClass, aClassMap);
	// }
	// }
	// }

	private boolean isMethodCalledByClass(SootMethod aMethod, SootClass aClass,
			MyCallGraph cg) {
		boolean result = false;
		_isMethodCalledByClass(new Path(), result, aMethod, aClass, cg);
		return result;
	}

	private void _isMethodCalledByClass(Path aPath, boolean result,
			SootMethod aMethod, SootClass aClass, MyCallGraph cg) {
		if (aPath.contains(aMethod)) {
			return;
		}
		if (aMethod.getDeclaringClass().equals(aClass)) {
			result = true;
			return;
		} else {
			aPath.add(aMethod);
			for (SootMethod aCallerMethod : getCallerMethods(aMethod, cg)) {
				// TODO: Check this code.
				// _isMethodCalledByClass(aPath.copy(), result, aCallerMethod,
				// aClass, cg);
				_isMethodCalledByClass(aPath.copy(), result, aCallerMethod,
						aClass, cg);
			}
		}
	}

	private HashSet<SootMethod> getCallerMethods(SootMethod aMethod, MyCallGraph cg) {
		return cg.edgesInto(aMethod);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void internalTransform(Body arg0, String arg1, Map arg2) {
		if (touch)
			return;

		touch = true;
		// MyCallGraph cg = Scene.v().getCallGraph();
		methodMap = new HashMap<String, SootMethod>();

		Hierarchy hierarchy = Scene.v().getActiveHierarchy();
		List<SootClass> classList = new ArrayList<SootClass>();
		classList.addAll(Scene.v().getApplicationClasses());
		HashMap<String, SootClass> classMap = new HashMap<String, SootClass>();
		List<SootClass> abstractTypeClassList = getAbstractTypeClassList(
				classList, hierarchy);

		for (SootClass aClass : classList) {
			classMap.put(aClass.getName(), aClass);

			for (SootMethod m : aClass.getMethods()) {
				methodMap.put(m.toString(), m);
			}
		}

		MyCallGraph cg = new MyCallGraph();
		cg = cg.load(callGraphPath, methodMap);

		try {
			File outputFile = new File(output);
			File dir = outputFile.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}
			PrintWriter writer = new PrintWriter(new FileWriter(output));
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
			writer.println("<AbstractTypeList>");
			for (SootClass abstractTypeClass : abstractTypeClassList) {
				List<SootClass> concreteClassList = getConcreteClassListOfType(
						classList, hierarchy, abstractTypeClass);
				writer.println("<AbstractType>" + "<TypeName>"
						+ abstractTypeClass + "</TypeName>");

				// Identification of 'Caller' of the abstract type receiver
				writer.println("<CallerList>");
				for (SootClass aClass : classList) {
					for (SootMethod aMethod : aClass.getMethods()) {
						for (Unit aUnit : getUnits(aMethod)) {
							if (isAbstractOperationInvokeStatement(aUnit,
									abstractTypeClass, classMap)) {
								writer.print("<Caller>");
								writer.print(sootMethodToXML(aMethod));
								writer.print(statementToXML(aUnit));
								writer.println("</Caller>");
							}
						}
					}
				}
				writer.println("</CallerList>");
				writer.println();

				for (SootClass aClass : classList) {
					for (SootField aField : aClass.getFields()) {
						HashSet<Path> set = new HashSet<Path>();
						String patternName = "Strategy";
						// Identification of 'Store' of the abstract type
						// reference
						if (isAbstractTypeField(aField, abstractTypeClass)) {
							writer.println("<Field>" + "<FieldName>"
									+ removeBracket(aField.toString())
									+ "</FieldName>");
							writer.print("<Store>" + aClass + "</Store>");
							writer.println();
							// Identification of reference injecting paths and
							// creators
							for (SootMethod aMethod : aClass.getMethods()) {
								if (isFieldSetMethod(aMethod, aField)) {
									HierarchyPathCollector analzyer = new HierarchyPathCollector(
											aMethod, abstractTypeClass,
											hierarchy, cg, classMap, methodMap);
									ArrayList<Path> paths = analzyer.run();
									set.addAll(paths);
								}
							}

							writer.println("<InjectorList>");
							for (Path aPath : set) {
								writer.println("<Injector>");
								writer.println(aPath.toString());
								writer.print("<Creator>");
								SootMethod creatorMethod = aPath.last();
								writer.print(sootMethodToXML(creatorMethod));
								writer.println("</Creator>");

//								 Identification of Design Patterns
								SootClass originOfCreation;
								for (SootClass aConcreteClass :
									concreteClassList) {
									if (creatorMethod.getDeclaringClass()
											.equals(aConcreteClass)) {
										patternName = "State";
									} else if (isMethodCalledByClass(
											creatorMethod, aConcreteClass, cg)) {
										patternName = "State";
										originOfCreation = aConcreteClass;
										if (!originOfCreation
												.equals(creatorMethod
														.getDeclaringClass())) {
											writer.println("<OriginOfCreation>"
													+ originOfCreation
													+ "</OriginOfCreation>");
										}
									}
								}

								writer.println("</Injector>");
							}
							writer.println("</InjectorList>");
							writer.println("<Pattern>" + patternName
									+ "</Pattern>");
							writer.println("</Field>");
						}
					}
				}
				writer.println("</AbstractType>");
			}
			writer.println("</AbstractTypeList>");
			writer.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
