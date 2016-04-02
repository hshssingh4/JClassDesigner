/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import jcd.JClassDesigner;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.JClassDesignerState;
import jcd.data.RectanglesBox;
import jcd.gui.Workspace;

/**
 *
 * @author RaniSons
 */
public class PageEditController 
{
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
    
    public void handleAddClassRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
         
        int x = (int)(Math.random() * (canvas.getWidth() - (int)(DEFAULT_WIDTH)));
        int y = (int) ((canvas.getLayoutBounds().getMinY() + canvas.getLayoutBounds().getMaxY())/2) - (int)((DEFAULT_HEIGHT)/2);
         
        RectanglesBox box = new RectanglesBox();
        box.getStackPanesVBox().setTranslateX(x);
        box.getStackPanesVBox().setTranslateY(y);
        int randomInt = (int) (Math.random() * 100);
        String randomClassNameString = "Dummy" + randomInt;
         
        ClassObject obj = new ClassObject(randomClassNameString, "", box);
         
        if (dataManager.checkIfUnique(obj))
        {
            canvas.getChildren().add(obj.getRectanglesBox().getStackPanesVBox());
            dataManager.addClassObject(obj);
            // AND SELECT IT
            workspace.getCanvasEditController().handleSelectionRequest(obj);
        }
         
        // Maybe show a message here that adding wasn't successful?
         
        app.getGUI().updateToolbarControls(false);
        workspace.reloadWorkspace();
    }
    
    public void handleClassNameChangeRequest(Object obj, String className)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        if (obj instanceof ClassObject)
        {
            ClassObject object = ((ClassObject)obj);
            object.setClassName(className);
            workspace.getClassNameTextField().setText(((ClassObject) obj).getClassName());
        }
        
        workspace.reloadWorkspace();
    }
    
    public void handlePackageNameChangeRequest(Object obj, String packageName)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        if (obj instanceof ClassObject)
        {
            ClassObject object = ((ClassObject)obj);
            object.setPackageName(packageName);
            workspace.getPackageTextField().setText(((ClassObject) obj).getPackageName());
        }
        
        workspace.reloadWorkspace();
    }
}
