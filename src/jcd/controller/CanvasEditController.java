/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

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
import jcd.gui.Workspace;

/**
 * This class handles all the activity (user interactions) with the
 * canvas itself. 
 * @author RaniSons
 */
public class CanvasEditController 
{
    /* This offset if for the extra space that we need to show for the
    scroll pane */
    private static final double DEFAULT_OFFSET = 20;
    DropShadow dropShadowEffect; // Effect for highlighting the shape
    
    // HERE'S THE FULL APP, WHICH GIVES US ACCESS TO OTHER STUFF
    JClassDesigner app;
    DataManager dataManager;
    
    /**
     * Constructor to set up the application.
     * @param initApp 
     * the application itself.
     */
    public CanvasEditController(JClassDesigner initApp) 
    {
	// KEEP IT FOR LATER
	app = initApp;
        dataManager = (DataManager)app.getDataComponent();
        
        // Now initialize the highlighting effect.
        dropShadowEffect = new DropShadow();
        dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.LIGHTGREEN);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(5);
    }
    
    // Page Edit Requests
    
    /**
     * Handles the selection request. Highlights the object that is selected.
     * @param layoutX
     * @param layoutY
     */
    public void handleSelectionRequest(double layoutX, double layoutY)
    {
        if (dataManager.isInState(JClassDesignerState.SELECTING_SHAPE))
        {
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
        
            // Unhighlight any previously selected objects
            if(workspace.getSelectedObject() != null)
            {
                unhighlight(workspace.getSelectedObject());
                workspace.setSelectedObject(null);
            }
            
            ClassObject obj = dataManager.fetchTopObject(layoutX, layoutY);
            
            if (obj == null)
                handleDeselectRequest();
            else
            {
                // And now select the object
                workspace.setSelectedObject(obj);
                highlight(obj);
            }
            
            //workspace.reloadWorkspace();
        }
    }
    
    public void handleSelectionRequest(ClassObject obj)
    {
        if (dataManager.isInState(JClassDesignerState.SELECTING_SHAPE))
        {
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
        
            // Unhighlight any previously selected objects
            if(workspace.getSelectedObject() != null)
            {
                unhighlight(workspace.getSelectedObject());
                workspace.setSelectedObject(null);
            }
            
            // And now select the object
            workspace.setSelectedObject(obj);
            highlight(obj);
            
            workspace.reloadWorkspace();
        }
    }
    
    /**
     * Method to handle the deselect request by unhighlighting the object. 
     */
    public void handleDeselectRequest()
    {
        if (dataManager.isInState(JClassDesignerState.SELECTING_SHAPE))
        {
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
        
            // Only if there is a selected object.
            if(workspace.getSelectedObject() != null)
            {
                unhighlight(workspace.getSelectedObject());
                workspace.setSelectedObject(null);
            }
            workspace.reloadWorkspace();
        }
    }
    
    /**
     * Helper method to unhighlight the object by setting the effect to null.
     * @param obj 
     * the object whose effect is to be set to null
     */
    private void unhighlight(ClassObject obj)
    {
        obj.getBox().getMainVBox().setEffect(null);
    }
    
    /**
     * Helper method to highlight the object by setting the effect property.
     * @param obj 
     * the object whose effect is to be set
     */
    private void highlight(ClassObject obj)
    {
            obj.getBox().getMainVBox().setEffect(dropShadowEffect);
    }
    
    /**
     * This method handles the request where the user tries to relocate or
     * reposition the selected object.
     * @param newTranslateX
     * @param newTranslateY
     */
    public void handlePositionChangeRequest(double newTranslateX, double newTranslateY)
    {
        if (dataManager.isInState(JClassDesignerState.DRAGGING_SHAPE))
        {
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
            ClassObject selectedObject = workspace.getSelectedObject();
            VBox box = selectedObject.getBox().getMainVBox();
            
            box.setTranslateX(newTranslateX);
            box.setTranslateY(newTranslateY);    
        
            resizeCanvasIfNeeded();
        
            app.getGUI().updateToolbarControls(false);
            workspace.reloadWorkspace();
        }
    }
    
    /**
     * Helper method to resize the canvas so that the user can scroll to see
     * where the objects are placed if required.
     */
    private void resizeCanvasIfNeeded()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        double maxX = 0;
        double maxY = 0;
        
        for (ClassObject obj: dataManager.getClassesList())
        {
            VBox stackPanesVBox = obj.getBox().getMainVBox();
            maxX = Math.max(maxX, stackPanesVBox.getTranslateX() + stackPanesVBox.getWidth() + DEFAULT_OFFSET);
            maxY = Math.max(maxY, stackPanesVBox.getTranslateY() + stackPanesVBox.getHeight() + DEFAULT_OFFSET);
        }
        
        canvas.setMinWidth(maxX);
        canvas.setMinHeight(maxY);
    }
}