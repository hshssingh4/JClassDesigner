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
import jcd.data.DataManager;

/**
 *
 * @author RaniSons
 */
public class ApiInterfacesDialog extends Stage
{
    public static final String TITLE = "Java API Interfaces: ";
    public static final int DEFAULT_HEIGHT = 350;
    public static final int DEFAULT_WIDTH = 350;
    
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
    
    public ApiInterfacesDialog(Stage primaryStage, ClassObject object, DataManager dataManager)
    {
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        this.classObject = object;
        this.dataManager = dataManager;
        
        // Initialize the text field and buttons
        initTextFieldsVBox(classObject.getInterfaceNames());
        initButtonsBox();
        initBorderPane();
        initStyle();
        
        // AND PUT IT IN THE WINDOW
        dialogScene = new Scene(borderPane);
        this.setScene(dialogScene);
        this.setTitle(TITLE + classObject.getClassName());
    }
    
    private void initTextFieldsVBox(ArrayList<String> interfaceNames)
    {
        textFieldsVBox = new VBox(10);
        for (String interfaceName: interfaceNames)
        {
            if (!dataManager.getHashClasses().containsKey(interfaceName))
            {
                TextField textField = new TextField(interfaceName);
                textField.focusedProperty().addListener(e1 -> {
                    if (textField.isFocused()) 
                        selectedTextField = textField;
                });
                textFieldsVBox.getChildren().add(textField);
            }
        }
    }
    
    private void initButtonsBox()
    {
        buttonsBox = new HBox(10);
        addButton = new Button("Add Interface");
        deleteButton = new Button("Delete Interface");
        doneButton = new Button("Done");
        
        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        addButton.setOnAction(e -> {
            TextField textField = new TextField();
            textField.focusedProperty().addListener(e1 -> {
                if (textField.isFocused())
                    selectedTextField = textField;
            });
            textFieldsVBox.getChildren().add(textField);
        });
        deleteButton.setOnAction(e -> {
            if (selectedTextField != null)
            {
                classObject.getInterfaceNames().remove(selectedTextField.getText());
                textFieldsVBox.getChildren().remove(selectedTextField);
            }
        });
        doneButton.setOnAction(e -> {
            addInterfaceNames();
            this.hide();
        });
        
        buttonsBox.getChildren().add(addButton);
        buttonsBox.getChildren().add(deleteButton);
        buttonsBox.getChildren().add(doneButton);
    }
    
    private void initBorderPane()
    {
        // WE'LL PUT EVERYTHING HERE
        borderPane = new BorderPane();
        borderPane.setTop(buttonsBox);
        borderPane.setCenter(textFieldsVBox);
        borderPane.setPrefSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
    
    private void addInterfaceNames()
    {
        for (Node a : textFieldsVBox.getChildren())
        {
            TextField interfaceTextField = (TextField) a;
            String interfaceName = interfaceTextField.getText();
            if (!interfaceName.isEmpty() && !classObject.getInterfaceNames().contains(interfaceName))
                classObject.getInterfaceNames().add(interfaceName);
        }
    }
    
    private void initStyle()
    {
        buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonsBox.setPadding(new Insets(20, 20, 20, 20));
        textFieldsVBox.setPadding(new Insets(5, 20, 20, 20));
        borderPane.setStyle("-fx-background-color: ghostwhite;");
    }
    
    public void makeVisible()
    {
	// AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
	// WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
	// DO MORE WORK.
        showAndWait();
    }
}
