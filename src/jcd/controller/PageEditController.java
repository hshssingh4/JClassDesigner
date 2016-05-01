/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.controller;

import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import jcd.JClassDesigner;
import jcd.data.Box;
import static jcd.data.Box.DEFAULT_HEIGHT;
import static jcd.data.Box.DEFAULT_WIDTH;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.JClassDesignerState;
import jcd.data.VariableObject;
import jcd.gui.ApiInterfacesDialog;
import jcd.gui.LocalInterfacesDialog;
import jcd.gui.PackagesDialog;
import jcd.gui.VariableDialog;
import jcd.gui.Workspace;
import jcd.model.VariableObjectModel;
import properties_manager.PropertiesManager;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;
import static saf.components.AppStyleArbiter.CLASS_TEXT_LABEL;
import static saf.settings.AppPropertyType.REMOVE_ELEMENT_MESSAGE;
import static saf.settings.AppPropertyType.REMOVE_ELEMENT_TITLE;
import saf.ui.AppYesNoCancelDialogSingleton;

/**
 * This class serves as a controller to handle all the user actions except the ones
 * related directly to the canvas. 
 * @author RaniSons
 */
public class PageEditController 
{
    // These are the values needed for adding text fields to the box
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
    
    double originalSceneX, originalSceneY;
    double originalTranslateX, originalTranslateY;
    CanvasEditController canavasEditController;
    
    // HERE'S THE FULL APP, WHICH GIVES US ACCESS TO OTHER STUFF
    JClassDesigner app;
    DataManager dataManager;
    
    /**
     * Constructor to set up the application.
     * @param initApp 
     * the application itself.
     */
    public PageEditController(JClassDesigner initApp) 
    {
	// KEEP IT FOR LATER
	app = initApp;
        dataManager = (DataManager)app.getDataComponent();
    }
        
