/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;

/**
 * This class manages the class objects for our application. It contains all 
 * the properties that a Java class usually has.
 * @author RaniSons
 */
public class ClassObject
{
    private String className;
    private String packageName;
    private String parentName;
    private String interfaceName;
    private ArrayList<VariableObject> variables;
    private ArrayList<MethodObject> methods;
    private Box box;
    
    /**
     * Constructor to initialize the class object.
     * @param className
     * the name of this class
     * @param packageName
     * the name of the package for this class
     * @param box 
     * the box that displays this class
     */
    public ClassObject(String className, String packageName, Box box)
    {
        this.className = className;
        this.packageName = packageName;
        this.box = box;
    }
    
    /**
     * This checks if whether the two class objects are equal or not.
     * @param obj
     * the object to check if equal to
     * @return 
     * true if equal, false otherwise
     */
    public boolean equals(ClassObject obj)
    {
        return (this.className.equalsIgnoreCase(obj.className) && 
                this.packageName.equalsIgnoreCase(obj.packageName));
    }
    
    public boolean equals(String className, String packageName)
    {
        return (this.className.equalsIgnoreCase(className) && 
                this.packageName.equalsIgnoreCase(packageName));
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

    public Box getBox()
    {
        return box;
    }
    
    public String getParentName() 
    {
        return parentName;
    }

    public void setParentName(String parentName)
    {
        this.parentName = parentName;
    }

    public String getInterfaceName()
    {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) 
    {
        this.interfaceName = interfaceName;
    }

    public ArrayList<VariableObject> getVariables() 
    {
        return variables;
    }

    public void setVariables(ArrayList<VariableObject> variables) 
    {
        this.variables = variables;
    }

    public ArrayList<MethodObject> getMethods() 
    {
        return methods;
    }

    public void setMethods(ArrayList<MethodObject> methods) 
    {
        this.methods = methods;
    }
}
