/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.UndoRedo;

/**
 * This enum lists all the ways a user can make a change to the workspace.
 * @author RaniSons
 */
public enum Command 
{
    ADD_CLASS_OBJECT,
    REMOVE_CLASS_OBJECT,
    
    ZOOM_IN,
    ZOOM_OUT,
    
    GRID_RENDER,
    GRID_UNRENDER,
    
    GRID_SNAP,
    GRID_UNSNAP,
    
    MOVE_BOX,
    
    SIZE_BOX
}