    /**
     * Handles the case when the user clicks on the selection tool button
     * that is present in the edit toolbar.
     */
    public void handleSelectionToolButtonRequest() 
    {
	// CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.DEFAULT);
	
	// CHANGE THE STATE
	dataManager.setState(JClassDesignerState.SELECTING_SHAPE);
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Handles the case when the user clicks on the selection tool button
     * that is present in the edit toolbar.
     */
    public void handleResizeButtonRequest()
    {
        // CHANGE THE CURSOR
	//Scene scene = app.getGUI().getPrimaryScene();
	//scene.setCursor(Cursor.SE_RESIZE);
	
	// CHANGE THE STATE
	dataManager.setState(JClassDesignerState.RESIZING_SHAPE);
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Handles the request for adding the class object. It first initializes a class
     * object and then puts it on the canvas.
     */
    public void handleAddClassRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
         
        int randomInt = (int) (Math.random() * 100);
        String randomClassNameString = "DummyClass" + randomInt;
        
        // Initialize the box
        // x and y values where the box will be origined
        int x = (int)(Math.random() * (canvas.getWidth() - (int)(DEFAULT_WIDTH)));
        int y = (int) ((canvas.getHeight()/2) - (DEFAULT_HEIGHT/2));
        
        // Now initialize the class object
        ClassObject obj = new ClassObject();
        obj.setClassName(randomClassNameString);
        obj.setBox(new Box(x, y));
        obj.setPackageName(null);
        obj.setParentName(null);
        obj.setInterfaceType(false);
        obj.setInterfaceNames(new ArrayList<>());
        obj.setVariables(new ArrayList<>());
        obj.setMethods(new ArrayList<>());
        obj.setJavaApiPackages(new ArrayList<>());
        obj.setLineConnectors(new ArrayList<>());
        addClassTextFields(obj);
        
        if (dataManager.checkIfUnique(obj))
        {
            canvas.getChildren().add(obj.getBox().getMainVBox());
            dataManager.addClassObject(obj);
            // AND SELECT IT
            dataManager.setState(JClassDesignerState.SELECTING_SHAPE);
            workspace.getCanvasEditController().handleSelectionRequest(obj);
        }
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Handles the request for adding the interface object. It first initializes an interface
     * object and then puts it on the canvas.
     */
    public void handleAddInterfaceRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
         
        int randomInt = (int) (Math.random() * 100);
        String randomClassNameString = "DummyInterface" + randomInt;
        
        // Initialize the box
        // x and y values where the box will be origined
        int x = (int)(Math.random() * (canvas.getWidth() - (int)(DEFAULT_WIDTH)));
        int y = (int) ((canvas.getHeight()/2) - (DEFAULT_HEIGHT/2));
        
        // Now initialize the class object
        ClassObject obj = new ClassObject();
        obj.setClassName(randomClassNameString);
        obj.setBox(new Box(x, y));
        obj.setPackageName(null);
        obj.setParentName(null);
        obj.setInterfaceType(true);
        obj.setInterfaceNames(new ArrayList<>());
        obj.setVariables(new ArrayList<>());
        obj.setMethods(new ArrayList<>());
        obj.setJavaApiPackages(new ArrayList<>());
        obj.setLineConnectors(new ArrayList<>());
        addClassTextFields(obj);
        
        if (dataManager.checkIfUnique(obj))
        {
            canvas.getChildren().add(obj.getBox().getMainVBox());
            dataManager.addClassObject(obj);
            // AND SELECT IT
            dataManager.setState(JClassDesignerState.SELECTING_SHAPE);
            workspace.getCanvasEditController().handleSelectionRequest(obj);
        }
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Helper method to add the text field inside the class vbox.
     * @param classObject 
     * the class object for which the fields have to be added.
     */
    private void addClassTextFields(ClassObject classObject)
    {
        // GET THE BOX OF THIS PARTICULAR CLASS OBJECT
        Box box = classObject.getBox();
        // THEN CLEAR ITS CONTENTS
        box.getClassVBox().getChildren().clear();
        
        // NOW ADD IT AGAIN
        Text text = new Text(classObject.getClassName());
        text.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        box.getClassVBox().getChildren().add(text);
        
        // Also, if it is an interface or an abstract class, add the extra text
        if (classObject.isInterfaceType())
        {
            Text interfaceText = new Text("<<interface>>");
            interfaceText.getStyleClass().add(CLASS_TEXT_LABEL);
            box.getClassVBox().getChildren().add(interfaceText);
        }
        else if (classObject.isAbstractType())
        {
            Text abstractText = new Text("{abstract}");
            abstractText.getStyleClass().add(CLASS_TEXT_LABEL);
            box.getClassVBox().getChildren().add(abstractText);
        }
    }
    
    /**
     * This method handles the remove request for the selected object in the
     * workspace, be it a class or an interface.
     */
    public void handleRemoveClassObjectRequest()
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show(props.getProperty(REMOVE_ELEMENT_TITLE), props.getProperty(REMOVE_ELEMENT_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();
        
        if (selection != null && selection.equals(AppYesNoCancelDialogSingleton.YES))
        {
            dataManager.removeClassObject(workspace.getSelectedObject());
            workspace.setSelectedObject(null);
        }
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Handles the request where the user changes the name of a class.
     * It makes sure that the name is not changed such that the classes
     * always stay unique.
     * @param className 
     * the name to change it to
     */
    public void handleClassNameChangeRequest(String className)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        
        if (selectedObject != null)
        {
            boolean unique = dataManager.checkIfUnique(className, selectedObject.getPackageName());

            // Only change the name of the class to such if name is unique
            if (unique)
            {
                selectedObject.setClassName(className);
                ((Text)selectedObject.getBox().getClassVBox().getChildren().get(0)).setText(className);
            }
        }
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Handles the case where the user tries to change the name of the package.
     * @param packageName 
     * the name to be changed to
     */
    public void handlePackageNameChangeRequest(String packageName)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        
        boolean unique = dataManager.checkIfUnique(selectedObject.getClassName(), packageName);
            
        // Only change if it is unique.
        if (unique)
        {
            if (packageName.isEmpty())
                selectedObject.setPackageName(null);
            else
                selectedObject.setPackageName(packageName);
        }
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method changes the name of the parent of the selected object inside
     * the workspace.
     * @param parentName
     * the name for the selected object parent
     */
    public void handleParentClassRequest(String parentName)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        
        selectedObject.setParentName(parentName);
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This event is triggered when there is a double click on the selected object.
     * It opens a dialog box for entering API package names.
     */
    public void handleOpenPackagesDialogRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();

        if (selectedObject != null) 
        {
            PackagesDialog dialog = new PackagesDialog(app, selectedObject);
            dialog.makeVisible();
        }
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method opens a dialog box to ask user for local interfaces that the
     * user might want to add to a certain class.
     */
    public void handleOpenLocalInterfacesDialogRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();

        if (selectedObject != null) 
        {
            LocalInterfacesDialog dialog = new LocalInterfacesDialog(app, selectedObject);
            dialog.makeVisible();
        }
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Method similar to one above but this asks for API interfaces instead of 
     * local ones. Opens a dialog box similar to packages dialog.
     */
    public void handleOpenApiInterfacesDialogRequest() 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();

        if (selectedObject != null) 
        {
            ApiInterfacesDialog dialog = new ApiInterfacesDialog(app, selectedObject);
            dialog.makeVisible();
        }
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method makes visible a dialog box to let user enter info about
     * a variable that needs to be added or edited to/for the selected class object.
     * @param variable 
     */
    public void handleOpenVariableDialogRequest(VariableObject variable) 
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        
        if (selectedObject != null) 
        {
            VariableDialog dialog = new VariableDialog(app,
                    selectedObject, variable);
            dialog.makeVisible();
        }
        
        workspace.reloadWorkspace();
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * This method does three thing. It first adds the variable object to the 
     * selected class object list of variables. Then it adds a text field in the
     * box on the canvas. And lastly, it adds the variable to the table view.
     * @param variable 
     * the variable object to be added
     */
    public void handleAddVariableRequest(VariableObject variable)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        
        selectedObject.getVariables().add(variable);
        addVariableTextField(variable);
        addVariableToTableView(variable);
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Helper method to add the variable text field the box on canvas.
     * @param variable 
     * the variable for which the text field is to be added
     */
    private void addVariableTextField(VariableObject variable)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        
        // GET THE BOX OF THIS PARTICULAR CLASS OBJECT
        Box box = selectedObject.getBox();

        Text text = getVariableTextField(variable);
        text.getStyleClass().add(CLASS_TEXT_LABEL);
        if (variable.isStaticType())
            text.setUnderline(true);
        box.getVariablesVBox().getChildren().add(text);
    }
    
    /**
     * Helper method to get the text field for the variable object that needs to be
     * added to the box.
     * @param variable
     * @return 
     */
    private Text getVariableTextField(VariableObject variable)
    {
        Text text = new Text();
        String textString = new String();
        
        String name = variable.getName();
        String type = variable.getType();
        String scope = variable.getScope();
        
        switch (scope)
        {
            case PUBLIC:
                textString += PLUS;
                break;
            case PRIVATE:
                textString += MINUS;
                break;
            case PROTECTED:
                textString += HASHTAG;
                break;
        }
        
        textString += SPACE + name + COLON + SPACE + type;
        text.setText(textString);
        
        return text;
    }
    
    /**
     * Helper method to add the variable to table view.
     * @param variable 
     */
    private void addVariableToTableView(VariableObject variable)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        TableView variablesTable = workspace.getVariablesTableView();
        
        String name = variable.getName();
        String type = variable.getType();
        String scope = variable.getScope();
        String isStatic = String.valueOf(variable.isStaticType());
        String isFinal = String.valueOf(variable.isFinalType());
        
        VariableObjectModel model = new VariableObjectModel(name, type, scope,
                isStatic, isFinal);
        
        variablesTable.getItems().add(model);
    }
    
    /**
     * This method also does three things to remove a variable. It first removes
     * it from the list of variables for the selected class object. It then removes it from
     * the box on canvas. And finally, it removes it from the table view.
     * @param variable 
     * the variable that is to be removed
     */
    public void handleRemoveVariableRequest(VariableObject variable)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        
        selectedObject.getVariables().remove(variable);
        TableView variablesTable = workspace.getVariablesTableView();
        int selectedIndex = variablesTable.getSelectionModel().getSelectedIndex();
        variablesTable.getItems().remove(selectedIndex);
        selectedObject.getBox().getVariablesVBox().getChildren().remove(selectedIndex);
    }
    
    /**
     * Helper method for editing a variable that is selected in the table view.
     * Note that this method is called from the dialog box GUI.
     * @param index
     * the index of the variable that is selected in the table
     * @param variable 
     * the variable object itself
     */
    public void handleEditVariableRequest(int index, VariableObject variable)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        
        selectedObject.getVariables().set(index, variable);
        editVariableTextField(index, variable);
        editVariableInTableView(index, variable);
        
        // Work has been edited!
        app.getGUI().updateToolbarControls(false);
    }
    
