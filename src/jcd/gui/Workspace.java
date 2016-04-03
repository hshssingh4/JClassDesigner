/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.io.IOException;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jcd.JClassDesigner;
import static jcd.PropertyType.ADD_CLASS_ICON;
import static jcd.PropertyType.ADD_CLASS_TOOLTIP;
import static jcd.PropertyType.ADD_INTERFACE_ICON;
import static jcd.PropertyType.ADD_INTERFACE_TOOLTIP;
import static jcd.PropertyType.ADD_METHOD_TOOLTIP;
import static jcd.PropertyType.ADD_VARIABLE_TOOLTIP;
import static jcd.PropertyType.MINUS_ICON;
import static jcd.PropertyType.PLUS_ICON;
import static jcd.PropertyType.REDO_ICON;
import static jcd.PropertyType.REDO_TOOLTIP;
import static jcd.PropertyType.REMOVE_ICON;
import static jcd.PropertyType.REMOVE_METHOD_TOOLTIP;
import static jcd.PropertyType.REMOVE_TOOLTIP;
import static jcd.PropertyType.REMOVE_VARIABLE_TOOLTIP;
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
import jcd.controller.CanvasEditController;
import jcd.controller.PageEditController;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.JClassDesignerState;
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
    
    // Center pane for the canvas that is placed inside the scroll pane
    Pane canvas;
    ScrollPane canvasScrollPane;
    
    // VBox for the component toolbar
    VBox componentToolbar;
    
    // Flow pane for edit toolbar buttons and view toolbar buttons
    FlowPane editToolbarPane;
    FlowPane viewToolbarPane;
    
    // Buttons for edit toolbar
    Button selectionToolButton, resizeButton, addClassButton, addInterfaceButton, 
            removeButton, undoButton, redoButton;
    
    // Buttons and other controls for view toolbar
    Button zoomInButton, zoomOutButton;
    CheckBox gridRenderCheckBox, gridSnapCheckBox;
    
    // Controls for the component toolbar
    // Grid Pane for first three elements
    GridPane gridPane;
    
    // First row
    Label classNameLabel;
    TextField classNameTextField;
    
    // Second row
    Label packageLabel;
    TextField packageTextField;
    
    // Third row
    Label parentLabel;
    ComboBox<String> parentComboBox;
    
    // Fourth row
    VBox variablesVBox;
    HBox variablesHBox;
    Label variablesLabel;
    Button addVariableButton;
    Button removeVariableButton;
    TableView variablesTableView;
    
    // Fifth row
    VBox methodsVBox;
    HBox methodsHBox;
    Label methodsLabel;
    Button addMethodButton;
    Button removeMethodButton;
    TableView methodsTableView;
    
    // Selected Object
    Object selectedObject;
    
    // HERE IS THE CONTROLLER
    PageEditController pageEditController;
    CanvasEditController canvasEditController;
    DataManager dataManager;
    
    public Workspace(AppTemplate initApp) throws IOException 
    {
	// KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();
        pageEditController = new PageEditController((JClassDesigner) app);
        canvasEditController = new CanvasEditController((JClassDesigner) app);
        dataManager = (DataManager) app.getDataComponent();
        
        initEditToolbar();
        initViewToolbar();
        initComponentToolbar();
        initCanvas();
        initGUI();
        initHandlers();
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
    
    private void initCanvas()
    {
        canvas = new Pane();
        canvasScrollPane = new ScrollPane(canvas);
        canvasScrollPane.setFitToHeight(true);
        canvasScrollPane.setFitToWidth(true);
        canvas.setStyle("-fx-background-color: skyblue;"); // DELETE
    }
    
    private void initComponentToolbar()
    {
        componentToolbar = new VBox(20);
        componentToolbar.setMaxWidth(300);
        initGridPane();
        initVariablesRow();
        initMethodsRow();
        
        componentToolbar.getChildren().add(gridPane);
        componentToolbar.getChildren().add(variablesVBox);
        componentToolbar.getChildren().add(methodsVBox);
    }
    
    private void initGridPane()
    {
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // First row
        classNameLabel = new Label("Class Name:");
        classNameLabel.setMinWidth(Region.USE_PREF_SIZE);
        classNameLabel.setMaxWidth(Region.USE_PREF_SIZE);
        classNameTextField = new TextField();
        classNameTextField.setDisable(true);
        classNameTextField.setAlignment(Pos.BOTTOM_RIGHT);
        classNameTextField.setPromptText("Enter Class Name");
        
        // Second row
        packageLabel = new Label("Package:");
        packageTextField = new TextField();
        packageTextField.setDisable(true);
        packageTextField.setAlignment(Pos.BOTTOM_RIGHT);
        packageTextField.setPromptText("Enter Package Name");
        
        // Third row
        parentLabel = new Label("Parent:");
        parentComboBox = new ComboBox<>();
        parentComboBox.setMaxWidth(Double.MAX_VALUE);
        parentComboBox.setTooltip(new Tooltip("Choose Parent"));
        
        // Now add these rows to the grid pane
        gridPane.add(classNameLabel, 0, 0);
        gridPane.add(classNameTextField, 1, 0);
        gridPane.add(packageLabel, 0, 1);
        gridPane.add(packageTextField, 1, 1);
        gridPane.add(parentLabel, 0, 2);
        gridPane.add(parentComboBox, 1, 2);
        
        // Align the combo box to the right of the grid pane
        GridPane.setHalignment(parentComboBox, HPos.RIGHT);
    }
    
    private void initVariablesRow()
    {
        variablesVBox = new VBox(5);
        
        variablesHBox = new HBox(10);
        variablesHBox.setAlignment(Pos.CENTER_LEFT);
        variablesLabel = new Label("Variables:");
        variablesHBox.getChildren().add(variablesLabel);
        addVariableButton = gui.initChildButton(variablesHBox, PLUS_ICON.toString(),
                ADD_VARIABLE_TOOLTIP.toString(), false);
        removeVariableButton = gui.initChildButton(variablesHBox, MINUS_ICON.toString(),
                REMOVE_VARIABLE_TOOLTIP.toString(), false);
        
        variablesTableView = new TableView();
        variablesTableView.setPrefHeight(200);

        TableColumn variableNameCol = new TableColumn("Name");
        TableColumn variableTypeCol = new TableColumn("Type");
        TableColumn variableStaticCol = new TableColumn("Static");
        TableColumn variableAccessCol = new TableColumn("Access");
        
        variablesTableView.getColumns().addAll(variableNameCol, variableTypeCol, variableStaticCol, variableAccessCol);
        
        variablesVBox.getChildren().addAll(variablesHBox, variablesTableView);
    }
    
    private void initMethodsRow()
    {
        methodsVBox = new VBox(5);
        
        methodsHBox = new HBox(10);
        methodsHBox.setAlignment(Pos.CENTER_LEFT);
        methodsLabel = new Label("Methods:");
        methodsHBox.getChildren().add(methodsLabel);
        addMethodButton = gui.initChildButton(methodsHBox, PLUS_ICON.toString(), ADD_METHOD_TOOLTIP.toString(), false);
        removeMethodButton = gui.initChildButton(methodsHBox, MINUS_ICON.toString(), REMOVE_METHOD_TOOLTIP.toString(), false);
        
        methodsTableView = new TableView();
        methodsTableView.setPrefHeight(200);
        TableColumn variableNameCol = new TableColumn("Name");
        TableColumn variableReturnCol = new TableColumn("Return");
        TableColumn variableTypeCol = new TableColumn("Type");
        TableColumn variableAbstractCol = new TableColumn("Abstract");
        TableColumn variableStaticCol = new TableColumn("Static");
        TableColumn variableAccessCol = new TableColumn("Access");
        
        methodsTableView.getColumns().addAll(variableNameCol, variableReturnCol,
                variableTypeCol, variableAbstractCol, variableStaticCol, variableAccessCol);
        
        methodsVBox.getChildren().addAll(methodsHBox, methodsTableView);
    }
    
    private void initGUI()
    {
        // AND NOW SETUP THE WORKSPACE
	workspace = new BorderPane();
	((BorderPane)workspace).setCenter(canvasScrollPane);
	((BorderPane)workspace).setRight(componentToolbar);
    }
    
    private void initHandlers()
    {
        selectionToolButton.setOnAction(e -> {
            pageEditController.handleSelectionToolButtonRequest();
        });
        resizeButton.setOnAction(e -> {
            pageEditController.handleResizeButtonRequest();
        });
        addClassButton.setOnAction(e -> {
            pageEditController.handleAddClassRequest();
        });
        canvas.setOnMousePressed((MouseEvent e) -> {
            if (dataManager.isInState(JClassDesignerState.SELECTING_SHAPE))
            {
                if(e.getTarget() instanceof VBox)
                {
                    for (ClassObject obj: dataManager.getClassesList())
                        if ( obj.getRectanglesBox().getClassNameTextVBox() == (VBox)e.getTarget()
                                || obj.getRectanglesBox().getVariablesTextVBox() == ((VBox)e.getTarget())
                                || obj.getRectanglesBox().getMethodsTextVBox() == ((VBox) e.getTarget()))
                                canvasEditController.handleSelectionRequest(obj);
                    
                    
                    double initialX = ((ClassObject)selectedObject).getRectanglesBox().getStackPanesVBox().getTranslateX(); /* This code only works for now*/
                    double initialY = ((ClassObject)selectedObject).getRectanglesBox().getStackPanesVBox().getTranslateY(); /* This code only works for now*/
                    canvas.setOnMouseDragged((MouseEvent e1) -> {
                        canvasEditController.handlePositionChangeRequest(selectedObject, e1, e, initialX, initialY);
                    });
                }
                else
                    canvasEditController.handleUnselectRequest();
            }
        });
        classNameTextField.textProperty().addListener((observable, oldClassName, newClassName) -> {
            if (selectedObject != null)
                pageEditController.handleClassNameChangeRequest(selectedObject, newClassName);
        });
        packageTextField.textProperty().addListener((observable, oldClassName, newClassName) -> {
            if (selectedObject != null) 
                pageEditController.handlePackageNameChangeRequest(selectedObject, newClassName);
        });
    }
    
    @Override
    public void initStyle() 
    {
        // For Edit Toolbar
        for (Node b: editToolbarPane.getChildren())
            ((Button)b).getStyleClass().add(CLASS_FILE_BUTTON);
        
        // For View Toolbar
        zoomInButton.getStyleClass().add(CLASS_FILE_BUTTON);
        zoomOutButton.getStyleClass().add(CLASS_FILE_BUTTON);
        gridRenderCheckBox.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, FontPosture.REGULAR, 12));
        gridSnapCheckBox.setFont(Font.font("Helvetica Neue", FontWeight.BOLD, FontPosture.REGULAR, 12));
        
        // For Component Toolbar
        classNameLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        packageLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        parentLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        parentComboBox.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        gridPane.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        
        variablesLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addVariableButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        removeVariableButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        variablesVBox.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        
        methodsLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addMethodButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        removeMethodButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        methodsVBox.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        
        componentToolbar.getStyleClass().add(CLASS_COMPONENT_TOOLBAR);
    }
    
    @Override
    public void reloadWorkspace() 
    {
        if (selectedObject != null)
        {
            if (selectedObject instanceof ClassObject)
            {
                ClassObject obj = (ClassObject) selectedObject;
                classNameTextField.setText(obj.getClassName());
                packageTextField.setText(obj.getPackageName());
            }
        }
        else
        {
            classNameTextField.clear();
            packageTextField.clear();
        }
        
        // Now load all the childrens again
        //ArrayList
        
        enableLegalButtons();
    }
    
    private void enableLegalButtons()
    {
        for (Node b: editToolbarPane.getChildren())
            b.setDisable(false);
        
        for (Node b: viewToolbarPane.getChildren())
            b.setDisable(false);
        
        if (selectedObject != null)
        {
            classNameTextField.setDisable(false);
            packageTextField.setDisable(false);
        }
        else
        {
            classNameTextField.setDisable(true);
            packageTextField.setDisable(true);      
        }
    }

    public Pane getCanvas() 
    {
        return canvas;
    }

    public ScrollPane getCanvasScrollPane() 
    {
        return canvasScrollPane;
    }

    public Object getSelectedObject()
    {
        return selectedObject;
    }

    public void setSelectedObject(Object selectedObject) 
    {
        this.selectedObject = selectedObject;
    }

    public TextField getClassNameTextField() 
    {
        return classNameTextField;
    }

    public TextField getPackageTextField() 
    {
        return packageTextField;
    }

    public CanvasEditController getCanvasEditController() 
    {
        return canvasEditController;
    }
}
