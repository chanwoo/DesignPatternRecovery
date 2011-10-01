package soot.jimple.toolkits.callgraph;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;

import soot.SootMethod;

public class CallGraphUtil {
	public static void write(CallGraph cg, String filePath) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(filePath));
			Set<Edge> edges = cg.edges;
			for (Edge edge : edges) {
				SootMethod src = edge.src();
				SootMethod tgt = edge.tgt();
				// System.out.println(src.getName() + "->" + tgt.getName());

				writer.print(src.toString());
				writer.print("\t");
				writer.println(tgt.toString());
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	public static kr.ac.snu.selab.soot.MyCallGraph load(String filePath,
			Map<String, SootMethod> methodMap) {
		kr.ac.snu.selab.soot.MyCallGraph g = new kr.ac.snu.selab.soot.MyCallGraph();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] tokens = line.split("\t");
				if (tokens == null || tokens.length != 2)
					continue;

				g.addEdge(tokens[0], tokens[1], methodMap);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}
		}
		return g;
	}
}
