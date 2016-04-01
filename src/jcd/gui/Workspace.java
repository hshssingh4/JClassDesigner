/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.io.IOException;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import static jcd.PropertyType.ADD_CLASS_ICON;
import static jcd.PropertyType.ADD_CLASS_TOOLTIP;
import static jcd.PropertyType.ADD_INTERFACE_ICON;
import static jcd.PropertyType.ADD_INTERFACE_TOOLTIP;
import static jcd.PropertyType.REDO_ICON;
import static jcd.PropertyType.REDO_TOOLTIP;
import static jcd.PropertyType.REMOVE_ICON;
import static jcd.PropertyType.REMOVE_TOOLTIP;
import static jcd.PropertyType.RESIZE_ICON;
import static jcd.PropertyType.RESIZE_TOOLTIP;
import static jcd.PropertyType.SELECTION_TOOL_ICON;
import static jcd.PropertyType.SELECTION_TOOL_TOOLTIP;
import static jcd.PropertyType.UNDO_ICON;
import static jcd.PropertyType.UNDO_TOOLTIP;
import static jcd.PropertyType.ZOOM_IN_ICON;
import static jcd.PropertyType.ZOOM_IN_TOOLTIP;
import static jcd.PropertyType.ZOOM_OUT_ICON;
import static jcd.PropertyType.ZOOM_OUT_TOOLTIP;
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
    FlowPane editToolbarPane;
    FlowPane viewToolbarPane;
    
    // Buttons for edit toolbar
    Button selectionToolButton, resizeButton, addClassButton, addInterfaceButton, 
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
        initViewToolbar();
        
    }
    
    private void initEditToolbar()
    {
        // Initialize edit toolbar
        editToolbarPane = new FlowPane();
        editToolbarPane.setHgap(5);
        editToolbarPane.setVgap(5);
        
        // Add buttons to edit toolbar
        selectionToolButton = gui.initChildButton(editToolbarPane, SELECTION_TOOL_ICON.toString(), SELECTION_TOOL_TOOLTIP.toString(), true);
        resizeButton        = gui.initChildButton(editToolbarPane, RESIZE_ICON.toString(), RESIZE_TOOLTIP.toString(), true);
        addClassButton      = gui.initChildButton(editToolbarPane, ADD_CLASS_ICON.toString(), ADD_CLASS_TOOLTIP.toString(), true);
        addInterfaceButton  = gui.initChildButton(editToolbarPane, ADD_INTERFACE_ICON.toString(), ADD_INTERFACE_TOOLTIP.toString(), true);
        removeButton        = gui.initChildButton(editToolbarPane, REMOVE_ICON.toString(), REMOVE_TOOLTIP.toString(), true);
        undoButton          = gui.initChildButton(editToolbarPane, UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), true);
        redoButton          = gui.initChildButton(editToolbarPane, REDO_ICON.toString(), REDO_TOOLTIP.toString(), true);
        
        // put it on top of the pane alongside file toolbar
        FlowPane topPane = (FlowPane) app.getGUI().getAppPane().getTop();
        topPane.getChildren().add(editToolbarPane);
    }
    
    private void initViewToolbar()
    {
        viewToolbarPane = new FlowPane();
        viewToolbarPane.setHgap(5);
        viewToolbarPane.setVgap(5);
        
        zoomInButton = gui.initChildButton(viewToolbarPane, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), true);
        zoomOutButton = gui.initChildButton(viewToolbarPane, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), true);
        
        gridRenderCheckBox = new CheckBox("Grid");
        gridSnapCheckBox = new CheckBox("Snap");
        VBox checkBoxes = new VBox(5);
        checkBoxes.getChildren().addAll(gridRenderCheckBox, gridSnapCheckBox);
        checkBoxes.setDisable(true);
        
        viewToolbarPane.getChildren().add(checkBoxes);
        
        // put it on top of the pane alongside file toolbar
        FlowPane topPane = (FlowPane) app.getGUI().getAppPane().getTop();
        topPane.getChildren().add(viewToolbarPane);
    }
    
    @Override
    public void initStyle() 
    {
        for (Node b: editToolbarPane.getChildren())
            ((Button)b).getStyleClass().add(CLASS_FILE_BUTTON);
        
        zoomInButton.getStyleClass().add(CLASS_FILE_BUTTON);
        zoomOutButton.getStyleClass().add(CLASS_FILE_BUTTON);
        System.out.print((Font.getFamilies()));
        gridRenderCheckBox.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, FontPosture.REGULAR, 12));
        gridSnapCheckBox.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, FontPosture.REGULAR, 12));
    }
    
    @Override
    public void reloadWorkspace() 
    {
        enforceLegalButtons();
    }
    
    private void enforceLegalButtons()
    {
        for (Node b: editToolbarPane.getChildren())
            b.setDisable(false);
        
        for (Node b: viewToolbarPane.getChildren())
            b.setDisable(false);
    }
}
