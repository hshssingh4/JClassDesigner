package jcd;

/**
 * These are properties that are to be loaded from workspace_properties.xml. They
 * will provide custom labels and other UI details for this application and
 * it's custom workspace. Note that in this application we're using two different
 * properties XML files. simple_app_properties.xml is for settings known to the
 * Simple App Framework and so helps to set it up. These properties would be for
 * anything relevant to this custom application. The reason for loading this stuff
 * from an XML file like this is to make these settings independent of the code
 * and therefore easily interchangeable, like if we wished to change the language
 * the application ran in.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public enum PropertyType 
{
    SELECTION_TOOL_ICON,
    SELECTION_TOOL_TOOLTIP,
    RESIZE_ICON,
    RESIZE_TOOLTIP,
    ADD_CLASS_ICON,
    ADD_CLASS_TOOLTIP,
    ADD_INTERFACE_ICON,
    ADD_INTERFACE_TOOLTIP,
    REMOVE_ICON,
    REMOVE_TOOLTIP,
    UNDO_ICON,
    UNDO_TOOLTIP,
    REDO_ICON,
    REDO_TOOLTIP,
    ZOOM_IN_ICON,
    ZOOM_IN_TOOLTIP,
    ZOOM_OUT_ICON,
    ZOOM_OUT_TOOLTIP,
    
    PLUS_ICON,
    MINUS_ICON,
    EDIT_ICON,
    ADD_VARIABLE_TOOLTIP,
    REMOVE_VARIABLE_TOOLTIP,
    EDIT_VARIABLE_TOOLTIP,
    ADD_METHOD_TOOLTIP,
    REMOVE_METHOD_TOOLTIP,
    EDIT_METHOD_TOOLTIP,

    ATTRIBUTE_UPDATE_ERROR_MESSAGE,
    ATTRIBUTE_UPDATE_ERROR_TITLE,
    ADD_ELEMENT_ERROR_MESSAGE,
    ADD_ELEMENT_ERROR_TITLE,
    REMOVE_ELEMENT_ERROR_MESSAGE,
    REMOVE_ELEMENT_ERROR_TITLE,
    ILLEGAL_NODE_REMOVAL_ERROR_MESSAGE, 
    ILLEGAL_NODE_REMOVAL_ERROR_TITLE,
    TEMP_PAGE_LOAD_ERROR_MESSAGE,
    TEMP_PAGE_LOAD_ERROR_TITLE,
    CSS_EXPORT_ERROR_MESSAGE,
    CSS_EXPORT_ERROR_TITLE,
    UPDATE_ERROR_MESSAGE,
    UPDATE_ERROR_TITLE
}