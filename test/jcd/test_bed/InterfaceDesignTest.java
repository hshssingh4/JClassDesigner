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
public class InterfaceDesignTest 
{
    public InterfaceDesignTest() {}

    /**
     * Test of loadTestData method, of class TestLoad.
     * @throws java.lang.Exception
     */
    @Test
    public void testInterfaceDesign() throws Exception 
    {
        System.out.println("Testing App Style Arbiter");
        DataManager dataManager = new DataManager();
        String filePath = "./work/InterfaceDesign.json";
        
        // BUILD THE CLASS
        InterfaceDesignBuilder build = new InterfaceDesignBuilder();
        build.hardCodeInterfaceDesign(dataManager);
        
        ClassObject savedAppStyleArbiter = dataManager.getHashClasses().get("AppStyleArbiter");
        int savedXLocation = (int) savedAppStyleArbiter.getBox().getMainVBox().getTranslateX();
        int savedYLocation = (int) savedAppStyleArbiter.getBox().getMainVBox().getTranslateY();
        String savedVariableName = savedAppStyleArbiter.getVariables().get(0).getName();
        String savedVariableType = savedAppStyleArbiter.getVariables().get(0).getType();
        
        String savedMethodArgumentOneName = savedAppStyleArbiter.getMethods().get(0).getArguments().get(0).getName();
        String savedMethodArgumentOneType = savedAppStyleArbiter.getMethods().get(0).getArguments().get(0).getType();
        String savedMethodArgumentTwoName = savedAppStyleArbiter.getMethods().get(0).getArguments().get(1).getName();
        String savedMethodArgumentTwoType = savedAppStyleArbiter.getMethods().get(0).getArguments().get(1).getType();
        int savedStartXLocation = (int) savedAppStyleArbiter.getLineConnectors().get(0).getLines().get(0).getStartX();
        int savedStartYLocation = (int) savedAppStyleArbiter.getLineConnectors().get(0).getLines().get(0).getStartY();
        int savedEndXLocation = (int) savedAppStyleArbiter.getLineConnectors().get(0).getLines().get(0).getEndX();
        int savedEndYLocation = (int) savedAppStyleArbiter.getLineConnectors().get(0).getLines().get(0).getEndY();
        
        
        // NOW SAVE THIS CLASS
        TestSave testSave = new TestSave();
        testSave.saveTestData(dataManager, filePath);
        
        // NOW LOAD IT
        TestLoad testLoad = new TestLoad();
        testLoad.loadTestData(dataManager, filePath);
        
        ClassObject loadedAppStyleArbiter = dataManager.getHashClasses().get("AppStyleArbiter");
        int loadedXLocation = (int) loadedAppStyleArbiter.getBox().getMainVBox().getTranslateX();
        int loadedYLocation = (int) loadedAppStyleArbiter.getBox().getMainVBox().getTranslateY();
        String loadedVariableName = loadedAppStyleArbiter.getVariables().get(0).getName();
        String loadedVariableType = loadedAppStyleArbiter.getVariables().get(0).getType();
        String loadedMethodArgumentOneName = loadedAppStyleArbiter.getMethods().get(0).getArguments().get(0).getName();
        String loadedMethodArgumentOneType = loadedAppStyleArbiter.getMethods().get(0).getArguments().get(0).getType();
        String loadedMethodArgumentTwoName = loadedAppStyleArbiter.getMethods().get(0).getArguments().get(1).getName();
        String loadedMethodArgumentTwoType = loadedAppStyleArbiter.getMethods().get(0).getArguments().get(1).getType();
        int loadedStartXLocation = (int) loadedAppStyleArbiter.getLineConnectors().get(0).getLines().get(0).getStartX();
        int loadedStartYLocation = (int) loadedAppStyleArbiter.getLineConnectors().get(0).getLines().get(0).getStartY();
        int loadedEndXLocation = (int) loadedAppStyleArbiter.getLineConnectors().get(0).getLines().get(0).getEndX();
        int loadedEndYLocation = (int) loadedAppStyleArbiter.getLineConnectors().get(0).getLines().get(0).getEndY();
        
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
