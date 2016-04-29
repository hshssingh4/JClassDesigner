/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

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
import jcd.data.ClassObject;
import jcd.data.VariableObject;
import static jcd.gui.Workspace.PRIVATE;
import static jcd.gui.Workspace.PROTECTED;
import static jcd.gui.Workspace.PUBLIC;
import saf.AppTemplate;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_BUTTON;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_CHILD_ELEMENT;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;
import saf.ui.AppMessageDialogSingleton;

/**
 * This class provides the dialog box functionality for adding a variable.
 * @author RaniSons
 */
public class VariableDialog extends Stage
{
    public static final String TITLE = "Add Variable";
    public static final String VARIABLE_ADD_ERROR_TITLE = "Add Variable Error";
    public static final String VARIABLE_ADD_ERROR_MESSAGE = "Make sure text fields are "
            + "not empty!";
    
    AppTemplate app;
    
    Scene dialogScene;
    ClassObject classObject;
    
    // BELOW ARE THE PANES AND CONTROLS NECESSARY FOR ADDING A VARIABLE
    GridPane gridPane; // HOLDS IN EVERYTHING
    
    // For adding name of the variable object
    Label nameLabel;
    TextField nameTextField;
    
    // For adding type of the variable object
    Label typeLabel;
    TextField typeTextField;
    
    // For choosing scope of the variable object
    Label scopeLabel;
    ToggleGroup scopeGroup;
    RadioButton privateRadioButton;
    RadioButton publicRadioButton;
    RadioButton protectedRadioButton;
    
    // For choosing whether variable is static or not
    Label staticLabel;
    CheckBox staticCheckBox;
    
    // For choosing whether variable is final or not
    Label finalLabel;
    CheckBox finalCheckBox;
    
    // Button for submitting the choices
    Button submitButton;
    
    /**
     * Constructor for initializing this dialog box.
     * @param primaryStage
     * the stage to place this dialog box in.
     * @param classObject 
     * the selected object of the workspace
     * @param initApp
     * current application
     */
    public VariableDialog(Stage primaryStage, ClassObject classObject, 
            AppTemplate initApp)
    {
        app = initApp;
        
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        this.classObject = classObject;
        
        // Init all the rows of the grid pane
        initNameGroup();
        initTypeGroup();
        initScopeGroup();
        initStaticGroup();
        initFinalGroup();
        initSubmitButton();
        
        // NOW THAT WE HAVE ALL ROWS, INIT GRID PANE
        initGridPane();
        
        // AND PUT IT IN THE WINDOW
        dialogScene = new Scene(gridPane);
        this.setScene(dialogScene);
        this.setTitle(TITLE);
        
        // AND NOW THE STYLE
        initStylesheet();
        initStyle();
    }
    
    /**
     * Helper method to help initialize the grid pane row for naming a variable object.
     */
    private void initNameGroup()
    {
        nameLabel = new Label("Name: ");
        nameTextField = new TextField();
        nameTextField.setAlignment(Pos.BOTTOM_RIGHT);
        nameTextField.setPromptText("Enter Variable Name");
    }
    
    /**
     * Helper method to help initialize the grid pane row for specifying type
     * of a variable object.
     */
    private void initTypeGroup()
    {
        typeLabel = new Label("Type: ");
        typeTextField = new TextField();
        typeTextField.setAlignment(Pos.BOTTOM_RIGHT);
        typeTextField.setPromptText("Enter Variable Type");
    }
    
    /**
     * Helper method to help initialize the grid pane row for choosing scope
     * of a variable object.
     */
    private void initScopeGroup()
    {
        scopeLabel = new Label("Scope: ");
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
     * type of a variable object.
     */
    private void initStaticGroup()
    {
        staticLabel = new Label("Static: ");
        staticCheckBox = new CheckBox();
    }
    
    /**
     * Helper method to help initialize the grid pane row for choosing final 
     * type of a variable object.
     */
    private void initFinalGroup()
    {
        finalLabel = new Label("Final: ");
        finalCheckBox = new CheckBox();
    }
    
    /**
     * Helper method to initialize the submit button.
     */
    private void initSubmitButton()
    {
        submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            VariableObject variable = new VariableObject();
            variable.setName(nameTextField.getText());
            variable.setType(typeTextField.getText());
            if (privateRadioButton.isSelected())
                variable.setScope(PRIVATE);
            else if (publicRadioButton.isSelected())
                variable.setScope(PUBLIC);
            else if (protectedRadioButton.isSelected())
                variable.setScope(PROTECTED);
            variable.setStaticType(staticCheckBox.isSelected());
            variable.setFinalType(finalCheckBox.isSelected());
            
            if (nameTextField.getText().isEmpty() || typeTextField.getText().isEmpty())
            {
                AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
                messageDialog.show(VARIABLE_ADD_ERROR_TITLE, VARIABLE_ADD_ERROR_MESSAGE);
            }
            else
            {
                classObject.getVariables().add(variable);
                this.hide();
            }
        });
    }
    
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
        gridPane.add(submitButton, 1, 7);
        
        // Align the combo box to the right of the grid pane
        GridPane.setHalignment(submitButton, HPos.RIGHT);
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
    
    private void initStyle()
    {
        gridPane.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        nameLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        typeLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        scopeLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        staticLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        finalLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        privateRadioButton.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        publicRadioButton.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        protectedRadioButton.getStyleClass().add(CLASS_SUBHEADING_LABEL);
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
