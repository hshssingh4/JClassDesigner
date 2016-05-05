/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import jcd.Shapes.Triangle;
import jcd.data.ArgumentObject;
import jcd.data.Box;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.JClassDesignerMode;
import jcd.data.LineConnector;
import jcd.data.LineConnectorType;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import saf.components.AppDataComponent;
import static jcd.file.FileManager.*;

/**
 * This class loads the data file and populates the data manager with the right data.
 * @author RaniSons
 */
public class FileLoader 
{
    public FileLoader() {}
    /**
     * This method loads data from a JSON formatted file into the data 
     * management component and then forces the updating of the workspace
     * such that the user may edit the data.
     * 
     * @param data Data management component where we'll load the file into.
     * 
     * @param filePath Path (including file name/extension) to where
     * to load the data from.
     * 
     * @throws IOException Thrown should there be an error reading
     * in data from the file.
     */
    public void loadData(AppDataComponent data, String filePath) throws IOException
    {
        // CLEAR THE OLD DATA OUT
	DataManager dataManager = (DataManager)data;
	dataManager.reset();
        
        // LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadTestJSONFile(filePath);
        
        // First get all the canvas values
        double canvasZoomScaleX = json.getJsonNumber(JSON_CANVAS_ZOOM_SCALE_X).doubleValue();
        double canvasZoomScaleY = json.getJsonNumber(JSON_CANVAS_ZOOM_SCALE_Y).doubleValue();
        String mode = json.getString(JSON_GRID_MODE);
        dataManager.setCanvasZoomScaleX(canvasZoomScaleX);
        dataManager.setCanvasZoomScaleY(canvasZoomScaleY);
        dataManager.setMode(JClassDesignerMode.valueOf(mode));
        
        // NOW GET THE CLASSES ARRAY
        JsonArray jsonClassesArray = json.getJsonArray(JSON_CLASSES_ARRAY);
        
        loadJsonClasses(jsonClassesArray, dataManager);
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadTestJSONFile(String jsonFilePath) throws IOException
    {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    /**
     * Loads the JSONData from the JSONArray that is passed as the argument
     * and initialized the dataManager based on that.
     * @param jsonClassesArray
     * the array from where to retrieve the stored shapes.
     * @param dataManager 
     * the dataManager component to save loaded data in.
     */
    private void loadJsonClasses(JsonArray jsonClassesArray, DataManager dataManager)
    {
        for (int i = 0; i < jsonClassesArray.size(); i++)
        {
            JsonObject jsoClass = jsonClassesArray.getJsonObject(i);
            ClassObject classObject = loadClassObject(jsoClass);
            dataManager.addClassObject(classObject);
        }
    }
    
    /**
     * Helper method to make and return a class object from a Json Class Object.
     * @param jsoClass
     * the json class object
     * @return 
     * the class object to be added to data manager
     */
    private ClassObject loadClassObject(JsonObject jsoClass)
    {
        ClassObject classObject;
        
        // NOW GET ALL THE VALUES SAVED IN THE CLASS JSON OBJECT
        String className = jsoClass.getString(JSON_CLASS_NAME);
        String packageName = jsoClass.getString(JSON_PACKAGE_NAME);
        if (packageName.equals("null"))
            packageName = null;
        String parentName = jsoClass.getString(JSON_PARENT_NAME);
        if (parentName.equals("null"))
            parentName = null;
        boolean interfaceType = jsoClass.getBoolean(JSON_IS_INTERFACE);
        ArrayList<String> interfaceNames = loadInterfaceNames(jsoClass);
        ArrayList<VariableObject> variables = loadVariables(jsoClass);
        ArrayList<MethodObject> methods = loadMethods(jsoClass);
        ArrayList<String> javaApiPackages = loadJavaApiPackages(jsoClass);
        Box box = loadBoxObject(jsoClass);
        
        classObject = new ClassObject();
        classObject.setClassName(className);
        classObject.setBox(box);
        classObject.setParentName(parentName);
        classObject.setPackageName(packageName);
        classObject.setInterfaceType(interfaceType);
        classObject.setInterfaceNames(interfaceNames);
        classObject.setVariables(variables);
        classObject.setMethods(methods);
        classObject.setJavaApiPackages(javaApiPackages);
        
        return classObject;
    }
    
    /**
     * Helper method to load the interface names
     * @param jsoClass
     * the json class object from which the interface names are to be loaded
     * @return 
     * list of interface names
     */
    private ArrayList<String> loadInterfaceNames(JsonObject jsoClass)
    {
        ArrayList<String> interfaceNames = new ArrayList<>();
        JsonArray interfaceNamesJsonArray = jsoClass.getJsonArray(JSON_INTERFACE_NAMES);
        
        for (int i = 0; i < interfaceNamesJsonArray.size(); i++)
        {
            String interfaceName = interfaceNamesJsonArray.getString(i);
            interfaceNames.add(interfaceName);
        }
        
        return interfaceNames;
    }
    
    /**
     * Loads the variable object from the json class object.
     * @param jsoClass
     * the json class object
     * @return 
     * the list of variable objects
     */
    private ArrayList<VariableObject> loadVariables(JsonObject jsoClass)
    {
        ArrayList<VariableObject> variables = new ArrayList<>();
        JsonArray variablesJsonArray = jsoClass.getJsonArray(JSON_VARIABLES_ARRAY);
        
        for (int i = 0; i < variablesJsonArray.size(); i++)
        {
            JsonObject jsoVariable = variablesJsonArray.getJsonObject(i);
            VariableObject variable = new VariableObject();
            variable.setName(jsoVariable.getString(JSON_NAME));
            variable.setType(jsoVariable.getString(JSON_TYPE));
            variable.setScope(jsoVariable.getString(JSON_SCOPE));
            variable.setStaticType(jsoVariable.getBoolean(JSON_IS_STATIC));
            variable.setFinalType(jsoVariable.getBoolean(JSON_IS_FINAL));
            variables.add(variable);
        }
        
        return variables;
    }
    
    /**
     * Helper method to load all the method inside the json class object.
     * @param jsoClass
     * the json class object
     * @return 
     * the array list of method objects.
     */
    private ArrayList<MethodObject> loadMethods(JsonObject jsoClass)
    {
        ArrayList<MethodObject> methods = new ArrayList<>();
        JsonArray methodsJsonArray = jsoClass.getJsonArray(JSON_METHODS_ARRAY);
        
        for (int i = 0; i < methodsJsonArray.size(); i++)
        {
            JsonObject jsoMethod = methodsJsonArray.getJsonObject(i);
            MethodObject method = new MethodObject();
            method.setName(jsoMethod.getString(JSON_NAME));
            method.setType(jsoMethod.getString(JSON_TYPE));
            method.setScope(jsoMethod.getString(JSON_SCOPE));
            method.setStaticType(jsoMethod.getBoolean(JSON_IS_STATIC));
            method.setFinalType(jsoMethod.getBoolean(JSON_IS_FINAL));
            method.setAbstractType(jsoMethod.getBoolean(JSON_IS_ABSTRACT));
            
            ArrayList<ArgumentObject> arguments = loadMethodArguments(jsoMethod);
            method.setArguments(arguments);
            
            methods.add(method);
        }
        
        return methods;
    }
    
    /**
     * Helper method to load the argument objects from the json class object.
     * @param jsoMethod
     * the json method object
     * @return 
     * list of argument objects
     */
    private ArrayList<ArgumentObject> loadMethodArguments(JsonObject jsoMethod)
    {
        ArrayList<ArgumentObject> arguments = new ArrayList<>();
        JsonArray argumentsJsonArray = jsoMethod.getJsonArray(JSON_ARGUMENTS_ARRAY);
        
        for (int i = 0; i < argumentsJsonArray.size(); i++)
        {
            JsonObject jsoArgument = argumentsJsonArray.getJsonObject(i);
            ArgumentObject argument = new ArgumentObject();
            argument.setName(jsoArgument.getString(JSON_NAME));
            argument.setType(jsoArgument.getString(JSON_TYPE));
            arguments.add(argument);
        }
        
        return arguments;
    }
    
    /**
     * Helper metho to load the java api package names from the json class object.
     * @param jsoClass
     * the json class object
     * @return 
     * list of api packages
     */
    private ArrayList<String> loadJavaApiPackages(JsonObject jsoClass)
    {
        ArrayList<String> javaApiPackages = new ArrayList<>();
        JsonArray javaApiPackagesJsonArray = jsoClass.getJsonArray(JSON_JAVA_API_PACKAGES_ARRAY);
        
        for (int i = 0; i < javaApiPackagesJsonArray.size(); i++)
        {
            String interfaceName = javaApiPackagesJsonArray.getString(i);
            javaApiPackages.add(interfaceName);
        }
        
        return javaApiPackages;
    }
    
    /**
     * Helper method to load and initialize the box from json class object.
     * @param jsoClass
     * the json class object
     * @return 
     * the box object itself
     */
    private Box loadBoxObject(JsonObject jsoClass)
    {
        // ALSO GET THE X AND Y COORDINATES FOR THE BOX
        JsonObject jsoBox = jsoClass.getJsonObject(JSON_BOX_OBJECT);
        int translateX = jsoBox.getInt(JSON_TRANSLATE_X);
        int translateY = jsoBox.getInt(JSON_TRANSLATE_Y);
        
        Box box = new Box(translateX, translateY);
        box.getClassVBox().getChildren().addAll(loadClassTextFields(jsoBox));
        box.getVariablesVBox().getChildren().addAll(loadVariablesTextFields(jsoBox));
        box.getMethodsVBox().getChildren().addAll(loadMethodsTextFields(jsoBox));
        box.getLineConnectors().addAll(loadLineConnectors(jsoBox));
        
        return box;
    }
    
    /**
     * Helper method to load the class text field to be added to the box object
     * @param jsoBox
     * the box object
     * @return 
     * the list of text field inside the class box
     */
    private ArrayList<Text> loadClassTextFields(JsonObject jsoBox)
    {
        ArrayList<Text> classTextFields = new ArrayList<>();
        
        JsonArray classTextFieldsJsonArray = jsoBox.getJsonArray(JSON_CLASS_TEXT_FIELDS);
        
        for (int i = 0; i < classTextFieldsJsonArray.size(); i++)
        {
            JsonObject jsoText = classTextFieldsJsonArray.getJsonObject(i);
            String textString = jsoText.getString(JSON_TEXT);
            String styleClass = jsoText.getString(JSON_STYLE_CLASS);
            boolean underlined = jsoText.getBoolean(JSON_IS_UNDERLINED);
            Text text = new Text(textString);
            text.getStyleClass().add(styleClass);
            text.setUnderline(underlined);
            classTextFields.add(text);
        }
        
        return classTextFields;
    }
    
    /**
     * Helper method to load the variable text field to be added to the box object
     * @param jsoBox
     * the box object
     * @return 
     * the list of text field inside the variables box
     */
    private ArrayList<Text> loadVariablesTextFields(JsonObject jsoBox)
    {
        ArrayList<Text> variablesTextFields = new ArrayList<>();
        
        JsonArray variablesTextFieldsJsonArray = jsoBox.getJsonArray(JSON_VARIABLES_TEXT_FIELDS);
        
        for (int i = 0; i < variablesTextFieldsJsonArray.size(); i++)
        {
            JsonObject jsoText = variablesTextFieldsJsonArray.getJsonObject(i);
            String textString = jsoText.getString(JSON_TEXT);
            String styleClass = jsoText.getString(JSON_STYLE_CLASS);
            boolean underlined = jsoText.getBoolean(JSON_IS_UNDERLINED);
            Text text = new Text(textString);
            text.getStyleClass().add(styleClass);
            text.setUnderline(underlined);
            variablesTextFields.add(text);
        }
        
        return variablesTextFields;
    }
    
    /**
     * Helper method to load the methods text field to be added to the box object
     * @param jsoBox
     * the box object
     * @return 
     * the list of text field inside the methods box
     */ 
    private ArrayList<Text> loadMethodsTextFields(JsonObject jsoBox)
    {
        ArrayList<Text> methodsTextFields = new ArrayList<>();
        
        JsonArray methodsTextFieldsJsonArray = jsoBox.getJsonArray(JSON_METHODS_TEXT_FIELDS);
        
        for (int i = 0; i < methodsTextFieldsJsonArray.size(); i++)
        {
            JsonObject jsoText = methodsTextFieldsJsonArray.getJsonObject(i);
            String textString = jsoText.getString(JSON_TEXT);
            String styleClass = jsoText.getString(JSON_STYLE_CLASS);
            boolean underlined = jsoText.getBoolean(JSON_IS_UNDERLINED);
            Text text = new Text(textString);
            text.getStyleClass().add(styleClass);
            text.setUnderline(underlined);
            methodsTextFields.add(text);
        }
        
        return methodsTextFields;
    }
    
    private ArrayList<LineConnector> loadLineConnectors(JsonObject jsoBox)
    {
        ArrayList<LineConnector> lineConnectors = new ArrayList<>();
        
        JsonArray lineConnectorsJsonArray = jsoBox.getJsonArray(JSON_LINE_CONNECTORS_ARRAY);
        
        for (int i = 0; i < lineConnectorsJsonArray.size(); i++)
        {
            LineConnector lineConnector = loadLineConnector(lineConnectorsJsonArray.getJsonObject(i));
            lineConnectors.add(lineConnector);
        }
        
        return lineConnectors;
    }
    
    private LineConnector loadLineConnector(JsonObject jsoLineConnector)
    {
        LineConnector lineConnector = new LineConnector();
        ArrayList<Line> lines = new ArrayList<>();
        
        JsonArray linesJsonArray = jsoLineConnector.getJsonArray(JSON_LINES_ARRAY);
        for (int i = 0; i < linesJsonArray.size(); i++)
            lines.add(loadLine(linesJsonArray.getJsonObject(i)));
        
        lineConnector.setLines(lines);
        lineConnector.setType(LineConnectorType.valueOf(
                jsoLineConnector.getString(JSON_LINE_CONNECTOR_TYPE)));
        lineConnector.setEndClassObjectName(jsoLineConnector.getString(
                JSON_LINE_CONNECTOR_END_CLASS_NAME));
        lineConnector.setShape(loadLineConnectorShape(jsoLineConnector));
        return lineConnector;
    }
    
    private Line loadLine(JsonObject jsoLine)
    {
        Line line = new Line();

        line.setStartX(jsoLine.getInt(JSON_START_X));
        line.setStartY(jsoLine.getInt(JSON_START_Y));
        line.setEndX(jsoLine.getInt(JSON_END_X));
        line.setEndY(jsoLine.getInt(JSON_END_Y));
        line.setStrokeWidth(jsoLine.getJsonNumber(
                JSON_LINE_STROKE_WIDTH).doubleValue());

        return line;
    }
    
    private Shape loadLineConnectorShape(JsonObject jsoLineConnector)
    {
        Shape shape = null;
        switch (LineConnectorType.valueOf(
                jsoLineConnector.getString(JSON_LINE_CONNECTOR_TYPE)))
        {
            case DIAMOND:
                shape = loadRectangle(jsoLineConnector.getJsonObject(JSON_LINE_CONNECTOR_SHAPE));
                break;
            case TRIANGLE:
                shape = loadTriangle(jsoLineConnector.getJsonObject(JSON_LINE_CONNECTOR_SHAPE));
                break;
            case ARROW:
                shape = loadTriangle(jsoLineConnector.getJsonObject(JSON_LINE_CONNECTOR_SHAPE));
                break;
            default:
                break;
        }
        return shape;
    }
    
    private Rectangle loadRectangle(JsonObject jsoRectangle)
    {
        Rectangle rect = new Rectangle();
        
        rect.setX(jsoRectangle.getJsonNumber(JSON_RECT_X).doubleValue());
        rect.setY(jsoRectangle.getJsonNumber(JSON_RECT_Y).doubleValue());
        rect.setWidth(jsoRectangle.getJsonNumber(JSON_RECT_WIDTH).doubleValue());
        rect.setHeight(jsoRectangle.getJsonNumber(JSON_RECT_HEIGHT).doubleValue());
        rect.setStroke(Color.valueOf(jsoRectangle.getString(JSON_RECT_STROKE_COLOR)));
        rect.setStrokeWidth(jsoRectangle.getJsonNumber(JSON_RECT_STROKE_WIDTH).doubleValue());
        rect.setFill(Color.valueOf(jsoRectangle.getString(JSON_RECT_FILL_COLOR)));
        rect.setRotate(jsoRectangle.getJsonNumber(JSON_RECT_ROTATE_DEGREES).doubleValue());
        
        return rect;
    }
    
    private Triangle loadTriangle(JsonObject jsoTriangle)
    {
        Triangle triangle = new Triangle();
        
        triangle.setHeight(jsoTriangle.getJsonNumber(JSON_TRIANGLE_HEIGHT).doubleValue());
        triangle.setX(jsoTriangle.getJsonNumber(JSON_TRIANGLE_X).doubleValue());
        triangle.setY(jsoTriangle.getJsonNumber(JSON_TRIANGLE_Y).doubleValue());
        triangle.setStroke(Color.valueOf(jsoTriangle.getString(JSON_TRIANGLE_STROKE_COLOR)));
        triangle.setStrokeWidth(jsoTriangle.getJsonNumber(JSON_TRIANGLE_STROKE_WIDTH).doubleValue());
        triangle.setFill(Color.valueOf(jsoTriangle.getString(JSON_TRIANGLE_FILL_COLOR)));
        
        return triangle;
    }
}
