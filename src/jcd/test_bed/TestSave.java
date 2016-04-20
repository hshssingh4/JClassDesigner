/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import jcd.data.ArgumentObject;
import jcd.data.Box;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import saf.components.AppDataComponent;

/**
 *
 * @author RaniSons
 */
public class TestSave 
{
    public static final String JSON_CLASS_NAME = "class_name";
    public static final String JSON_PACKAGE_NAME = "package_name";
    public static final String JSON_PARENT_NAME = "parent_name";
    public static final String JSON_INTERFACE_NAMES = "interface_names";
    public static final String JSON_CLASSES_ARRAY = "classes";
    public static final String JSON_VARIABLES_ARRAY = "variables";
    public static final String JSON_METHODS_ARRAY = "methods";
    public static final String JSON_ARGUMENTS_ARRAY = "arguments";
    public static final String JSON_NAME = "name";
    public static final String JSON_TYPE = "type";
    public static final String JSON_SCOPE = "scope";
    public static final String JSON_IS_STATIC = "is_static";
    public static final String JSON_IS_FINAL = "is_final";
    public static final String JSON_BOX_OBJECT = "box";
    public static final String JSON_TRANSLATE_X = "translate_x";
    public static final String JSON_TRANSLATE_Y = "translate_y";
    
    public void saveTestData(AppDataComponent data, String filePath) throws IOException 
    {
        // First create the string writer
        StringWriter sw = new StringWriter();
        
        // Then case the data component to a data manager
        DataManager dataManager = (DataManager) data;
        
        // Now create the array builder from the class objects
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        ArrayList<ClassObject> classesList = dataManager.getClassesList();
        
        // Now loop through the class objects and add them to the array builder
        for(ClassObject classObject: classesList)
            arrayBuilder.add(makeClassJsonObject(classObject));
        
        // Now put all the classes in a json array
        JsonArray nodesArray = arrayBuilder.build();
        // THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
		.add(JSON_CLASSES_ARRAY, nodesArray)
		.build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }
    
    public JsonObject makeClassJsonObject(ClassObject classObject)
    {
        // First initialize all the objects that can possibly be null
        String parentName;
        
        parentName = ((classObject.getParentName() == null)? 
                JsonObject.NULL.toString(): classObject.getParentName());
        
        JsonObject jso = Json.createObjectBuilder()
                .add(JSON_CLASS_NAME, classObject.getClassName())
                .add(JSON_PACKAGE_NAME, classObject.getPackageName())
                .add(JSON_PARENT_NAME, parentName)
                .add(JSON_INTERFACE_NAMES, buildInterfaceNamesJsonArray(classObject.getInterfaceNames()))
                .add(JSON_VARIABLES_ARRAY, buildVariablesJsonArray(classObject.getVariables()))
                .add(JSON_METHODS_ARRAY, buildMethodsJsonArray(classObject.getMethods()))
                .add(JSON_BOX_OBJECT, buildBoxJsonObject(classObject.getBox()))
		.build();
	return jso;
    }
    
    private JsonArray buildInterfaceNamesJsonArray(ArrayList<String> interfaceNames)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (String name: interfaceNames)
            arrayBuilder.add(name);
        
        JsonArray jA = arrayBuilder.build();
        return jA;
    }
    
    private JsonArray buildVariablesJsonArray(ArrayList<VariableObject> variables)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        for (VariableObject variable: variables)
        {
            JsonObject jso = Json.createObjectBuilder()
                .add(JSON_NAME, variable.getName())
                .add(JSON_TYPE, variable.getType())
                .add(JSON_SCOPE, variable.getScope())
                .add(JSON_IS_STATIC, variable.isStaticType())
                .add(JSON_IS_FINAL, variable.isFinalType())
		.build();
            arrayBuilder.add(jso);
        }
        
        JsonArray jA = arrayBuilder.build();
	return jA;
    }
    
    private JsonArray buildMethodsJsonArray(ArrayList<MethodObject> methods)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (MethodObject method: methods)
        {
            ArrayList<ArgumentObject> arguments = method.getArguments();
            
            JsonObject jso = Json.createObjectBuilder()
                .add(JSON_NAME, method.getName())
                .add(JSON_TYPE, method.getType())
                .add(JSON_SCOPE, method.getScope())
                .add(JSON_IS_STATIC, method.isStaticType())
                .add(JSON_IS_FINAL, method.isFinalType())
                .add(JSON_ARGUMENTS_ARRAY, (arguments != null) ? 
                        buildArgumentsJsonArray(method.getArguments()) : JsonObject.NULL)
		.build();
            arrayBuilder.add(jso);
        }
        
        JsonArray jA = arrayBuilder.build();
	return jA;
    }
    
    private JsonArray buildArgumentsJsonArray(ArrayList<ArgumentObject> arguments)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (ArgumentObject argument: arguments)
        {
            JsonObject jso = Json.createObjectBuilder()
                .add(JSON_NAME, argument.getName())
                .add(JSON_TYPE, argument.getType())
		.build();
            arrayBuilder.add(jso);
        }
        
        JsonArray jA = arrayBuilder.build();
	return jA;
    }
    
    private JsonObject buildBoxJsonObject(Box box)
    {
        JsonObject jso = Json.createObjectBuilder()
                .add(JSON_TRANSLATE_X, box.getMainVBox().getTranslateX())
                .add(JSON_TRANSLATE_Y, box.getMainVBox().getTranslateY())
		.build();
        
        return jso;
    }
}
