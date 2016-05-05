/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.file;

import java.io.IOException;
import jcd.data.DataManager;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;

/**
 * This is the file manager for our app which manages file I/O for our data.
 * @author RaniSons
 */
public class FileManager implements AppFileComponent
{
    // CONSTANTS FOR THE VALUES RELATED TO CANVAS
    public static final String JSON_CANVAS_ZOOM_SCALE_X = "canvas_zoom_scale_x";
    public static final String JSON_CANVAS_ZOOM_SCALE_Y = "canvas_zoom_scale_y";
    public static final String JSON_GRID_MODE = "grid_mode";
    
    // CONSTANTS FOR FILE SAVING AND LOADING
    public static final String JSON_CLASS_NAME = "class_name";
    public static final String JSON_PACKAGE_NAME = "package_name";
    public static final String JSON_PARENT_NAME = "parent_name";
    public static final String JSON_IS_INTERFACE = "is_interface";
    public static final String JSON_INTERFACE_NAMES = "interface_names";
    public static final String JSON_CLASSES_ARRAY = "classes";
    public static final String JSON_VARIABLES_ARRAY = "variables";
    public static final String JSON_METHODS_ARRAY = "methods";
    public static final String JSON_JAVA_API_PACKAGES_ARRAY = "java_api_packages";
    public static final String JSON_LINE_CONNECTORS_ARRAY = "line_connectors";
    public static final String JSON_LINES_ARRAY = "lines";
    public static final String JSON_LINE_STROKE_WIDTH = "line_stroke_width";
    public static final String JSON_FROM_BOX_CLASS = "from_box_class_name";
    public static final String JSON_TO_BOX_CLASS = "to_box_class_name";
    public static final String JSON_LINE_CONNECTOR_TYPE = "line_connector_type";
    public static final String JSON_LINE_CONNECTOR_SHAPE = "line_connector_shape";
    public static final String JSON_LINE_CONNECTOR_END_CLASS_NAME = 
            "line_connector_end_class_name";
    public static final String JSON_ARGUMENTS_ARRAY = "arguments";
    public static final String JSON_NAME = "name";
    public static final String JSON_TYPE = "type";
    public static final String JSON_SCOPE = "scope";
    public static final String JSON_IS_STATIC = "is_static";
    public static final String JSON_IS_FINAL = "is_final";
    public static final String JSON_IS_ABSTRACT = "is_abstract";
    public static final String JSON_BOX_OBJECT = "box";
    public static final String JSON_TRANSLATE_X = "translate_x";
    public static final String JSON_TRANSLATE_Y = "translate_y";
    public static final String JSON_CLASS_TEXT_FIELDS = "class_text_fields";
    public static final String JSON_VARIABLES_TEXT_FIELDS = "variables_text_fields";
    public static final String JSON_METHODS_TEXT_FIELDS = "methods_text_fields";
    public static final String JSON_TEXT = "text";
    public static final String JSON_STYLE_CLASS = "style_class";
    public static final String JSON_IS_UNDERLINED = "underlined";
    public static final String JSON_START_X = "start_x";
    public static final String JSON_START_Y = "start_y";
    public static final String JSON_END_X = "end_x";
    public static final String JSON_END_Y = "end_y";
    public static final String JSON_RECT_X = "rect_x";
    public static final String JSON_RECT_Y = "rect_y";
    public static final String JSON_RECT_WIDTH = "rect_width";
    public static final String JSON_RECT_HEIGHT = "rect_height";
    public static final String JSON_RECT_STROKE_COLOR = "rect_stroke_color";
    public static final String JSON_RECT_STROKE_WIDTH = "rect_stroke_width";
    public static final String JSON_RECT_FILL_COLOR = "rect_fill_color";
    public static final String JSON_RECT_ROTATE_DEGREES = "rect_rotate_degrees";
    public static final String JSON_TRIANGLE_X = "triangle_x";
    public static final String JSON_TRIANGLE_Y = "triangle_y";
    public static final String JSON_TRIANGLE_HEIGHT = "triangle_height";
    public static final String JSON_TRIANGLE_STROKE_COLOR = "triangle_stroke_color";
    public static final String JSON_TRIANGLE_STROKE_WIDTH = "triangle_stroke_width";
    public static final String JSON_TRIANGLE_FILL_COLOR = "triangle_fill_color";
    
    
    // CONSTANTS FOR FILE EXPORTING
    public static final String PACKAGE = "package";
    public static final String PRIVATE = "private";
    public static final String PUBLIC = "public";
    public static final String INTERFACE = "interface";
    public static final String ABSTRACT = "abstract";
    public static final String CLASS = "class";
    public static final String EXTENDS = "extends";
    public static final String IMPLEMENTS = "implements";
    public static final String STATIC = "static";
    public static final String FINAL = "final";
    public static final String RETURN = "return";
    public static final String FALSE = "false";
    public static final String IMPORT = "import";
    
    public static final String INT = "int";
    public static final String DOUBLE = "double";
    public static final String BOOLEAN = "boolean";
    public static final String CHAR = "char";
    public static final String BYTE = "byte";
    public static final String SHORT = "short";
    public static final String LONG = "long";
    public static final String FLOAT = "float";
    public static final String STRING = "String";
    public static final String ARRAY_LIST = "ArrayList";
    public static final String NULL = "null";
    
    public static final String SPACE = " ";
    public static final String OPENING_CURLY_BRACE = "{";
    public static final String CLOSING_CURLY_BRACE = "}";
    public static final String OPENING_PARENTHESIS = "(";
    public static final String CLOSING_PARENTHESIS = ")";
    public static final String SINGLE_TAB = "    ";
    public static final String DOUBLE_TAB = "        ";
    public static final String BACKSPACE = "\b";
    public static final String SEMI_COLON = ";";
    public static final String COMMA = ",";
    public static final String EQUALS = "=";
    public static final String PERIOD = ".";
    
    public static final String ZERO = "0";
    
    public static final String WELCOME_STRING = 
            "/*\n* Author:\n* Co-Author:\n* To change this license header, "
            + "choose License Headers in Project Properties.\n*/";

    /**
     * This method is called from SAF and it saves the data to a json file.
     * @param data
     * the data component
     * @param filePath
     * the file path where the data is to saved
     * @throws IOException
     * if there is an issue writing data to file
     */
    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException 
    {
        // SAVE IT
        FileSaver fileSaver = new FileSaver();
        fileSaver.saveData((DataManager) data, filePath);
    }

    /**
     * This method helps load the data from a given json file.
     * @param data
     * the data component
     * @param filePath
     * the file path of the json file
     * @throws IOException 
     * if there is an issue reading data from the file
     */
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException
    {
        // LOAD IT
        FileLoader fileLoader = new FileLoader();
        fileLoader.loadData((DataManager) data, filePath);
    }
    
    /**
     * This method helps export data to a java format.
     * @param data
     * the data component
     * @param filePath
     * the path of the file
     * @throws IOException 
     * if there is an issue writing to the java file
     */
    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException 
    {
        // EXPORT IT
        JavaExporter javaExporter = new JavaExporter();
        javaExporter.exportToSourceCode(data, filePath);
    }
    
    /**
     * This method is here just to satisfy the compiler.
     * @param data
     * @param filePath
     * @throws IOException 
     */
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException
    {
        System.out.println("Import Data");
    }
}