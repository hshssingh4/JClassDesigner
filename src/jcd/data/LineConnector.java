/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.data;

import java.util.ArrayList;
import javafx.scene.shape.Line;

/**
 *
 * @author RaniSons
 */
public class LineConnector
{
    private ArrayList<Line> lines;
    
    public LineConnector() 
    {
        lines = new ArrayList<>();
    }

    public ArrayList<Line> getLines() 
    {
        return lines;
    }
}
