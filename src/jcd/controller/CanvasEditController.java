/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import jcd.JClassDesigner;
import jcd.Shapes.Triangle;
import jcd.data.Box;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.JClassDesignerState;
import jcd.data.LineConnector;
import jcd.data.LineConnectorType;
import jcd.gui.Workspace;
import saf.ui.AppMessageDialogSingleton;

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
    private static final double SIDE = 10.0;
    private static final String MERGE_LINE_TITLE = "Merge Error";
    private static final String MERGE_LINE_ERROR = "Please make sure consecutive"
            + " lines are selected!";
    DropShadow dropShadowEffect; // Effect for highlighting the shape
    DropShadow lineShadowEffect; // Effect for highlighting the line
    
    // HERE'S THE FULL APP, WHICH GIVES US ACCESS TO OTHER STUFF
    JClassDesigner app;
    DataManager dataManager;
    
    // HERE ARE THE CONSTANTS FOR RESIZING
    double originalPressLocation, originalWidth, originalMainVBoxHeight,
            originalClassVBoxHeight, originalVariablesVBoxHeight, originalMethodsVBoxHeight;
    
    // HERE ARE THE CONSTANTS FOR DRAGGING
    double originalX, originalY;
    double originalTranslateX, originalTranslateY;
    
    double originalLineStartX, originalLineStartY, 
            originalLineEndX, originalLineEndY, 
            originalCenterX, originalCenterY;
    DoubleProperty originalStartXProperty, originalEndXProperty;
    DoubleProperty originalStartYProperty, originalEndYProperty;
    DoubleBinding originalSpecialStartXProperty, originalSpecialEndYProperty;
  
    ObjectBinding<List<Double>> binding;
    ObjectBinding<List<Double>> arrowBinding;
    
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
        
        // Now initialize the highlighting effect.
        lineShadowEffect = new DropShadow();
        lineShadowEffect.setOffsetX(0.0f);
	lineShadowEffect.setOffsetY(0.0f);
	lineShadowEffect.setSpread(1.0);
	lineShadowEffect.setColor(Color.LIGHTGRAY);
	lineShadowEffect.setBlurType(BlurType.GAUSSIAN);
	lineShadowEffect.setRadius(5);
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
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
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
    
    
    // BELOW WILL BE ALL THE METHODS FOR ADDING API BOXES AND LINE CONNECTORS
    
    /**
     * This method adds a single diamond line connector from one box
     * to another.
     * @param fromBox
     * add a line from this box
     * @param toBox
     * add a line to this box
     * @param type
     * Line Connector
     */
    public void handleAddLineConnector(Box fromBox, Box toBox, LineConnectorType type) 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        VBox fromMainVBox = fromBox.getMainVBox();
        VBox toMainVBox = toBox.getMainVBox();
        
        Line line1 = new Line();
        line1.setEndX(fromBox.getCenterX());
        line1.setEndY(toBox.getCenterY());
        line1.setStrokeWidth(3);
        
        Line line2 = new Line();
        line2.setStrokeWidth(3);
        
        line1.startXProperty().bind(fromMainVBox.translateXProperty().add(fromMainVBox.widthProperty().divide(2)));

        switch (type) 
        {
            case DIAMOND:
                line1.startYProperty().bind(fromMainVBox.translateYProperty().subtract(SIDE));
                line2.endXProperty().bind(toMainVBox.translateXProperty());
                break;
            case TRIANGLE:
                line1.startYProperty().bind(fromMainVBox.translateYProperty());
                line2.endXProperty().bind(toMainVBox.translateXProperty().subtract(SIDE));
                break;
            case ARROW:
                line1.startYProperty().bind(fromMainVBox.translateYProperty());
                line2.endXProperty().bind(toMainVBox.translateXProperty().subtract(SIDE));
                break;
            default:
                break;
        }
        line2.startXProperty().bind(line1.endXProperty());
        line2.startYProperty().bind(line1.endYProperty());
        line2.endYProperty().bind(toMainVBox.translateYProperty().add(toMainVBox.heightProperty().divide(2)));
        
        
        ArrayList<Line> lines = new ArrayList<>();
        lines.add(line1);
        lines.add(line2);
        
        LineConnector lineConnector = new LineConnector();
        lineConnector.setLines(lines);
        
        switch (type) 
        {
            case DIAMOND:
                lineConnector.setShape(addRect(line1));
                lineConnector.setType(LineConnectorType.DIAMOND);
                break;
            case TRIANGLE:
                lineConnector.setShape(addTriangle(line2));
                lineConnector.setType(LineConnectorType.TRIANGLE);
                break;
            case ARROW:
                lineConnector.setShape(addArrowTriangle(line2));
                lineConnector.setType(LineConnectorType.ARROW);
                break;
            default:
                break;
        }
        lineConnector.setFromBox(fromBox);
        lineConnector.setToBox(toBox);
        
        // ONLY ADD IF IT IS UNIQUE
        if (!fromBox.containsLineConnector(lineConnector))
        {
            canvas.getChildren().addAll(lineConnector.getLines());
            canvas.getChildren().add(lineConnector.getShape());
            fromBox.getLineConnectors().add(lineConnector);
        }
    }
    
    /**
     * Helper method to add rect to a line connector object
     * @param line
     * the line with which to bind its properties
     * @return
     * rectangle object
     */
    private Rectangle addRect(Line line)
    {
        Rectangle rect = new Rectangle(SIDE, SIDE);
        rect.xProperty().bind(line.startXProperty().subtract(SIDE/2));
        rect.yProperty().bind(line.startYProperty());
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(3);
        rect.setFill(Color.BLACK);
        rect.setRotate(45);
        
        return rect;
    }
    
    /**
     * Helper method to add triangle to a line connector object.
     * @param line
     * the line with which to bind its properties
     * @return
     * triangle object
     */
    private Triangle addTriangle(Line line) 
    {
        Triangle triangle = new Triangle(line.getEndX() + SIDE, line.getEndY(), SIDE);

        binding = new ObjectBinding<List<Double>>() {

            {
                super.bind(line.endXProperty(), line.endYProperty());
            }

            @Override
            protected List<Double> computeValue() 
            {
                double x = line.getEndX() + SIDE;
                double y = line.getEndY();
                double height = SIDE;
                
                List<Double> list = new ArrayList<>();
                list.add(x);
                list.add(y);

                list.add(x - height);
                list.add(y - height);

                list.add(x - height);
                list.add(y + height);

                return list;
            }
        };
        
        binding.addListener((Observable observable) -> {
            triangle.getPoints().setAll(binding.get());
        });

        return triangle;
    }
    
    /**
     * Helper method to add arrow triangle to a line connector object.
     * @param line
     * the line with which to bind its properties
     * @return
     * triangle object
     */
    private Triangle addArrowTriangle(Line line) 
    {
        Triangle triangle = new Triangle(line.getEndX() + SIDE, line.getEndY(), SIDE);
        triangle.setFill(Color.TRANSPARENT);
        triangle.setStroke(Color.BLACK);
        triangle.setStrokeWidth(3);
        
        arrowBinding = new ObjectBinding<List<Double>>() {

            {
                super.bind(line.endXProperty(), line.endYProperty());
            }

            @Override
            protected List<Double> computeValue() 
            {
                double x = line.getEndX() + SIDE;
                double y = line.getEndY();
                double height = SIDE;
                
                List<Double> list = new ArrayList<>();
                list.add(x);
                list.add(y);

                list.add(x - height);
                list.add(y - height);

                list.add(x - height);
                list.add(y + height);

                return list;
            }
        };
        
        arrowBinding.addListener((Observable observable) -> {
            triangle.getPoints().setAll(arrowBinding.get());
        });

        return triangle;
    }
    
    /**
     * This method is used to split a line segment into two separate line segments.
     * @param line 
     * the line to be split into two
     * @param x
     * mouse press x location
     * @param y
     * mouse press y location
     */
    public void handleLineSelectionRequest(Line line, double x, double y) 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        // Unhighlight any previously selected objects
        if (workspace.getSelectedLine() != null)
        {
            unhighlightLine(workspace.getSelectedLine());
            workspace.setSelectedLine(null);
        }

        if (line == null) 
            handleDeselectLineRequest();
        else 
        {
            LineConnector lineConnector = dataManager.fetchLineConnector(line);
            VBox fromMainVBox = lineConnector.getFromBox().getMainVBox();
            VBox toMainVBox = lineConnector.getToBox().getMainVBox();
            originalX = x;
            originalY = y;
            originalLineStartX = line.getStartX();
            originalLineStartY = line.getStartY();
            originalLineEndX = line.getEndX();
            originalLineEndY = line.getEndY();
            originalCenterX = (originalLineStartX + originalLineEndX) / 2;
            originalCenterY = (originalLineStartY + originalLineEndY) / 2;
            originalStartXProperty = new SimpleDoubleProperty(line.startXProperty().getValue());
            originalStartYProperty = new SimpleDoubleProperty(line.startYProperty().getValue());
            originalEndXProperty = new SimpleDoubleProperty(line.endXProperty().getValue());
            originalEndYProperty = new SimpleDoubleProperty(line.endYProperty().getValue());
            originalSpecialStartXProperty = fromMainVBox.translateXProperty().add(line.getStartX()
                    - fromMainVBox.getTranslateX());
            originalSpecialEndYProperty = toMainVBox.translateYProperty().add(line.getEndY()
                    - toMainVBox.getTranslateY());
            // And now select the object
            workspace.setSelectedLine(line);
            highlightLine(line);
        }

        workspace.reloadWorkspace();

        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Helper method to un highlight the line by setting the effect to null.
     * @param line 
     * the line whose effect is to be set to null
     */
    private void unhighlightLine(Line line)
    {
        line.setEffect(null);
    }
    
    /**
     * Helper method to highlight the line by setting the effect property.
     * @param line 
     * the object whose effect is to be set
     */
    private void highlightLine(Line line)
    {
            line.setEffect(lineShadowEffect);
    }
    
    /**
     * Method to handle the deselect request by un highlighting the object. 
     */
    public void handleDeselectLineRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        // Only if there is a selected object.
        if (workspace.getSelectedLine() != null) 
        {
            unhighlightLine(workspace.getSelectedLine());
            workspace.setSelectedLine(null);
        }
        workspace.reloadWorkspace();

        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Method to handle the line split request by breaking the line into two. 
     */
    public void handleLineSplitRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        Line selectedLine = workspace.getSelectedLine();
        double startX = selectedLine.getStartX();
        double startY = selectedLine.getStartY();
        double endX = selectedLine.getEndX();
        double endY = selectedLine.getEndY();
        double middleX = (startX + endX) / 2;
        double middleY = (startY + endY) / 2;
        
        Line line1 = new Line();
        line1.setEndX(middleX);
        line1.setEndY(middleY);
        line1.setStrokeWidth(3);
        
        Line line2 = new Line();
        line2.setStrokeWidth(3);
        
        setSplitLineProperties(line1, line2);

        LineConnector lineConnector = dataManager.fetchLineConnector(selectedLine);
        int index = lineConnector.getLines().indexOf(selectedLine);
        lineConnector.getLines().remove(selectedLine);
        lineConnector.getLines().add(index, line2);
        lineConnector.getLines().add(index, line1);
        
        canvas.getChildren().remove(selectedLine);
        canvas.getChildren().addAll(line1, line2);
        
        workspace.setSelectedLine(null);
        workspace.reloadWorkspace();
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method just sets up the binding properties for the newly splitted lines.
     * @param line1
     * first line from split
     * @param line2 
     * second line from split
     */
    private void setSplitLineProperties(Line line1, Line line2)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        
        Line selectedLine = workspace.getSelectedLine();
        
        line1.startXProperty().bind(selectedLine.startXProperty());
        line1.startYProperty().bind(selectedLine.startYProperty());
        line2.startXProperty().bind(line1.endXProperty());
        line2.startYProperty().bind(line1.endYProperty());
        line2.endXProperty().bind(selectedLine.endXProperty());
        line2.endYProperty().bind(selectedLine.endYProperty());
        
        // Check if there is a next line. If yes, then bind its properties
        LineConnector lineConnector = dataManager.fetchLineConnector(selectedLine);
        int index = lineConnector.getLines().indexOf(selectedLine);
        int nextLineIndex = index + 1;
        if (nextLineIndex != lineConnector.getLines().size())
        {
            Line nextLine = lineConnector.getLines().get(nextLineIndex);
            nextLine.startXProperty().bind(line2.endXProperty());
            nextLine.startYProperty().bind(line2.endYProperty());
        }
    }
    
    // Below is the code for mergind lines
    /**
     * This method is used to split a line segment into two separate line segments.
     * @param line 
     * the line to be split into two
     */
    public void handleDoubleLineSelectionRequest(Line line) 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        if (workspace.getSelectedLine() == null)
        {
            workspace.setSelectedLine(line);
            highlightLine(line);
        }
        else if (workspace.getSelectedLine2() == null)
        {
            workspace.setSelectedLine2(line);
            highlightLine(line);
        }
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    public void handleMergeLinesRequest()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        Line selectedLine = workspace.getSelectedLine();
        Line selectedLine2 = workspace.getSelectedLine2();
        
        int startX1 = (int) selectedLine.getStartX();
        int startY1 = (int) selectedLine.getStartY();
        int endX1 = (int) selectedLine.getEndX();
        int endY1 = (int) selectedLine.getEndY();
        
        int startX2 = (int) selectedLine2.getStartX();
        int startY2 = (int) selectedLine2.getStartY();
        int endX2 = (int) selectedLine2.getEndX();
        int endY2 = (int) selectedLine2.getEndY();
        
        Line mergedLine = null;
        
        // Check if these lines are right next to each other
        if (endX1 == startX2 && endY1 == startY2)
        {
            mergedLine = mergeLines(selectedLine, selectedLine2);
            mergedLine.setStrokeWidth(3);
            
        }
        else if (endX2 == startX1 && endY2 == startY1)
        {
            mergedLine = mergeLines(selectedLine2, selectedLine);
            mergedLine.setStrokeWidth(3);
        }
        else
        {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(MERGE_LINE_TITLE, MERGE_LINE_ERROR);
        }
        
        workspace.setSelectedLine(null);
        workspace.setSelectedLine2(null);
        unhighlightLine(selectedLine);
        unhighlightLine(selectedLine2);
        
        if (mergedLine != null)
        {
            LineConnector lineConnector = dataManager.fetchLineConnector(selectedLine);
            lineConnector.getLines().add(lineConnector.getLines().indexOf(selectedLine),
                    mergedLine);
            lineConnector.getLines().remove(selectedLine);
            lineConnector.getLines().remove(selectedLine2);
            canvas.getChildren().removeAll(selectedLine, selectedLine2);
            canvas.getChildren().add(mergedLine);
        }
    }
    
    /**
     * Helper method to merge the two lines.
     * @param line1
     * first line
     * @param line2
     * second line
     * @ return 
     * merged line
     */
    private Line mergeLines(Line line1, Line line2)
    {
        Line line = new Line();
        line.startXProperty().bind(line1.startXProperty());
        line.startYProperty().bind(line1.startYProperty());
        line.endXProperty().bind(line2.endXProperty());
        line.endYProperty().bind(line2.endYProperty());
        
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        Line selectedLine = workspace.getSelectedLine();
        Line selectedLine2 = workspace.getSelectedLine2();
         // Check if there is a next line. If yes, then bind its properties
        LineConnector lineConnector = dataManager.fetchLineConnector(selectedLine);
        int indexOne = lineConnector.getLines().indexOf(selectedLine);
        int indexTwo = lineConnector.getLines().indexOf(selectedLine2);
        int index = Math.max(indexOne, indexTwo);
        int nextLineIndex = index + 1;
        if (nextLineIndex != lineConnector.getLines().size())
        {
            Line nextLine = lineConnector.getLines().get(nextLineIndex);
            nextLine.startXProperty().bind(line.endXProperty());
            nextLine.startYProperty().bind(line.endYProperty());
        }
        
        return line;
    }
    
    // Moving lines request is handled below
    
    public void handleMoveLineRequest(double newX, double newY)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Line line = workspace.getSelectedLine();
        LineConnector lineConnector = dataManager.fetchLineConnector(line);
        int indexOfLine = lineConnector.getLines().indexOf(line);
        
        double offsetX = newX - originalX;
        double offsetY = newY - originalY;
        //double newStartX = originalLineStartX + offsetX;
        //double newStartY = originalLineStartY + offsetY;
        //double newEndX = originalLineEndX + offsetX;
        //double newEndY = originalLineEndY + offsetY;
        if (indexOfLine == 0)
            moveFromBoxLine(lineConnector, offsetX);
        else if (indexOfLine == (lineConnector.getLines().size() - 1))
            moveToBoxLine(lineConnector, offsetY);
        else
            moveLineEndPoint(offsetX, offsetY);
    }
    
    /*private void moveLineStartPoint(double offsetX, double offsetY)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Line line = workspace.getSelectedLine();
        LineConnector lineConnector = dataManager.fetchLineConnector(line);
        int indexOfLine = lineConnector.getLines().indexOf(line);
        if (indexOfLine != 0)
        {
            line.startXProperty().bind(originalStartXProperty.add(offsetX));
            line.startYProperty().bind(originalStartYProperty.add(offsetY));
        }
    }*/
    
    private void moveFromBoxLine(LineConnector lineConnector, double offsetX)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        VBox fromMainVBox = lineConnector.getFromBox().getMainVBox();
        Line line = workspace.getSelectedLine();
        int OFFSET = 5;
        double leftBoundary = fromMainVBox.getTranslateX() + OFFSET;
        double rightBoundary = fromMainVBox.getTranslateX() + fromMainVBox.getWidth() - OFFSET;
        DoubleBinding newProperty = originalSpecialStartXProperty.add(offsetX);
        if (newProperty.getValue() > leftBoundary
                && newProperty.getValue() < rightBoundary)
            line.startXProperty().bind(newProperty);
    }
    
    private void moveToBoxLine(LineConnector lineConnector, double offsetY)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        VBox toMainVBox = lineConnector.getToBox().getMainVBox();
        Line line = workspace.getSelectedLine();
        int OFFSET = 5;
        double topBoundary = toMainVBox.getTranslateY() + OFFSET;
        double bottomBoundary = toMainVBox.getTranslateY() + toMainVBox.getHeight() - OFFSET;
        DoubleBinding newProperty = originalSpecialEndYProperty.add(offsetY);
        if (newProperty.getValue() > topBoundary
                && newProperty.getValue() < bottomBoundary)
            line.endYProperty().bind(newProperty);
    }
    
    private void moveLineEndPoint(double offsetX, double offsetY)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Line line = workspace.getSelectedLine();
        line.endXProperty().bind(originalEndXProperty.add(offsetX));
        line.endYProperty().bind(originalEndYProperty.add(offsetY));
    }
}