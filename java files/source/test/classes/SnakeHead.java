/*
* Author:
* Co-Author:
* To change this license header, choose License Headers in Project Properties.
*/
package test.classes;

import test.interfaces.SnakInterface;

public class SnakeHead implements SnakInterface
{
    private int currentX;
    private int currentY;

    private boolean changeLocation(int newX, int newY)
    {
        return false;
    }
}
