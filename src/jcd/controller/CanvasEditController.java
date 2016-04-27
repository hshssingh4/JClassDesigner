/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.util.ArrayList;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import jcd.JClassDesigner;
import jcd.data.Box;
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
    
    // HERE ARE THE CONSTANTS FOR RESIZING
    double originalPressLocation, originalWidth, originalHeight;
  
    
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
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        VBox box = selectedObject.getBox().getMainVBox();
            
        box.setTranslateX(newTranslateX);
        box.setTranslateY(newTranslateY);    
        
        resizeCanvasIfNeeded();
        
        app.getGUI().updateToolbarControls(false);
        workspace.reloadWorkspace();
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
    
    public void handleSnapRequest()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        // First get the lines array list
        ArrayList<Line> lines = new ArrayList<>();
        for (Object obj: canvas.getChildren())
        {
            if (obj instanceof Line)
            {
                lines.add((Line) obj);
            }
            else
                break;
        }
        
        // Now we have the lines array
        for (ClassObject classObject: dataManager.getClassesList())
        {
            VBox mainVBox = classObject.getBox().getMainVBox();
            Line verticalLine = fetchClosestVerticalLine((int) mainVBox.getTranslateX());
            Line horizontalLine = fetchClosestHorizontalLine((int) mainVBox.getTranslateY());
            if (verticalLine != null && horizontalLine != null)
            {
                mainVBox.setTranslateX(verticalLine.getStartX());
                mainVBox.setTranslateY(horizontalLine.getStartY());
            }
        }
    }
    
    private Line fetchClosestVerticalLine(int translateX)
    {
        Line closestLine = null;
        
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        // First get the lines array list
        ArrayList<Line> lines = new ArrayList<>();
        for (Object obj: canvas.getChildren())
        {
            if (obj instanceof Line)
                lines.add((Line) obj);
            else
                break;
        }
        
        for (Line line: lines)
            if (line.getStartX() <= translateX)
                closestLine = line;
        
        return closestLine;
    }
    
    private Line fetchClosestHorizontalLine(int translateY)
    {
        Line closestLine = null;
        
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        // First get the lines array list
        ArrayList<Line> lines = new ArrayList<>();
        for (Object obj: canvas.getChildren())
        {
            if (obj instanceof Line)
                lines.add((Line) obj);
            else
                break;
        }

        for (Line line: lines)
        {
            if (line.getStartY() <= translateY)
                closestLine = line;
            else
                break;
        }
        
        return closestLine;
    }

    public void handleCheckResizeRequest(int mouseXLocation, int mouseYLocation) 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();

        if (selectedObject != null) 
        {
            int RESIZE_OFFSET = 5;

            VBox mainVBox = selectedObject.getBox().getMainVBox();
            Bounds bounds = mainVBox.getBoundsInParent();
            
            // FIRST INITIALIZE THE HORIZONTAL VALUES
            int leftXRegion = (int) (bounds.getMaxX() - RESIZE_OFFSET);
            int rightXRegion = (int) (bounds.getMaxX() + RESIZE_OFFSET);
            int topY = (int) (bounds.getMinY() + RESIZE_OFFSET);
            int bottomY = (int) (bounds.getMaxY() - RESIZE_OFFSET);
            
            // THEN INITIALIZE THE VERTICAL VALUES
            int topYRegion = (int) (bounds.getMaxY() - RESIZE_OFFSET);
            int bottomYRegion = (int) (bounds.getMaxY() + RESIZE_OFFSET);
            int leftX = (int) (bounds.getMinX() + RESIZE_OFFSET);
            int rightX = (int) (bounds.getMaxX() - RESIZE_OFFSET);
            
            
            
            if (mouseXLocation > leftXRegion && mouseXLocation < rightXRegion
                    && mouseYLocation > topY && mouseYLocation < bottomY)
                changeCursorToHorizontalResize();
            else if (mouseYLocation > topYRegion && mouseYLocation < bottomYRegion
                    && mouseXLocation > leftX && mouseXLocation < rightX)
                changeCursorToVerticalResize();
            else 
                changeCursorToNormal();
        }
    }
    
    private void changeCursorToHorizontalResize() 
    {
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.E_RESIZE);
    }
    
    private void changeCursorToVerticalResize() 
    {
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.S_RESIZE);
    }

    private void changeCursorToNormal() 
    {
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);
    }
    
    public void handleResizePressDetected(double originalPressLocation)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        VBox mainVBox = selectedObject.getBox().getMainVBox();
        this.originalPressLocation = originalPressLocation;
        this.originalWidth = mainVBox.getWidth();
        this.originalHeight = mainVBox.getHeight();
    }
    
    public void handleHorizontalResizeRequest(double newX)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        VBox mainVBox = selectedObject.getBox().getMainVBox();
        double offsetWidth = newX - originalPressLocation;
        double newWidth = originalWidth + offsetWidth;
        mainVBox.setMinWidth(newWidth);
    }
    
    public void handleVerticalResizeRequest(double newY)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        Box box = selectedObject.getBox();
        double offsetHeight = newY - originalPressLocation;
        double newHeight = originalHeight + offsetHeight;
        
        box.getMainVBox().setMinHeight(newHeight);
        box.getClassVBox().setMinHeight(newHeight / 3);
        box.getVariablesVBox().setMinHeight(newHeight / 3);
        box.getMethodsVBox().setMinHeight(newHeight / 3);
    }
}