/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.UndoRedo;

import java.util.ArrayList;
import java.util.Stack;
import javafx.scene.layout.VBox;
import jcd.JClassDesigner;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.JClassDesignerMode;
import jcd.gui.Workspace;
import saf.ui.AppMessageDialogSingleton;

/**
 * This class implements the undo functionality for the JClassDesigner.
 * @author RaniSons
 */
public class RedoManager 
{
    public static final String REDO_ERROR_TITLE = "Redo";
    public static final String REDO_ERROR_MESSAGE = "There is no action to redo!";
    
    Stack<String> redoStack;
    Stack<ClassObject> selectedObjectStack;
    Stack<ArrayList<Double>> locationStack;
    Stack<ArrayList<Double>> sizeStack;
    
    // HERE'S THE FULL APP, WHICH GIVES US ACCESS TO OTHER STUFF
    JClassDesigner app;
    DataManager dataManager;
    
    /**
     * Constructor to set up the application.
     * @param initApp 
     * the application itself.
     */
    public RedoManager(JClassDesigner initApp) 
    {
	// KEEP IT FOR LATER
        
	app = initApp;
        dataManager = (DataManager)app.getDataComponent();
        // Also init the undo stack
        redoStack = new Stack();
        selectedObjectStack = new Stack();
        locationStack = new Stack();
        sizeStack = new Stack();
    }
    
    /**
     * This method pushes on to the stack the last action a user made.
     * @param command 
     * the command that the user just performed
     */
    public void push(Command command)
    {
        redoStack.push(command.toString());
    }
    
    /**
     * This method pushes on to the stack the last action a user made.
     * @param selectedObject
     * the object that was at the top of redo stack
     */
    public void pushClassObject(ClassObject selectedObject)
    {
        selectedObjectStack.push(selectedObject);
    }
    
    /**
     * This method pushes on to the stack the last translates of the box before being
     * moved.
     * @param location
     * the old location before moving
     */
    public void pushLocation(ArrayList<Double> location)
    {
        locationStack.push(location);
    }
    
    /**
     * This method pushes on to the stack the last size of the box before being
     * changes.
     * @param size
     * the old size before moving
     */
    public void pushSize(ArrayList<Double> size)
    {
        sizeStack.push(size);
    }
    
    /**
     * This method pops off the last pushed operation from the undo stack.
     * @return 
     * the command popped off in form of a string
     */
    public String pop()
    {
        if (!redoStack.isEmpty())
        {
            undoManager().push(undoManager().getOpposite(Command.valueOf(redoStack.peek())));
            return redoStack.pop();
        }
        return null;
    }
    
    /**
     * This method pops off the last pushed operation from the undo stack.
     * @return 
     * the command popped off in form of a string
     */
    public ClassObject popClassObject()
    {
        if (!selectedObjectStack.isEmpty())
        {
            undoManager().pushClassObject(selectedObjectStack.peek());
            return selectedObjectStack.pop();
        }
        return null;
    }
    
    /**
     * This method pops off the last pushed location from the undo stack.
     * @return 
     * the command popped off in form of a string
     */
    public ArrayList<Double> popLocation()
    {
        if (!locationStack.isEmpty())
            return locationStack.pop();
        return null;
    }
    
    /**
     * This method pops off the last pushed size from the undo stack.
     * @return 
     * the old size
     */
    public ArrayList<Double> popSize()
    {
        if (!sizeStack.isEmpty())
            return sizeStack.pop();
        return null;
    }
    
    /**
     * This method gets called when redo button is clicked.
     */
    public void redo()
    {
        String actionToRedo = pop();
        
        if (actionToRedo != null)
            performAction(Command.valueOf(actionToRedo));
        else
        {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(REDO_ERROR_TITLE, REDO_ERROR_MESSAGE);
        }
    }
    
    /**
     * This method clears all the stacks in this redo manager.
     */
    public void clearStacks()
    {
        redoStack.clear();
        selectedObjectStack.clear();
        locationStack.clear();
        sizeStack.clear();
    }
    
