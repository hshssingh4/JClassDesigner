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

/**
 *
 * @author RaniSons
 */
public class CounterTaskClassTest {
    
    public CounterTaskClassTest() {}

    /**
     * Test of loadTestData method, of class TestLoad.
     * @throws java.lang.Exception
     */
    @Test
    public void testCounterTaskClass() throws Exception 
    {
        System.out.println("Testing Counter Task");
        DataManager dataManager = new DataManager();
        String filePath = "./work/CounterTaskClass.json";
        
        // BUILD THE CLASS
        ClassBuilder build = new ClassBuilder();
        build.hardCodeCounterTaskClass(dataManager);
        
        ClassObject savedCounterTask = dataManager.getHashClasses().get("CounterTask");
        int savedXLocation = (int) savedCounterTask.getBox().getMainVBox().getTranslateX();
        int savedYLocation = (int) savedCounterTask.getBox().getMainVBox().getTranslateY();
        String savedVariableName = savedCounterTask.getVariables().get(0).getName();
        String savedVariableType = savedCounterTask.getVariables().get(0).getType();
        
        // NOW SINCE NO METHODS HAVE ARGUMENTS, ADD THEM HERE
        ArgumentObject arg1 = new ArgumentObject();
        arg1.setName("arg1");
        arg1.setType("String");
        ArgumentObject arg2 = new ArgumentObject();
        arg2.setName("arg2");
        arg2.setType("String");
        
        MethodObject method1 = savedCounterTask.getMethods().get(0);
        method1.getArguments().add(arg1);
        method1.getArguments().add(arg2);
        // METHOD ARGUMENTS HAVE NOW BEEN ADDED BEFORE SAVING
       
        
        String savedMethodArgumentOneName = savedCounterTask.getMethods().get(0).getArguments().get(0).getName();
        String savedMethodArgumentOneType = savedCounterTask.getMethods().get(0).getArguments().get(0).getType();
        String savedMethodArgumentTwoName = savedCounterTask.getMethods().get(0).getArguments().get(1).getName();
        String savedMethodArgumentTwoType = savedCounterTask.getMethods().get(0).getArguments().get(1).getType();
        // ****************Line Segment Points Left*********************
        
        
        // NOW SAVE THIS CLASS
        TestSave testSave = new TestSave();
        testSave.saveTestData(dataManager, filePath);
        
        // NOW LOAD IT
        TestLoad testLoad = new TestLoad();
        testLoad.loadTestData(dataManager, filePath);
        
        ClassObject loadedCounterTask = dataManager.getHashClasses().get("CounterTask");
        int loadedXLocation = (int) loadedCounterTask.getBox().getMainVBox().getTranslateX();
        int loadedYLocation = (int) loadedCounterTask.getBox().getMainVBox().getTranslateY();
        String loadedVariableName = loadedCounterTask.getVariables().get(0).getName();
        String loadedVariableType = loadedCounterTask.getVariables().get(0).getType();
        String loadedMethodArgumentOneName = loadedCounterTask.getMethods().get(0).getArguments().get(0).getName();
        String loadedMethodArgumentOneType = loadedCounterTask.getMethods().get(0).getArguments().get(0).getType();
        String loadedMethodArgumentTwoName = loadedCounterTask.getMethods().get(0).getArguments().get(1).getName();
        String loadedMethodArgumentTwoType = loadedCounterTask.getMethods().get(0).getArguments().get(1).getType();
        
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
