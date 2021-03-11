package hu.bme.mit.yakindu.analysis.workhere;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
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
		System.out.println("public static void print (IExampleStatemachine s) {");
		while (iterator.hasNext()) {
			EObject content = iterator.next();			
			if(content instanceof VariableDefinition) {
				VariableDefinition varDef = (VariableDefinition)content;
				String cap = varDef.getName().substring(0,1).toUpperCase() + varDef.getName().substring(1);
				char shortName = cap.charAt(0);
				System.out.println("System.out.println(\""+shortName+ "= \" + s.getSCInterface().get" + cap+"());");
				}
			if(content instanceof EventDefinition) {
				EventDefinition evDef = (EventDefinition)content;
				String cap = evDef.getName().substring(0,1).toUpperCase() + evDef.getName().substring(1);
				char shortName = cap.charAt(0);
				System.out.println("System.out.println(\"Event_"+cap+ "= \" + s.getSCInterface().get" + cap+"());");
				}
			
		}		
		System.out.println("}");
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
	
	
}
