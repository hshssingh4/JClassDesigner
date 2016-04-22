/*
* Author:
* Co-Author:
* To change this license header, choose License Headers in Project Properties.
*/
package ThreadExample;

import javafx.application.Platform;
import javafx.concurrent.Task;

public class CounterTask extends Task<Void>
{
    private ThreadExample app;
    private int counter;

    protected Void call()
    {
        return null;
    }
}
