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
public class AbstractDesignTest 
{
    public AbstractDesignTest() {}
    
    /**
     * Test of loadTestData method, of class TestLoad.
     * @throws java.lang.Exception
     */
    @Test
    public void testAbstractDesign() throws Exception 
    {
        System.out.println("Testing Abstract Design");
        DataManager dataManager = new DataManager();
        String filePath = "./work/AbstractDesign.json";
        
        // BUILD THE CLASS
        AbstractDesignBuilder build = new AbstractDesignBuilder();
        build.hardCodeAbstractDesign(dataManager);
        
        ClassObject savedGraphicObject = dataManager.getHashClasses().get("GraphicObject");
        int savedXLocation = (int) savedGraphicObject.getBox().getMainVBox().getTranslateX();
        int savedYLocation = (int) savedGraphicObject.getBox().getMainVBox().getTranslateY();
        String savedVariableName = savedGraphicObject.getVariables().get(0).getName();
        String savedVariableType = savedGraphicObject.getVariables().get(0).getType();
        String savedMethodArgumentOneName = savedGraphicObject.getMethods().get(0).getArguments().get(0).getName();
        String savedMethodArgumentOneType = savedGraphicObject.getMethods().get(0).getArguments().get(0).getType();
        String savedMethodArgumentTwoName = savedGraphicObject.getMethods().get(0).getArguments().get(1).getName();
        String savedMethodArgumentTwoType = savedGraphicObject.getMethods().get(0).getArguments().get(1).getType();
        int savedStartXLocation = (int) savedGraphicObject.getLineConnectors().get(0).getLines().get(0).getStartX();
        int savedStartYLocation = (int) savedGraphicObject.getLineConnectors().get(0).getLines().get(0).getStartY();
        int savedEndXLocation = (int) savedGraphicObject.getLineConnectors().get(0).getLines().get(0).getEndX();
        int savedEndYLocation = (int) savedGraphicObject.getLineConnectors().get(0).getLines().get(0).getEndY();
        
        
        // NOW SAVE THIS CLASS
        TestSave testSave = new TestSave();
        testSave.saveTestData(dataManager, filePath);
        
        // NOW LOAD IT
        TestLoad testLoad = new TestLoad();
        testLoad.loadTestData(dataManager, filePath);
        
        ClassObject loadedGraphicObject = dataManager.getHashClasses().get("GraphicObject");
        int loadedXLocation = (int) loadedGraphicObject.getBox().getMainVBox().getTranslateX();
        int loadedYLocation = (int) loadedGraphicObject.getBox().getMainVBox().getTranslateY();
        String loadedVariableName = loadedGraphicObject.getVariables().get(0).getName();
        String loadedVariableType = loadedGraphicObject.getVariables().get(0).getType();
        String loadedMethodArgumentOneName = loadedGraphicObject.getMethods().get(0).getArguments().get(0).getName();
        String loadedMethodArgumentOneType = loadedGraphicObject.getMethods().get(0).getArguments().get(0).getType();
        String loadedMethodArgumentTwoName = loadedGraphicObject.getMethods().get(0).getArguments().get(1).getName();
        String loadedMethodArgumentTwoType = loadedGraphicObject.getMethods().get(0).getArguments().get(1).getType();
        int loadedStartXLocation = (int) loadedGraphicObject.getLineConnectors().get(0).getLines().get(0).getStartX();
        int loadedStartYLocation = (int) loadedGraphicObject.getLineConnectors().get(0).getLines().get(0).getStartY();
        int loadedEndXLocation = (int) loadedGraphicObject.getLineConnectors().get(0).getLines().get(0).getEndX();
        int loadedEndYLocation = (int) loadedGraphicObject.getLineConnectors().get(0).getLines().get(0).getEndY();
        
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
