/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.util.ArrayList;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import jcd.data.ArgumentObject;
import jcd.data.Box;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.LineConnector;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import static jcd.test_bed.ClassBuilder.BOOLEAN;
import static jcd.test_bed.ClassBuilder.INT;
import static jcd.test_bed.ClassBuilder.PRIVATE;
import static jcd.test_bed.ClassBuilder.PUBLIC;
import static jcd.test_bed.ClassBuilder.THREAD_EXAMPLE;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;

/**
 *
 * @author RaniSons
 */
public class ThreadExampleClassBuilder 
{
    public static final String TEST_PACKAGE = "test";
    
    public ThreadExampleClassBuilder() {}
    
    public void hardCodeThreadExampleClass(DataManager dataManager)
    {
        dataManager.addClassObject(createThreadExampleClass());
    }
    
    // HERE IS THE FIFTH HARD CODED CLASS
    private ClassObject createThreadExampleClass()
    {
        Box box = initializeNewBox();
        addClassNameText(THREAD_EXAMPLE, box);
        
        ClassObject threadExampleClass = new ClassObject(THREAD_EXAMPLE, box);
        
        threadExampleClass.setParentName(null);
        threadExampleClass.setPackageName(TEST_PACKAGE);
        threadExampleClass.setInterfaceType(false);
        
        ArrayList<String> interfaceNames = new ArrayList<>();
        threadExampleClass.setInterfaceNames(interfaceNames);
        ArrayList<VariableObject> variables = addThreadExampleClassVariables();
        threadExampleClass.setVariables(variables);
        ArrayList<MethodObject> methods = addThreadExampleClassMethods();
        threadExampleClass.setMethods(methods);
        ArrayList<String> javaApiPackages = new ArrayList<>();
        threadExampleClass.setJavaApiPackages(javaApiPackages);
        ArrayList<LineConnector> linesConnector = addThreadExampleClassLineConnectors();
        threadExampleClass.setLineConnectors(linesConnector); 
        
        return threadExampleClass;
    }
    
    private ArrayList<VariableObject> addThreadExampleClassVariables()
    {
        VariableObject work = new VariableObject();
        work.setName("work");
        work.setScope(PRIVATE);
        work.setType(BOOLEAN);
        work.setStaticType(false);
        work.setFinalType(false);
        
        ArrayList<VariableObject> variables = new ArrayList<>();
        variables.add(work);
        
        return variables;
    }
    
    private ArrayList<MethodObject> addThreadExampleClassMethods()
    {
        MethodObject addVariables = new MethodObject();
        addVariables.setName("addVariables");
        addVariables.setType(INT);
        addVariables.setScope(PUBLIC);
        addVariables.setStaticType(false);
        addVariables.setFinalType(false);
        addVariables.setAbstractType(false);
        
        ArrayList<ArgumentObject> addVariablesArguments = new ArrayList<>();
        
        ArgumentObject arg1 = new ArgumentObject();
        arg1.setName("arg1");
        arg1.setType(INT);
        
        ArgumentObject arg2 = new ArgumentObject();
        arg2.setName("arg2");
        arg2.setType(INT);
        
        addVariablesArguments.add(arg1);
        addVariablesArguments.add(arg2);
        
        addVariables.setArguments(addVariablesArguments);

        
        // SECOND METHOD
        MethodObject main = new MethodObject();
        main.setName("main");
        main.setType("void");
        main.setScope(PUBLIC);
        main.setStaticType(true);
        main.setFinalType(false);
        main.setAbstractType(false);
        
        ArrayList<ArgumentObject> mainArguments = new ArrayList<>();
        ArgumentObject args = new ArgumentObject();
        args.setName("args");
        args.setType("String[]");
        mainArguments.add(args);
        
        main.setArguments(mainArguments);
        
        // FINISHED HARD CODING THE METHODS
        ArrayList<MethodObject> methods = new ArrayList<>();
        methods.add(addVariables);
        methods.add(main);
        
        return methods;
    }
    
    private ArrayList<LineConnector> addThreadExampleClassLineConnectors()
    {
        ArrayList<LineConnector> linesConnector = new ArrayList<>();
        
        LineConnector lineConnector1 = createThreadExampleClassLineConnector();
        
        linesConnector.add(lineConnector1);
        
        return linesConnector;
    }
    
    private LineConnector createThreadExampleClassLineConnector()
    {
        LineConnector lineConnector = new LineConnector();
        
        int startX = (int) (Math.random() * 500);
        int startY = (int) (Math.random() * 500);
        int endX = (int) (Math.random() * 500);
        int endY = (int) (Math.random() * 500);
        Line line = new Line(startX, startY, endX, endY);
        
        lineConnector.getLines().add(line);
        return lineConnector;
    }
    
    /**
     * Helper method to initialize a new box.
     */
    private Box initializeNewBox()
    {
        // x and y values where the box will be origined
        int x = (int) ((Math.random() * 800));
        int y = 300;
        
        Box box = new Box();
        box.getMainVBox().setTranslateX(x);
        box.getMainVBox().setTranslateY(y);
        
        return box;
    }
    
    private void addClassNameText(String className, Box box)
    {
        Text text = new Text(className);
        text.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        box.getClassVBox().getChildren().add(text);
    }
}
