/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import jcd.JClassDesigner;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.gui.Workspace;

/**
 *
 * @author RaniSons
 */
public class PageEditController 
{
    // HERE'S THE FULL APP, WHICH GIVES US ACCESS TO OTHER STUFF
    JClassDesigner app;
    DataManager dataManager;
    
    /**
     * Constructor to set up the application.
     * @param initApp 
     * the application itself.
     */
    public PageEditController(JClassDesigner initApp) 
    {
	// KEEP IT FOR LATER
	app = initApp;
        dataManager = (DataManager)app.getDataComponent();
    }
    
    // Page Edit Requests
    
    public void handleSelectionRequest()
    {
        
    }
    
    public void handleUnselectRequest()
    {
        
    }
    
    public void handleAddClassRequest(ClassObject obj) 
    {
         Workspace workspace = (Workspace) app.getWorkspaceComponent();
         Pane canvas = workspace.getCanvas();
         canvas.getChildren().add(obj.getRectangle());
         dataManager.addClassObject(obj);
         System.out.println(dataManager.getClassesList().size());
         app.getGUI().updateToolbarControls(false);
         workspace.reloadWorkspace();
    }
    
}
