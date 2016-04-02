/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

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
    
    public DataManager(AppTemplate initApp) throws Exception 
    {
	// KEEP THE APP FOR LATER
	app = initApp;
    }
    
    @Override
    public void reset()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
    }
}
