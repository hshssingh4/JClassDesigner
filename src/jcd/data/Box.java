/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;
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
}
