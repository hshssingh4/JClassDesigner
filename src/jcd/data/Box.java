/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;
import java.util.ArrayList;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

/**
 * This class defines the rectangle box that contains all the properties
 * of the class and interface object.
 * @author RaniSons
 */
public class Box 
{
    public static final double DEFAULT_WIDTH = 180.0;
    public static final double DEFAULT_HEIGHT = 30.0;
    
    private static final String CLASS_BOX_ELEMENTS = "box_elements";
    
    // VBox to contain other 3 vboxes
    private VBox mainVBox;
    
    // Box to put classes in
    private VBox classVBox;
    
    // Box to put all variables in
    private VBox variablesVBox;
    
    // Box to put all methods in
    private VBox methodsVBox;
    
    // These will be all of its line connectors
    private ArrayList<LineConnector> lineConnectors;

    /**
     * Constructor to initialize this box.
     * @param x
     * the x value for this box on canvas
     * @param y
     * the y value for this box on canvas
     */
    public Box(int x, int y)
    {
        initClassVBox();
        initVariablesVBox();
        initMethodsVBox();
        initLineConnectors();
        mainVBox = new VBox(classVBox, variablesVBox, methodsVBox);
        mainVBox.setTranslateX(x);
        mainVBox.setTranslateY(y);
    }
    
    /**
     * Helper method to initialize the class stack pane.
     */
    private void initClassVBox()
    {
        classVBox = new VBox();
        classVBox.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        classVBox.setAlignment(Pos.CENTER);
        classVBox.getStyleClass().add(CLASS_BOX_ELEMENTS);
    }
    
    /**
     * Helper method to initialize the variables stack pane.
     */
    private void initVariablesVBox()
    {
        variablesVBox = new VBox();
        variablesVBox.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        variablesVBox.setAlignment(Pos.TOP_LEFT);
        variablesVBox.getStyleClass().add(CLASS_BOX_ELEMENTS);
    }
    
    /**
     * Helper method to initialize the methods stack pane.
     */
    private void initMethodsVBox()
    {
        methodsVBox = new VBox();
        methodsVBox.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        methodsVBox.setAlignment(Pos.TOP_LEFT);
        methodsVBox.getStyleClass().add(CLASS_BOX_ELEMENTS);
    }
    
    private void initLineConnectors()
    {
        lineConnectors = new ArrayList();
    }
    
    // HERE ARE THE SPECFIC METHODS USED BY OTHER CLASSES
    
    public boolean containsLineConnector(LineConnector lineConnector)
    {
        for (LineConnector lc: lineConnectors)
            if (lc.getFromBox() == lineConnector.getFromBox() &&
                    lc.getToBox() == lineConnector.getToBox() && 
                    lc.getType() == lineConnector.getType())
                return true;
        return false;
    }
    
    
    /**
     * This function return the top center point in 2D of mainVbox.
     * @return 
     * top center point
     */
    public Point2D getTopCenterPoint()
    {
        double x = mainVBox.getTranslateX() + (mainVBox.getWidth() / 2);
        double y = mainVBox.getTranslateY();
        return new Point2D(x, y);
    }
    
    /**
     * This function return the left center point in 2D of mainVbox.
     * @return 
     * top center point
     */
    public Point2D getLeftCenterPoint()
    {
        double x = mainVBox.getTranslateX();
        double y = mainVBox.getTranslateY() + (mainVBox.getHeight() / 2);
        return new Point2D(x, y);
    }
    
    /**
     * Returns the top center x coordinate of the main box.
     * @return 
     * x coordinate
     */
    public double getCenterX()
    {
        return mainVBox.getTranslateX() + (mainVBox.getWidth() / 2);
    }
    
    /**
     * Returns the bottom center y coordinate of the main box.
     * @return 
     * y coordinate
     */
    public double getCenterY()
    {
        return mainVBox.getTranslateY() + (mainVBox.getHeight() / 2);
    }
    
    public void removeParentLineConnector()
    {
        LineConnector parentLineConnector = null;
        
        for (LineConnector lineConnector: lineConnectors)
            if (lineConnector.isType(LineConnectorType.TRIANGLE))
            {
                parentLineConnector = lineConnector;
                break;
            }
        if (parentLineConnector != null)
            lineConnectors.remove(parentLineConnector);
    }
    
    public VBox getMainVBox() 
    {
        return mainVBox;
    }

    public VBox getClassVBox() 
    {
        return classVBox;
    }

    public VBox getVariablesVBox() 
    {
        return variablesVBox;
    }

    public VBox getMethodsVBox() 
    {
        return methodsVBox;
    }

    public ArrayList<LineConnector> getLineConnectors() 
    {
        return lineConnectors;
    }
}
