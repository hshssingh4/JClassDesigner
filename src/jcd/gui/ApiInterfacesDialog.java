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
import jcd.data.ClassObject;
import jcd.data.DataManager;
import saf.AppTemplate;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_BUTTON;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_CHILD_ELEMENT;

/**
 * This class is basically a dialog box that asks user to enter the 
 * names of the API interfaces for a selected class object.
 * @author RaniSons
 */
public class ApiInterfacesDialog extends Stage
{
    public static final String TITLE = "Java API Interfaces: ";
    
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
    TextField selectedTextField;
    ClassObject classObject;
    
    DataManager dataManager;
    
    /**
     * Constructor to initialize the dialog box before it is displayed.
     * @param initApp
     * the current application
     * @param object 
     * the selected class object
     */
    public ApiInterfacesDialog(AppTemplate initApp, ClassObject object)
    {
        app = initApp;
        
        initModality(Modality.WINDOW_MODAL);
        initOwner(app.getGUI().getWindow());
        
        this.classObject = object;
        this.dataManager = (DataManager) app.getDataComponent();
        
        // Initialize the text field and buttons
        initTextFieldsVBox(classObject.getInterfaceNames());
        initButtonsBox();
        initBorderPane();
        initScrollPane();
        
        // AND PUT IT IN THE WINDOW
        dialogScene = new Scene(scrollPane);
        this.setScene(dialogScene);
        this.setTitle(TITLE + classObject.getClassName());
        
        // AND NOW THE STYLE
        initStylesheet();
        initStyle();
    }
    
    /**
     * Helper method to initialize the text fields VBox in the dialog box.
     * @param interfaceNames 
     * the names of the interfaces to be added to the text field
     */
    private void initTextFieldsVBox(ArrayList<String> interfaceNames)
    {
        textFieldsVBox = new VBox(10);
        for (String interfaceName: interfaceNames)
        {
            if (!dataManager.containsClassObject(interfaceName))
            {
                TextField textField = new TextField(interfaceName);
                textField.setPromptText("Enter Interface Name! (eg. Runnable)");
                textField.setAlignment(Pos.BOTTOM_LEFT);
                textField.focusedProperty().addListener(e1 -> {
                    if (textField.isFocused()) 
                        selectedTextField = textField;
                });
                textFieldsVBox.getChildren().add(textField);
            }
        }
    }
    
    /**
     * Helper method to initialize the buttons box in the dialog.
     */
    private void initButtonsBox()
    {
        buttonsBox = new HBox(10);
        addButton = new Button("Add Interface");
        deleteButton = new Button("Delete Interface");
        doneButton = new Button("Done");
        
        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        addButton.setOnAction(e -> {
            TextField textField = new TextField();
            textField.setPromptText("Enter Interface Name! (eg. Runnable)");
            textField.setAlignment(Pos.BOTTOM_LEFT);
            textField.focusedProperty().addListener(e1 -> {
                if (textField.isFocused())
                    selectedTextField = textField;
            });
            textFieldsVBox.getChildren().add(textField);
        });
        deleteButton.setOnAction(e -> {
            if (selectedTextField != null)
                textFieldsVBox.getChildren().remove(selectedTextField);
        });
        doneButton.setOnAction(e -> {
            addInterfaceNames();
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
     * them to the list of interfaces for this class.
     */
    private void addInterfaceNames()
    {
        // First clear out old api names
        dataManager.clearApiInterfaceNames(classObject);
        
        for (Node a : textFieldsVBox.getChildren())
        {
            TextField interfaceTextField = (TextField) a;
            String interfaceName = interfaceTextField.getText();
            if (!interfaceName.isEmpty())
                classObject.getInterfaceNames().add(interfaceName);
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
