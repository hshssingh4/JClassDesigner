/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.Shapes;
import javafx.scene.shape.Polygon;

/**
 * This class is basically a triangle shaped polygon that is
 * needed for line connectors.
 * @author RaniSons
 */
public class Triangle extends Polygon
{
    private double x = 0.0;
    private double y = 0.0;
    private double height = 0.0;
    
    public Triangle() {}
    
    public Triangle(double x, double y, double height)
    {
        this.x = x;
        this.y = y;
        this.height = height;
        setPoints();
    }
    
    private void setPoints()
    {
        Double[] points = {x, y, x - height, y - height, x - height, y + height};
        this.getPoints().setAll(points);
    }

    public double getX()
    {
        return x;
    }

    public void setX(double x) 
    {
        this.x = x;
        setPoints();
    }

    public double getY() 
    {
        return y;
    }

    public void setY(double y)
    {
        this.y = y;
        setPoints();
    }

    public double getHeight() 
    {
        return height;
    }

    public void setHeight(double height) 
    {
        this.height = height;
    }
}