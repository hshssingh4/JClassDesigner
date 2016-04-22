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
import static jcd.gui.Workspace.PROTECTED;
import static jcd.test_bed.ClassBuilder.INT;
import static jcd.test_bed.ThreadExampleClassBuilder.TEST_PACKAGE;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;

/**
 *
 * @author RaniSons
 */
public class AbstractDesignBuilder 
{
    public static final String GRAPHIC_OBJECT = "GraphicObject";
    
    public AbstractDesignBuilder() {}
    
    public void hardCodeAbstractDesign(DataManager dataManager)
    {
        dataManager.addClassObject(createAbstractDesign());
    }
    
    private ClassObject createAbstractDesign()
    {
        Box box = initializeNewBox();
        addClassNameText(GRAPHIC_OBJECT, box);
        
        ClassObject graphicObjectClass = new ClassObject(GRAPHIC_OBJECT, box);
        
        graphicObjectClass.setParentName(null);
        graphicObjectClass.setPackageName(TEST_PACKAGE);
        graphicObjectClass.setInterfaceType(false);
        
        ArrayList<String> interfaceNames = new ArrayList<>();
        graphicObjectClass.setInterfaceNames(interfaceNames);
        ArrayList<VariableObject> variables = addGraphicObjectClassVariables();
        graphicObjectClass.setVariables(variables);
        ArrayList<MethodObject> methods = addGraphicObjectClassMethods();
        graphicObjectClass.setMethods(methods);
        ArrayList<String> javaApiPackages = new ArrayList<>();
        graphicObjectClass.setJavaApiPackages(javaApiPackages);
        
        return graphicObjectClass;
    }
    
    private ArrayList<VariableObject> addGraphicObjectClassVariables()
    {
        VariableObject x = new VariableObject();
        x.setName("x");
        x.setScope(PROTECTED);
        x.setType(INT);
        x.setStaticType(false);
        x.setFinalType(false);
        
        VariableObject y = new VariableObject();
        y.setName("y");
        y.setScope(PROTECTED);
        y.setType(INT);
        y.setStaticType(false);
        y.setFinalType(false);
        
        ArrayList<VariableObject> variables = new ArrayList<>();
        variables.add(x);
        variables.add(y);
        
        return variables;
    }
    
    private ArrayList<MethodObject> addGraphicObjectClassMethods()
    {
        MethodObject moveTo = new MethodObject();
        moveTo.setName("moveTo");
        moveTo.setType("void");
        moveTo.setScope(PROTECTED);
        moveTo.setStaticType(false);
        moveTo.setFinalType(false);
        moveTo.setAbstractType(false);
        
        ArrayList<ArgumentObject> addVariablesArguments = new ArrayList<>();
        
        ArgumentObject newX = new ArgumentObject();
        newX.setName("newX");
        newX.setType(INT);
        
        ArgumentObject newY = new ArgumentObject();
        newY.setName("newY");
        newY.setType(INT);
        
        addVariablesArguments.add(newX);
        addVariablesArguments.add(newY);
        
        moveTo.setArguments(addVariablesArguments);

        
        // SECOND METHOD
        MethodObject draw = new MethodObject();
        draw.setName("draw");
        draw.setType("void");
        draw.setScope(PROTECTED);
        draw.setStaticType(false);
        draw.setFinalType(false);
        draw.setAbstractType(true);
        draw.setArguments(new ArrayList<>());
        
        // FINISHED HARD CODING THE METHODS
        ArrayList<MethodObject> methods = new ArrayList<>();
        methods.add(moveTo);
        methods.add(draw);
        
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
