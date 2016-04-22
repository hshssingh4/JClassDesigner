/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javafx.scene.shape.Line;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import jcd.data.ArgumentObject;
import jcd.data.Box;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.LineConnector;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import saf.components.AppDataComponent;

/**
 *
 * @author RaniSons
 */
public class TestLoad 
{
    public static final String JSON_CLASS_NAME = "class_name";
    public static final String JSON_PACKAGE_NAME = "package_name";
    public static final String JSON_PARENT_NAME = "parent_name";
    public static final String JSON_IS_INTERFACE = "is_interface";
    public static final String JSON_INTERFACE_NAMES = "interface_names";
    public static final String JSON_CLASSES_ARRAY = "classes";
    public static final String JSON_VARIABLES_ARRAY = "variables";
    public static final String JSON_METHODS_ARRAY = "methods";
    public static final String JSON_JAVA_API_PACKAGES_ARRAY = "java_api_packages";
    public static final String JSON_LINE_CONNECTORS_ARRAY = "line_connectors";
    public static final String JSON_ARGUMENTS_ARRAY = "arguments";
    public static final String JSON_NAME = "name";
    public static final String JSON_TYPE = "type";
    public static final String JSON_SCOPE = "scope";
    public static final String JSON_IS_STATIC = "is_static";
    public static final String JSON_IS_FINAL = "is_final";
    public static final String JSON_IS_ABSTRACT = "is_abstract";
    public static final String JSON_BOX_OBJECT = "box";
    public static final String JSON_TRANSLATE_X = "translate_x";
    public static final String JSON_TRANSLATE_Y = "translate_y";
    public static final String JSON_START_X = "start_x";
    public static final String JSON_START_Y = "start_y";
    public static final String JSON_END_X = "end_x";
    public static final String JSON_END_Y = "end_y";
    
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
    public void loadTestData(AppDataComponent data, String filePath) throws IOException
    {
        // CLEAR THE OLD DATA OUT
	DataManager dataManager = (DataManager)data;
	dataManager.reset();
        
        // LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadTestJSONFile(filePath);
        
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
        ArrayList<LineConnector> lineConnectors = loadLineConnectors(jsoClass);
        
        // ALSO GET THE X AND Y COORDINATES FOR THE BOX
        JsonObject jsoBox = jsoClass.getJsonObject(JSON_BOX_OBJECT);
        int translateX = jsoBox.getInt(JSON_TRANSLATE_X);
        int translateY = jsoBox.getInt(JSON_TRANSLATE_Y);
        
        Box box = new Box();
        box.getMainVBox().setTranslateX(translateX);
        box.getMainVBox().setTranslateY(translateY);
        
        classObject = new ClassObject(className, box);
        classObject.setParentName(parentName);
        classObject.setPackageName(packageName);
        classObject.setInterfaceType(interfaceType);
        classObject.setInterfaceNames(interfaceNames);
        classObject.setVariables(variables);
        classObject.setMethods(methods);
        classObject.setJavaApiPackages(javaApiPackages);
        classObject.setLineConnectors(lineConnectors);
        
        return classObject;
    }
    
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
    
    private ArrayList<LineConnector> loadLineConnectors(JsonObject jsoClass)
    {
        ArrayList<LineConnector> lineConnectors = new ArrayList<>();
        
        JsonArray lineConnectorsJsonArray = jsoClass.getJsonArray(JSON_LINE_CONNECTORS_ARRAY);
        
        for (int i = 0; i < lineConnectorsJsonArray.size(); i++)
        {
            LineConnector lineConnector = loadLineConnector(lineConnectorsJsonArray.getJsonArray(i));
            lineConnectors.add(lineConnector);
        }
        
        return lineConnectors;
    }
    
    private LineConnector loadLineConnector(JsonArray jsoLineConnector)
    {
        LineConnector lineConnector = new LineConnector();
        
        for (int i = 0; i < jsoLineConnector.size(); i++)
        {
            Line line = new Line();
            JsonObject jsoLine = jsoLineConnector.getJsonObject(i);
            
            line.setStartX(jsoLine.getInt(JSON_START_X));
            line.setStartY(jsoLine.getInt(JSON_START_Y));
            line.setEndX(jsoLine.getInt(JSON_END_X));
            line.setEndY(jsoLine.getInt(JSON_END_Y));
            
            lineConnector.getLines().add(line);
        }
        
        return lineConnector;
    }
}
