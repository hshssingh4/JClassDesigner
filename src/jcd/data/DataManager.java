/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.shape.Line;
import static jcd.data.JClassDesignerMode.GRID_DEFAULT_MODE;
import jcd.gui.Workspace;
import saf.AppTemplate;
import saf.components.AppDataComponent;

/**
 * This class manages all the data related to this application.
 * @author RaniSons
 */
public class DataManager implements AppDataComponent
{
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    //HashMap<String, ClassObject> hashClasses;
    ArrayList<ClassObject> classesList;
    
    // CURRENT STATE OF THE APP
    JClassDesignerState state;
    
    // Current mode of the app (grid render, grid snap etc.)
    JClassDesignerMode mode;
    double canvasZoomScaleX;
    double canvasZoomScaleY;
    
    /**
     * Constructor to initialize the data manager.
     * @param initApp
     * the current application
     * @throws Exception 
     * Indicates that there was an error constructing it
     */
    public DataManager(AppTemplate initApp) throws Exception 
    {
	// KEEP THE APP FOR LATER
	app = initApp;
        
        //hashClasses = new HashMap();
        classesList = new ArrayList();
    }

    // EXTRA CONSTRUCOTR FOR TEST BED DRIVER CLASS
    public DataManager() 
    {
        //hashClasses = new HashMap();
        classesList = new ArrayList();
    }
    
    /**
     * This method adds the class object to the data manager.
     * @param obj 
     * the object to be added
     */
    public void addClassObject(ClassObject obj)
    {
        //hashClasses.put(obj.getClassName(), obj);
        classesList.add(obj);
    }
    
    /**
     * This method removes the class object from the data manager.
     * @param obj 
     * the object to be removed
     */
    public void removeClassObject(ClassObject obj)
    {
        //hashClasses.remove(obj.getClassName());
        classesList.remove(obj);
    }
    
    /**
     * Checks whether a class object is in the data manager basically.
     * @param obj
     * the object on which the check is made
     * @return 
     * true if it is unique (means not in the data manager), false otherwise
     */
    public boolean checkIfUnique(ClassObject obj)
    {
        for (ClassObject c: classesList)
            if (c.equals(obj))
                    return false;
        return true;
    }
    
    /**
     * Overloaded method that checks whether the class object with the given
     * package and class names is unique or not.
     * @param className
     * the class name of the class object to be checked for
     * @param packageName
     * the package name of the class object to be checked for
     * @return 
     * true if unique, false otherwise
     */
    public boolean checkIfUnique(String className, String packageName)
    {
        for (ClassObject c: classesList)
            if (c.equals(className, packageName))
                    return false;
        return true;
    }
    
    /**
     * This method check whether there is a class object with name same
     * as the argument name.
     * @param name
     * the name to look for
     * @return 
     * true if found, false otherwise
     */
    public boolean containsClassObject(String name)
    {
        for (ClassObject classObject: classesList)
            if (classObject.getClassName().equals(name))
                return true;
        return false;
    }
    
    /**
     * This method helps get the list of all the class objects that share a 
     * relation with the class object passed as the argument. Helps in saving data.
     * @param classObject
     * the class object of whose relatives are being searched
     * @return 
     * the list of class objects related to the argument class object
     */
    public ArrayList<ClassObject> fetchRelationshipsList(ClassObject classObject)
    {
        ArrayList<ClassObject> realationshipClasses = new ArrayList<>();
        
        if (classObject.getParentName() != null)
        {
            for (ClassObject obj: classesList)
                if (classObject.getParentName().equals(obj.getClassName())
                        && !realationshipClasses.contains(obj))
                    realationshipClasses.add(obj);
        }
        
        for (String interfaceName: classObject.getInterfaceNames())
            for (ClassObject obj: classesList)
                if (interfaceName.equals(obj.getClassName()) 
                        && !realationshipClasses.contains(obj))
                    realationshipClasses.add(obj);
        
        for (VariableObject variable: classObject.getVariables())
            for (ClassObject obj: classesList)
                if (variable.getType().equals(obj.getClassName())
                        && !realationshipClasses.contains(obj))
                    realationshipClasses.add(obj);
        
        for (MethodObject method: classObject.getMethods())
            for (ClassObject obj: classesList)
                if (method.getType().equals(obj.getClassName())
                        && !realationshipClasses.contains(obj))
                    realationshipClasses.add(obj);
        
        return realationshipClasses;
    }
    
    /**
     * This method returns the object that is present at the location of the
     * arguments and is at top. Helps in selection of an object.
     * @param layoutX
     * the x location of the mouse press
     * @param layoutY
     * the y location of the mouse press
     * @return 
     * the class object at this location (top one)
     */
    public ClassObject fetchTopObject(double layoutX, double layoutY)
    {
        for (int i = classesList.size() - 1; i >= 0; i--)
        {
            ClassObject obj = classesList.get(i);
            if (obj.getBox().getMainVBox().getBoundsInParent().contains(layoutX, layoutY))
                return obj;
        }
        return null;
    }
    
