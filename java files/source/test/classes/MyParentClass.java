/*
* Author:
* Co-Author:
* To change this license header, choose License Headers in Project Properties.
*/
package test.classes;

import java.lang.Comparable;
import test.interfaces.MyInterface;

public class MyParentClass implements MyInterface, Comparable
{
    protected int x;

    public int compareTo(Object o)
    {
        return 0;
    }

    public boolean areEqual(int x, int y)
    {
        return false;
    }

    public String getString()
    {
        return null;
    }
}
