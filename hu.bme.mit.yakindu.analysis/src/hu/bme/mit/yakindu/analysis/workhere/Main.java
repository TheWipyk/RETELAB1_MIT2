package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		List<String> variables = new LinkedList<String>();
		List<String> events = new LinkedList<String>();
		while (iterator.hasNext()) {
			EObject content = iterator.next();			
			if(content instanceof VariableDefinition) {
				VariableDefinition varDef = (VariableDefinition)content;
				variables.add(varDef.getName());
				}
			if(content instanceof EventDefinition) {
				EventDefinition evDef = (EventDefinition)content;
				events.add(evDef.getName());
				}			
		}	
		System.out.println("public class RunStatechart {");
		System.out.println(""+
				"\tpublic static void main(String[] args) throws IOException {\r\n" + 
				"\t\tExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"\t\tBufferedReader reader = new BufferedReader(new InputStreamReader(System.in));\r\n" + 
				"\t\ts.setTimer(new TimerService());\r\n" + 
				"\t\tRuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"\t\ts.init();\r\n" + 
				"\t\ts.enter();\r\n" + 
				"\t\ts.runCycle();\r\n" + 
				"\t\tString command = reader.readLine();");
		System.out.println(""+		
				"\t\twhile(!command.matches(\"exit\")) {\r\n" +
				"\t\t\tswitch(command) {");
		for (String loopstring : events) {
			String cap = loopstring.substring(0, 1).toUpperCase() + loopstring.substring(1);
			System.out.println(""+
					"\t\t\tcase \""+loopstring +"\":\r\n" + 
					"\t\t\t\ts.raise"+cap +"();\r\n" + 
					"\t\t\t\ts.runCycle();\r\n" + 
					"\t\t\t\tprint(s);\r\n" + 
					"\t\t\t\tcommand = reader.readLine();\r\n" + 
					"\t\t\t\tbreak;");
		}
		System.out.println("" + 
				"\t\t\tcase \"exit\":\r\n" + 
				"\t\t\t\tbreak;\r\n" + 
				"\t\t\tdefault:\r\n" + 
				"\t\t\t\tSystem.out.println(\"Unknown command\");	\r\n" + 
				"\t\t\t\tprint(s);\r\n" + 
				"\t\t\t\tcommand = reader.readLine();\r\n" + 
				"\t\t\t\tbreak;\r\n" + 
				"\t\t\t}\r\n" + 
				"\t\t}\r\n"+
				"\t\tSystem.out.println(\"Final variables:\");\r\n" + 
				"\t\tprint(s);\r\n" + 
				"\t\t\tSystem.exit(0);\r\n" + 
				"\t}\r\n");
	
		System.out.println(""+
				"\tpublic static void print(IExampleStatemachine s) {\r\n");
		for (String loopstring : variables) {
			String cap = loopstring.substring(0, 1).toUpperCase() + loopstring.substring(1);
			char shortName = cap.charAt(0);
			System.out.println(""+
				"\t\tSystem.out.println(\""+shortName +" = \" + s.getSCInterface().get"+cap+"());");	
		}
		
		System.out.println("\t}");				
		
		System.out.println("}");
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
	
	
}