    /**
     * This method returns an array list of all the interfaces in data manager.
     * @return 
     * the list of all interfaces
     */
    public ArrayList<String> fetchLocalInterfaceNames()
    {
        ArrayList<String> localInterfaceNames = new ArrayList<>();
        
        for (ClassObject obj: classesList)
            if (obj.isInterfaceType())
                localInterfaceNames.add(obj.getClassName());
        
        return localInterfaceNames;
    }
    
    /**
     * This method helps get all the local interface names present in the
     * class object that is passed as the argument.
     * @param classObject
     * the class object whose interfaces are being looked into
     * @return
     * the list of local interface names that this class object has
     */
    public ArrayList<String> fetchLocalInterfaceNames(ClassObject classObject)
    {
        ArrayList<String> classLocalInterfaceNames = new ArrayList<>();
        
        for (String interfaceName: classObject.getInterfaceNames())
            if (containsClassObject(interfaceName))
                classLocalInterfaceNames.add(interfaceName);
        
        return classLocalInterfaceNames;
    }
    
    /**
     * This method clears out all the local interface names for a particular
     * class object.
     * @param classObject 
     * object whose local interface names are to be removed
     */
    public void clearLocalInterfaceNames(ClassObject classObject)
    {
        ArrayList<String> interfaceNames = new ArrayList<>();
        for (String interfaceName: classObject.getInterfaceNames())
            if (containsClassObject(interfaceName))
                interfaceNames.add(interfaceName);
            
        for (String interfaceName: interfaceNames)
            classObject.getInterfaceNames().remove(interfaceName);
    }
    
    /**
     * This method clears out all the api interface names for this
     * class object.
     * @param classObject
     * object whose api interface names are to be removed
     */
    public void clearApiInterfaceNames(ClassObject classObject)
    {
        ArrayList<String> interfaceNames = new ArrayList<>();
        for (String interfaceName: classObject.getInterfaceNames())
            if (!containsClassObject(interfaceName))
                interfaceNames.add(interfaceName);
            
        for (String interfaceName: interfaceNames)
            classObject.getInterfaceNames().remove(interfaceName);
    }
    
    /**
     * This method basically returns the class object that has a class name similar
     * to the argument provided.
     * @param className
     * the name of the object to look for
     * @return 
     * class object with the given argument name if found, null otherwise
     */
    public ClassObject fetchClassObject(String className) 
    {
        for (ClassObject classObject: classesList)
            if (classObject.getClassName().equals(className))
                return classObject;
        return null;
    }
    
    /**
     * This method takes in as an argument a line segment, and searches through
     * all the line connectors to see which has a this line inside.
     * @param line
     * the line to look for
     * @return 
     * the line connector that has that line
     * @Note: This method should never return null!
     */
    public LineConnector fetchLineConnector(Line line)
    {
        for (ClassObject classObject: classesList)
        {
            Box box = classObject.getBox();
            
            for(LineConnector lineConnector: box.getLineConnectors())
                if (lineConnector.getLines().contains(line))
                    return lineConnector;
        }
        
        return null;
    }
    
    /**
     * This method removes all the lines to this class object from the data manager.
     * @param obj 
     * the object to be removed
     */
    public void removeLineConnectors(ClassObject obj)
    {
        ArrayList<LineConnector> toRemoveList = new ArrayList<>();
        for (ClassObject classObject: classesList)
        {
            for (LineConnector lineConnector: classObject.getBox().getLineConnectors())
                if (obj.getBox().containsLineConnector(lineConnector))
                    toRemoveList.add(lineConnector);
            
            classObject.getBox().getLineConnectors().removeAll(toRemoveList);
        }
        
    }
    
    public Box getFromBox(LineConnector lineConnector)
    {
        for (ClassObject classObject: classesList)
            if (classObject.getBox().containsLineConnector(lineConnector))
                return classObject.getBox();
        return null;
    }
    
    public Box getToBox(LineConnector lineConnector)
    {
        ClassObject obj = fetchClassObject(lineConnector.getEndClassObjectName());
        return obj.getBox();
    }

    public ArrayList<ClassObject> getClassesList() 
    {
        return classesList;
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

    public JClassDesignerMode getMode() 
    {
        return mode;
    }

    public void setMode(JClassDesignerMode mode)
    {
        this.mode = mode;
    }
    
    public boolean isInMode(JClassDesignerMode testMode)
    {
	return mode == testMode;
    }

    public double getCanvasZoomScaleX() {
        return canvasZoomScaleX;
    }

    public void setCanvasZoomScaleX(double canvasZoomScaleX) 
    {
        this.canvasZoomScaleX = canvasZoomScaleX;
    }

    public double getCanvasZoomScaleY() {
        return canvasZoomScaleY;
    }

    public void setCanvasZoomScaleY(double canvasZoomScaleY) 
    {
        this.canvasZoomScaleY = canvasZoomScaleY;
    }
    
    /**
     * This method resets the whole workspace for a complete new application.
     */
    @Override
    public void reset()
    {
        if (app != null)
        {
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
            workspace.setSelectedObject(null);
            workspace.getCanvas().getChildren().clear();
        }
        //hashClasses.clear();
        classesList.clear();
        state = null;
        mode = GRID_DEFAULT_MODE;
        canvasZoomScaleX = 1;
        canvasZoomScaleY = 1;
    }
}
