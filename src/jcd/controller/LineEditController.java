/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.util.ArrayList;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
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
import jcd.data.DataManager;
import jcd.data.LineConnector;
import jcd.data.LineConnectorType;
import jcd.gui.Workspace;
import saf.ui.AppMessageDialogSingleton;

/**
 * This class handles all the activity (user interactions) with the
 * lines. 
 * @author RaniSons
 */
public class LineEditController 
{
    /* This offset if for the extra space that we need to show for the
    scroll pane */
    private static final double SIDE = 10.0;
    private static final double OFFSET = 5.0;
    private static final String MERGE_LINE_TITLE = "Merge Error";
    private static final String MERGE_LINE_ERROR = "Please make sure consecutive"
            + " lines are selected!";
    DropShadow lineShadowEffect; // Effect for highlighting the line
    
    // HERE'S THE FULL APP, WHICH GIVES US ACCESS TO OTHER STUFF
    JClassDesigner app;
    DataManager dataManager;
    
    // HERE ARE THE CONSTANTS FOR DRAGGING
    double originalX, originalY;
    
    DoubleProperty originalEndXProperty, originalEndYProperty;
    DoubleBinding originalSpecialStartXProperty, originalSpecialEndYProperty;
  
    /**
     * Constructor to set up the application.
     * @param initApp 
     * the application itself.
     */
    public LineEditController(JClassDesigner initApp) 
    {
	// KEEP IT FOR LATER
	app = initApp;
        dataManager = (DataManager)app.getDataComponent();
        
        // Now initialize the highlighting effect.
        lineShadowEffect = new DropShadow();
        lineShadowEffect.setOffsetX(0.0f);
	lineShadowEffect.setOffsetY(0.0f);
	lineShadowEffect.setSpread(1.0);
	lineShadowEffect.setColor(Color.LIGHTGRAY);
	lineShadowEffect.setBlurType(BlurType.GAUSSIAN);
	lineShadowEffect.setRadius(5);
    }
    
    /**
     * This method adds a single diamond line connector from one box
     * to another.
     * @param fromBox
     * add a line from this box
     * @param toBox
     * add a line to this box
     * @param endClassObjectName
     * the class object where this line ends
     * @param type
     * Line Connector Type (diamond/triangle/arrow)
     */
    public void handleAddLineConnector(Box fromBox, Box toBox,
            String endClassObjectName, LineConnectorType type) 
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
        
        line1.startXProperty().bind(fromMainVBox.translateXProperty().add(
                fromMainVBox.widthProperty().divide(2)));

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
        line2.endYProperty().bind(toMainVBox.translateYProperty().add(
                toMainVBox.heightProperty().divide(2)));
        
        ArrayList<Line> lines = new ArrayList<>();
        lines.add(line1);
        lines.add(line2);
        
        // Now initialize the line connector
        LineConnector lineConnector = new LineConnector();
        lineConnector.setLines(lines);
        lineConnector.setEndClassObjectName(endClassObjectName);
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
        
        // ONLY ADD IF IT IS UNIQUE
        if (!fromBox.containsLineConnector(lineConnector))
        {
            canvas.getChildren().addAll(lineConnector.getLines());
            canvas.getChildren().add(lineConnector.getShape());
            fromBox.getLineConnectors().add(lineConnector);
        }
        
