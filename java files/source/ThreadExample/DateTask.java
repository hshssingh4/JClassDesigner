/*
* Author:
* Co-Author:
* To change this license header, choose License Headers in Project Properties.
*/
package ThreadExample;

import java.util.Date;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class DateTask extends Task<Void>
{
    private ThreadExample app;
    private Date now;

    protected Void call()
    {
        return null;
    }
}
