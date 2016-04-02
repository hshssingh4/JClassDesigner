/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 *
 * @author RaniSons
 */
public class RectanglesBox 
{
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
    private Text classNameText;
    
    // Box to put all variables in
    private VBox variablesTextVBox;
    private Text[] variablesText;
    
    // Box to put all methods in
    private VBox methodsTextVBox;
    private Text[] methodsText;

    public RectanglesBox()
    {
        
    }

    public VBox getStackPanesVBox() 
    {
        return stackPanesVBox;
    }
}
