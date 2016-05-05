/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 *
 * @author RaniSons
 */
public class LineConnector
{
    private ArrayList<Line> lines;
    private Shape shape;
    private LineConnectorType type;
    private Box fromBox;
    private Box toBox;
    
    public LineConnector() {}
    
    /**
     * Check whether this line connector is of argument value's type.
     * @param type
     * the type to check for
     * @return 
     * true if matched, false otherwise
     */
    public boolean isType(LineConnectorType type)
    {
        return this.type == type;
    }

    public ArrayList<Line> getLines() 
    {
        return lines;
    }

    public void setLines(ArrayList<Line> lines) 
    {
        this.lines = lines;
    }

    public Shape getShape() 
    {
        return shape;
    }

    public void setShape(Shape shape) 
    {
        this.shape = shape;
    }

    public LineConnectorType getType() 
    {
        return type;
    }

    public void setType(LineConnectorType type)
    {
        this.type = type;
    }

    public Box getFromBox()
    {
        return fromBox;
    }

    public void setFromBox(Box fromBox) 
    {
        this.fromBox = fromBox;
    }

    public Box getToBox() 
    {
        return toBox;
    }

    public void setToBox(Box toBox) 
    {
        this.toBox = toBox;
    }
}