    /**
     * Helper method to edit the variable text field in the box on canvas.
     * @param index
     * the index of the selected variable
     * @param variable 
     * the variable for which the text field needs to be edited
     */
    private void editVariableTextField(int index, VariableObject variable)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        ClassObject selectedObject = workspace.getSelectedObject();
        
        // GET THE BOX OF THIS PARTICULAR CLASS OBJECT
        Box box = selectedObject.getBox();

        Text text = getVariableTextField(variable);
        text.getStyleClass().add(CLASS_TEXT_LABEL);
        if (variable.isStaticType())
            text.setUnderline(true);
        box.getVariablesVBox().getChildren().set(index, text);
    }
    
    /**
     * This helper method edits the values for the selected variable in the table
     * view itself.
     * @param index
     * index of the selected variable in the table view
     * @param variable 
     * the variable that is to edited in the table view
     */
    private void editVariableInTableView(int index, VariableObject variable)
    {
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        TableView variablesTable = workspace.getVariablesTableView();
        
        String name = variable.getName();
        String type = variable.getType();
        String scope = variable.getScope();
        String isStatic = String.valueOf(variable.isStaticType());
        String isFinal = String.valueOf(variable.isFinalType());
        
        VariableObjectModel model = new VariableObjectModel(name, type, scope,
                isStatic, isFinal);
        
        variablesTable.getItems().set(index, model);
    }
    
    /*private void reloadMethodsTextFields(ClassObject classObject)
    {
        // GET THE BOX OF THIS PARTICULAR CLASS OBJECT
        Box box = classObject.getBox();
        // THEN CLEAR ITS CONTENTS
        box.getMethodsVBox().getChildren().clear();
        
        // NOW ADD IT AGAIN
        for (MethodObject method: classObject.getMethods())
        {
            Text text = getMethodTextField(method);
            text.getStyleClass().add(CLASS_TEXT_LABEL);
            if (method.isStaticType())
                text.setUnderline(true);
            box.getMethodsVBox().getChildren().add(text);
        }
    }
    
    private Text getMethodTextField(MethodObject method)
    {
        Text text = new Text();
        String textString = new String();
        
        String name = method.getName();
        String type = method.getType();
        String scope = method.getScope();
        
        switch (scope)
        {
            case PUBLIC:
                textString += PLUS;
                break;
            case PRIVATE:
                textString += MINUS;
                break;
            case PROTECTED:
                textString += HASHTAG;
                break;
        }
        
        textString += SPACE + name + "(" + methodArgumentsString(method) + ")" 
                + COLON + SPACE + type;
        text.setText(textString);
        
        return text;
    }
    
    private String methodArgumentsString(MethodObject method)
    {
        String answer = "";
        ArrayList<ArgumentObject> arguments = method.getArguments();
        
        for (int i = 0; i < arguments.size(); i++)
        {
            ArgumentObject argument = arguments.get(i);
            
            if (i == arguments.size() - 1)
                answer += argument.getName() + COLON + SPACE + argument.getType();
            else
                answer += argument.getName() + COLON + SPACE + argument.getType() + ", ";
        }
        
        return answer;
    }*/
}