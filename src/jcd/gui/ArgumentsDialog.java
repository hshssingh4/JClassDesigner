/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jcd.data.ArgumentObject;
import saf.AppTemplate;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_BUTTON;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_CHILD_ELEMENT;

/**
 * This class is basically a dialog box that helps user to add
 * arguments to a method object.
 * @author RaniSons
 */
public class ArgumentsDialog extends Stage
{
    public static final String TITLE = "Add/Edit Arguments";
    
    ArrayList<ArgumentObject> arguments;
    
    // This is our app
    AppTemplate app;
    
    ScrollPane scrollPane;
    BorderPane borderPane;
    VBox textFieldsVBox;
    
    Button addButton;
    Button deleteButton;
    Button doneButton;
    HBox buttonsBox;
    
    Scene dialogScene;
    HBox selectedHBox;
    
    /**
     * Constructor to initialize the dialog box before it is displayed.
     * @param initApp
     * the current application
     * @param arguments 
     * the selected class object
     */
    public ArgumentsDialog(AppTemplate initApp, ArrayList<ArgumentObject> arguments)
    {
        app = initApp;
        
        initModality(Modality.WINDOW_MODAL);
        initOwner(app.getGUI().getWindow());
        
        this.arguments = arguments;
        
        // Initialize the text field and buttons
        initTextFieldsVBox(arguments);
        initButtonsBox();
        initBorderPane();
        initScrollPane();
        
        // AND PUT IT IN THE WINDOW
        dialogScene = new Scene(scrollPane);
        this.setScene(dialogScene);
        this.setTitle(TITLE);
        
        // AND NOW THE STYLE
        initStylesheet();
        initStyle();
    }
    
    /**
     * Helper method to initialize the text fields VBox in the dialog box.
     * @param arguments 
     * the names of the arguments to be added to the text fields in the box
     */
    private void initTextFieldsVBox(ArrayList<ArgumentObject> arguments)
    {
        textFieldsVBox = new VBox(10);
        for (ArgumentObject argument: arguments)
        {
            TextField nameTextField = new TextField(argument.getName());
            TextField typeTextField = new TextField(argument.getType());
            HBox box = new HBox(10);
            box.getChildren().addAll(nameTextField, typeTextField);
            nameTextField.focusedProperty().addListener(e1 -> {
                if (nameTextField.isFocused())
                    selectedHBox = box;
            });
            typeTextField.focusedProperty().addListener(e1 -> {
                if (typeTextField.isFocused())
                    selectedHBox = box;
            });
            textFieldsVBox.getChildren().add(box);
        }
    }
    
    /**
     * Helper method to initialize the buttons box in the dialog.
     */
    private void initButtonsBox()
    {
        buttonsBox = new HBox(10);
        addButton = new Button("Add Argument");
        deleteButton = new Button("Delete Argument");
        doneButton = new Button("Done");
        
        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        addButton.setOnAction(e -> {
            TextField nameTextField = new TextField();
            nameTextField.setPromptText("Enter Arg Name! (eg. x)");
            nameTextField.setAlignment(Pos.BOTTOM_LEFT);
            TextField typeTextField = new TextField();
            typeTextField.setPromptText("Enter Arg Type! (eg. int)");
            typeTextField.setAlignment(Pos.BOTTOM_LEFT);
            HBox box = new HBox(10);
            box.getChildren().addAll(nameTextField, typeTextField);
            nameTextField.focusedProperty().addListener(e1 -> {
                if (nameTextField.isFocused())
                    selectedHBox = box;
            });
            typeTextField.focusedProperty().addListener(e1 -> {
                if (typeTextField.isFocused())
                    selectedHBox = box;
            });
            
            textFieldsVBox.getChildren().add(box);
        });
        deleteButton.setOnAction(e -> {
            if (selectedHBox != null)
                textFieldsVBox.getChildren().remove(selectedHBox);
        });
        doneButton.setOnAction(e -> {
            addArguments();
            this.hide();
        });
        
        buttonsBox.getChildren().add(addButton);
        buttonsBox.getChildren().add(deleteButton);
        buttonsBox.getChildren().add(doneButton);
    }
    
    /**
     * Helper method to initialize the border pane.
     */
    private void initBorderPane()
    {
        // WE'LL PUT EVERYTHING HERE
        borderPane = new BorderPane();
        borderPane.setTop(buttonsBox);
        borderPane.setCenter(textFieldsVBox);
        borderPane.setPrefHeight(300);
    }
    
    /**
     * Helper method to initialize the scroll pane.
     */
    private void initScrollPane()
    {
        scrollPane = new ScrollPane(borderPane);
    }
    
    /**
     * This method is called when the user presses done and this method adds
     * them to the list of arguments.
     */
    private void addArguments()
    {
        arguments.clear();
        for (Node box: textFieldsVBox.getChildren())
        {
            HBox argumentsBox = (HBox) box;
            TextField nameTextField = (TextField) argumentsBox.getChildren().get(0);
            String argumentName = nameTextField.getText();
            TextField typeTextField = (TextField) argumentsBox.getChildren().get(1);
            String argumentType = typeTextField.getText();
            
            if (!argumentName.isEmpty() && !argumentType.isEmpty())
            {
                ArgumentObject argumentObject = new ArgumentObject();
                argumentObject.setName(argumentName);
                argumentObject.setType(argumentType);
                arguments.add(argumentObject);
            }
        }
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
     * This method initializes the style for the components of the dialog box.
     */
    private void initStyle()
    {
        addButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        deleteButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        doneButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        borderPane.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonsBox.setPadding(new Insets(20, 20, 20, 20));
        textFieldsVBox.setPadding(new Insets(5, 20, 20, 20));
    }
    
    /**
     * This method pops up the dialog box and waits for a user response.
     */
    public void makeVisible()
    {
	// AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
	// WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
	// DO MORE WORK.
        showAndWait();
    }
}
