/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import jcd.JClassDesigner;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.JClassDesignerState;
import jcd.data.RectanglesBox;
import jcd.gui.Workspace;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;

/**
 * This class serves as a controller to handle all the user actions except the ones
 * related directly to the canvas. 
 * @author RaniSons
 */
public class PageEditController 
{
    // These define the default height and width values for the rectangles inside vbox
    private static final double DEFAULT_WIDTH = 300.0;
    private static final double DEFAULT_HEIGHT = 100.0;
    
    // HERE'S THE FULL APP, WHICH GIVES US ACCESS TO OTHER STUFF
    JClassDesigner app;
    DataManager dataManager;
    
    /**
     * Constructor to set up the application.
     * @param initApp 
     * the application itself.
     */
    public PageEditController(JClassDesigner initApp) 
    {
	// KEEP IT FOR LATER
	app = initApp;
        dataManager = (DataManager)app.getDataComponent();
    }
    
    // Button Requests
    
    /**
     * Handles the case when the user clicks on the selection tool button
     * that is present in the edit toolbar.
     */
    public void handleSelectionToolButtonRequest() 
    {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.DEFAULT);
	
	// CHANGE THE STATE
	dataManager.setState(JClassDesignerState.SELECTING_SHAPE);
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    /**
     * Handles the case when the user clicks on the selection tool button
     * that is present in the edit toolbar.
     */
    public void handleResizeButtonRequest()
    {
        // CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.SE_RESIZE);
	
	// CHANGE THE STATE
	dataManager.setState(JClassDesignerState.RESIZING_SHAPE);	
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    /**
     * Handles the request for adding the class object. It first initializes a class
     * object and then puts it on the canvas.
     */
    public void handleAddClassRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
         
        // x and y values where the box will be origined
        int x = (int)(Math.random() * (canvas.getWidth() - (int)(DEFAULT_WIDTH)));
        int y = (int) ((canvas.getLayoutBounds().getMinY() + canvas.getLayoutBounds().getMaxY())/2) - (int)((DEFAULT_HEIGHT)/2);
         
        // Initialize the box
        RectanglesBox box = new RectanglesBox();
        box.getStackPanesVBox().setTranslateX(x);
        box.getStackPanesVBox().setTranslateY(y);
        int randomInt = (int) (Math.random() * 100);
        String randomClassNameString = "Dummy" + randomInt;
        box.getClassesTextList().add(new Text(randomClassNameString));
        box.getClassesTextList().get(0).getStyleClass().add(CLASS_SUBHEADING_LABEL);
        box.getClassNameTextVBox().getChildren().addAll(box.getClassesTextList());
        box.getClassNameTextVBox().setAlignment(Pos.CENTER);
        box.getClassRectangleStackPane().getChildren().addAll(box.getClassRectangle(), box.getClassNameTextVBox());
        box.getStackPanesVBox().getChildren().addAll(box.getClassRectangleStackPane(),
                box.getVariablesRectangleStackPane(), box.getMethodsRectangleStackPane());

        // Now initialize the class object
        ClassObject obj = new ClassObject(randomClassNameString, "", box);
        if (dataManager.checkIfUnique(obj))
        {
            canvas.getChildren().add(obj.getRectanglesBox().getStackPanesVBox());
            dataManager.addClassObject(obj);
            // AND SELECT IT
            dataManager.setState(JClassDesignerState.SELECTING_SHAPE);
            workspace.getCanvasEditController().handleSelectionRequest(obj);
        }
         
        // Maybe show a message here that adding wasn't successful?
         
        app.getGUI().updateToolbarControls(false);
        workspace.reloadWorkspace();
    }
    
    /**
     * Handles the request where the user changes the name of a class.
     * It makes sure that the name is not changed such that the classes
     * always stay unique.
     * @param obj
     * the object for which the name is to be changed
     * @param className 
     * the name to change it to
     */
    public void handleClassNameChangeRequest(Object obj, String className)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        
        // Case where the object is a class object
        if (obj instanceof ClassObject)
        {
            ClassObject object = ((ClassObject)obj);
            boolean unique = true;
            
            for (ClassObject classObj: dataManager.getClassesList())
            {
                if (classObj.getClassName().equalsIgnoreCase(className) &&
                        classObj.getPackageName().equalsIgnoreCase(object.getPackageName()))
                    unique = false;
            }
            
            // Only change the name of the class to such if name is unique
            if (unique)
            {
                object.setClassName(className);
                object.getRectanglesBox().getClassesTextList().get(0).setText(className);
            }
        }
        
        workspace.reloadWorkspace();
    }
    
    /**
     * Handles the case where the user tries to change the name of the package.
     * @param obj
     * the object whose package name is to be changed
     * @param packageName 
     * the name to be changed to
     */
    public void handlePackageNameChangeRequest(Object obj, String packageName)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        
        // Case where the object is a class object.
        if (obj instanceof ClassObject)
        {
            ClassObject object = ((ClassObject)obj);
            boolean unique = true;
            
            for (ClassObject classObj: dataManager.getClassesList())
            {
                if (classObj.getClassName().equalsIgnoreCase(object.getClassName()) &&
                        classObj.getPackageName().equalsIgnoreCase(packageName))
                    unique = false;
            }
            
            // Only change if it is unique.
            if (unique)
                object.setPackageName(packageName);           
        }
        
        workspace.reloadWorkspace();
    }
}
