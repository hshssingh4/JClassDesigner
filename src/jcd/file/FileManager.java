/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;

/**
 *
 * @author RaniSons
 */
public class FileManager implements AppFileComponent
{

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException 
    {
        System.out.println("Save Data");
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException
    {
        System.out.println("Load Data");
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException
    {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }
    
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException 
    {
        System.out.println("Export Data");
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException
    {
        System.out.println("Import Data");
    }
    
}
