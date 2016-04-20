/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.util.ArrayList;
import jcd.data.ArgumentObject;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import static jcd.test_bed.TestLoad.JSON_CLASS_NAME;
import static jcd.test_bed.TestLoad.JSON_IS_INTERFACE;
import static jcd.test_bed.TestLoad.JSON_NAME;
import static jcd.test_bed.TestLoad.JSON_PACKAGE_NAME;
import static jcd.test_bed.TestLoad.JSON_PARENT_NAME;
import static jcd.test_bed.TestLoad.JSON_SCOPE;
import static jcd.test_bed.TestLoad.JSON_TYPE;

/**
 *
 * @author RaniSons
 */
public class PrintDataManager 
{
    public PrintDataManager() {}
    
    // BELOW IS THE PRINTING OF THE LOADED DATA TO THE CONSOLE FOR HOMEWORK 5
    public void printLoadedData(DataManager dataManager)
    {
        for (ClassObject classObject: dataManager.getClassesList())
        {
            printNewLines(2);
            printHashLines(3);
            printClassObject(classObject);
            printHashLines(3);
            printNewLines(2);
        }
    }
    
    private void printClassObject(ClassObject classObject)
    {
        // First print the className, packageName, and parentName
        printKeyValuePair(JSON_CLASS_NAME, classObject.getClassName());
        printKeyValuePair(JSON_PACKAGE_NAME, classObject.getPackageName());
        printKeyValuePair(JSON_PARENT_NAME, classObject.getParentName());
        printKeyValuePair(JSON_IS_INTERFACE, Boolean.toString(classObject.isInterfaceType()));
        printInterfaceNames(classObject.getInterfaceNames());
        printVariables(classObject.getVariables());
        printMethods(classObject.getMethods());
    }
    
    private void printInterfaceNames(ArrayList<String> interfaceNames)
    {
        System.out.print("Interfaces:\t");
        for (int i = 0; i < interfaceNames.size(); i++)
        {
            if (i == interfaceNames.size() - 1)
            {
                System.out.print(interfaceNames.get(i));
            }
            else
                System.out.print(interfaceNames.get(i) + ",");
        }
        printNewLines(1);
    }
    
    private void printVariables(ArrayList<VariableObject> variables)
    {
        System.out.println("\nVariables:");
        for (int i = 0; i < variables.size(); i++)
        {
            printDashLines(2);
            System.out.println("Variable " + (i+1));
            printVariable(variables.get(i));
        }
        printNewLines(1);
    }
    
    private void printVariable(VariableObject variable)
    {
        printKeyValuePair(JSON_NAME, variable.getName());
        printKeyValuePair(JSON_TYPE, variable.getType());
        printKeyValuePair(JSON_SCOPE, variable.getScope());
    }
    
    private void printMethods(ArrayList<MethodObject> methods)
    {
        System.out.println("\nMethods:");
        for (int i = 0; i < methods.size(); i++)
        {
            printDashLines(2);
            System.out.println("Method " + (i+1));
            printMethod(methods.get(i));
        }
        printNewLines(1);
    }
    
    private void printMethod(MethodObject method)
    {
        printKeyValuePair(JSON_NAME, method.getName());
        printKeyValuePair(JSON_TYPE, method.getType());
        printKeyValuePair(JSON_SCOPE, method.getScope());
        printMethodArguments(method.getArguments());
    }
    
    private void printMethodArguments(ArrayList<ArgumentObject> arguments)
    {
        System.out.println("\nArguments:");
        for (int i = 0; i < arguments.size(); i++)
        {
            printDashLines(1);
            System.out.println("Argument " + (i+1));
            printArgument(arguments.get(i));
        }
        printNewLines(1);
    }
    
    private void printArgument(ArgumentObject argument)
    {
        printKeyValuePair(JSON_NAME, argument.getName());
        printKeyValuePair(JSON_TYPE, argument.getType());
    }
    
    private void printKeyValuePair(String key, String value)
    {
        System.out.println(key + ":\t" + value);
    }
    
    private void printHashLines(int lines)
    {
        for (int i = 0; i < lines; i++)
            System.out.println("###############################################");
    }
    
    private void printDashLines(int lines)
    {
        for (int i = 0; i < lines; i++)
            System.out.println("-----------------------------------------------");
    }
    
    private void printNewLines(int lines)
    {
        for (int i = 0; i < lines; i++)
            System.out.println("");
    }
    
    private void printTabs(int number)
    {
        for (int i = 0; i < number; i++)
            System.out.print("\t");
    }
}
