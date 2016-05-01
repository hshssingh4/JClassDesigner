/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

/**
 * This class represents the argument object in the application. It has all the
 * properties that a usual argument inside a method has.
 * @author RaniSons
 */
public class ArgumentObject 
{
    private String name; // name of the argument
    private String type; // type of the argument
    
    /**
     * Default constructor
     */
    public ArgumentObject() {}

    public String getName()
    {
        return name;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type) 
    {
        this.type = type;
    }
}
