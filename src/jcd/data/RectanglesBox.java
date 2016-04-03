/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author RaniSons
 */
public class RectanglesBox 
{
    private static final double DEFAULT_WIDTH = 300.0;
    private static final double DEFAULT_HEIGHT = 50.0;
    private static final double DEFAULT_STROKE_WIDTH = 2.0;
    
    // VBox to contain all stackpanes
    private VBox stackPanesVBox;
    
    // Stack panes to contain rectangles with text
    private StackPane classRectangleStackPane;
    private StackPane variablesRectangleStackPane;
    private StackPane methodsRectangleStackPane;
    
    // Other rectangles and text boxes
    private Rectangle classRectangle;
    private Rectangle variablesRectangle;
    private Rectangle methodsRectangle;
    
    // Box to put classes in
    private VBox classNameTextVBox;
    private ArrayList<Text> classesTextList;
    
    // Box to put all variables in
    private VBox variablesTextVBox;
    private ArrayList<Text> variablesTextList;
    
    // Box to put all methods in
    private VBox methodsTextVBox;
    private ArrayList<Text> methodsTextList;

    public RectanglesBox()
    {
        stackPanesVBox = new VBox();
        initClassStackPane();
        initVariablesStackPane();
        initMethodsStackPane();
    }
    
    private void initClassStackPane()
    {
        classRectangleStackPane = new StackPane();
        classRectangle = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        classRectangle.setFill(Color.WHITE);
        classRectangle.setStroke(Color.BLACK);
        classRectangle.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        classNameTextVBox = new VBox();
        classesTextList = new ArrayList();
    }
    
    private void initVariablesStackPane()
    {
        variablesRectangleStackPane = new StackPane();
        variablesRectangle = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        variablesRectangle.setFill(Color.WHITE);
        variablesRectangle.setStroke(Color.BLACK);
        variablesRectangle.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        variablesTextVBox = new VBox();
        variablesTextList = new ArrayList();
        variablesTextVBox.getChildren().addAll(variablesTextList);
        variablesRectangleStackPane.getChildren().addAll(variablesRectangle, variablesTextVBox);
    }
    
    private void initMethodsStackPane()
    {
        methodsRectangleStackPane = new StackPane();
        methodsRectangle = new Rectangle(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        methodsRectangle.setFill(Color.WHITE);
        methodsRectangle.setStroke(Color.BLACK);
        methodsRectangle.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        methodsTextVBox = new VBox();
        methodsTextList = new ArrayList();
        methodsTextVBox.getChildren().addAll(methodsTextList);
        methodsRectangleStackPane.getChildren().addAll(methodsRectangle, methodsTextVBox);
    }

    public VBox getStackPanesVBox() 
    {
        return stackPanesVBox;
    }

    public Rectangle getClassRectangle() 
    {
        return classRectangle;
    }

    public VBox getClassNameTextVBox()
    {
        return classNameTextVBox;
    }
    
    public VBox getVariablesTextVBox() 
    {
        return variablesTextVBox;
    }

    public VBox getMethodsTextVBox() 
    {
        return methodsTextVBox;
    }

    public ArrayList<Text> getClassesTextList()
    {
        return classesTextList;
    }

    public StackPane getClassRectangleStackPane() 
    {
        return classRectangleStackPane;
    }

    public StackPane getVariablesRectangleStackPane() {
        return variablesRectangleStackPane;
    }

    public StackPane getMethodsRectangleStackPane() {
        return methodsRectangleStackPane;
    }
    
    
}
