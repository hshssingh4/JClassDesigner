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
import jcd.data.ClassObject;
import jcd.data.DataManager;
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
    public static final String JSON_INTERFACE_NAME = "interface_name";
    public static final String JSON_CLASSES_ARRAY = "classes";
    
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
        String parentName, interfaceName;
        
        parentName = ((classObject.getParentName() == null)? 
                JsonObject.NULL.toString(): classObject.getParentName());
        interfaceName = ((classObject.getInterfaceName() == null)? 
                JsonObject.NULL.toString(): classObject.getInterfaceName());
        
        JsonObject jso = Json.createObjectBuilder()
                .add(JSON_CLASS_NAME, classObject.getClassName())
                .add(JSON_PACKAGE_NAME, classObject.getPackageName())
                .add(JSON_PARENT_NAME, parentName)
                .add(JSON_INTERFACE_NAME, interfaceName)
		.build();
	return jso;
    }
}
