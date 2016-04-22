/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.util.ArrayList;
import javafx.scene.text.Text;
import jcd.data.ArgumentObject;
import jcd.data.Box;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import static jcd.gui.Workspace.PUBLIC;
import static jcd.gui.Workspace.STRING;
import static jcd.test_bed.ThreadExampleClassBuilder.TEST_PACKAGE;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;

/**
 *
 * @author RaniSons
 */
public class InterfaceDesignBuilder 
{
    public static final String APP_STYLE_ARBITER = "AppStyleArbiter";
    
    public InterfaceDesignBuilder() {}
    
    public void hardCodeInterfaceDesign(DataManager dataManager)
    {
        dataManager.addClassObject(createInterfaceDesign());
    }
    
    private ClassObject createInterfaceDesign()
    {
        Box box = initializeNewBox();
        addClassNameText(APP_STYLE_ARBITER, box);
        
        ClassObject appStyleArbiterInterface = new ClassObject(APP_STYLE_ARBITER, box);
        
        appStyleArbiterInterface.setParentName(null);
        appStyleArbiterInterface.setPackageName(TEST_PACKAGE);
        appStyleArbiterInterface.setInterfaceType(true);
        
        ArrayList<String> interfaceNames = new ArrayList<>();
        appStyleArbiterInterface.setInterfaceNames(interfaceNames);
        ArrayList<VariableObject> variables = addAppStyleArbiterInterfaceVariables();
        appStyleArbiterInterface.setVariables(variables);
        ArrayList<MethodObject> methods = addAppStyleArbiterInterfaceMethods();
        appStyleArbiterInterface.setMethods(methods);
        ArrayList<String> javaApiPackages = new ArrayList<>();
        appStyleArbiterInterface.setJavaApiPackages(javaApiPackages);
        
        return appStyleArbiterInterface;
    }
    
    private ArrayList<VariableObject> addAppStyleArbiterInterfaceVariables()
    {
        VariableObject CLASS_BORDERED_PANE = new VariableObject();
        CLASS_BORDERED_PANE.setName("CLASS_BORDERED_PANE");
        CLASS_BORDERED_PANE.setScope(PUBLIC);
        CLASS_BORDERED_PANE.setType(STRING);
        CLASS_BORDERED_PANE.setStaticType(true);
        CLASS_BORDERED_PANE.setFinalType(true);
        
        ArrayList<VariableObject> variables = new ArrayList<>();
        variables.add(CLASS_BORDERED_PANE);
        
        return variables;
    }
    
    private ArrayList<MethodObject> addAppStyleArbiterInterfaceMethods()
    {
        MethodObject initStyle = new MethodObject();
        initStyle.setName("initStyle");
        initStyle.setType("void");
        initStyle.setScope(PUBLIC);
        initStyle.setStaticType(false);
        initStyle.setFinalType(false);
        initStyle.setAbstractType(false);
        
        ArrayList<ArgumentObject> addVariablesArguments = new ArrayList<>();
        
        ArgumentObject object = new ArgumentObject();
        object.setName("object");
        object.setType(STRING);
        
        ArgumentObject style = new ArgumentObject();
        style.setName("style");
        style.setType(STRING);
        
        addVariablesArguments.add(object);
        addVariablesArguments.add(style);
        
        initStyle.setArguments(addVariablesArguments);
        
        // FINISHED HARD CODING THE METHODS
        ArrayList<MethodObject> methods = new ArrayList<>();
        methods.add(initStyle);
        
        return methods;
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
