/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.model;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import jcd.data.ArgumentObject;

/**
 * This class is a model representation of a particular method object so it
 * could be added to the table view.
 * @author RaniSons
 */
public class MethodObjectModel 
{
    private final SimpleStringProperty name;
    private final SimpleStringProperty type;
    private final SimpleStringProperty scope;
    private final SimpleStringProperty isStatic;
    private final SimpleStringProperty isFinal;
    private final SimpleStringProperty isAbstract;
 
    public MethodObjectModel(String name, String type, String scope,
            String isStatic, String isFinal, String isAbstract) {
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.scope = new SimpleStringProperty(scope);
        this.isStatic = new SimpleStringProperty(isStatic);
        this.isFinal = new SimpleStringProperty(isFinal);
        this.isAbstract = new SimpleStringProperty(isAbstract);
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
    
    public String getIsAbstract()
    {
        return isAbstract.get();
    }
}