        workspace.reloadWorkspace();
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
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
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(3);
        rect.setFill(Color.TRANSPARENT);
        rect.setRotate(45);
        bindRectanglePoints(rect, line);
        
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
        double x = line.getEndX() + SIDE;
        double y = line.getEndY();
        Triangle triangle = new Triangle(x, y, SIDE);
        triangle.setFill(Color.BLACK);
        triangle.setStroke(Color.BLACK);
        triangle.setStrokeWidth(2);
        bindTrianglePoints(triangle, line);
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
        double x = line.getEndX() + SIDE;
        double y = line.getEndY();
        Triangle triangle = new Triangle(x, y, SIDE);
        triangle.setFill(Color.TRANSPARENT);
        triangle.setStroke(Color.BLACK);
        triangle.setStrokeWidth(2);
        bindTrianglePoints(triangle, line);
        return triangle;
    }
    
    /**
     * This method helps bind the rectangle points properly.
     * @param rect
     * the rectangle to bind
     * @param line 
     * the line to be bound to
     */
    private void bindRectanglePoints(Rectangle rect, Line line)
    {
        rect.xProperty().bind(line.startXProperty().subtract(SIDE/2));
        rect.yProperty().bind(line.startYProperty());
    }
    
    /**
     * This method bind the triangle points to the line passed as an argument.
     * @param triangle
     * the triangle to be bound
     * @param line 
     * the line to be bound to
     */
    private void bindTrianglePoints(Triangle triangle, Line line)
    {
        line.endXProperty().addListener(e -> {
            triangle.setX(line.getEndX() + SIDE);
            triangle.setY(line.getEndY());
        });
        line.endYProperty().addListener(e -> {
            triangle.setX(line.getEndX() + SIDE);
            triangle.setY(line.getEndY());
        });
    }
    
    /**
     * This method is used to select a line segment so it
     * could be later split into two separate line segments.
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

        // Unhighlight any previously selected lines
        if (workspace.getSelectedLine() != null)
        {
            unhighlightLine(workspace.getSelectedLine());
            workspace.setSelectedLine(null);
        }
        
        if (workspace.getSelectedLine2() != null)
        {
            unhighlightLine(workspace.getSelectedLine2());
            workspace.setSelectedLine2(null);
        }
        
        LineConnector lineConnector = dataManager.fetchLineConnector(line);
        Box fromBox = dataManager.getFromBox(lineConnector);
        VBox fromMainVBox = fromBox.getMainVBox();
        Box toBox = dataManager.getToBox(lineConnector);
        VBox toMainVBox = toBox.getMainVBox();
        
        originalX = x;
        originalY = y;
        originalEndXProperty = new SimpleDoubleProperty(line.endXProperty().getValue());
        originalEndYProperty = new SimpleDoubleProperty(line.endYProperty().getValue());
        originalSpecialStartXProperty = fromMainVBox.translateXProperty().add(line.getStartX()
                - fromMainVBox.getTranslateX());
        originalSpecialEndYProperty = toMainVBox.translateYProperty().add(line.getEndY()
                - toMainVBox.getTranslateY());
        
        // And now select the object
        workspace.setSelectedLine(line);
        highlightLine(line);

        workspace.reloadWorkspace();
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Method to handle the deselect request by un highlighting the lines. 
     */
    public void handleDeselectLinesRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();

        // Only if there is a selected line 1.
        if (workspace.getSelectedLine() != null) 
        {
            unhighlightLine(workspace.getSelectedLine());
            workspace.setSelectedLine(null);
        }
        
        // Only if there is a selected line 2.
        if (workspace.getSelectedLine2() != null) 
        {
            unhighlightLine(workspace.getSelectedLine2());
            workspace.setSelectedLine2(null);
        }
        
        workspace.reloadWorkspace();
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Method to handle the line split request by breaking the line into two. 
     */
    public void handleSplitLineRequest() 
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
        
        /* Also bind the diamond  and triangle 
           properties if its the first line or the last line and line connector
           has a diamond */
        if (index == 0 && lineConnector.getType() == LineConnectorType.DIAMOND)
            bindRectanglePoints((Rectangle)lineConnector.getShape(), line1);
        else if (nextLineIndex == lineConnector.getLines().size() && 
                lineConnector.isType(LineConnectorType.TRIANGLE) ||
                    lineConnector.isType(LineConnectorType.ARROW))
            bindTrianglePoints((Triangle)lineConnector.getShape(), line2);
    }
    
    // Below is the code for merging lines
    /**
     * This method is used to double select lines so they could be merged.
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
        
        workspace.reloadWorkspace();
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
            int minSelectedIndex = Math.min(lineConnector.getLines().indexOf(selectedLine),
                    lineConnector.getLines().indexOf(selectedLine2));
            int maxSelectedIndex = Math.max(lineConnector.getLines().indexOf(selectedLine),
                    lineConnector.getLines().indexOf(selectedLine2));
            
            if (minSelectedIndex == 0 && lineConnector.isType(LineConnectorType.DIAMOND))
                bindRectanglePoints((Rectangle)lineConnector.getShape(), mergedLine);
            else if (maxSelectedIndex == (lineConnector.getLines().size() - 1) && 
                    (lineConnector.isType(LineConnectorType.ARROW) ||
                    lineConnector.isType(LineConnectorType.TRIANGLE)))
                bindTrianglePoints((Triangle)lineConnector.getShape(), mergedLine);
            
            lineConnector.getLines().add(minSelectedIndex, mergedLine);
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
    /**
     * This method handles the moving of lines.
     * @param newX
     * the new x location of the mouse drag
     * @param newY 
     * the new y location of the mouse drag
     */
    public void handleMoveLineRequest(double newX, double newY)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Line line = workspace.getSelectedLine();
        LineConnector lineConnector = dataManager.fetchLineConnector(line);
        int indexOfLine = lineConnector.getLines().indexOf(line);
        
        double offsetX = newX - originalX;
        double offsetY = newY - originalY;
        double startX = line.getStartX();
        double startY = line.getStartY();
        double endX = line.getEndX();
        double endY = line.getEndY();
        
        double distanceToBox = Math.sqrt(Math.pow(newX-startX, 2) + Math.pow(newY-startY, 2));
        double distanceToEnd = Math.sqrt(Math.pow(newX - endX, 2) + Math.pow(newY - endY, 2));
        
        if (indexOfLine == 0 && distanceToBox <= distanceToEnd)
            moveFromBoxLine(lineConnector, offsetX);
        else if (indexOfLine == 0 && distanceToEnd < distanceToBox)
            moveLineEndPoint(offsetX, offsetY);
        else if (indexOfLine == (lineConnector.getLines().size() - 1))
            moveToBoxLine(lineConnector, offsetY);
        else
            moveLineEndPoint(offsetX, offsetY);
    }
    
    private void moveFromBoxLine(LineConnector lineConnector, double offsetX)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        VBox fromMainVBox = dataManager.getFromBox(lineConnector).getMainVBox();
        Line line = workspace.getSelectedLine();
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
        VBox toMainVBox = dataManager.getToBox(lineConnector).getMainVBox();
        Line line = workspace.getSelectedLine();
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
}
