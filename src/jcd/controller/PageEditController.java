/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import jcd.JClassDesigner;
import jcd.data.Box;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.JClassDesignerState;
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
    private static final double DEFAULT_HEIGHT = 150.0;
    
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
	//Scene scene = app.getGUI().getPrimaryScene();
	//scene.setCursor(Cursor.SE_RESIZE);
	
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
         
        int randomInt = (int) (Math.random() * 100);
        String randomClassNameString = "DummyClass" + randomInt;
        
        // Initialize the box
        // x and y values where the box will be origined
        int x = (int)(Math.random() * (canvas.getWidth() - (int)(DEFAULT_WIDTH)));
        int y = (int) ((canvas.getHeight()/2) - (DEFAULT_HEIGHT/2));
        
        Box box = new Box();
        box.getMainVBox().setTranslateX(x);
        box.getMainVBox().setTranslateY(y);
        
        // Now initialize the class object
        ClassObject obj = new ClassObject(randomClassNameString, "", box);
        obj.setInterfaceNames(new ArrayList<>());
        obj.setVariables(new ArrayList<>());
        obj.setMethods(new ArrayList<>());
        
        if (dataManager.checkIfUnique(obj))
        {
            canvas.getChildren().add(obj.getBox().getMainVBox());
            dataManager.addClassObject(obj);
            // AND SELECT IT
            dataManager.setState(JClassDesignerState.SELECTING_SHAPE);
            workspace.getCanvasEditController().handleSelectionRequest(obj);
        }
         
        app.getGUI().updateToolbarControls(false);
        workspace.reloadWorkspace();
    }
    
    /**
     * Handles the request where the user changes the name of a class.
     * It makes sure that the name is not changed such that the classes
     * always stay unique.
     * @param className 
     * the name to change it to
     */
    public void handleClassNameChangeRequest(String className)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Object selectedObject = workspace.getSelectedObject();
        
        // Case where the object is a class object
        if (selectedObject != null && selectedObject instanceof ClassObject)
        {
            ClassObject object = ((ClassObject)selectedObject);
            boolean unique = dataManager.checkIfUnique(className, object.getPackageName());
            
            // Only change the name of the class to such if name is unique
            if (unique)
            {
                object.setClassName(className);
                ((Text)object.getBox().getClassVBox().getChildren().get(0)).setText(className);
            }
        }
        
        workspace.reloadWorkspace();
    }
    
    /**
     * Handles the case where the user tries to change the name of the package.
     * @param packageName 
     * the name to be changed to
     */
    public void handlePackageNameChangeRequest(String packageName)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Object selectedObject = workspace.getSelectedObject();
        
        // Case where the object is a class object.
        if (selectedObject instanceof ClassObject)
        {
            ClassObject object = ((ClassObject)selectedObject);
            boolean unique = dataManager.checkIfUnique(object.getClassName(), packageName);
            
            // Only change if it is unique.
            if (unique)
                object.setPackageName(packageName);
        }
        
        workspace.reloadWorkspace();
    }
}
