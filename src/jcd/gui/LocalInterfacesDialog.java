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
import javafx.scene.control.SelectionMode;
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
 * This class is basically a dialog box that is shows to the user for helping
 * user to add local interface names to the selected class object.
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
    
    /**
     * Constructor to initialize the dialog box before it is displayed.
     * @param initApp
     * the current application
     * @param classObject 
     * the selected class object
     */
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
    
    /**
     * Helper method to initialize the list view on the left in the dialog box.
     * @param localInterfaceNames 
     * the names of the interfaces to be added to the left list view
     */
    private void initLeftListView(ArrayList<String> localInterfaceNames)
    {
        localInterfaceNamesListView = new ListView<>();
        localInterfaceNamesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        localInterfaceNamesListView.setOnMousePressed(e -> {
            classLocalInterfaceNamesListView.getSelectionModel().clearSelection();
            addButton.setDisable(false);
            removeButton.setDisable(true);
        });
        
        for (String interfaceName: localInterfaceNames)
            if (!classObject.getClassName().equals(interfaceName))
                localInterfaceNamesListView.getItems().add(interfaceName);
    }
    
    /**
     * Helper method to initialize the list view on the right in the dialog box
     * with the names of the interfaces that the selected object already has.
     * @param classLocalInterfaceNames 
     * the names of the interfaces to be added to the right list view
     */
    private void initRightListView(ArrayList<String> classLocalInterfaceNames)
    {
        classLocalInterfaceNamesListView = new ListView<>();
        classLocalInterfaceNamesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        classLocalInterfaceNamesListView.setOnMousePressed(e -> {
            localInterfaceNamesListView.getSelectionModel().clearSelection();
            addButton.setDisable(true);
            removeButton.setDisable(false);
        });
        classLocalInterfaceNamesListView.getItems().addAll(classLocalInterfaceNames);
        localInterfaceNamesListView.getItems().removeAll(classLocalInterfaceNames);
    }
    
    /**
     * Helper method to initialize the buttons vbox.
     */
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
            ArrayList<String> selectedInterfaceNames = new ArrayList<>(); 
            for (String interfaceName: 
                    localInterfaceNamesListView.getSelectionModel().getSelectedItems())
                selectedInterfaceNames.add(interfaceName);
            localInterfaceNamesListView.getItems().removeAll(selectedInterfaceNames);
            classLocalInterfaceNamesListView.getItems().addAll(selectedInterfaceNames);
            
            // Finally remove any selections and disable add button
            localInterfaceNamesListView.getSelectionModel().clearSelection();
            addButton.setDisable(true);
        });
        removeButton.setOnAction(e -> {
            ArrayList<String> selectedInterfaceNames = new ArrayList<>();
            for (String interfaceName: 
                    classLocalInterfaceNamesListView.getSelectionModel().getSelectedItems())
                selectedInterfaceNames.add(interfaceName);
            classLocalInterfaceNamesListView.getItems().removeAll(selectedInterfaceNames);
            localInterfaceNamesListView.getItems().addAll(selectedInterfaceNames);
            
            // Finally remove any selections and disable remove button
            classLocalInterfaceNamesListView.getSelectionModel().clearSelection();
            removeButton.setDisable(true);
        });
        doneButton.setOnAction(e -> {
            dataManager.clearLocalInterfaceNames(classObject);
            for (String interfaceName: classLocalInterfaceNamesListView.getItems())
                classObject.getInterfaceNames().add(interfaceName);
            this.hide();
        });
        
        buttonsBox.getChildren().addAll(addButton, removeButton, doneButton);
    }
    
    /**
     * Helper method to initialize the border pane.
     */
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
    
    /**
     * This method initializes the style for the components of the dialog box.
     */
    private void initStyle()
    {
        addButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        removeButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        doneButton.getStyleClass().add(CLASS_COMPONENT_BUTTON);
        borderPane.getStyleClass().add(CLASS_COMPONENT_CHILD_ELEMENT);
        buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonsBox.setPadding(new Insets(20, 20, 20, 20));
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
