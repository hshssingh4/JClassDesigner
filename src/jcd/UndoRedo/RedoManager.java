/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.UndoRedo;

import java.util.Stack;
import jcd.JClassDesigner;
import jcd.data.ClassObject;
import jcd.data.DataManager;
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
     * This method gets called when redo button is clicked.
     */
    public void redo()
    {
        String actionToRedo = pop();
        
        if (actionToRedo != null)
        {
            performAction(Command.valueOf(actionToRedo));
        }
        else
        {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(REDO_ERROR_TITLE, REDO_ERROR_MESSAGE);
        }
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
            default:
                break;
        }
    }
    
    private void removeClassObject()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        dataManager.removeClassObject(popClassObject());
        workspace.reloadWorkspace();
    }
    
    private void addClassObject()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        dataManager.addClassObject(popClassObject());
        workspace.reloadWorkspace();
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

    public Stack<String> getRedoStack() 
    {
        return redoStack;
    }

    public Stack<ClassObject> getSelectedObjectStack() 
    {
        return selectedObjectStack;
    }
}
