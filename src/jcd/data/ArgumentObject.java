/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

/**
 *
 * @author RaniSons
 */
public class ArgumentObject 
{
    private String name;
    private String type;
    
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
