/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import java.util.HashMap;
import jcd.gui.Workspace;
import saf.AppTemplate;
import saf.components.AppDataComponent;

/**
 *
 * @author RaniSons
 */
public class DataManager implements AppDataComponent
{
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    HashMap<String, ClassObject> hashClasses;
    ArrayList<ClassObject> classesList;
    
    // CURRENT STATE OF THE APP
    JClassDesignerState state;
    
    public DataManager(AppTemplate initApp) throws Exception 
    {
	// KEEP THE APP FOR LATER
	app = initApp;
        
        hashClasses = new HashMap();
        classesList = new ArrayList();
    }
    
    public void addClassObject(ClassObject obj)
    {
        hashClasses.put(obj.getClassName(), obj);
        classesList.add(obj);
    }

    public HashMap<String, ClassObject> getHashClasses()
    {
        return hashClasses;
    }

    public ArrayList<ClassObject> getClassesList() 
    {
        return classesList;
    }
    
    public boolean checkIfUnique(ClassObject obj)
    {
        for (ClassObject c: classesList)
            if (c.equals(obj))
                    return false;
        return true;
    }
    
    public JClassDesignerState getState()
    {
	return state;
    }

    public void setState(JClassDesignerState initState) 
    {
	state = initState;
    }

    public boolean isInState(JClassDesignerState testState)
    {
	return state == testState;
    }
    
    @Override
    public void reset()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
    }
}
