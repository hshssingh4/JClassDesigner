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
import jcd.gui.Workspace;

/**
 *
 * @author RaniSons
 */
public class CanvasEditController 
{
    private static final double DEFAULT_OFFSET = 20;
    DropShadow dropShadowEffect = new DropShadow();
    
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
        dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(1.0);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(5);
    }
    
    // Page Edit Requests
    
    public void handleSelectionRequest(Object obj)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        
        if(workspace.getSelectedObject() != null)
        {
            unhighlight(workspace.getSelectedObject());
            workspace.setSelectedObject(null);
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
            unhighlight(workspace.getSelectedObject());
            workspace.setSelectedObject(null);
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
        if (obj instanceof ClassObject)
            ((ClassObject)obj).getRectanglesBox().getStackPanesVBox().setEffect(dropShadowEffect);
    }
    
    public void handlePositionChangeRequest(Object obj, MouseEvent e1, MouseEvent e,
            double initialX, double initialY)
    {
        double xDiff;
        double yDiff;
        
        if (obj instanceof ClassObject)
        {
            VBox box = ((ClassObject)obj).getRectanglesBox().getStackPanesVBox();
            xDiff = e1.getX() - e.getX();
            yDiff = e1.getY() - e.getY();
            box.setTranslateX(initialX + xDiff);
            box.setTranslateY(initialY + yDiff);
        }
        
        resizeCanvasIfNeeded();
        
        app.getGUI().updateToolbarControls(false);
    }
    
    private void resizeCanvasIfNeeded()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        double maxX = 0;
        double maxY = 0;
        
        for (ClassObject obj: dataManager.getClassesList())
        {
            VBox stackPanesVBox = obj.getRectanglesBox().getStackPanesVBox();
            maxX = Math.max(maxX, stackPanesVBox.getTranslateX() + stackPanesVBox.getWidth() + DEFAULT_OFFSET);
            maxY = Math.max(maxY, stackPanesVBox.getTranslateY() + stackPanesVBox.getHeight() + DEFAULT_OFFSET);
        }
        
        canvas.setMinWidth(maxX);
        canvas.setMinHeight(maxY);
    }
}