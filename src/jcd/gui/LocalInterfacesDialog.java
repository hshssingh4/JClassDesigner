/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.gui;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import saf.AppTemplate;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_BUTTON;
import static saf.components.AppStyleArbiter.CLASS_COMPONENT_CHILD_ELEMENT;

/**
 *
 * @author RaniSons
 */
public class LocalInterfacesDialog extends Stage
{
    public static final String TITLE = "Local Interfaces: ";
    public static final int HEIGHT = 300;
    public static final int WIDTH = 700;
    
    // This is our app
    AppTemplate app;
    
    BorderPane borderPane;
    ListView<String> localInterfaceNamesListView;
    ListView<String> classLocalInterfaceNamesListView;
    
    Button addButton, removeButton, doneButton;
    VBox buttonsBox;
    
    Scene dialogScene;
    
    ClassObject classObject;
    DataManager dataManager;
    
    public LocalInterfacesDialog(AppTemplate initApp, ClassObject classObject)
    {
        app = initApp;
        
        initModality(Modality.WINDOW_MODAL);
        initOwner(app.getGUI().getWindow());
        
        this.classObject = classObject;
        this.dataManager = (DataManager) app.getDataComponent();
        
        // Initialize the text field and buttons
        initLeftListView(dataManager.fetchLocalInterfaceNames());
        initRightListView(dataManager.fetchLocalInterfaceNames(classObject));
        initButtonsBox();
        initBorderPane();
        initStyle();
        
        // AND PUT IT IN THE WINDOW
        dialogScene = new Scene(borderPane);
        this.setScene(dialogScene);
        this.setTitle(TITLE + classObject.getClassName());
        
        // AND NOW THE STYLE
        initStylesheet();
        initStyle();
    }
    
    private void initLeftListView(ArrayList<String> localInterfaceNames)
    {
        localInterfaceNamesListView = new ListView<>();
        localInterfaceNamesListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            addButton.setDisable(false);
            removeButton.setDisable(true);
        });
        localInterfaceNamesListView.getItems().addAll(localInterfaceNames);
    }
    
    private void initRightListView(ArrayList<String> classLocalInterfaceNames)
    {
        classLocalInterfaceNamesListView = new ListView<>();
        classLocalInterfaceNamesListView.getSelectionModel().selectedItemProperty().addListener(ov -> {
            addButton.setDisable(true);
            removeButton.setDisable(false);
        });
        classLocalInterfaceNamesListView.getItems().addAll(classLocalInterfaceNames);
        localInterfaceNamesListView.getItems().removeAll(classLocalInterfaceNames);
    }
    
    private void initButtonsBox()
    {
        buttonsBox = new VBox(10);
        
        addButton = new Button("Add");
        addButton.setDisable(true);
        addButton.setPrefWidth(Double.MAX_VALUE);
        removeButton = new Button("Remove");
        removeButton.setDisable(true);
        removeButton.setPrefWidth(Double.MAX_VALUE);
        doneButton = new Button("Done");
        doneButton.setDisable(false);
        doneButton.setPrefWidth(Double.MAX_VALUE);
        
        // AND THEN REGISTER THEM TO RESPOND TO INTERACTIONS
        addButton.setOnAction(e -> {
            String selectedInterfaceName = localInterfaceNamesListView.getSelectionModel().getSelectedItem();
            localInterfaceNamesListView.getItems().remove(selectedInterfaceName);
            classLocalInterfaceNamesListView.getItems().add(selectedInterfaceName);
        });
        removeButton.setOnAction(e -> {
            String selectedInterfaceName = classLocalInterfaceNamesListView.getSelectionModel().getSelectedItem();
            classLocalInterfaceNamesListView.getItems().remove(selectedInterfaceName);
            localInterfaceNamesListView.getItems().add(selectedInterfaceName);
        });
        doneButton.setOnAction(e -> {
            for (String interfaceName: classLocalInterfaceNamesListView.getItems())
                if (!classObject.getInterfaceNames().contains(interfaceName))
                    classObject.getInterfaceNames().add(interfaceName);
            this.hide();
        });
        
        buttonsBox.getChildren().addAll(addButton, removeButton, doneButton);
    }
    
    private void initBorderPane()
    {
        // WE'LL PUT EVERYTHING HERE
        borderPane = new BorderPane();
        borderPane.setLeft(localInterfaceNamesListView);
        borderPane.setCenter(buttonsBox);
        borderPane.setRight(classLocalInterfaceNamesListView);
        borderPane.setPrefSize(WIDTH, HEIGHT);
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
        addButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        removeButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        doneButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        borderPane.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonsBox.setPadding(new Insets(20, 20, 20, 20));
    }
    
    public void makeVisible()
    {
	// AND OPEN UP THIS DIALOG, MAKING SURE THE APPLICATION
	// WAITS FOR IT TO BE RESOLVED BEFORE LETTING THE USER
	// DO MORE WORK.
        showAndWait();
    }
}
