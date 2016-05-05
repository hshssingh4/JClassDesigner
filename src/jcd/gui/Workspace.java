/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.io.IOException;
import java.util.ArrayList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import jcd.JClassDesigner;
import static jcd.PropertyType.ADD_CLASS_ICON;
import static jcd.PropertyType.ADD_CLASS_TOOLTIP;
import static jcd.PropertyType.ADD_INTERFACE_ICON;
import static jcd.PropertyType.ADD_INTERFACE_TOOLTIP;
import static jcd.PropertyType.ADD_METHOD_TOOLTIP;
import static jcd.PropertyType.ADD_VARIABLE_TOOLTIP;
import static jcd.PropertyType.DEFAULT_ZOOM_ICON;
import static jcd.PropertyType.DEFAULT_ZOOM_TOOLTIP;
import static jcd.PropertyType.EDIT_ICON;
import static jcd.PropertyType.EDIT_METHOD_TOOLTIP;
import static jcd.PropertyType.EDIT_VARIABLE_TOOLTIP;
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
import jcd.controller.LineEditController;
import jcd.controller.PageEditController;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import static jcd.data.JClassDesignerMode.GRID_DEFAULT_MODE;
import static jcd.data.JClassDesignerMode.GRID_RENDER_MODE;
import static jcd.data.JClassDesignerMode.GRID_RENDER_SNAP_MODE;
import jcd.data.JClassDesignerState;
import jcd.data.LineConnector;
import jcd.data.LineConnectorType;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import saf.ui.AppGUI;

