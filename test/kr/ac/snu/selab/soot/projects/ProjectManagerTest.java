package kr.ac.snu.selab.soot.projects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.channels.FileChannel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectManagerTest {
	private boolean projectsFileExists;
	private String projectsFilePath, tempProjectsFilePath;
	private ProjectManager manager;

	@Test
	public void projectInformationTest() throws ProjectFileNotFoundException {
		manager.loadProejcts();

		AbstractProject project = manager.getProject("ProjectA");
		assertNotNull("Project is empty", project);

		assertEquals("ProjectA", project.getProjectName());
		assertEquals("SRC", project.getSourceDirectory());
		assertEquals("CP1:CP2", project.getClassPath());
		assertEquals("JIMPLE", project.getJimpleDirectory());
		assertEquals("OUTPUT_XML", project.getOutputPath());
		assertEquals("CALLGRAPH_TXT", project.getCallGraphPath());
		assertEquals("CALLGRAPH_XML", project.getCallGraphXMLPath());
		assertEquals("CODE_ANALYSIS", project.getCodeAnalysisOutputPath());
	}

	@Test
	public void argumentsTest() throws ProjectFileNotFoundException {
		manager.loadProejcts();

		AbstractProject project = manager.getProject("ProjectB");
		assertNotNull("Project is empty", project);

		assertEquals("ProjectB", project.getProjectName());
		assertEquals("ROOT/ProjectB/src", project.getSourceDirectory());
		assertEquals("CP1:CP2:ROOT/ProjectB/src", project.getClassPath());
		assertEquals("ROOT/ProjectB/jimple", project.getJimpleDirectory());
		assertEquals("ROOT/ProjectB/output/ProjectB_analysis.xml",
				project.getOutputPath());
		assertEquals("ROOT/ProjectB/callgraph/ProjectB_callgraph.txt",
				project.getCallGraphPath());
		assertEquals("ROOT/ProjectB/callgraph/ProjectB_callgraph.xml",
				project.getCallGraphXMLPath());
		assertEquals(
				"ROOT/ProjectB/code_analysis_output/ProjectB_code_analysis.xml",
				project.getCodeAnalysisOutputPath());
	}

	@Test
	public void noProjectTest() throws ProjectFileNotFoundException {
		manager.loadProejcts();

		AbstractProject project = manager.getProject("NoProject");
		assertNull("Project should be empty", project);
	}

	@Before
	public void init() throws Exception {
		URL url = ClassLoader
				.getSystemResource(ProjectManager.PROJECT_FILE_NAME);
		if (url == null)
			fail("Project file initialize falied.");

		File file = new File(url.getPath());
		projectsFilePath = file.getAbsolutePath();

		if (file.exists()) {
			projectsFileExists = true;
			File originalProjectsFile = new File(file.getParentFile(),
					"projects.xml.tmp");
			tempProjectsFilePath = originalProjectsFile.getAbsolutePath();
			copy(projectsFilePath, tempProjectsFilePath);
		} else {
			projectsFileExists = false;
		}

		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new FileWriter(projectsFilePath));
			writer.println("<?xml version=\"1.0\"?>");
			writer.println("<projects>");
			writer.println("<project name=\"ProjectA\">");
			writer.println("<project_root>PROJECT_ROOT</project_root>");
			writer.println("<sources>");
			writer.println("<path>SRC</path>");
			writer.println("</sources>");
			writer.println("<classpaths>");
			writer.println("<path>CP1</path>");
			writer.println("<path>CP2</path>");
			writer.println("</classpaths>");
			writer.println("<output>");
			writer.println("<jimple>JIMPLE</jimple>");
			writer.println("<output_xml>OUTPUT_XML</output_xml>");
			writer.println("<call_graph>CALLGRAPH_TXT</call_graph>");
			writer.println("<call_graph_xml>CALLGRAPH_XML</call_graph_xml>");
			writer.println("<code_ana_output_xml>CODE_ANALYSIS</code_ana_output_xml>");
			writer.println("</output>");
			writer.println("</project>");
			writer.println("<project name=\"ProjectB\">");
			writer.println("<project_root>ROOT/${PROJECT_NAME}</project_root>");
			writer.println("<sources>");
			writer.println("<path>${PROJECT_ROOT}/src</path>");
			writer.println("</sources>");
			writer.println("<classpaths>");
			writer.println("<path>CP1</path>");
			writer.println("<path>CP2</path>");
			writer.println("<path>${SRC_PATHS}</path>");
			writer.println("</classpaths>");
			writer.println("<output>");
			writer.println("<jimple>${PROJECT_ROOT}/jimple</jimple>");
			writer.println("<output_xml>${PROJECT_ROOT}/output/${PROJECT_NAME}_analysis.xml</output_xml>");
			writer.println("<call_graph>${PROJECT_ROOT}/callgraph/${PROJECT_NAME}_callgraph.txt</call_graph>");
			writer.println("<call_graph_xml>${PROJECT_ROOT}/callgraph/${PROJECT_NAME}_callgraph.xml</call_graph_xml>");
			writer.println("<code_ana_output_xml>${PROJECT_ROOT}/code_analysis_output/${PROJECT_NAME}_code_analysis.xml</code_ana_output_xml>");
			writer.println("</output>");
			writer.println("</project>");
			writer.println("</projects>");

			writer.flush();
		} catch (IOException e) {
			throw e;
		} finally {
			if (writer != null)
				writer.close();
		}

		manager = ProjectManager.getInstance();
	}

	@After
	public void cleanUp() {
		if (projectsFileExists) {
			File tempFile = new File(tempProjectsFilePath);
			copy(tempProjectsFilePath, projectsFilePath);
			tempFile.delete();
		} else {
			File projectsFile = new File(projectsFilePath);
			projectsFile.delete();
		}
	}

	private static void copy(String source, String target) {
		// 복사 대상이 되는 파일 생성
		File sourceFile = new File(source);

		// 스트림, 채널 선언
		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel fcin = null;
		FileChannel fcout = null;

		try {
			// 스트림 생성
			inputStream = new FileInputStream(sourceFile);
			outputStream = new FileOutputStream(target);
			// 채널 생성
			fcin = inputStream.getChannel();
			fcout = outputStream.getChannel();

			// 채널을 통한 스트림 전송
			long size = fcin.size();
			fcin.transferTo(0, size, fcout);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 자원 해제
			try {
				fcout.close();
			} catch (IOException ioe) {
			}
			try {
				fcin.close();
			} catch (IOException ioe) {
			}
			try {
				outputStream.close();
			} catch (IOException ioe) {
			}
			try {
				inputStream.close();
			} catch (IOException ioe) {
			}
		}
	}
}
