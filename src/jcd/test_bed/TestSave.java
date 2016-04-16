/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.text.Text;
import jcd.data.Box;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import saf.components.AppDataComponent;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;

/**
 *
 * @author RaniSons
 */
public class TestSave 
{
    ClassObject counterTaskClass;
    
    public static final String PRIVATE = "private";
    public static final String PUBLIC = "public";
    public static final String PROTECTED = "protected";
    
    public static final String INT = "int";
    public static final String DOUBLE = "double";
    public static final String BOOLEAN = "boolean";
    public static final String CHAR = "char";
    public static final String ARRAY_LIST = "ArrayList";    
    
    public static final String COUNTER_TASK = "CounterTask";
    public static final String DATE_TASK = "DateTask";
    public static final String PAUSE_HANDLER = "PauseHandler";
    public static final String START_HANDLER = "StartHandler";
    public static final String THREAD_EXAMPLE = "ThreadExample";
    
    public void saveTestData(AppDataComponent data, String filePath) throws IOException 
    {
        System.out.println("Save Test Data");
        hardCodeClasses(data);
        
        
    }
    
    public void hardCodeClasses(AppDataComponent data)
    {
        DataManager dataManager = (DataManager) data;
        
        createCounterTaskClass();
        
        dataManager.addClassObject(counterTaskClass);
    }
    
    private void createCounterTaskClass()
    {
        Box box = initializeNewBox();
        addClassNameText(COUNTER_TASK, box);
        
        counterTaskClass = new ClassObject(COUNTER_TASK, "", box);
        
        counterTaskClass.setParentName("Task<Void>");
        counterTaskClass.setInterfaceName(null);
        ArrayList<VariableObject> variables = addCounterTaskClassVariables();
        counterTaskClass.setVariables(variables);
        ArrayList<MethodObject> methods = addCounterTaskClassMethods();
        counterTaskClass.setMethods(methods);
        
        
    }
    
    private ArrayList<VariableObject> addCounterTaskClassVariables()
    {
        VariableObject app = new VariableObject();
        app.setName("app");
        app.setScope(PRIVATE);
        app.setType("ThreadExample");
        app.setStaticType(false);
        app.setFinalType(false);
        
        VariableObject counter = new VariableObject();
        counter.setName("counter");
        counter.setScope(PRIVATE);
        counter.setType(INT);
        counter.setStaticType(false);
        counter.setFinalType(false);
        
        ArrayList<VariableObject> variables = new ArrayList<VariableObject>();
        variables.add(app);
        variables.add(counter);
        
        return variables;
    }
    
    private ArrayList<MethodObject> addCounterTaskClassMethods()
    {
        MethodObject call = new MethodObject();
        call.setName("call");
        call.setScope(PROTECTED);
        call.setType("Void");
        call.setStaticType(false);
        call.setFinalType(false);
        call.setArguments(null);
        
        ArrayList<MethodObject> methods = new ArrayList<MethodObject>();
        methods.add(call);
        
        return methods;
    }
    
    /**
     * Helper method to initialize a new box.
     */
    private Box initializeNewBox()
    {
        // x and y values where the box will be origined
        int x = 300;
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
