/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import jcd.data.ArgumentObject;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.MethodObject;
import org.junit.Test;
import static org.junit.Assert.*;
import saf.components.AppDataComponent;

/**
 *
 * @author RaniSons
 */
public class StartHandlerClassTest {
    
    public StartHandlerClassTest() {}
    
    /**
     * Test of loadTestData method, of class TestLoad.
     * @throws java.lang.Exception
     */
    @Test
    public void testStartHandlerClass() throws Exception 
    {
        System.out.println("Testing Start Handler");
        DataManager dataManager = new DataManager();
        String filePath = "./work/StartHandlerClass.json";
        
        // BUILD THE CLASS
        ClassBuilder build = new ClassBuilder();
        build.hardCodeStartHandlerClass(dataManager);
        
        ClassObject savedStartHandler = dataManager.getHashClasses().get("StartHandler");
        int savedXLocation = (int) savedStartHandler.getBox().getMainVBox().getTranslateX();
        int savedYLocation = (int) savedStartHandler.getBox().getMainVBox().getTranslateY();
        String savedVariableName = savedStartHandler.getVariables().get(0).getName();
        String savedVariableType = savedStartHandler.getVariables().get(0).getType();
        
        // NOW SINCE METHODS HAVE ONLY ONE ARGUMENT, ADD ONE MORE HERE
        ArgumentObject arg1 = new ArgumentObject();
        arg1.setName("arg1");
        arg1.setType("String");
        
        MethodObject method1 = savedStartHandler.getMethods().get(0);
        method1.getArguments().add(arg1);
        // METHOD ARGUMENT HAS NOW BEEN ADDED BEFORE SAVING
       
        
        String savedMethodArgumentOneName = savedStartHandler.getMethods().get(0).getArguments().get(0).getName();
        String savedMethodArgumentOneType = savedStartHandler.getMethods().get(0).getArguments().get(0).getType();
        String savedMethodArgumentTwoName = savedStartHandler.getMethods().get(0).getArguments().get(1).getName();
        String savedMethodArgumentTwoType = savedStartHandler.getMethods().get(0).getArguments().get(1).getType();
        // ****************Line Segment Points Left*********************
        
        
        // NOW SAVE THIS CLASS
        TestSave testSave = new TestSave();
        testSave.saveTestData(dataManager, filePath);
        
        // NOW LOAD IT
        TestLoad testLoad = new TestLoad();
        testLoad.loadTestData(dataManager, filePath);
        
        ClassObject loadedStartHandler = dataManager.getHashClasses().get("StartHandler");
        int loadedXLocation = (int) loadedStartHandler.getBox().getMainVBox().getTranslateX();
        int loadedYLocation = (int) loadedStartHandler.getBox().getMainVBox().getTranslateY();
        String loadedVariableName = loadedStartHandler.getVariables().get(0).getName();
        String loadedVariableType = loadedStartHandler.getVariables().get(0).getType();
        String loadedMethodArgumentOneName = loadedStartHandler.getMethods().get(0).getArguments().get(0).getName();
        String loadedMethodArgumentOneType = loadedStartHandler.getMethods().get(0).getArguments().get(0).getType();
        String loadedMethodArgumentTwoName = loadedStartHandler.getMethods().get(0).getArguments().get(1).getName();
        String loadedMethodArgumentTwoType = loadedStartHandler.getMethods().get(0).getArguments().get(1).getType();
        
        // AND NOW CHECK WHAT HAS BEEN LOADED
        assertEquals(savedXLocation, loadedXLocation);
        assertEquals(savedYLocation, loadedYLocation);
        assertEquals(savedVariableName, loadedVariableName);
        assertEquals(savedVariableType, loadedVariableType);
        assertEquals(savedMethodArgumentOneName, loadedMethodArgumentOneName);
        assertEquals(savedMethodArgumentTwoName, loadedMethodArgumentTwoName);
        assertEquals(savedMethodArgumentOneType, loadedMethodArgumentOneType);
        assertEquals(savedMethodArgumentTwoType, loadedMethodArgumentTwoType);
    }
}
