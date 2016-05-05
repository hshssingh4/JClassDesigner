/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;

/**
 * This class represents a method object. It has all the properties that a
 * method in java usual has.
 * @author RaniSons
 */
public class MethodObject 
{
    private String name;
    private String type;
    private String scope;
    private boolean staticType;
    private boolean finalType;
    private boolean abstractType;
    private ArrayList<ArgumentObject> arguments;

    public MethodObject() {}
    
    /**
     * Checks whether this method has a return type other than void.
     * @return 
     * true if it does have a return type other than void, false otherwise
     */
    public boolean isReturnable()
    {
        return !(type.equalsIgnoreCase("void"));
    }
    
    /**
     * This method checks whether this method object has a variable
     * with the type specified in the argument.
     * @param type
     * the type to check for
     * @return 
     * true if there is, false otherwise
     */
    public boolean hasArgumentOfType(String type)
    {
        for (ArgumentObject argument: arguments)
            if (argument.getType().equals(type))
                return true;
        return false;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
    
    public String getScope() 
    {
        return scope;
    }

    public void setScope(String scope) 
    {
        this.scope = scope;
    }

    public boolean isStaticType() 
    {
        return staticType;
    }

    public void setStaticType(boolean staticType) 
    {
        this.staticType = staticType;
    }

    public boolean isFinalType() 
    {
        return finalType;
    }

    public void setFinalType(boolean finalType) 
    {
        this.finalType = finalType;
    }   

    public ArrayList<ArgumentObject> getArguments()
    {
        return arguments;
    }

    public void setArguments(ArrayList<ArgumentObject> arguments)
    {
        this.arguments = arguments;
    }

    public boolean isAbstractType() {
        return abstractType;
    }

    public void setAbstractType(boolean abstractType) 
    {
        this.abstractType = abstractType;
    }
}
