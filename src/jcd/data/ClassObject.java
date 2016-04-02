/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author RaniSons
 */
public class ClassObject
{
    private String className;
    private String packageName;
    private RectanglesBox rectanglesBox;
    
    public ClassObject(String className, String packageName, RectanglesBox box)
    {
        this.className = className;
        this.packageName = packageName;
        this.rectanglesBox = box;
    }
    
    public boolean equals(ClassObject obj)
    {
        return this.className.equalsIgnoreCase(obj.className);
    }

    public String getClassName() 
    {
        return className;
    }

    public void setClassName(String className) 
    {
        this.className = className;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public RectanglesBox getRectanglesBox()
    {
        return rectanglesBox;
    }

    public void setRectanglesBox(RectanglesBox rectanglesBox) 
    {
        this.rectanglesBox = rectanglesBox;
    }
}
