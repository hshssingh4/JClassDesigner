/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
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
import jcd.data.LineConnector;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import saf.components.AppDataComponent;
import static jcd.file.FileManager.*;

/**
 *
 * @author RaniSons
 */
public class FileSaver 
{
    public FileSaver() {}
    
    public void saveData(AppDataComponent data, String filePath) throws IOException 
    {
        // First create the string writer
        StringWriter sw = new StringWriter();
        
        // Then cast the data component to a data manager
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
        String parentName, packageName;
        
        parentName = ((classObject.getParentName() == null)? 
                JsonObject.NULL.toString(): classObject.getParentName());
        packageName = ((classObject.getPackageName() == null)? 
                JsonObject.NULL.toString(): classObject.getPackageName());
        
        JsonObject jso = Json.createObjectBuilder()
                .add(JSON_CLASS_NAME, classObject.getClassName())
                .add(JSON_PACKAGE_NAME, packageName)
                .add(JSON_PARENT_NAME, parentName)
                .add(JSON_IS_INTERFACE, classObject.isInterfaceType())
                .add(JSON_INTERFACE_NAMES, buildInterfaceNamesJsonArray(classObject.getInterfaceNames()))
                .add(JSON_VARIABLES_ARRAY, buildVariablesJsonArray(classObject.getVariables()))
                .add(JSON_METHODS_ARRAY, buildMethodsJsonArray(classObject.getMethods()))
                .add(JSON_JAVA_API_PACKAGES_ARRAY, buildJavaApiPackagesJsonArray(classObject.getJavaApiPackages()))
                .add(JSON_BOX_OBJECT, buildBoxJsonObject(classObject.getBox()))
                .add(JSON_LINE_CONNECTORS_ARRAY, buildLineConnectorsJsonArray(classObject.getLineConnectors()))
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
                .add(JSON_IS_ABSTRACT, method.isAbstractType())
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
    
    private JsonArray buildJavaApiPackagesJsonArray(ArrayList<String> javaApiPackages)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (String name: javaApiPackages)
            arrayBuilder.add(name);
        
        JsonArray jA = arrayBuilder.build();
        return jA;
    }
    
    private JsonObject buildBoxJsonObject(Box box)
    {
        JsonObject jso = Json.createObjectBuilder()
                .add(JSON_TRANSLATE_X, box.getMainVBox().getTranslateX())
                .add(JSON_TRANSLATE_Y, box.getMainVBox().getTranslateY())
                .add(JSON_CLASS_TEXT_FIELDS, buildClassTextFieldsJsonArray(box))
                .add(JSON_VARIABLES_TEXT_FIELDS, buildVariablesTextFieldsJsonArray(box))
                .add(JSON_METHODS_TEXT_FIELDS, buildMethodsTextFieldsJsonArray(box))
		.build();
        
        return jso;
    }
    
    private JsonArray buildClassTextFieldsJsonArray(Box box)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (Object obj: box.getClassVBox().getChildren())
        {
            Text text = (Text) obj;
            JsonObject jso = Json.createObjectBuilder()
                .add(JSON_TEXT, text.getText())
                .add(JSON_STYLE_CLASS, text.getStyleClass().get(0))
                .add(JSON_IS_UNDERLINED, text.isUnderline())
		.build();
            arrayBuilder.add(jso);
        }
        
        JsonArray jA = arrayBuilder.build();
	return jA;
    }
    
    private JsonArray buildVariablesTextFieldsJsonArray(Box box)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (Object obj: box.getVariablesVBox().getChildren())
        {
            Text text = (Text) obj;
            JsonObject jso = Json.createObjectBuilder()
                .add(JSON_TEXT, text.getText())
                .add(JSON_STYLE_CLASS, text.getStyleClass().get(0))
                .add(JSON_IS_UNDERLINED, text.isUnderline())
		.build();
            arrayBuilder.add(jso);
        }
        
        JsonArray jA = arrayBuilder.build();
	return jA;
    }
    
    private JsonArray buildMethodsTextFieldsJsonArray(Box box)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (Object obj: box.getMethodsVBox().getChildren())
        {
            Text text = (Text) obj;
            JsonObject jso = Json.createObjectBuilder()
                .add(JSON_TEXT, text.getText())
                .add(JSON_STYLE_CLASS, text.getStyleClass().get(0))
                .add(JSON_IS_UNDERLINED, text.isUnderline())
		.build();
            arrayBuilder.add(jso);
        }
        
        JsonArray jA = arrayBuilder.build();
	return jA;
    }
    
    private JsonArray buildLineConnectorsJsonArray(ArrayList<LineConnector> lineConnectors)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (LineConnector lineConnector: lineConnectors)
            arrayBuilder.add(buildLineConnectorJsonArray(lineConnector));
        
            
        JsonArray jA = arrayBuilder.build();
	return jA;
    }
    
    private JsonArray buildLineConnectorJsonArray(LineConnector lineConnector)
    {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        for (Line line: lineConnector.getLines())
        {
            JsonObject jso = Json.createObjectBuilder()
                .add(JSON_START_X, line.getStartX())
                .add(JSON_START_Y, line.getStartY())
                .add(JSON_END_X, line.getEndX())
                .add(JSON_END_Y, line.getEndY())
		.build();
            arrayBuilder.add(jso);
        }
        
        JsonArray jA = arrayBuilder.build();
	return jA;
    }
}
