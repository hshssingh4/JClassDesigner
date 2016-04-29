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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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
    public static final String PRIVATE = "private";
    public static final String PUBLIC = "public";
    public static final String PROTECTED = "protected";
    
    public static final String INT = "int";
    public static final String DOUBLE = "double";
    public static final String BOOLEAN = "boolean";
    public static final String CHAR = "char";
    public static final String STRING = "String";
    public static final String ARRAY_LIST = "ArrayList"; 
    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String HASHTAG = "#";
    public static final String COLON = ":";
    public static final String SPACE = " ";
    public static final String NONE = "None";
        
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
    TextField packageNameTextField;
    
    // Third row (Parent)
    ToggleGroup parentClassGroup;
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
    ScrollPane variablesTableViewScrollPane;
    TableView variablesTableView;
    
    // Fifth row
    VBox methodsVBox;
    HBox methodsHBox;
    Label methodsLabel;
    Button addMethodButton;
    Button removeMethodButton;
    ScrollPane methodsTableViewScrollPane;
    TableView methodsTableView;
    
    // Selected Object
    ClassObject selectedObject;
    
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
        final String CLASS_CANVAS = "canvas_class";
        canvas.getStyleClass().add(CLASS_CANVAS);
    }
    
    private void initComponentToolbar()
    {
        componentToolbar = new VBox(20);
        componentToolbar.setMaxWidth(400);
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
        ColumnConstraints columnConstraint = new ColumnConstraints();
        columnConstraint.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(columnConstraint);
        
        // First row
        classNameLabel = new Label("Class Name:");
        classNameTextField = new TextField();
        classNameTextField.setDisable(true);
        classNameTextField.setAlignment(Pos.BOTTOM_RIGHT);
        classNameTextField.setPromptText("Enter Class Name");
        
        // Second row
        packageLabel = new Label("Package:");
        packageNameTextField = new TextField();
        packageNameTextField.setDisable(true);
        packageNameTextField.setAlignment(Pos.BOTTOM_RIGHT);
        packageNameTextField.setPromptText("Enter Package Name");
        
        // Third row
        parentClassGroup = new ToggleGroup();
        
        localParentClassRadioButton = new RadioButton("Local Parent: ");
        localParentClassRadioButton.setToggleGroup(parentClassGroup);
        parentClassComboBox = new ComboBox<>();
        parentClassComboBox.getItems().add(NONE);
        parentClassComboBox.setMaxWidth(Double.MAX_VALUE);
        parentClassComboBox.setTooltip(new Tooltip("Choose Local Parent Class"));
        
        // Fourth row
        apiParentClassRadioButton = new RadioButton("API Parent: ");
        apiParentClassRadioButton.setToggleGroup(parentClassGroup);
        parentClassTextField = new TextField();
        parentClassTextField.setDisable(true);
        parentClassTextField.setAlignment(Pos.BOTTOM_RIGHT);
        parentClassTextField.setPromptText("Enter Java API Class");
        
        // Fifth row
        localInterfacesLabel = new Label("Local Interfaces: ");
        addLocalInterfacesButton = new Button("Add Local Interfaces");
        addLocalInterfacesButton.setMaxWidth(Double.MAX_VALUE);
        apiInterfacesLabel = new Label("API Interfaces: ");
        addApiInterfacesButton = new Button("Add API Interfaces");
        addApiInterfacesButton.setMaxWidth(Double.MAX_VALUE);
        
        // Now add these rows to the grid pane
        gridPane.add(classNameLabel, 0, 0);
        gridPane.add(classNameTextField, 1, 0);
        gridPane.add(packageLabel, 0, 1);
        gridPane.add(packageNameTextField, 1, 1);
        gridPane.add(localParentClassRadioButton, 0, 2);
        gridPane.add(parentClassComboBox, 1, 2);
        gridPane.add(apiParentClassRadioButton, 0, 3);
        gridPane.add(parentClassTextField, 1, 3);
        gridPane.add(localInterfacesLabel, 0, 4);
        gridPane.add(addLocalInterfacesButton, 1, 4);
        gridPane.add(apiInterfacesLabel, 0, 5);
        gridPane.add(addApiInterfacesButton, 1, 5);
        
        // Align the combo box to the right of the grid pane
        GridPane.setHalignment(parentClassComboBox, HPos.RIGHT);
        GridPane.setHalignment(addLocalInterfacesButton, HPos.RIGHT);
        GridPane.setHalignment(addApiInterfacesButton, HPos.RIGHT);
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
        variablesTableViewScrollPane = new ScrollPane(variablesTableView);

        TableColumn variableNameCol = new TableColumn("Name");
        TableColumn variableTypeCol = new TableColumn("Type");
        TableColumn variableScopeCol = new TableColumn("Scope");
        TableColumn variableStaticCol = new TableColumn("Static");
        TableColumn variableFinalCol = new TableColumn("Final");
        
        variablesTableView.getColumns().addAll(variableNameCol, variableTypeCol,
                variableScopeCol, variableStaticCol, variableFinalCol);
        
        variablesVBox.getChildren().addAll(variablesHBox, variablesTableViewScrollPane);
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
        methodsTableViewScrollPane = new ScrollPane(methodsTableView);
        
        TableColumn variableNameCol = new TableColumn("Name");
        TableColumn variableTypeCol = new TableColumn("Type");
        TableColumn variableScopeCol = new TableColumn("Scope");
        TableColumn variableStaticCol = new TableColumn("Static");
        TableColumn variableFinalCol = new TableColumn("Final");
        TableColumn variableAbstractCol = new TableColumn("Abstract");
        TableColumn variableArgumentsCol = new TableColumn("Arguments");

        methodsTableView.getColumns().addAll(variableNameCol, variableTypeCol, variableScopeCol,
                variableStaticCol, variableFinalCol, variableAbstractCol, variableArgumentsCol);
        
        methodsVBox.getChildren().addAll(methodsHBox, methodsTableViewScrollPane);
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
        // HANDLER FOR THE CONTROLS ON THE TOP
        selectionToolButton.setOnAction(e -> {
            pageEditController.handleSelectionToolButtonRequest();
        });
        resizeButton.setOnAction(e -> {
            pageEditController.handleResizeButtonRequest();
        });
        addClassButton.setOnAction(e -> {
            pageEditController.handleAddClassRequest();
        });
        addInterfaceButton.setOnAction(e -> {
            pageEditController.handleAddInterfaceRequest();
        });
        removeButton.setOnAction(e -> {
            pageEditController.handleRemoveRequest();
        });
        zoomInButton.setOnAction(e -> {
            canvasEditController.handleZoomInRequest();
        });
        zoomOutButton.setOnAction(e -> {
            canvasEditController.handleZoomOutRequest();
        });
        gridRenderCheckBox.setOnAction(e -> {
            reloadWorkspace();
        });
        gridSnapCheckBox.setOnAction(e -> {
            if (gridSnapCheckBox.isSelected())
                canvasEditController.handleSnapRequest();
        });
        
        // HANDLERS FOR CONTROLS ON THE RIGHT
        classNameTextField.textProperty().addListener((observable, oldClassName, newClassName) -> {
            if (selectedObject != null)
                pageEditController.handleClassNameChangeRequest(newClassName);
        });
        packageNameTextField.textProperty().addListener((observable, oldClassName, newClassName) -> {
            if (selectedObject != null)
                pageEditController.handlePackageNameChangeRequest(newClassName);
        });
        parentClassGroup.selectedToggleProperty().addListener((observable, old_toggle, new_toggle) -> {
            if (localParentClassRadioButton.isSelected()) 
            {
                if (!parentClassComboBox.getItems().contains(NONE))
                    parentClassComboBox.getItems().add(NONE);
                for (ClassObject obj: dataManager.getClassesList())
                    if (!obj.equals(selectedObject) && !obj.isInterfaceType()
                            && !parentClassComboBox.getItems().contains(obj.getClassName()))
                        parentClassComboBox.getItems().add(obj.getClassName());
                if (parentClassComboBox.getItems().contains(selectedObject.getClassName()))
                    parentClassComboBox.getItems().remove(selectedObject.getClassName());
            }
            reloadWorkspace();
        });
        parentClassTextField.textProperty().addListener((observable, oldClassName, newParentName) -> {
            if (selectedObject != null)
                pageEditController.handleParentClassRequest(newParentName);
        });
        parentClassComboBox.setOnAction(e -> {
            if (parentClassComboBox.getValue() != null)
                pageEditController.handleParentClassRequest(parentClassComboBox.getValue());
        });
        addLocalInterfacesButton.setOnAction(e -> {
            pageEditController.handleOpenLocalInterfacesDialogRequest();
        });
        addApiInterfacesButton.setOnAction(e -> {
            pageEditController.handleOpenApiInterfacesDialogRequest();
        });
        addVariableButton.setOnAction(e -> {
            pageEditController.handleOpenVariableDialogRequest();
        });
        
        // AND THESE ARE THE SELECTION, RESIZING, DRAGGING HANDLERS
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
        localParentClassRadioButton.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        apiParentClassRadioButton.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        parentClassComboBox.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        localInterfacesLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        apiInterfacesLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addLocalInterfacesButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        addApiInterfacesButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
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
        canvas.getChildren().clear();
        
        // NOW CHECK THE GRID RENDER AND GRID SNAP
        if (gridRenderCheckBox.isSelected())
            canvasEditController.handleRenderLinesRequest();
        if (gridSnapCheckBox.isSelected())
            canvasEditController.handleSnapRequest();
        
        for (ClassObject classObject: dataManager.getClassesList())
            canvas.getChildren().add(classObject.getBox().getMainVBox());
        
        if (selectedObject != null)
            loadComponentToolbar();
        else
        {
            classNameTextField.clear();
            packageNameTextField.clear();
            localParentClassRadioButton.setSelected(false);
            apiParentClassRadioButton.setSelected(false);
            parentClassComboBox.getItems().clear();
            parentClassTextField.clear();
        }
        
        // Now load all the childrens again
        //ArrayList
        
        enableLegalButtons();
    }
    
    private void loadComponentToolbar()
    {
        classNameTextField.setText(selectedObject.getClassName());
        packageNameTextField.setText(selectedObject.getPackageName());
        if (selectedObject.isInterfaceType())
        {
            localParentClassRadioButton.setSelected(false);
            apiParentClassRadioButton.setSelected(false);
            parentClassComboBox.getItems().clear();
            parentClassTextField.clear();
        }
        else
        {   
            if (selectedObject.getParentName() == null)
            {
                parentClassComboBox.setValue(NONE);
                parentClassTextField.clear();
            }
            else if (dataManager.getHashClasses().containsKey(selectedObject.getParentName()))
            {
                localParentClassRadioButton.setSelected(true);
                apiParentClassRadioButton.setSelected(false);
                parentClassComboBox.setValue(selectedObject.getParentName());
                String tempParentName = selectedObject.getParentName();
                parentClassTextField.clear();
                selectedObject.setParentName(tempParentName);
            }
            else
            {
                localParentClassRadioButton.setSelected(false);
                apiParentClassRadioButton.setSelected(true);
                String tempParentName = selectedObject.getParentName();
                parentClassComboBox.setValue(NONE);
                selectedObject.setParentName(tempParentName);
                parentClassTextField.setText(selectedObject.getParentName());
            }
        }
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
            packageNameTextField.setDisable(false);
            
            if (selectedObject.isInterfaceType())
            {
                localParentClassRadioButton.setDisable(true);
                apiParentClassRadioButton.setDisable(true);
                parentClassComboBox.setDisable(true);
                parentClassTextField.setDisable(true);
            }
            else
            {
                localParentClassRadioButton.setDisable(false);
                apiParentClassRadioButton.setDisable(false);
            }
            if (localParentClassRadioButton.isSelected())
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
            removeVariableButton.setDisable(false);
            addMethodButton.setDisable(false);
            removeMethodButton.setDisable(false);
            removeButton.setDisable(false);
            resizeButton.setDisable(false);
        }
        else
        {
            classNameTextField.setDisable(true);
            packageNameTextField.setDisable(true);
            localParentClassRadioButton.setDisable(true);
            apiParentClassRadioButton.setDisable(true);
            parentClassComboBox.setDisable(true);
            parentClassTextField.setDisable(true);
            addLocalInterfacesButton.setDisable(true);
            addApiInterfacesButton.setDisable(true);
            addVariableButton.setDisable(true);
            removeVariableButton.setDisable(true);
            addMethodButton.setDisable(true);
            removeMethodButton.setDisable(true);
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
        
        // NOW CHECK WHETHER SNAP SHOULD BE ENABLED OR NOT
        if (gridRenderCheckBox.isSelected())
            gridSnapCheckBox.setDisable(false);
        else
            gridSnapCheckBox.setDisable(true);
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

    public CanvasEditController getCanvasEditController() 
    {
        return canvasEditController;
    }
}
