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
import jcd.data.JClassDesignerMode;
import jcd.gui.Workspace;
import saf.ui.AppMessageDialogSingleton;

/**
 * This class implements the undo functionality for the JClassDesigner.
 * @author RaniSons
 */
public class UndoManager 
{
    public static final String UNDO_ERROR_TITLE = "Undo";
    public static final String UNDO_ERROR_MESSAGE = "There is no action to undo!";
    
    Stack<String> undoStack;
    Stack<ClassObject> selectedObjectStack;
    
    // HERE'S THE FULL APP, WHICH GIVES US ACCESS TO OTHER STUFF
    JClassDesigner app;
    DataManager dataManager;
    
    /**
     * Constructor to set up the application.
     * @param initApp 
     * the application itself.
     */
    public UndoManager(JClassDesigner initApp) 
    {
	// KEEP IT FOR LATER
        
	app = initApp;
        dataManager = (DataManager)app.getDataComponent();
        // Also init the undo stack
        undoStack = new Stack();
        selectedObjectStack = new Stack();
    }
    
    /**
     * This method pushes on to the stack the last action a user made.
     * @param command 
     * the command that the user just performed
     */
    public void push(Command command)
    {
        undoStack.push(command.toString());
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
        if (!undoStack.isEmpty())
        {
            redoManager().push(getOpposite(Command.valueOf(undoStack.peek())));
            return undoStack.pop();
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
            redoManager().pushClassObject(selectedObjectStack.peek());
            return selectedObjectStack.pop();
        }
        return null;
    }
    
    /**
     * This method gets called when undo button is clicked.
     */
    public void undo()
    {
        String actionToUndo = pop();
        
        if (actionToUndo != null)
            performAction(Command.valueOf(actionToUndo));
        else
        {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(UNDO_ERROR_TITLE, UNDO_ERROR_MESSAGE);
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
    
    
    
    
    
    
    
    
    
    
    /**
     * This method returns the opposite of the command passed as the argument.
     * It helps in pushing to the redo stack the right commands.
     * @param command
     * the command whose opposite is needed
     * @return 
     * the opposite command
     */
    public Command getOpposite(Command command)
    {
        switch (command)
        {
            case ADD_CLASS_OBJECT:
                return Command.REMOVE_CLASS_OBJECT;
            case REMOVE_CLASS_OBJECT:
                return Command.ADD_CLASS_OBJECT;
            case ZOOM_IN:
                return Command.ZOOM_OUT;
            case ZOOM_OUT:
                return Command.ZOOM_IN;
            case GRID_RENDER:
                return Command.GRID_UNRENDER;
            case GRID_UNRENDER:
                return Command.GRID_RENDER;
            default: 
                return null;
        }
    }
    
    /**
     * Helper method to get the redoManager quickly.
     * @return 
     * the redo manager
     */
    private RedoManager redoManager()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        return workspace.getRedoManager();
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

    public Stack<String> getUndoStack() 
    {
        return undoStack;
    }

    public Stack<ClassObject> getSelectedObjectStack() 
    {
        return selectedObjectStack;
    }
}