    /**
     * This method get the last action performed by the used and is
     * responsible for calling the correct method to undo the last change.
     * @param commandToUndo 
     */
    private void performAction(Command commandToUndo)
    {
        switch (commandToUndo)
        {
            case ADD_CLASS_OBJECT:
                removeClassObject();
                break;
            case REMOVE_CLASS_OBJECT:
                addClassObject();
                break;
            case ZOOM_IN:
                zoomOut();
                break;
            case ZOOM_OUT:
                zoomIn();
                break;
            case GRID_RENDER:
                gridUnrender();
                break;
            case GRID_UNRENDER:
                gridRender();
                break;
            case MOVE_BOX:
                relocate();
                break;
            case SIZE_BOX:
                resize();
                break;
            default:
                break;
        }
    }
    
    private void removeClassObject()
    {
        dataManager.removeClassObject(popClassObject());
        workspace().reloadWorkspace();
    }
    
    private void addClassObject()
    {
        dataManager.addClassObject(popClassObject());
        workspace().reloadWorkspace();
    }
    
    private void zoomOut()
    {
        workspace().getCanvasEditController().handleZoomOutRequest();
        workspace().reloadWorkspace();
    }
    
    private void zoomIn()
    {
        workspace().getCanvasEditController().handleZoomInRequest();
        workspace().reloadWorkspace();
    }
    
    private void gridUnrender()
    {
        dataManager.setMode(JClassDesignerMode.GRID_DEFAULT_MODE);
        workspace().reloadWorkspace();
    }
    
    private void gridRender()
    {
        dataManager.setMode(JClassDesignerMode.GRID_RENDER_MODE);
        workspace().reloadWorkspace();
    }
    
     private void relocate()
    {
        ArrayList<Double> points = popLocation();
        VBox mainVBox = workspace().getSelectedObject().getBox().getMainVBox();
        ArrayList<Double> location = new ArrayList<>();
        location.add(mainVBox.getTranslateX());
        location.add(mainVBox.getTranslateY());
        undoManager().pushLocation(location);
        
        mainVBox.setTranslateX(points.get(0));
        mainVBox.setTranslateY(points.get(1));
        workspace().reloadWorkspace();
    }
    
    private void resize()
    {
        ArrayList<Double> oldSize = popSize();
        ClassObject selectedObject = workspace().getSelectedObject();
        VBox mainVBox = selectedObject.getBox().getMainVBox();
        VBox classVBox = selectedObject.getBox().getClassVBox();
        VBox variablesVBox = selectedObject.getBox().getVariablesVBox();
        VBox methodsVBox = selectedObject.getBox().getMethodsVBox();
        ArrayList<Double> size = new ArrayList<>();
        size.add(mainVBox.getHeight());
        size.add(mainVBox.getWidth());
        size.add(classVBox.getHeight());
        size.add(variablesVBox.getHeight());
        size.add(methodsVBox.getHeight());
        undoManager().pushSize(size);
        
        mainVBox.setMinHeight(oldSize.get(0));
        mainVBox.setMinWidth(oldSize.get(1));
        classVBox.setMinHeight(oldSize.get(2));
        variablesVBox.setMinHeight(oldSize.get(3));
        methodsVBox.setMinHeight(oldSize.get(4));
        workspace().reloadWorkspace();
    }
    
    
    /**
     * Helper method to get the undoManager quickly.
     * @return 
     * the undo manager
     */
    private UndoManager undoManager()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        return workspace.getUndoManager();
    }
    
    /**
     * Helper method to get the workspace faster;
     * @return 
     * the workspace
     */
    private Workspace workspace()
    {
        return (Workspace) app.getWorkspaceComponent();
    }

    public Stack<String> getRedoStack() 
    {
        return redoStack;
    }

    public Stack<ClassObject> getSelectedObjectStack() 
    {
        return selectedObjectStack;
    }
    
    public Stack<ArrayList<Double>> getLocationStack() 
    {
        return locationStack;
    }
    
    public Stack<ArrayList<Double>> getSizeStack() 
    {
        return sizeStack;
    }
}
