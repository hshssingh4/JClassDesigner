/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author RaniSons
 */
public class VariableObjectModel 
{
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;
    private final SimpleStringProperty scope;
    private final SimpleStringProperty isStatic;
    private final SimpleStringProperty isFinal;
 
    public VariableObjectModel(String name, String type, String scope,
            String isStatic, String isFinal) {
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.scope = new SimpleStringProperty(scope);
        this.isStatic = new SimpleStringProperty(isStatic);
        this.isFinal = new SimpleStringProperty(isFinal);
    }

    public String getName() 
    {
        return name.get();
    }

    public String getType() 
    {
        return type.get();
    }

    public String getScope()
    {
        return scope.get();
    }

    public String getIsStatic() 
    {
        return isStatic.get();
    }

    public String getIsFinal()
    {
        return isFinal.get();
    }
}
