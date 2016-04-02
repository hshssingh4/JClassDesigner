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
import javafx.scene.layout.Pane;
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
    
    // Page Edit Requests
    
    public void handleSelectionRequest(Object obj)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        
        if(workspace.getSelectedObject() != null)
        {
            workspace.setSelectedObject(null);
            unhighlight(workspace.getSelectedObject());
        }

        workspace.setSelectedObject(obj);
        highlight(obj);
        
        workspace.reloadWorkspace();
    }
    
    public void handleUnselectRequest()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        
        if(workspace.getSelectedObject() != null)
        {
            workspace.setSelectedObject(null);
            unhighlight(workspace.getSelectedObject());
        }
        workspace.reloadWorkspace();
    }
    
    private void unhighlight(Object obj)
    {
        if (obj instanceof ClassObject)
            ((ClassObject)obj).getRectanglesBox().getStackPanesVBox().setEffect(null);
    }
    
    private void highlight(Object obj)
    {
        // THIS IS FOR THE SELECTED SHAPE
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(5);
        
        if (obj instanceof ClassObject)
            ((ClassObject)obj).getRectanglesBox().getStackPanesVBox().setEffect(dropShadowEffect);
    }
    
    public void handleAddClassRequest() 
    {
         Workspace workspace = (Workspace) app.getWorkspaceComponent();
         Pane canvas = workspace.getCanvas();
         
         RectanglesBox box = new RectanglesBox();
         int randomInt = (int) (Math.random() * 100);
         String randomClassNameString = "Dummy" + randomInt;
         
         ClassObject obj = new ClassObject(randomClassNameString, "", box);
         
         // CHANGE THE STATE AND SELECT THE SHAPE
	 dataManager.setState(JClassDesignerState.SELECTING_SHAPE);
         handleSelectionRequest(obj);
         
         canvas.getChildren().add(obj.getRectanglesBox().getStackPanesVBox());
         dataManager.addClassObject(obj);
         
         app.getGUI().updateToolbarControls(false);
         workspace.reloadWorkspace();
    }
}