/**
 * This class basically sets up the entire workspace for the application. It 
 * initializes all the UI components along with their handlers and therefore
 * serves as the core base of this app.
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
    ScrollPane componentToolbarScrollPane;
    VBox componentToolbar;
    
    // Flow pane for edit toolbar buttons and view toolbar buttons
    FlowPane editToolbarPane;
    FlowPane viewToolbarPane;
    
    // Buttons for edit toolbar
    Button selectionToolButton, resizeButton, addClassButton, addInterfaceButton, 
            removeButton, undoButton, redoButton;
    
    // Buttons and other controls for view toolbar
    Button zoomInButton, zoomOutButton, defaultZoomButton;
    CheckBox gridRenderCheckBox, gridSnapCheckBox;
    
    // Controls for the component toolbar
    // Grid Pane for first three elements
    GridPane gridPane;
    
    // First row
    Label classNameLabel;
    TextField classNameTextField;
    
    // Second row
    Label packageLabel;
    TextField packageNameTextField;
    
    // Third row (Parent)
    ToggleGroup parentClassGroup;
    RadioButton noParentClassRadioButton;
    RadioButton localParentClassRadioButton;
    ComboBox<String> parentClassComboBox;
    RadioButton apiParentClassRadioButton;
    TextField parentClassTextField;
    //Third Row (Interfaces)
    Label localInterfacesLabel;
    Button addLocalInterfacesButton;
    Label apiInterfacesLabel;
    Button addApiInterfacesButton;
    
    
    // Fourth row
    VBox variablesVBox;
    HBox variablesHBox;
    Label variablesLabel;
    Button addVariableButton;
    Button removeVariableButton;
    Button editVariableButton;
    TableView variablesTableView;
    
    // Fifth row
    VBox methodsVBox;
    HBox methodsHBox;
    Label methodsLabel;
    Button addMethodButton;
    Button removeMethodButton;
    Button editMethodButton;
    TableView methodsTableView;
    
    // Selected Object
    ClassObject selectedObject;
    Line selectedLine;
    Line selectedLine2;
    
    // HERE IS THE CONTROLLER
    PageEditController pageEditController;
    CanvasEditController canvasEditController;
    LineEditController lineEditController;
    DataManager dataManager;
    
    /**
     * This is the constructor that gets called for the SAF and its job is to
     * construct the workspace for this application.
     * @param initApp
     * the actual application
     * @throws IOException 
     * if error occurs while writing to file (not really necessary!)
     */
    public Workspace(AppTemplate initApp) throws IOException 
    {
	// KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();
        pageEditController = new PageEditController((JClassDesigner) app);
        canvasEditController = new CanvasEditController((JClassDesigner) app);
        lineEditController = new LineEditController((JClassDesigner) app);
        dataManager = (DataManager) app.getDataComponent();
        
        // NOW INITIALIZE EVERYTHING ONE BY ONE
        initEditToolbar();
        initViewToolbar();
        initComponentToolbar();
        initCanvas();
        initGUI();
        initHandlers();
    }
    
    /**
     * This method helps initialize the UI elements in the Edit toolbar
     * which is the toolbar in the middle of the file and view toolbars.
     * It initializes elements like selection button, resize button, etc and
     * puts them into the edit toolbar pane which gets put into the bigger
     * flow pane we have at the top.
     */
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
    
    /**
     * This method helps initialize the rightmost toolbar which contains elements
     * like zoom in, out, and several others.
     */
    private void initViewToolbar()
    {
        // Initialize the view toolbar pane first.
        viewToolbarPane = new FlowPane();
        viewToolbarPane.setHgap(5);
        viewToolbarPane.setVgap(5);
        
        // Then the buttons that are put into this toolbar.
        zoomInButton = gui.initChildButton(viewToolbarPane, ZOOM_IN_ICON.toString(), ZOOM_IN_TOOLTIP.toString(), true);
        zoomOutButton = gui.initChildButton(viewToolbarPane, ZOOM_OUT_ICON.toString(), ZOOM_OUT_TOOLTIP.toString(), true);
        defaultZoomButton = gui.initChildButton(viewToolbarPane, DEFAULT_ZOOM_ICON.toString(), DEFAULT_ZOOM_TOOLTIP.toString(), true);
        
        // And then the check boxes.
        gridRenderCheckBox = new CheckBox("Grid");
        gridSnapCheckBox = new CheckBox("Snap");
        VBox checkBoxes = new VBox(5);
        checkBoxes.getChildren().addAll(gridRenderCheckBox, gridSnapCheckBox);
        checkBoxes.setDisable(true);
        
        // Then put them into the view toolbar pane.
        viewToolbarPane.getChildren().add(checkBoxes);
        
        // Put it on top of the pane alongside file toolbar.
        FlowPane topPane = (FlowPane) app.getGUI().getAppPane().getTop();
        topPane.getChildren().add(viewToolbarPane);
    }
    
    /**
     * This methods helps initialize the canvas that basically is the area
     * where UML diagrams are rendered.
     */
    private void initCanvas()
    {
        canvas = new Pane();
        canvasScrollPane = new ScrollPane(canvas);
        canvasScrollPane.setFitToHeight(true);
        canvasScrollPane.setFitToWidth(true);
    }
    
    /**
     * This method helps initialize the component toolbar in the right
     * which has all the controls for editing a particular UML diagram.
     */
    private void initComponentToolbar()
    {
        componentToolbar = new VBox(20);
        componentToolbar.setPrefWidth(400);
        initGridPane();
        initVariablesRow();
        initMethodsRow();
        
        componentToolbar.getChildren().add(gridPane);
        componentToolbar.getChildren().add(variablesVBox);
        componentToolbar.getChildren().add(methodsVBox);
        
        componentToolbarScrollPane = new ScrollPane(componentToolbar);
        componentToolbarScrollPane.setPrefWidth(420);
    }
    
    /**
     * Helper method to initialize the grid pane inside the component toolbar.
     */
    private void initGridPane()
    {
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ColumnConstraints columnConstraint = new ColumnConstraints();
        columnConstraint.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(columnConstraint);
        
        // First row
        classNameLabel = new Label("Class Name:");
        classNameTextField = new TextField();
        classNameTextField.setDisable(true);
        classNameTextField.setAlignment(Pos.BOTTOM_LEFT);
        classNameTextField.setPromptText("Enter Class Name");
        
        // Second row
        packageLabel = new Label("Package:");
        packageNameTextField = new TextField();
        packageNameTextField.setDisable(true);
        packageNameTextField.setAlignment(Pos.BOTTOM_LEFT);
        packageNameTextField.setPromptText("Enter Package Name");
        
        // Third row
        parentClassGroup = new ToggleGroup();
        
        noParentClassRadioButton = new RadioButton("No Parent");
        noParentClassRadioButton.setToggleGroup(parentClassGroup);
        
        localParentClassRadioButton = new RadioButton("Local Parent:");
        localParentClassRadioButton.setToggleGroup(parentClassGroup);
        parentClassComboBox = new ComboBox<>();
        parentClassComboBox.setMaxWidth(Double.MAX_VALUE);
        parentClassComboBox.setTooltip(new Tooltip("Choose Local Parent Class"));
        
        // Fourth row
        apiParentClassRadioButton = new RadioButton("API Parent:");
        apiParentClassRadioButton.setToggleGroup(parentClassGroup);
        parentClassTextField = new TextField();
        parentClassTextField.setDisable(true);
        parentClassTextField.setAlignment(Pos.BOTTOM_LEFT);
        parentClassTextField.setPromptText("Enter Java API Class");
        
        // Fifth row
        localInterfacesLabel = new Label("Local Interfaces:");
        addLocalInterfacesButton = new Button("Add Local Interfaces");
        addLocalInterfacesButton.setMaxWidth(Double.MAX_VALUE);
        apiInterfacesLabel = new Label("API Interfaces:");
        addApiInterfacesButton = new Button("Add API Interfaces");
        addApiInterfacesButton.setMaxWidth(Double.MAX_VALUE);
        
        // Now add these rows to the grid pane
        gridPane.add(classNameLabel, 0, 0);
        gridPane.add(classNameTextField, 1, 0);
        gridPane.add(packageLabel, 0, 1);
        gridPane.add(packageNameTextField, 1, 1);
        gridPane.add(noParentClassRadioButton, 0, 2);
        gridPane.add(localParentClassRadioButton, 0, 3);
        gridPane.add(parentClassComboBox, 1, 3);
        gridPane.add(apiParentClassRadioButton, 0, 4);
        gridPane.add(parentClassTextField, 1, 4);
        gridPane.add(localInterfacesLabel, 0, 5);
        gridPane.add(addLocalInterfacesButton, 1, 5);
        gridPane.add(apiInterfacesLabel, 0, 6);
        gridPane.add(addApiInterfacesButton, 1, 6);
        
        // Align the combo box to the right of the grid pane
        GridPane.setHalignment(parentClassComboBox, HPos.RIGHT);
        GridPane.setHalignment(addLocalInterfacesButton, HPos.RIGHT);
        GridPane.setHalignment(addApiInterfacesButton, HPos.RIGHT);
    }
    
    /**
     * Helper method to help initialize the variables row inside the component
     * toolbar.
     */
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
        editVariableButton = gui.initChildButton(variablesHBox, EDIT_ICON.toString(),
                EDIT_VARIABLE_TOOLTIP.toString(), false);
        
        variablesTableView = new TableView();
        variablesTableView.setPrefHeight(200);

        TableColumn variableNameCol = new TableColumn("Name");
        TableColumn variableTypeCol = new TableColumn("Type");
        TableColumn variableScopeCol = new TableColumn("Scope");
        TableColumn variableStaticCol = new TableColumn("Static");
        TableColumn variableFinalCol = new TableColumn("Final");
                
        variableNameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        variableTypeCol.setCellValueFactory(
                new PropertyValueFactory<>("type"));
        variableScopeCol.setCellValueFactory(
                new PropertyValueFactory<>("scope"));
        variableStaticCol.setCellValueFactory(
                new PropertyValueFactory<>("isStatic"));
        variableFinalCol.setCellValueFactory(
                new PropertyValueFactory<>("isFinal"));

        variablesTableView.getColumns().addAll(variableNameCol, variableTypeCol,
                variableScopeCol, variableStaticCol, variableFinalCol);
        
        variablesVBox.getChildren().addAll(variablesHBox, variablesTableView);
    }
    
    /**
     * Helper method to help initialize the methods row inside the component toolbar.
     */
    private void initMethodsRow()
    {
        methodsVBox = new VBox(5);
        
        methodsHBox = new HBox(10);
        methodsHBox.setAlignment(Pos.CENTER_LEFT);
        methodsLabel = new Label("Methods:");
        methodsHBox.getChildren().add(methodsLabel);
        addMethodButton = gui.initChildButton(methodsHBox, PLUS_ICON.toString(), 
                ADD_METHOD_TOOLTIP.toString(), false);
        removeMethodButton = gui.initChildButton(methodsHBox, MINUS_ICON.toString(), 
                REMOVE_METHOD_TOOLTIP.toString(), false);
        editMethodButton = gui.initChildButton(methodsHBox, EDIT_ICON.toString(),
                EDIT_METHOD_TOOLTIP.toString(), false);
        
        methodsTableView = new TableView();
        methodsTableView.setPrefHeight(200);
        
        TableColumn methodNameCol = new TableColumn("Name");
        TableColumn methodTypeCol = new TableColumn("Type");
        TableColumn methodScopeCol = new TableColumn("Scope");
        TableColumn methodStaticCol = new TableColumn("Static");
        TableColumn methodFinalCol = new TableColumn("Final");
        TableColumn methodAbstractCol = new TableColumn("Abstract");
                
        methodNameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        methodTypeCol.setCellValueFactory(
                new PropertyValueFactory<>("type"));
        methodScopeCol.setCellValueFactory(
                new PropertyValueFactory<>("scope"));
        methodStaticCol.setCellValueFactory(
                new PropertyValueFactory<>("isStatic"));
        methodFinalCol.setCellValueFactory(
                new PropertyValueFactory<>("isFinal"));
        methodAbstractCol.setCellValueFactory(
                new PropertyValueFactory<>("isAbstract"));

        methodsTableView.getColumns().addAll(methodNameCol, methodTypeCol, methodScopeCol,
                methodStaticCol, methodFinalCol, methodAbstractCol);
        
        methodsVBox.getChildren().addAll(methodsHBox, methodsTableView);
    }
    
    /**
     * This method finally initializes the whole workspace.
     */
    private void initGUI()
    {
        // AND NOW SETUP THE WORKSPACE
	workspace = new BorderPane();
	((BorderPane)workspace).setCenter(canvasScrollPane);
	((BorderPane)workspace).setRight(componentToolbarScrollPane);
    }
    
    /**
     * After initializing all the components, we are ready to initialize the
     * handler for the all the components.
     */
    private void initHandlers()
    {
        initEditToolbarHandlers();
        initViewToolbarHandlers();
        initComponentToolbarHandlers();
        initCanvasEventHandlers();
    }
    
    /**
     * Helper method to initialize handlers for controls in the edit toolbar.
     */
    private void initEditToolbarHandlers()
    {
        selectionToolButton.setOnAction(e -> {
            pageEditController.handleSelectionToolButtonRequest();
        });
        resizeButton.setOnAction(e -> {
            pageEditController.handleResizeButtonRequest();
        });
        addClassButton.setOnAction(e -> {
            int randomInt = (int) (Math.random() * 100);
            String randomClassNameString = "DummyClass" + randomInt;
            pageEditController.handleAddClassRequest(randomClassNameString);
        });
        addInterfaceButton.setOnAction(e -> {
            pageEditController.handleAddInterfaceRequest();
        });
        removeButton.setOnAction(e -> {
            pageEditController.handleRemoveClassObjectRequest();
        });
    }
    
    /**
     * Helper method to initialize handler for controls in the view toolbar.
     */
    private void initViewToolbarHandlers()
    {
        zoomInButton.setOnAction(e -> {
            canvasEditController.handleZoomInRequest();
        });
        zoomOutButton.setOnAction(e -> {
            canvasEditController.handleZoomOutRequest();
        });
        defaultZoomButton.setOnAction(e -> {
            canvasEditController.handleDefaultZoomRequest();
        });
        gridRenderCheckBox.setOnAction(e -> {
            if (gridRenderCheckBox.isSelected())
                dataManager.setMode(GRID_RENDER_MODE);
            else
                dataManager.setMode(GRID_DEFAULT_MODE);
            reloadWorkspace();
        });
        gridSnapCheckBox.setOnAction(e -> {
            if (gridSnapCheckBox.isSelected())
            {
                dataManager.setMode(GRID_RENDER_SNAP_MODE);
                canvasEditController.handleSnapRequest();
            }
            else
                dataManager.setMode(GRID_RENDER_MODE);
        });
    }
    
    /**
     * Helper method to initialize handlers for controls in the component toolbar.
     */
    private void initComponentToolbarHandlers()
    {
        classNameTextField.textProperty().addListener((observable, oldClassName, newClassName) -> {
            if (selectedObject != null)
                pageEditController.handleClassNameChangeRequest(newClassName);
        });
        packageNameTextField.textProperty().addListener((observable, oldClassName, newClassName) -> {
            if (selectedObject != null)
                pageEditController.handlePackageNameChangeRequest(newClassName);
        });
        noParentClassRadioButton.setOnAction(e -> {
            parentClassComboBox.setDisable(true);
            parentClassTextField.setDisable(true);
            parentClassComboBox.getItems().clear();
            parentClassTextField.clear();
            pageEditController.handleParentClassRequest(null);
            selectedObject.getBox().removeParentLineConnector();
            reloadWorkspace();
        });
        localParentClassRadioButton.setOnAction(e -> {
            parentClassComboBox.setDisable(false);
            parentClassTextField.setDisable(true);
            parentClassComboBox.getItems().clear();
            parentClassTextField.clear();
            for (ClassObject obj : dataManager.getClassesList())
                if (!obj.equals(selectedObject) && !obj.isInterfaceType()
                        && !parentClassComboBox.getItems().contains(obj.getClassName()))
                    parentClassComboBox.getItems().add(obj.getClassName());
        });
        apiParentClassRadioButton.setOnAction(e -> {
            parentClassComboBox.setDisable(true);
            parentClassTextField.setDisable(false);
            parentClassComboBox.getItems().clear();
            parentClassTextField.clear();
        });
        parentClassComboBox.setOnAction(e -> {
            if (parentClassComboBox.getValue() != null)
                pageEditController.handleParentClassRequest(parentClassComboBox.getValue());
        });
        parentClassTextField.textProperty().addListener((observable, oldClassName, newParentName) -> {
            if (!newParentName.isEmpty())
                pageEditController.handleParentClassRequest(newParentName);
        });
        addLocalInterfacesButton.setOnAction(e -> {
            pageEditController.handleOpenLocalInterfacesDialogRequest();
        });
        addApiInterfacesButton.setOnAction(e -> {
            pageEditController.handleOpenApiInterfacesDialogRequest();
        });
        addVariableButton.setOnAction(e -> {
            pageEditController.handleOpenVariableDialogRequest(null);
        });
        editVariableButton.setOnAction(e -> {
            int selectedIndex = variablesTableView.getSelectionModel().getSelectedIndex();
            VariableObject variable = selectedObject.getVariables().get(selectedIndex);
            pageEditController.handleOpenVariableDialogRequest(variable);
        });
        removeVariableButton.setOnAction(e -> {
            int selectedIndex = variablesTableView.getSelectionModel().getSelectedIndex();
            VariableObject variable = selectedObject.getVariables().get(selectedIndex);
            pageEditController.handleRemoveVariableRequest(variable);
        });
        variablesTableView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            removeVariableButton.setDisable(false);
            editVariableButton.setDisable(false);
        });
        addMethodButton.setOnAction(e -> {
            pageEditController.handleOpenMethodDialogRequest(null);
        });
        editMethodButton.setOnAction(e -> {
            int selectedIndex = methodsTableView.getSelectionModel().getSelectedIndex();
            MethodObject method = selectedObject.getMethods().get(selectedIndex);
            pageEditController.handleOpenMethodDialogRequest(method);
        });
        removeMethodButton.setOnAction(e -> {
            int selectedIndex = methodsTableView.getSelectionModel().getSelectedIndex();
            MethodObject method = selectedObject.getMethods().get(selectedIndex);
            pageEditController.handleRemoveMethodRequest(method);
        });
        methodsTableView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            removeMethodButton.setDisable(false);
            editMethodButton.setDisable(false);
        });
    }
    
    /**
     * Helper method to initialize all the event handlers associated with events
     * related to the canvas directly. Example selection of a box, resizing etc.
     */
    private void initCanvasEventHandlers()
    {
        canvas.setOnMouseMoved(e -> {
            if (dataManager.isInState(JClassDesignerState.RESIZING_SHAPE))
                canvasEditController.handleCheckResizeRequest((int) e.getX(), (int) e.getY());
        });
        canvas.setOnMousePressed((MouseEvent e) -> {
            if (dataManager.isInState(JClassDesignerState.SELECTING_SHAPE))
            {
                if (e.getClickCount() == 1)
                    canvasEditController.handleSelectionRequest(e.getX(), e.getY());
                if (e.getClickCount() == 2)
                {
                    canvasEditController.handleSelectionRequest(e.getX(), e.getY());
                    pageEditController.handleOpenPackagesDialogRequest();
                }
                canvas.setOnMouseDragged((MouseEvent e1) -> {
                    if (dataManager.isInState(JClassDesignerState.SELECTING_SHAPE))
                        if (selectedObject != null) 
                            canvasEditController.handlePositionChangeRequest(e1.getX(), e1.getY());
                });
            }
            else if (dataManager.isInState(JClassDesignerState.RESIZING_SHAPE))
            {
                Scene scene = app.getGUI().getPrimaryScene();
                Cursor cursor = scene.getCursor();
                
                if (cursor == Cursor.E_RESIZE)
                    canvasEditController.handleResizePressDetected(e.getX());
                else if (cursor == Cursor.S_RESIZE)
                    canvasEditController.handleResizePressDetected(e.getY());
                
                
                canvas.setOnMouseDragged((MouseEvent e1) -> {
                    if (dataManager.isInState(JClassDesignerState.RESIZING_SHAPE))
                    {
                        if (cursor == Cursor.E_RESIZE)
                            canvasEditController.handleHorizontalResizeRequest(e1.getX());
                        else if (cursor == Cursor.S_RESIZE)
                            canvasEditController.handleVerticalResizeRequest(e1.getY());
                    }
                });
            }
            
            
            // Check if the mouse press was on a line segment
            boolean isControlDown = e.isControlDown();
            if (e.getTarget() instanceof Line && !isControlDown)
            {
                    lineEditController.handleLineSelectionRequest((Line) e.getTarget(), e.getX(), e.getY());
                    canvas.setOnMouseDragged(e1 -> {
                        lineEditController.handleMoveLineRequest(e1.getX(), e1.getY());
                    });
            }
            else if (e.getTarget() instanceof Line && isControlDown)
                lineEditController.handleDoubleLineSelectionRequest((Line) e.getTarget());
            else
                lineEditController.handleDeselectLinesRequest();
         });
        
        app.getGUI().getPrimaryScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.S && selectedLine != null && selectedLine2 == null)
                lineEditController.handleSplitLineRequest();
            
            if (e.getCode() == KeyCode.M && selectedLine != null && selectedLine2 != null)
                lineEditController.handleMergeLinesRequest();
        });
    }
    
    /**
     * Now since we have the UI ready, this methods adds the CSS classes to make
     * the workspace look amazing!
     */
    @Override
    public void initStyle() 
    {
        // For Edit Toolbar
        for (Node b: editToolbarPane.getChildren())
            ((Button)b).getStyleClass().add(CLASS_FILE_BUTTON);
        
        // For The Canvas
        canvas.getStyleClass().add(CLASS_CANVAS);
        
        // For View Toolbar
        zoomInButton.getStyleClass().add(CLASS_FILE_BUTTON);
        zoomOutButton.getStyleClass().add(CLASS_FILE_BUTTON);
        defaultZoomButton.getStyleClass().add(CLASS_FILE_BUTTON);
        
        // For Component Toolbar
        // First the components in the grid pane. 
        classNameLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        packageLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        parentClassComboBox.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        localInterfacesLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        apiInterfacesLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addLocalInterfacesButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        addApiInterfacesButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        gridPane.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        
        //Then the components in the variables vbox.
        variablesLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addVariableButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        removeVariableButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        editVariableButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        variablesVBox.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        
        // And at the end the components in the mehtods vbox.
        methodsLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addMethodButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        removeMethodButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        editMethodButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        methodsVBox.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        
        // FINALLY THE COMPONENT TOOLBAR ITSELF
        componentToolbar.getStyleClass().add(CLASS_COMPONENT_TOOLBAR);
    }
    
    /**
     * This method is called whenever an event happens so it can
     * reload the workspace to display the changes.
     */
    @Override
    public void reloadWorkspace() 
    {
        loadViewToolbarSettings();
        loadCanvasSettings();
        
        /* Now check whether the selected object is null or not, and load component
           toolbar accordingly. */
        if (selectedObject != null)
            loadComponentToolbarSettings();
        else
            clearComponentToolbarSettings();
        
        // NOW ENABLE THE LEGAL BUTTONS ACCORDINGLY
        enableLegalButtons();
    }
    
    /**
     * Helper method to load view toolbar settings.
     */
    private void loadViewToolbarSettings()
    {
        // NOW CHECK THE GRID RENDER AND GRID SNAP
        if (dataManager.isInMode(GRID_RENDER_MODE))
        {
            gridRenderCheckBox.setSelected(true);
            gridSnapCheckBox.setSelected(false);
        }
        else if (dataManager.isInMode(GRID_RENDER_SNAP_MODE))
        {
            gridRenderCheckBox.setSelected(true);
            gridSnapCheckBox.setSelected(true);
        }
        else if (dataManager.isInMode(GRID_DEFAULT_MODE))
        {
            gridRenderCheckBox.setSelected(false);
            gridSnapCheckBox.setSelected(false);
        }
    }
    
    /**
     * Helper method to load canvas settings.
     */
    private void loadCanvasSettings()
    {
        canvas.getChildren().clear();
        
        // NOW CHECK THE GRID RENDER AND GRID SNAP
        if (dataManager.isInMode(GRID_RENDER_MODE))
            canvasEditController.handleRenderLinesRequest();
        else if (dataManager.isInMode(GRID_RENDER_SNAP_MODE))
        {
            canvasEditController.handleRenderLinesRequest();
            canvasEditController.handleSnapRequest();
        }
        
        canvas.setScaleX(dataManager.getCanvasZoomScaleX());
        canvas.setScaleY(dataManager.getCanvasZoomScaleY());
        
        // Then add all the boxes to the canvas
        for (ClassObject classObject: dataManager.getClassesList())
            canvas.getChildren().add(classObject.getBox().getMainVBox());
        
        // Then add all the lines to the canvas
        for (ClassObject classObject: dataManager.getClassesList())
            for (LineConnector lineConnector: classObject.getBox().getLineConnectors())
            {
                canvas.getChildren().addAll(lineConnector.getLines());
                canvas.getChildren().add(lineConnector.getShape());
            }
    }
    
    /**
     * Helper method to load the component toolbar settings for the 
     * object that is selected.
     */
    private void loadComponentToolbarSettings()
    {
        // First load the grid pane settings.
        loadGridPaneSettings();
        // Then the variables vbox settings.
        loadVariablesVBoxSettings();
        // And finally the methods vbox settings.
        loadMethodsVBoxSettings();
    }
    
    /**
     * Helper method to load the settings for the grid pane.
     */
    private void loadGridPaneSettings()
    {
        classNameTextField.setText(selectedObject.getClassName());
        packageNameTextField.setText(selectedObject.getPackageName());
        
        if (selectedObject.isInterfaceType())
            clearParentControlsSettings();
        else
            loadParentsControlsSettings();
    }
    
    /**
     * Helper method to clear parent controls setting because the selected object
     * is an interface and therefore there is no need to load the parent controls
     * settings.
     */
    private void clearParentControlsSettings() 
    {
        noParentClassRadioButton.setSelected(false);
        localParentClassRadioButton.setSelected(false);
        apiParentClassRadioButton.setSelected(false);
        parentClassComboBox.getItems().clear();
        parentClassComboBox.setValue(null);
        parentClassTextField.clear();
    }
    
    /**
     * Helper method to load parents controls setting because the selected object
     * is a type of a class object and therefore there is a need to load the parent
     * controls settings.
     */
    private void loadParentsControlsSettings()
    {
        if (selectedObject.getParentName() == null) 
        {
            noParentClassRadioButton.setSelected(true);
            localParentClassRadioButton.setSelected(false);
            apiParentClassRadioButton.setSelected(false);
            parentClassComboBox.setValue(null);
            parentClassTextField.clear();
        } 
        else if (dataManager.containsClassObject(selectedObject.getParentName())) 
        {
            noParentClassRadioButton.setSelected(false);
            localParentClassRadioButton.setSelected(true);
            apiParentClassRadioButton.setSelected(false);
            parentClassComboBox.setValue(selectedObject.getParentName());
            parentClassTextField.clear();
        } 
        else
        {
            noParentClassRadioButton.setSelected(false);
            localParentClassRadioButton.setSelected(false);
            apiParentClassRadioButton.setSelected(true);
            parentClassComboBox.setValue(null);
            parentClassTextField.setText(selectedObject.getParentName());
        }
    }
    
    /**
     * Helper method to load the settings for the variables vbox.
     */
    private void loadVariablesVBoxSettings()
    {
        // First store all the variables temporarily.
        ArrayList<VariableObject> tempVariables = new ArrayList();
        for (VariableObject variable: selectedObject.getVariables())
            tempVariables.add(variable);
        
        // Now clear the variables stuff.
        selectedObject.getVariables().clear();
        variablesTableView.getItems().clear();
        
        // And finally add it again.
        for (VariableObject variable: tempVariables)
            pageEditController.handleAddVariableRequest(variable);
    }
    
    /**
     * Helper method to load the setting for the methods vbox.
     */
    private void loadMethodsVBoxSettings()
    {
        // First store all the methods temporarily.
        ArrayList<MethodObject> tempMethods = new ArrayList();
        for (MethodObject method: selectedObject.getMethods())
            tempMethods.add(method);
        
        // Now clear the methods stuff.
        selectedObject.getMethods().clear();
        methodsTableView.getItems().clear();
        
        // And finally add it again.
        for (MethodObject method: tempMethods)
            pageEditController.handleAddMethodRequest(method);
    }
    
    /**
     * Helper method to clear the component toolbar setting when no item is
     * selected.
     */
    private void clearComponentToolbarSettings() 
    {
        classNameTextField.clear();
        packageNameTextField.clear();
        noParentClassRadioButton.setSelected(false);
        localParentClassRadioButton.setSelected(false);
        apiParentClassRadioButton.setSelected(false);
        parentClassComboBox.getItems().clear();
        parentClassComboBox.setValue(null);
        parentClassTextField.clear();
        variablesTableView.getItems().clear();
        methodsTableView.getItems().clear();
    }
    
    /**
     * Helper method for enabling/disabling buttons according to the current
     * state of the workspace.
     */
    private void enableLegalButtons()
    {
        // Edit Toolbar buttons are always enabled except for two (resize and remove).
        // Case for resize and remove is dealt later in this method.
        for (Node b: editToolbarPane.getChildren())
            b.setDisable(false);
        
        /* View Toolbar buttons are always enabled excpet for special cases which are
           delat later in this method. */
        for (Node b: viewToolbarPane.getChildren())
            b.setDisable(false);
        
        // Now enable/disable controls in the component toolbar.
        if (selectedObject != null)
        {
            enableComponentToolbarButtons();
            // Also enable the two buttons below in edit toolbar.
            removeButton.setDisable(false);
            resizeButton.setDisable(false);
        }
        else
        {
            disableComponentToolbarButtons();
            // Also disable the two buttons below in edit toolbar.
            removeButton.setDisable(true);
            resizeButton.setDisable(true);
        }
        
        // NOW CHECK ENABLING OF THE ZOOM BUTTONS
        if (canvas.getScaleX() > 2.00)
            zoomInButton.setDisable(true);
        else
            zoomInButton.setDisable(false);
        
        if (canvas.getScaleX() < 0.50)
            zoomOutButton.setDisable(true);
        else
            zoomOutButton.setDisable(false);
        
        // This is always enabled.
        defaultZoomButton.setDisable(false);
        
        // NOW CHECK WHETHER SNAP SHOULD BE ENABLED OR NOT
        if (gridRenderCheckBox.isSelected())
            gridSnapCheckBox.setDisable(false);
        else
        {
            gridSnapCheckBox.setSelected(false);
            gridSnapCheckBox.setDisable(true);
        }
    }
    
    /**
     * Helper method to enable the component toolbar buttons since there is a
     * selected object.
     */
    private void enableComponentToolbarButtons()
    {
        classNameTextField.setDisable(false);
        packageNameTextField.setDisable(false);

        if (selectedObject.isInterfaceType()) 
        {
            noParentClassRadioButton.setDisable(true);
            localParentClassRadioButton.setDisable(true);
            apiParentClassRadioButton.setDisable(true);
            parentClassComboBox.setDisable(true);
            parentClassTextField.setDisable(true);
        } 
        else 
        {
            noParentClassRadioButton.setDisable(false);
            localParentClassRadioButton.setDisable(false);
            apiParentClassRadioButton.setDisable(false);
        }

        if (noParentClassRadioButton.isSelected()) 
        {
            parentClassComboBox.setDisable(true);
            parentClassTextField.setDisable(true);
        } 
        else if (localParentClassRadioButton.isSelected())
        {
            parentClassComboBox.setDisable(false);
            parentClassTextField.setDisable(true);
        } 
        else if (apiParentClassRadioButton.isSelected()) 
        {
            parentClassComboBox.setDisable(true);
            parentClassTextField.setDisable(false);
        }

        addLocalInterfacesButton.setDisable(false);
        addApiInterfacesButton.setDisable(false);
        
        addVariableButton.setDisable(false);
        if (variablesTableView.getSelectionModel().getSelectedItem() != null) 
        {
            removeVariableButton.setDisable(false);
            editVariableButton.setDisable(false);
        } 
        else
        {
            removeVariableButton.setDisable(true);
            editVariableButton.setDisable(true);
        }
        
        addMethodButton.setDisable(false);
        if (methodsTableView.getSelectionModel().getSelectedItem() != null) 
        {
            removeMethodButton.setDisable(false);
            editMethodButton.setDisable(false);
        } 
        else
        {
            removeMethodButton.setDisable(true);
            editMethodButton.setDisable(true);
        }
    }
    
    /**
     * Helper method to disable the component toolbar buttons since no
     * object is selected and therefore this toolbar should be disabled.
     */
    private void disableComponentToolbarButtons() 
    {
        classNameTextField.setDisable(true);
        packageNameTextField.setDisable(true);
        noParentClassRadioButton.setDisable(true);
        localParentClassRadioButton.setDisable(true);
        apiParentClassRadioButton.setDisable(true);
        parentClassComboBox.setDisable(true);
        parentClassTextField.setDisable(true);
        addLocalInterfacesButton.setDisable(true);
        addApiInterfacesButton.setDisable(true);
        addVariableButton.setDisable(true);
        removeVariableButton.setDisable(true);
        editVariableButton.setDisable(true);
        addMethodButton.setDisable(true);
        removeMethodButton.setDisable(true);
        editMethodButton.setDisable(true);
    }

    public Pane getCanvas() 
    {
        return canvas;
    }

    public ClassObject getSelectedObject()
    {
        return selectedObject;
    }

    public void setSelectedObject(ClassObject selectedObject) 
    {
        this.selectedObject = selectedObject;
    }

    public Line getSelectedLine() 
    {
        return selectedLine;
    }

    public void setSelectedLine(Line selectedLine)
    {
        this.selectedLine = selectedLine;
    }

    public Line getSelectedLine2() 
    {
        return selectedLine2;
    }

    public void setSelectedLine2(Line selectedLine2) 
    {
        this.selectedLine2 = selectedLine2;
    }
    
    public CanvasEditController getCanvasEditController() 
    {
        return canvasEditController;
    }
    
    public PageEditController getPageEditController() 
    {
        return pageEditController;
    }

    public LineEditController getLineEditController() 
    {
        return lineEditController;
    }

    public TableView getVariablesTableView() 
    {
        return variablesTableView;
    }

    public TableView getMethodsTableView() 
    {
        return methodsTableView;
    }
}