/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;

/**
 *
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
    
    public boolean isReturnable()
    {
        return !(type.equalsIgnoreCase("void"));
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
