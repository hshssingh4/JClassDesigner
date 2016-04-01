/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import jcd.PropertyType;
import static jcd.PropertyType.SELECTION_TOOL_ICON;
import static jcd.PropertyType.SELECTION_TOOL_TOOLTIP;
import jcd.controller.PageEditController;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import saf.ui.AppGUI;

/**
 *
 * @author RaniSons
 */
public class Workspace extends AppWorkspaceComponent
{
    // HERE'S THE APP
    AppTemplate app;
    
    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;
    
    // Flow pane for edit toolbar buttons and view toolbar buttons
    FlowPane editToolbar;
    FlowPane viewToolbar;
    
    // Buttons for edit toolbar
    Button selectionToolButton, resizeButon, addClassButton, addInterfaceButton, 
            removeButton, undoButton, redoButton;
    
    // Buttons and other controls for view toolbar
    Button zoomInButton, zoomOutButton;
    CheckBox gridRenderCheckBox, gridSnapCheckBox;
    
    
    
    
    public Workspace(AppTemplate initApp) throws IOException 
    {
	// KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();
        
        initEditToolbar();
        
        
        
        
        
    }
    
    private void initEditToolbar()
    {
        selectionToolButton = gui.initChildButton(editToolbar, SELECTION_TOOL_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), false);
        
    }
    
    @Override
    public void initStyle() 
    {
    }
    
    @Override
    public void reloadWorkspace() 
    {
    }
}
