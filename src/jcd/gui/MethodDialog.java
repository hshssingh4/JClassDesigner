/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static jcd.controller.PageEditController.PROTECTED;
import jcd.data.ArgumentObject;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.LineConnectorType;
import jcd.data.MethodObject;
import static jcd.file.FileManager.PRIVATE;
import static jcd.file.FileManager.PUBLIC;
import saf.AppTemplate;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_BUTTON;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_CHILD_ELEMENT;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;
import saf.ui.AppMessageDialogSingleton;

/**
 * This class provides the dialog box functionality for adding a variable
 * by displaying a dialog box.
 * @author RaniSons
 */
public class MethodDialog extends Stage
{
    public static final String TITLE = "Add/Edit Method";
    public static final String METHOD_ADD_ERROR_TITLE = "Add/Edit Method Error";
    public static final String METHOD_ADD_ERROR_MESSAGE = "Make sure text fields are "
            + "not empty!";
    
    // This is our app
    AppTemplate app;
    DataManager dataManager;
    
    Scene dialogScene;
    ClassObject classObject;
    MethodObject method;
    ArrayList<ArgumentObject> arguments;
    
    // BELOW ARE THE PANES AND CONTROLS NECESSARY FOR ADDING A METHOD
    GridPane gridPane; // HOLDS IN EVERYTHING
    
    // For adding name of the method object
    Label nameLabel;
    TextField nameTextField;
    
    // For adding type of the method object
    Label typeLabel;
    TextField typeTextField;
    
    // For choosing scope of the method object
    Label scopeLabel;
    ToggleGroup scopeGroup;
    RadioButton privateRadioButton;
    RadioButton publicRadioButton;
    RadioButton protectedRadioButton;
    
    // For choosing whether method is static or not
    Label staticLabel;
    CheckBox staticCheckBox;
    
    // For choosing whether method is final or not
    Label finalLabel;
    CheckBox finalCheckBox;
    
    // For choosing whether method is abstract or not
    Label abstractLabel;
    CheckBox abstractCheckBox;
    
    // For adding arguments to the method object
    Label argumentsLabel;
    Button addEditArgumentsButton;
    
    // Button for submitting the choices
    Button submitButton;
    
    /**
     * Constructor for initializing this dialog box.
     * @param initApp
     * @param classObject 
     * the selected object of the workspace
     * @param method
     * the method for which dialog box is opened, could be null
     */
    public MethodDialog(AppTemplate initApp, ClassObject classObject, 
            MethodObject method)
    {
        app = initApp;
        dataManager = (DataManager) initApp.getDataComponent();
        
        initModality(Modality.WINDOW_MODAL);
        initOwner(app.getGUI().getWindow());
        
        this.classObject = classObject;
        this.method = method;
        this.arguments = new ArrayList<>();
        
        // Init all the rows of the grid pane
        initNameGroup();
        initTypeGroup();
        initScopeGroup();
        initStaticGroup();
        initFinalGroup();
        initAbstractGroup();
        initArgumentsGroup();
        initSubmitButton();
        
        // NOW THAT WE HAVE ALL ROWS, INIT GRID PANE
        initGridPane();
        
        if (method != null)
            initValues();
        
        // AND PUT IT IN THE WINDOW
        dialogScene = new Scene(gridPane);
        this.setScene(dialogScene);
        this.setTitle(TITLE);
        
        // AND NOW THE STYLE
        initStylesheet();
        initStyle();
    }
    
    /**
     * Helper method to help initialize the grid pane row for naming a method object.
     */
    private void initNameGroup()
    {
        nameLabel = new Label("Name:");
        nameTextField = new TextField();
        nameTextField.setAlignment(Pos.BOTTOM_RIGHT);
        nameTextField.setPromptText("Enter Method Name");
    }
    
    /**
     * Helper method to help initialize the grid pane row for specifying type
     * of a method object.
     */
    private void initTypeGroup()
    {
        typeLabel = new Label("Type:");
        typeTextField = new TextField();
        typeTextField.setAlignment(Pos.BOTTOM_RIGHT);
        typeTextField.setPromptText("Enter Method Type");
    }
    
    /**
     * Helper method to help initialize the grid pane row for choosing scope
     * of a method object.
     */
    private void initScopeGroup()
    {
        scopeLabel = new Label("Scope:");
        scopeGroup = new ToggleGroup();
        
        privateRadioButton = new RadioButton(PRIVATE);
        publicRadioButton = new RadioButton(PUBLIC);
        protectedRadioButton = new RadioButton(PROTECTED);
        
        privateRadioButton.setToggleGroup(scopeGroup);
        privateRadioButton.setSelected(true);
        publicRadioButton.setToggleGroup(scopeGroup);
        protectedRadioButton.setToggleGroup(scopeGroup);
    }
    
    /**
     * Helper method to help initialize the grid pane row for choosing static 
     * type of a method object.
     */
    private void initStaticGroup()
    {
        staticLabel = new Label("Static:");
        staticCheckBox = new CheckBox();
    }
    
    /**
     * Helper method to help initialize the grid pane row for choosing final 
     * type of a method object.
     */
    private void initFinalGroup()
    {
        finalLabel = new Label("Final:");
        finalCheckBox = new CheckBox();
    }
    
    /**
     * Helper method to help initialize the grid pane row for choosing abstract 
     * type of a method object.
     */
    private void initAbstractGroup()
    {
        abstractLabel = new Label("Abstract:");
        abstractCheckBox = new CheckBox();
    }
    
    /**
     * Helper method to help initialize the grid pane row for adding arguments 
     * to the method object.
     */
    private void initArgumentsGroup()
    {
        argumentsLabel = new Label("Arguments:");
        addEditArgumentsButton = new Button("Add/Edit Arguments");
        
        addEditArgumentsButton.setOnAction(e -> {
            ArgumentsDialog argumentsDialog = new ArgumentsDialog(app, arguments);
            argumentsDialog.makeVisible();
        });
    }
    
