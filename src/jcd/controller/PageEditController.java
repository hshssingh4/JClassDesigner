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
    double originalSceneX, originalSceneY;
    double originalTranslateX, originalTranslateY;
    CanvasEditController canavasEditController;
    
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
        
        // Now initialize the class object
        ClassObject obj = new ClassObject();
        obj.setClassName(randomClassNameString);
        obj.setBox(new Box(x, y));
        obj.setPackageName(null);
        obj.setParentName(null);
        obj.setInterfaceType(false);
        obj.setInterfaceNames(new ArrayList<>());
        obj.setVariables(new ArrayList<>());
        obj.setMethods(new ArrayList<>());
        obj.setJavaApiPackages(new ArrayList<>());
        obj.setLineConnectors(new ArrayList<>());
        
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
     * Handles the request for adding the interface object. It first initializes an interface
     * object and then puts it on the canvas.
     */
    public void handleAddInterfaceRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
         
        int randomInt = (int) (Math.random() * 100);
        String randomClassNameString = "DummyInterface" + randomInt;
        
        // Initialize the box
        // x and y values where the box will be origined
        int x = (int)(Math.random() * (canvas.getWidth() - (int)(DEFAULT_WIDTH)));
        int y = (int) ((canvas.getHeight()/2) - (DEFAULT_HEIGHT/2));
        
        // Now initialize the class object
        ClassObject obj = new ClassObject();
        obj.setClassName(randomClassNameString);
        obj.setBox(new Box(x, y));
        obj.setPackageName(null);
        obj.setParentName(null);
        obj.setInterfaceType(true);
        obj.setInterfaceNames(new ArrayList<>());
        obj.setVariables(new ArrayList<>());
        obj.setMethods(new ArrayList<>());
        obj.setJavaApiPackages(new ArrayList<>());
        obj.setLineConnectors(new ArrayList<>());
        
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
        ClassObject selectedObject = workspace.getSelectedObject();
        
        // Case where the object is a class object
        if (selectedObject != null)
        {
            boolean unique = dataManager.checkIfUnique(className, selectedObject.getPackageName());

            // Only change the name of the class to such if name is unique
            if (unique)
            {
                selectedObject.setClassName(className);
                ((Text)selectedObject.getBox().getClassVBox().getChildren().get(0)).setText(className);
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
        ClassObject selectedObject = workspace.getSelectedObject();
        
        boolean unique = dataManager.checkIfUnique(selectedObject.getClassName(), packageName);
            
        // Only change if it is unique.
        if (unique)
        {
            if (packageName.isEmpty())
                selectedObject.setPackageName(null);
            else
                selectedObject.setPackageName(packageName);
        }
        
        workspace.reloadWorkspace();
    }
    
    /**
     * This method handles the remove request for the selected object in the
     * workspace.
     */
    public void handleRemoveRequest()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        
        dataManager.removeClassObject(workspace.getSelectedObject());
        workspace.setSelectedObject(null);
        
        workspace.reloadWorkspace();
    }
    
    /**
     * This method helps in zooming in the workspace currently being worked on.
     */
    public void handleZoomInRequest()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();

        canvas.setScaleX(canvas.getScaleX() * 1.25);
        canvas.setScaleY(canvas.getScaleY() * 1.25);
        
        workspace.reloadWorkspace();
    }
    
    /**
     * This method helps in zooming out the workspace currently being worked on.
     */
    public void handleZoomOutRequest()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        canvas.setScaleX(canvas.getScaleX() * 0.8);
        canvas.setScaleY(canvas.getScaleY() * 0.8);

        workspace.reloadWorkspace();
    }
}
