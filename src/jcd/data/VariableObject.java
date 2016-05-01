/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

/**
 * This class represents a variable object. It has all the properties
 * that a variable in Java usually has.
 * @author RaniSons
 */
public class VariableObject 
{
    private String name;
    private String type;
    private String scope;
    private boolean staticType;
    private boolean finalType;
    
    public VariableObject() {}

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
}

