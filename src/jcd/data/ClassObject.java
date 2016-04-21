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
    private boolean interfaceType;
    private ArrayList<String> interfaceNames;
    private ArrayList<VariableObject> variables;
    private ArrayList<MethodObject> methods;
    private Box box;
    
    /**
     * Constructor to initialize the class object.
     * @param className
     * the name of this class
     * @param box 
     * the box that displays this class
     */
    public ClassObject(String className, Box box)
    {
        this.className = className;
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
                (this.packageName == null ? 
                packageName == null : this.packageName.equals(packageName)));
    }
    
    public boolean equals(String className, String packageName)
    {
        return (this.className.equalsIgnoreCase(className) && 
                (this.packageName == null ? 
                packageName == null : this.packageName.equals(packageName)));
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

    public boolean isInterfaceType()
    {
        return interfaceType;
    }

    public void setInterfaceType(boolean interfaceType) 
    {
        this.interfaceType = interfaceType;
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

    public ArrayList<String> getInterfaceNames() 
    {
        return interfaceNames;
    }

    public void setInterfaceNames(ArrayList<String> interfaceNames)
    {
        this.interfaceNames = interfaceNames;
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
