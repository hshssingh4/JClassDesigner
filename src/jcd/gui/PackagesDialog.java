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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jcd.data.ClassObject;
import saf.AppTemplate;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_BUTTON;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_CHILD_ELEMENT;

/**
 * This class is basically a dialog box that helps user to add
 * java api packages to the class object.
 * @author RaniSons
 */
public class PackagesDialog extends Stage
{
    public static final String TITLE = "Java API Packages: ";
    
    // This is our app
    AppTemplate app;
    
    BorderPane borderPane;
    VBox textFieldsVBox;
    
    Button addButton;
    Button deleteButton;
    Button doneButton;
    HBox buttonsBox;
    
    Scene dialogScene;
    TextField selectedTextField;
    ClassObject classObject;
    
    /**
     * Constructor to initialize the dialog box before it is displayed.
     * @param initApp
     * the current application
     * @param object 
     * the selected class object
     */
    public PackagesDialog(AppTemplate initApp, ClassObject object)
    {
        app = initApp;
        
        initModality(Modality.WINDOW_MODAL);
        initOwner(app.getGUI().getWindow());
        
        this.classObject = object;
        
        // Initialize the text field and buttons
        initTextFieldsVBox(classObject.getJavaApiPackages());
        initButtonsBox();
        initBorderPane();
        
        // AND PUT IT IN THE WINDOW
        dialogScene = new Scene(borderPane);
        this.setScene(dialogScene);
        this.setTitle(TITLE + classObject.getClassName());
        
        // AND NOW THE STYLE
        initStylesheet();
        initStyle();
    }
    
    /**
     * Helper method to initialize the text fields VBox in the dialog box.
     * @param javaApipackageNames 
     * the names of the packages to be added to the text fields in the box
     */
    private void initTextFieldsVBox(ArrayList<String> javaApiPackageNames)
    {
        textFieldsVBox = new VBox(10);
        for (String packageName: javaApiPackageNames)
        {
            TextField textField = new TextField(packageName);
            textField.focusedProperty().addListener(e1 -> {
                if (textField.isFocused())
                    selectedTextField = textField;
            });
            textFieldsVBox.getChildren().add(textField);
        }
    }
    
    /**
     * Helper method to initialize the buttons box in the dialog.
     */
    private void initButtonsBox()
    {
        buttonsBox = new HBox(10);
        addButton = new Button("Add Package");
        deleteButton = new Button("Delete Package");
        doneButton = new Button("Done");
        
        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        addButton.setOnAction(e -> {
            TextField textField = new TextField();
            textField.setPromptText("Enter Package Name! (eg. javafx.stage.Stage)");
            textField.setAlignment(Pos.BOTTOM_LEFT);
            textField.focusedProperty().addListener(e1 -> {
                if (textField.isFocused())
                    selectedTextField = textField;
            });
            textFieldsVBox.getChildren().add(textField);
        });
        deleteButton.setOnAction(e -> {
            if (selectedTextField != null)
            {
                classObject.getJavaApiPackages().remove(selectedTextField.getText());
                textFieldsVBox.getChildren().remove(selectedTextField);
            }
        });
        doneButton.setOnAction(e -> {
            addPackageNames();
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
     * This method is called when the user presses done and this method adds
     * them to the list of packages for this class.
     */
    private void addPackageNames()
    {
        for (Node a : textFieldsVBox.getChildren())
        {
            TextField packageTextField = (TextField) a;
            String packageName = packageTextField.getText();
            if (!packageName.isEmpty() && !classObject.getJavaApiPackages().contains(packageName))
                classObject.getJavaApiPackages().add(packageName);
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
