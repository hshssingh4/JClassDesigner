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
    double originalPressLocation, originalWidth, originalMainVBoxHeight,
            originalClassVBoxHeight, originalVariablesVBoxHeight, originalMethodsVBoxHeight;
    
    // HERE ARE THE CONSTANTS FOR DRAGGING
    double originalX, originalY;
    double originalTranslateX, originalTranslateY;
  
    
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
     * the x location for the click
     * @param layoutY
     * the y location for the click
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
                // NOW INITIALIZE THE CONSTANTS FOR RESIZING
                ClassObject selectedObject = workspace.getSelectedObject();
                originalX = layoutX;
                originalY = layoutY;
                originalTranslateX = selectedObject.getBox().getMainVBox().getTranslateX();
                originalTranslateY = selectedObject.getBox().getMainVBox().getTranslateY();
            }
            
            workspace.reloadWorkspace();
            
            // Work has been edited!
            app.getGUI().updateToolbarControls(false);
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
            
            // Work has been edited!
            app.getGUI().updateToolbarControls(false);
        }
    }
    
    /**
     * Method to handle the deselect request by un highlighting the object. 
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
            
            // Work has been edited!
            app.getGUI().updateToolbarControls(false);
        }
    }
    
    /**
     * Helper method to un highlight the object by setting the effect to null.
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
     * @param newX
     * the new x location for this object
     * @param newY
     * the new y location for this object
     */
    public void handlePositionChangeRequest(double newX, double newY)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        VBox box = selectedObject.getBox().getMainVBox();

        double offsetX = newX - originalX;
        double offsetY = newY - originalY;
        double newTranslateX = originalTranslateX + offsetX;
        double newTranslateY = originalTranslateY + offsetY;
        
        // ONLY CHANGE IF BOTH ARE GREAETER THAN 5
        if (newTranslateX > DEFAULT_OFFSET)
            box.setTranslateX(newTranslateX);
        if (newTranslateY > DEFAULT_OFFSET)
            box.setTranslateY(newTranslateY);
          
        resizeCanvasIfNeeded();
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Helper method to resize the canvas so that the user can scroll to see
     * where the objects are placed if required.
     */
    public void resizeCanvasIfNeeded()
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
        
        dataManager.setCanvasZoomScaleX(canvas.getScaleX());
        dataManager.setCanvasZoomScaleY(canvas.getScaleY());
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
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
        
        dataManager.setCanvasZoomScaleX(canvas.getScaleX());
        dataManager.setCanvasZoomScaleY(canvas.getScaleY());

        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method helps in applying default zoom to the workspace currently being worked on.
     */
    public void handleDefaultZoomRequest()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        canvas.setScaleX(1);
        canvas.setScaleY(1);
        
        dataManager.setCanvasZoomScaleX(canvas.getScaleX());
        dataManager.setCanvasZoomScaleY(canvas.getScaleY());

        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method is called when the user selects/deselects the grid render check box.
     */
    public void handleRenderLinesRequest()
    {
        renderLines();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Helper method to render the lines on the canvas.
     */
    private void renderLines()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        int numHorizontalLines = (int) canvas.getHeight();
        for (int i = 0; i < numHorizontalLines; i = i + 15)
        {
            Line horizontalLine = new Line();
            horizontalLine.setStartX(0);
            horizontalLine.setStartY(i);
            horizontalLine.setEndX(canvas.getWidth());
            horizontalLine.setEndY(i);
            canvas.getChildren().addAll(horizontalLine);
        }
        int numVerticalLines = (int) canvas.getWidth();
        for (int i = 0; i < numVerticalLines; i = i + 15)
        {
            Line verticalLine = new Line();
            verticalLine.setStartX(i);
            verticalLine.setStartY(0);
            verticalLine.setEndX(i);
            verticalLine.setEndY(canvas.getHeight());
            canvas.getChildren().add(verticalLine);
       }
    }
    
    /**
     * This method handles the snapping of the object to the left/top corner of the 
     * grid lines on the canvas.
     */
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
    
    /**
     * This is a helper method for snapping because it returns the closest vertical
     * lines to any object.
     * @param translateX
     * the x location to which the line is closest
     * @return 
     * the line that is closest to the translateX
     */
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
    
    /**
     * Another helper method for snapping and this returns the closest horizontal
     * line to the class object.
     * @param translateY
     * the y location to which the line is closest
     * @return 
     * the closest line object
     */
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

    /**
     * This method is continuously called when data manager is in resizing mode
     * as it looks for the mouse location on top of selected object.
     * @param mouseXLocation
     * the current x location of the mouse
     * @param mouseYLocation 
     * the current y location of the mouse
     */
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
    
    /**
     * Helper method to change cursor for Horizontal resizing.
     */
    private void changeCursorToHorizontalResize() 
    {
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.E_RESIZE);
    }
    
    /**
     * Helper method to change cursor for Vertical resizing.
     */
    private void changeCursorToVerticalResize() 
    {
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.S_RESIZE);
    }

    /**
     * Helper method to change cursor for Normal.
     */
    private void changeCursorToNormal() 
    {
        Scene scene = app.getGUI().getPrimaryScene();
        scene.setCursor(Cursor.DEFAULT);
    }
    
    /**
     * This method is called to initialize values when a press is detected for
     * resizing the selected object.
     * @param originalPressLocation
     * current press location
     */
    public void handleResizePressDetected(double originalPressLocation)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        VBox mainVBox = selectedObject.getBox().getMainVBox();
        VBox classVBox = selectedObject.getBox().getClassVBox();
        VBox variablesVBox = selectedObject.getBox().getVariablesVBox();
        VBox methodsVBox = selectedObject.getBox().getMethodsVBox();
        this.originalPressLocation = originalPressLocation;
        this.originalWidth = mainVBox.getWidth();
        this.originalMainVBoxHeight = mainVBox.getHeight();
        this.originalClassVBoxHeight = classVBox.getHeight();
        this.originalVariablesVBoxHeight = variablesVBox.getHeight();
        this.originalMethodsVBoxHeight = methodsVBox.getHeight();
    }
    
    /**
     * This method handles the horizontal resizing as it changes
     * the width of the selected object accordingly.
     * @param newX
     * the new x location of the mouse after press
     */
    public void handleHorizontalResizeRequest(double newX)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        VBox mainVBox = selectedObject.getBox().getMainVBox();
        double offsetWidth = newX - originalPressLocation;
        double newWidth = originalWidth + offsetWidth;
        mainVBox.setMinWidth(newWidth);
        
        workspace.reloadWorkspace();
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method handles the vertical resizing as it changes
     * the height of the selected object accordingly.
     * @param newY
     * the new y location of the mouse after press
     */
    public void handleVerticalResizeRequest(double newY)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        Box box = selectedObject.getBox();
        double offsetHeight = newY - originalPressLocation;
        double newMainVBoxHeight = originalMainVBoxHeight + offsetHeight;
        double newClassVBoxHeight = originalClassVBoxHeight + (offsetHeight / 3);
        double newVariablesVBoxHeight = originalVariablesVBoxHeight + (offsetHeight / 3);
        double newMethodsVBoxHeight = originalMethodsVBoxHeight + (offsetHeight / 3);
        
        box.getMainVBox().setMinHeight(newMainVBoxHeight);
        box.getClassVBox().setMinHeight(newClassVBoxHeight);
        box.getVariablesVBox().setMinHeight(newVariablesVBoxHeight);
        box.getMethodsVBox().setMinHeight(newMethodsVBoxHeight);
        
        workspace.reloadWorkspace();
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
}