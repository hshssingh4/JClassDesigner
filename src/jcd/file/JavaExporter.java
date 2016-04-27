/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import jcd.data.ArgumentObject;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import static jcd.file.FileManager.*;
import saf.components.AppDataComponent;

/**
 *
 * @author RaniSons
 */
public class JavaExporter 
{
    public JavaExporter() {}
    
    public void exportToSourceCode(AppDataComponent data, String filePath) throws IOException 
    {
        // First cast the data component to a data manager
        DataManager dataManager = (DataManager) data;
        
        // Now create all the needed directories
        for (ClassObject classObject: dataManager.getClassesList())
        {
            if (classObject.getPackageName() != null)
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
                
                String javaFilePath = tempFilePath;
                File javaFile = new File(javaFilePath + "/" + classObject.getClassName() + ".java");
                if (!javaFile.exists())
                    javaFile.createNewFile();
            }
        }
        
        // Since now we have all the directories, we can start writing to these files
        for (ClassObject classObject: dataManager.getClassesList())
        {
            if (classObject.getPackageName() != null)
            {
                // First get the java class file path for the print writer
                String packageName = classObject.getPackageName().replaceAll("[.]", "/");
                String className = classObject.getClassName();
                
                String javaClassFilePath = filePath + "/" + packageName + "/" + className + ".java";
                
                // Now create the print writer for the appropriate file
                PrintWriter pw = new PrintWriter(javaClassFilePath);
                
                // And now start writing using other helper methods
                writeJavaClass(dataManager, classObject, pw);
                
                // Finally, close the writer
                pw.close();
            }
        }
    }
    
    /**
     * Helper method to write the java class to the .java file.
     * @param classObject
     * the class to be written
     * @param pw 
     * the print writer object to aid in writing to file
     */
    private void writeJavaClass(DataManager dataManager, ClassObject classObject, PrintWriter pw)
    {
        // First print a few extra lines
        pw.println(WELCOME_STRING);
        writePackageNameLine(classObject, pw); // Write package name
        pw.println();
        
        // NOW WRITE THE IMPORT STATEMENTS
        writeJavaApiPackageImports(classObject, pw);
        writeUserClassImports(dataManager, classObject, pw);
        pw.println();
        
        writeClassNameLine(classObject, pw); // Write class name
        pw.println(OPENING_CURLY_BRACE);
        
        // Now write all the variables
        writeVariables(classObject, pw);
        
        // Now write all the methods
        writeMethods(classObject, pw);
        
        // Finally, close the class by printing a curly brace
        pw.println(CLOSING_CURLY_BRACE);
    }
    
    private void writeJavaApiPackageImports(ClassObject classObject, PrintWriter pw)
    {
        for (String importString: classObject.getJavaApiPackages())
            pw.println(fetchImportLine(importString));
    }
    
    private void writeUserClassImports(DataManager dataManager, ClassObject classObject, PrintWriter pw)
    {
        // First get the relationship classes that this class object has
        // If yes, write an import statement for that
        ArrayList<ClassObject> relationshipClasses = dataManager.fetchRelationshipsList(classObject);
        for (ClassObject obj: relationshipClasses)
        {
            String packageName = obj.getPackageName();
            String className = obj.getClassName();
            String importString = packageName + PERIOD + className;
            if (!classObject.getPackageName().equals(obj.getPackageName()))
                pw.println(fetchImportLine(importString));
        }
    }
    
    private String fetchImportLine(String importString)
    {
        return IMPORT + SPACE + importString + SEMI_COLON;
    }
    
    private void writePackageNameLine(ClassObject classObject, PrintWriter pw)
    {
        String packageNameLine = PACKAGE + SPACE + classObject.getPackageName() + SEMI_COLON;
        pw.println(packageNameLine); // Print package name followed by a newline
    }
    
    private void writeClassNameLine(ClassObject classObject, PrintWriter pw)
    {
        String classNameLine = PUBLIC;
        if (classObject.isInterfaceType())
            classNameLine += SPACE + INTERFACE;
        else
        {
            if (classObject.isAbstractType())
                classNameLine += SPACE + ABSTRACT;
            classNameLine += SPACE + CLASS;
        }
        classNameLine += SPACE + classObject.getClassName();
        
        // Now check if it has any parents
        if (classObject.getParentName() != null)
            classNameLine += SPACE + EXTENDS + SPACE + classObject.getParentName();
        // And now if it has implements any interfaces
        if (!classObject.getInterfaceNames().isEmpty())
            for (String interfaceName: classObject.getInterfaceNames())
                classNameLine += SPACE + IMPLEMENTS + SPACE + interfaceName;
        
        pw.println(classNameLine);
    }
    
    private void writeVariables(ClassObject classObject, PrintWriter pw)
    {
        for (VariableObject variable: classObject.getVariables())
            writeVariable(variable, pw);
    }
    
    private void writeVariable(VariableObject variable, PrintWriter pw)
    {
        String variableLine = SINGLE_TAB;
        variableLine += variable.getScope();
        
        if(variable.isStaticType())
            variableLine += SPACE + STATIC;
        if(variable.isFinalType())
            variableLine += SPACE + FINAL;
        variableLine += SPACE + variable.getType();
        variableLine += SPACE + variable.getName();
        if (variable.isFinalType())
            variableLine += SPACE + EQUALS + SPACE + NULL;
        variableLine += SEMI_COLON;
        
        pw.println(variableLine);
    }
    
    private void writeMethods(ClassObject classObject, PrintWriter pw)
    {
        for (MethodObject method: classObject.getMethods())
        {
            pw.println();
            writeMethod(classObject, method, pw);
        }
    }
    
    private void writeMethod(ClassObject classObject, MethodObject method, PrintWriter pw)
    {
        String methodLine = SINGLE_TAB;
        methodLine += method.getScope();
        
        if(method.isStaticType())
            methodLine += SPACE + STATIC;
        else if (method.isAbstractType())
            methodLine += SPACE + ABSTRACT;
            
        if(method.isFinalType())
            methodLine += SPACE + FINAL;
        
        methodLine += SPACE + method.getType();
        methodLine += SPACE + method.getName();
        
        pw.print(methodLine);
        
        // Print the arguments on the same line now
        pw.print(OPENING_PARENTHESIS);
        for (int i = 0; i < method.getArguments().size(); i++)
        {
            ArgumentObject arg = method.getArguments().get(i);
            writeMethodArgument(arg, pw);
            if (i != method.getArguments().size() - 1)
                pw.print(COMMA + SPACE);
        }
        pw.print(CLOSING_PARENTHESIS);
        
        if (method.isAbstractType() || classObject.isInterfaceType())
            pw.println(SEMI_COLON);
        else
        {
            // Now check whether the method has a return type. If yes, print appropriate line
            String returnLine = "";
            pw.println();
            pw.println(SINGLE_TAB + OPENING_CURLY_BRACE);
            if (!method.getType().equals("void"))
            {
                returnLine += DOUBLE_TAB + RETURN + SPACE + fetchReturnValue(method.getType()) + SEMI_COLON;
                pw.println(returnLine);
            }
            pw.println(SINGLE_TAB + CLOSING_CURLY_BRACE);
        }
    }

    private void writeMethodArgument(ArgumentObject arg, PrintWriter pw)
    {
        String argLine = arg.getType() + SPACE + arg.getName();
        pw.print(argLine);
    }
    
    private String fetchReturnValue(String returnType)
    {
        switch (returnType)
        {
            case INT:
                return ZERO;
            case DOUBLE:
                return ZERO;
            case LONG:
                return ZERO;
            case BYTE:
                return ZERO;
            case CHAR:
                return ZERO;
            case SHORT:
                return ZERO;
            case FLOAT:
                return ZERO;
            case BOOLEAN:
                return FALSE;
            default:
                return null;
        }
    }
    
}
