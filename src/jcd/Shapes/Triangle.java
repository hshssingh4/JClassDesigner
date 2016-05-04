/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.Shapes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.shape.Polygon;

/**
 * This class is basically a triangle shaped polygon that is
 * needed for line connectors.
 * @author RaniSons
 */
public class Triangle extends Polygon
{
    private double x;
    private double y;
    private double height;
    
    public Triangle(double x, double y, double height)
    {
        this.x = x;
        this.y = y;
        this.height = height;
        setPoints();
    }
    
    private void setPoints()
    {
        this.getPoints().addAll(new Double[] {
            x, y, x - height, y - height, x - height, y + height
        });
    }
}