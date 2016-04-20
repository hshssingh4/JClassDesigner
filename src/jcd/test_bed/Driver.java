/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.io.IOException;
import jcd.data.DataManager;

/**
 *
 * @author RaniSons
 */
public class Driver 
{
    public static void main (String [] args) throws IOException
    {
        DataManager dataManager = new DataManager();
        
        // FIRST BUILD ALL THE 5 CLASSES
        ClassBuilder build = new ClassBuilder();
        build.hardCodeClasses(dataManager);
        // NOW SAVE THOSE CLASSES
        TestSave testSave = new TestSave();
        testSave.saveTestData(dataManager, "./work/DesignSaveTest.json");
        
        // LOAD THE FILE USING THE TEST LOAD CLASS
        TestLoad testLoad = new TestLoad();
        testLoad.loadTestData(dataManager, "./work/DesignSaveTest.json");
        // NOW PRINT THE DATA THAT WAS LOADED
        PrintDataManager printDataManager = new PrintDataManager();
        printDataManager.printLoadedData(dataManager);
    }
}