    /**
     * Helper method to initialize the submit button.
     */
    private void initSubmitButton()
    {
        submitButton = new Button("Submit");
        submitButton.setOnAction((ActionEvent e) -> {
            MethodObject newMethod = createMethodObject();
            
            if (nameTextField.getText().isEmpty() || typeTextField.getText().isEmpty())
            {
                AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
                messageDialog.show(METHOD_ADD_ERROR_TITLE, METHOD_ADD_ERROR_MESSAGE);
            }
            else
            {
                Workspace workspace = (Workspace) app.getWorkspaceComponent();
                if (method == null)
                {
                    workspace.getPageEditController().handleAddMethodRequest(newMethod);
                    workspace.getPageEditController().handleAddMethodTextFieldRequest(newMethod);
                    
                    // NOW IN ORDER TO RENDER LINES, GET ALL THE LOCAL RELATIVES
                    ArrayList<ClassObject> list = new ArrayList<>();
                    
                    if (dataManager.containsClassObject(newMethod.getType()))
                        list.add(dataManager.fetchClassObject(newMethod.getType()));
                    
                    for (ArgumentObject arg: newMethod.getArguments())
                        if (dataManager.containsClassObject(arg.getType()))
                            list.add(dataManager.fetchClassObject(arg.getType()));
                    for (ClassObject obj: list)
                        workspace.getLineEditController().handleAddLineConnector(
                                classObject.getBox(), obj.getBox(), obj.getClassName(),
                                LineConnectorType.ARROW);
                }
                else
                {
                    int selectedIndex = classObject.getMethods().indexOf(method);
                    workspace.getPageEditController().handleEditMethodRequest(
                            selectedIndex, newMethod);
                    workspace.getPageEditController().handleEditMethodTextFieldRequest(
                        selectedIndex, newMethod);
                }
                this.hide();
            }
        });
    }
    
    /**
     * Helper method to create a method object from values inside the field in dialog box.
     */
    private MethodObject createMethodObject() 
    {
        MethodObject newMethod = new MethodObject();
        
        newMethod.setName(nameTextField.getText());
        newMethod.setType(typeTextField.getText());
        if (privateRadioButton.isSelected()) 
            newMethod.setScope(PRIVATE);
        else if (publicRadioButton.isSelected())
            newMethod.setScope(PUBLIC);
        else if (protectedRadioButton.isSelected())
            newMethod.setScope(PROTECTED);
        newMethod.setStaticType(staticCheckBox.isSelected());
        newMethod.setFinalType(finalCheckBox.isSelected());
        newMethod.setAbstractType(abstractCheckBox.isSelected());
        newMethod.setArguments(arguments);
        
        return newMethod;
    }
    
    /**
     * Helper method to initialize the grid panes.
     */
    private void initGridPane()
    {
        gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ColumnConstraints columnConstraint = new ColumnConstraints();
        columnConstraint.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().add(columnConstraint);
        
        // Now add these rows to the grid pane
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameTextField, 1, 0);
        gridPane.add(typeLabel, 0, 1);
        gridPane.add(typeTextField, 1, 1);
        gridPane.add(scopeLabel, 0, 3);
        gridPane.add(privateRadioButton, 1, 2);
        gridPane.add(publicRadioButton, 1, 3);
        gridPane.add(protectedRadioButton, 1, 4);
        gridPane.add(staticLabel, 0, 5);
        gridPane.add(staticCheckBox, 1, 5);
        gridPane.add(finalLabel, 0, 6);
        gridPane.add(finalCheckBox, 1, 6);
        gridPane.add(abstractLabel, 0, 7);
        gridPane.add(abstractCheckBox, 1, 7);
        gridPane.add(argumentsLabel, 0, 8);
        gridPane.add(addEditArgumentsButton, 1, 8);
        gridPane.add(submitButton, 1, 9);
        
        // Align the combo box to the right of the grid pane
        GridPane.setHalignment(submitButton, HPos.RIGHT);
    }
    
    /**
     * This is a special method that is only called if it's called for
     * editing purposes.
     */
    private void initValues()
    {
        nameTextField.setText(method.getName());
        typeTextField.setText(method.getType());
        
        switch (method.getScope())
        {
            case PRIVATE:
                privateRadioButton.setSelected(true);
                break;
            case PUBLIC:
                publicRadioButton.setSelected(true);
                break;
            case PROTECTED:
                protectedRadioButton.setSelected(true);
                break;
            default:
                break;
        }
        
        if (method.isStaticType())
            staticCheckBox.setSelected(true);
        if(method.isFinalType())
            finalCheckBox.setSelected(true);
        if(method.isAbstractType())
            abstractCheckBox.setSelected(true);
        this.arguments = method.getArguments();
    }
    
    /**
     * This function sets up the stylesheet to be used for specifying all
     * style for this application. Note that it does not attach CSS style
     * classes to controls, that must be done separately.
     */
    private void initStylesheet()
    {
	dialogScene.getStylesheets().addAll(
                app.getGUI().getPrimaryScene().getStylesheets());
    }
    
    /**
     * Helper method to add style classes for the components inside this dialog box.
     */
    private void initStyle()
    {
        gridPane.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        nameLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        typeLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        scopeLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        staticLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        finalLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        abstractLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        argumentsLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        privateRadioButton.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        publicRadioButton.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        protectedRadioButton.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        addEditArgumentsButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        submitButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
    }
    
    /**
     * This method puts it in view.
     */
    public void makeVisible()
    {
	// AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
	// WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
	// DO MORE WORK.
        showAndWait();
    }
}
