/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.test_bed.TestLoad;
import jcd.test_bed.TestSave;
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
        TestSave testSave = new TestSave();
        testSave.saveTestData((DataManager) data, filePath);
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException
    {
        System.out.println("Load Data");
        
        // NOW LOAD IT
        TestLoad testLoad = new TestLoad();
        testLoad.loadTestData((DataManager) data, filePath);
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
        // First cast the data component to a data manager
        DataManager dataManager = (DataManager) data;
        
        // Now create all the needed directories
        for (ClassObject classObject: dataManager.getClassesList())
        {
            String[] tokens = classObject.getPackageName().split("[.]");
            
            String tempFilePath = filePath;
            for (String packageName: tokens)
            {
                String packagePath = tempFilePath + "/" + packageName;
                File file = new File(packagePath);
                
                if(!file.exists())
                    file.mkdir();
                
                tempFilePath = packagePath;
            }
        }
        
        
        
        
        
        
        
        
        
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException
    {
        System.out.println("Import Data");
    }
    
}
