/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import jcd.data.ClassObject;
import jcd.data.DataManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author RaniSons
 */
public class ThreadExampleClassTest 
{
    public ThreadExampleClassTest() {}

    /**
     * Test of loadTestData method, of class TestLoad.
     * @throws java.lang.Exception
     */
    @Test
    public void testThreadExample() throws Exception 
    {
        System.out.println("Testing Thread Example");
        DataManager dataManager = new DataManager();
        String filePath = "./work/ThreadExampleClass.json";
        
        // BUILD THE CLASS
        ThreadExampleClassBuilder build = new ThreadExampleClassBuilder();
        build.hardCodeThreadExampleClass(dataManager);
        
        ClassObject savedThreadExample = dataManager.getHashClasses().get("ThreadExample");
        int savedXLocation = (int) savedThreadExample.getBox().getMainVBox().getTranslateX();
        int savedYLocation = (int) savedThreadExample.getBox().getMainVBox().getTranslateY();
        String savedVariableName = savedThreadExample.getVariables().get(0).getName();
        String savedVariableType = savedThreadExample.getVariables().get(0).getType();
        String savedMethodArgumentOneName = savedThreadExample.getMethods().get(0).getArguments().get(0).getName();
        String savedMethodArgumentOneType = savedThreadExample.getMethods().get(0).getArguments().get(0).getType();
        String savedMethodArgumentTwoName = savedThreadExample.getMethods().get(0).getArguments().get(1).getName();
        String savedMethodArgumentTwoType = savedThreadExample.getMethods().get(0).getArguments().get(1).getType();
        int savedStartXLocation = (int) savedThreadExample.getLineConnectors().get(0).getLines().get(0).getStartX();
        int savedStartYLocation = (int) savedThreadExample.getLineConnectors().get(0).getLines().get(0).getStartY();
        int savedEndXLocation = (int) savedThreadExample.getLineConnectors().get(0).getLines().get(0).getEndX();
        int savedEndYLocation = (int) savedThreadExample.getLineConnectors().get(0).getLines().get(0).getEndY();
        
        
        // NOW SAVE THIS CLASS
        TestSave testSave = new TestSave();
        testSave.saveTestData(dataManager, filePath);
        
        // NOW LOAD IT
        TestLoad testLoad = new TestLoad();
        testLoad.loadTestData(dataManager, filePath);
        
        ClassObject loadedThreadExample = dataManager.getHashClasses().get("ThreadExample");
        int loadedXLocation = (int) loadedThreadExample.getBox().getMainVBox().getTranslateX();
        int loadedYLocation = (int) loadedThreadExample.getBox().getMainVBox().getTranslateY();
        String loadedVariableName = loadedThreadExample.getVariables().get(0).getName();
        String loadedVariableType = loadedThreadExample.getVariables().get(0).getType();
        String loadedMethodArgumentOneName = loadedThreadExample.getMethods().get(0).getArguments().get(0).getName();
        String loadedMethodArgumentOneType = loadedThreadExample.getMethods().get(0).getArguments().get(0).getType();
        String loadedMethodArgumentTwoName = loadedThreadExample.getMethods().get(0).getArguments().get(1).getName();
        String loadedMethodArgumentTwoType = loadedThreadExample.getMethods().get(0).getArguments().get(1).getType();
        int loadedStartXLocation = (int) loadedThreadExample.getLineConnectors().get(0).getLines().get(0).getStartX();
        int loadedStartYLocation = (int) loadedThreadExample.getLineConnectors().get(0).getLines().get(0).getStartY();
        int loadedEndXLocation = (int) loadedThreadExample.getLineConnectors().get(0).getLines().get(0).getEndX();
        int loadedEndYLocation = (int) loadedThreadExample.getLineConnectors().get(0).getLines().get(0).getEndY();
        
        // AND NOW CHECK WHAT HAS BEEN LOADED
        assertEquals(savedXLocation, loadedXLocation);
        assertEquals(savedYLocation, loadedYLocation);
        assertEquals(savedVariableName, loadedVariableName);
        assertEquals(savedVariableType, loadedVariableType);
        assertEquals(savedMethodArgumentOneName, loadedMethodArgumentOneName);
        assertEquals(savedMethodArgumentTwoName, loadedMethodArgumentTwoName);
        assertEquals(savedMethodArgumentOneType, loadedMethodArgumentOneType);
        assertEquals(savedMethodArgumentTwoType, loadedMethodArgumentTwoType);
        assertEquals(savedStartXLocation, loadedStartXLocation);
        assertEquals(savedStartYLocation, loadedStartYLocation);
        assertEquals(savedEndXLocation, loadedEndXLocation);
        assertEquals(savedEndYLocation, loadedEndYLocation);
    }
    
}
