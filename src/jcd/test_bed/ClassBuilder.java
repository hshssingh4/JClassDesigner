/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jcd.test_bed;

import java.util.ArrayList;
import javafx.scene.text.Text;
import jcd.data.ArgumentObject;
import jcd.data.Box;
import jcd.data.ClassObject;
import jcd.data.DataManager;
import jcd.data.MethodObject;
import jcd.data.VariableObject;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;

/**
 *
 * @author RaniSons
 */
public class ClassBuilder 
{
    public static final String PRIVATE = "private";
    public static final String PUBLIC = "public";
    public static final String PROTECTED = "protected";
    
    public static final String INT = "int";
    public static final String DOUBLE = "double";
    public static final String BOOLEAN = "boolean";
    public static final String CHAR = "char";
    public static final String STRING = "String";
    public static final String ARRAY_LIST = "ArrayList";    
    
    public static final String COUNTER_TASK = "CounterTask";
    public static final String DATE_TASK = "DateTask";
    public static final String PAUSE_HANDLER = "PauseHandler";
    public static final String START_HANDLER = "StartHandler";
    public static final String THREAD_EXAMPLE = "ThreadExample";
    
    
    public ClassBuilder() {}
    
    public void hardCodeClasses(DataManager dataManager)
    {
        dataManager.addClassObject(createCounterTaskClass());
        dataManager.addClassObject(createDateTaskClass());
        dataManager.addClassObject(createPauseHandlerClass());
        dataManager.addClassObject(createStartHandlerClass());
        dataManager.addClassObject(createThreadExampleClass());
    }
    
    public void hardCodeThreadExampleClass(DataManager dataManager)
    {
        dataManager.addClassObject(createThreadExampleClass());
    }
    
    public void hardCodeCounterTaskClass(DataManager dataManager)
    {
        dataManager.addClassObject(createCounterTaskClass());
    }
    
    public void hardCodeStartHandlerClass(DataManager dataManager)
    {
        dataManager.addClassObject(createStartHandlerClass());
    }
    
    // HERE IS THE FIRST HARD CODED CLASS
    
    private ClassObject createCounterTaskClass()
    {
        Box box = initializeNewBox();
        addClassNameText(COUNTER_TASK, box);
        
        ClassObject counterTaskClass = new ClassObject(COUNTER_TASK, "", box);
        
        counterTaskClass.setParentName("Task<Void>");
        
        ArrayList<String> interfaceNames = new ArrayList<>();
        counterTaskClass.setInterfaceNames(interfaceNames);
        ArrayList<VariableObject> variables = addCounterTaskClassVariables();
        counterTaskClass.setVariables(variables);
        ArrayList<MethodObject> methods = addCounterTaskClassMethods();
        counterTaskClass.setMethods(methods);
        
        return counterTaskClass;
    }
    
    private ArrayList<VariableObject> addCounterTaskClassVariables()
    {
        VariableObject app = new VariableObject();
        app.setName("app");
        app.setScope(PRIVATE);
        app.setType("ThreadExample");
        app.setStaticType(false);
        app.setFinalType(false);
        
        VariableObject counter = new VariableObject();
        counter.setName("counter");
        counter.setScope(PRIVATE);
        counter.setType(INT);
        counter.setStaticType(false);
        counter.setFinalType(false);
        
        ArrayList<VariableObject> variables = new ArrayList<>();
        variables.add(app);
        variables.add(counter);
        
        return variables;
    }
    
    private ArrayList<MethodObject> addCounterTaskClassMethods()
    {
        MethodObject call = new MethodObject();
        call.setName("call");
        call.setScope(PROTECTED);
        call.setType("Void");
        call.setStaticType(false);
        call.setFinalType(false);
        call.setArguments(new ArrayList<>());
        
        ArrayList<MethodObject> methods = new ArrayList<>();
        methods.add(call);
        
        return methods;
    }
    
    // HERE IS THE SECOND HARD CODED CLASS
    
    private ClassObject createDateTaskClass()
    {
        Box box = initializeNewBox();
        addClassNameText(DATE_TASK, box);
        
        ClassObject dateTaskClass = new ClassObject(DATE_TASK, "", box);
        
        dateTaskClass.setParentName("Task<Void>");
        
        ArrayList<String> interfaceNames = new ArrayList<>();
        dateTaskClass.setInterfaceNames(interfaceNames);
        ArrayList<VariableObject> variables = addDateTaskClassVariables();
        dateTaskClass.setVariables(variables);
        ArrayList<MethodObject> methods = addDateTaskClassMethods();
        dateTaskClass.setMethods(methods);
        
        return dateTaskClass;
    }
    
    private ArrayList<VariableObject> addDateTaskClassVariables()
    {
        VariableObject app = new VariableObject();
        app.setName("app");
        app.setScope(PRIVATE);
        app.setType("ThreadExample");
        app.setStaticType(false);
        app.setFinalType(false);
        
        VariableObject now = new VariableObject();
        now.setName("now");
        now.setScope(PRIVATE);
        now.setType("Date");
        now.setStaticType(false);
        now.setFinalType(false);
        
        ArrayList<VariableObject> variables = new ArrayList<>();
        variables.add(app);
        variables.add(now);
        
        return variables;
    }
    
    private ArrayList<MethodObject> addDateTaskClassMethods()
    {
        MethodObject call = new MethodObject();
        call.setName("call");
        call.setScope(PROTECTED);
        call.setType("Void");
        call.setStaticType(false);
        call.setFinalType(false);
        call.setArguments(new ArrayList<>());
        
        ArrayList<MethodObject> methods = new ArrayList<>();
        methods.add(call);
        
        return methods;
    }
    
    // HERE IS THE THIRD HARD CODED CLASS
    
    private ClassObject createPauseHandlerClass()
    {
        Box box = initializeNewBox();
        addClassNameText(PAUSE_HANDLER, box);
        
        ClassObject pauseHandlerClass = new ClassObject(PAUSE_HANDLER, "", box);
        
        pauseHandlerClass.setParentName(null);
        
        ArrayList<String> interfaceNames = addPauseHandlerInterfaceNames();
        pauseHandlerClass.setInterfaceNames(interfaceNames);
        ArrayList<VariableObject> variables = addPauseHandlerClassVariables();
        pauseHandlerClass.setVariables(variables);
        ArrayList<MethodObject> methods = addPauseHandlerClassMethods();
        pauseHandlerClass.setMethods(methods);
        
        return pauseHandlerClass;
    }
    
    private ArrayList<String> addPauseHandlerInterfaceNames()
    {
        ArrayList<String> interfaceNames = new ArrayList<>();
        interfaceNames.add("EventHandler");
        
        return interfaceNames;
    }
    
    private ArrayList<VariableObject> addPauseHandlerClassVariables()
    {
        VariableObject app = new VariableObject();
        app.setName("app");
        app.setScope(PRIVATE);
        app.setType("ThreadExample");
        app.setStaticType(false);
        app.setFinalType(false);
        
        ArrayList<VariableObject> variables = new ArrayList<>();
        variables.add(app);
        
        return variables;
    }
    
    private ArrayList<MethodObject> addPauseHandlerClassMethods()
    {
        MethodObject handle = new MethodObject();
        handle.setName("handle");
        handle.setScope(PUBLIC);
        handle.setType("void");
        handle.setStaticType(false);
        handle.setFinalType(false);
        
        ArrayList<ArgumentObject> arguments = new ArrayList<>();
        ArgumentObject event = new ArgumentObject();
        event.setName("event");
        event.setType("Event");
        arguments.add(event);
        
        handle.setArguments(arguments);
        
        ArrayList<MethodObject> methods = new ArrayList<>();
        methods.add(handle);
        
        return methods;
    }
    
    // HERE IS THE FOURTH HARD CODED CLASS
    
    private ClassObject createStartHandlerClass()
    {
        Box box = initializeNewBox();
        addClassNameText(START_HANDLER, box);
        
        ClassObject startHandlerClass = new ClassObject(START_HANDLER, "", box);
        
        startHandlerClass.setParentName(null);
        
        ArrayList<String> interfaceNames = addStartHandlerInterfaceNames();
        startHandlerClass.setInterfaceNames(interfaceNames);
        ArrayList<VariableObject> variables = addStartHandlerClassVariables();
        startHandlerClass.setVariables(variables);
        ArrayList<MethodObject> methods = addStartHandlerClassMethods();
        startHandlerClass.setMethods(methods);
        
        return startHandlerClass;
    }
    
    private ArrayList<String> addStartHandlerInterfaceNames()
    {
        ArrayList<String> interfaceNames = new ArrayList<>();
        interfaceNames.add("EventHandler");
        
        return interfaceNames;
    }
    
    private ArrayList<VariableObject> addStartHandlerClassVariables()
    {
        VariableObject app = new VariableObject();
        app.setName("app");
        app.setScope(PRIVATE);
        app.setType("ThreadExample");
        app.setStaticType(false);
        app.setFinalType(false);
        
        ArrayList<VariableObject> variables = new ArrayList<>();
        variables.add(app);
        
        return variables;
    }
    
    private ArrayList<MethodObject> addStartHandlerClassMethods()
    {
        MethodObject handle = new MethodObject();
        handle.setName("handle");
        handle.setScope(PUBLIC);
        handle.setType("void");
        handle.setStaticType(false);
        handle.setFinalType(false);
        
        ArrayList<ArgumentObject> arguments = new ArrayList<>();
        ArgumentObject event = new ArgumentObject();
        event.setName("event");
        event.setType("Event");
        arguments.add(event);
        
        handle.setArguments(arguments);
        
        ArrayList<MethodObject> methods = new ArrayList<>();
        methods.add(handle);
        
        return methods;
    }
    
    // HERE IS THE FIFTH HARD CODED CLASS
    private ClassObject createThreadExampleClass()
    {
        Box box = initializeNewBox();
        addClassNameText(THREAD_EXAMPLE, box);
        
        ClassObject threadExampleClass = new ClassObject(THREAD_EXAMPLE, "", box);
        
        threadExampleClass.setParentName("Application");
        
        ArrayList<String> interfaceNames = new ArrayList<>();
        threadExampleClass.setInterfaceNames(interfaceNames);
        ArrayList<VariableObject> variables = addThreadExampleClassVariables();
        threadExampleClass.setVariables(variables);
        ArrayList<MethodObject> methods = addThreadExampleClassMethods();
        threadExampleClass.setMethods(methods);
        
        return threadExampleClass;
    }
    
    private ArrayList<VariableObject> addThreadExampleClassVariables()
    {
        VariableObject START_TEXT = new VariableObject();
        START_TEXT.setName("Start");
        START_TEXT.setScope(PUBLIC);
        START_TEXT.setType(STRING);
        START_TEXT.setStaticType(true);
        START_TEXT.setFinalType(true);
        
        VariableObject PAUSE_TEXT = new VariableObject();
        PAUSE_TEXT.setName("Pause");
        PAUSE_TEXT.setScope(PUBLIC);
        PAUSE_TEXT.setType(STRING);
        PAUSE_TEXT.setStaticType(true);
        PAUSE_TEXT.setFinalType(true);
        
        VariableObject window = new VariableObject();
        window.setName("window");
        window.setScope(PRIVATE);
        window.setType("Stage");
        window.setStaticType(false);
        window.setFinalType(false);
        
        VariableObject appPane = new VariableObject();
        appPane.setName("appPane");
        appPane.setScope(PRIVATE);
        appPane.setType("BorderPane");
        appPane.setStaticType(false);
        appPane.setFinalType(false);
        
        VariableObject topPane = new VariableObject();
        topPane.setName("topPane");
        topPane.setScope(PRIVATE);
        topPane.setType("FlowPane");
        topPane.setStaticType(false);
        topPane.setFinalType(false);
        
        VariableObject startButton = new VariableObject();
        startButton.setName("startButton");
        startButton.setScope(PRIVATE);
        startButton.setType("Button");
        startButton.setStaticType(false);
        startButton.setFinalType(false);
        
        VariableObject pauseButton = new VariableObject();
        pauseButton.setName("pauseButton");
        pauseButton.setScope(PRIVATE);
        pauseButton.setType("Button");
        pauseButton.setStaticType(false);
        pauseButton.setFinalType(false);
        
        VariableObject scrollPane = new VariableObject();
        scrollPane.setName("scrollPane");
        scrollPane.setScope(PRIVATE);
        scrollPane.setType("ScrollPane");
        scrollPane.setStaticType(false);
        scrollPane.setFinalType(false);
        
        VariableObject textArea = new VariableObject();
        textArea.setName("textArea");
        textArea.setScope(PRIVATE);
        textArea.setType("TextArea");
        textArea.setStaticType(false);
        textArea.setFinalType(false);
        
        VariableObject dateThread = new VariableObject();
        dateThread.setName("dateThread");
        dateThread.setScope(PRIVATE);
        dateThread.setType("Thread");
        dateThread.setStaticType(false);
        dateThread.setFinalType(false);
        
        VariableObject dateTask = new VariableObject();
        dateTask.setName("dateTask");
        dateTask.setScope(PRIVATE);
        dateTask.setType("Task");
        dateTask.setStaticType(false);
        dateTask.setFinalType(false);
        
        VariableObject counterThread = new VariableObject();
        counterThread.setName("counterThread");
        counterThread.setScope(PRIVATE);
        counterThread.setType("Thread");
        counterThread.setStaticType(false);
        counterThread.setFinalType(false);
        
        VariableObject counterTask = new VariableObject();
        counterTask.setName("counterTask");
        counterTask.setScope(PRIVATE);
        counterTask.setType("Task");
        counterTask.setStaticType(false);
        counterTask.setFinalType(false);
        
        VariableObject work = new VariableObject();
        work.setName("work");
        work.setScope(PRIVATE);
        work.setType(BOOLEAN);
        work.setStaticType(false);
        work.setFinalType(false);
        
        ArrayList<VariableObject> variables = new ArrayList<>();
        variables.add(START_TEXT);
        variables.add(PAUSE_TEXT);
        variables.add(window);
        variables.add(appPane);
        variables.add(topPane);
        variables.add(startButton);
        variables.add(pauseButton);
        variables.add(scrollPane);
        variables.add(textArea);
        variables.add(dateThread);
        variables.add(dateTask);
        variables.add(counterThread);
        variables.add(counterTask);
        variables.add(work);
        
        return variables;
    }
    
    private ArrayList<MethodObject> addThreadExampleClassMethods()
    {
        // FIRST METHOD
        MethodObject start = new MethodObject();
        start.setName("start");
        start.setScope(PUBLIC);
        start.setType("void");
        start.setStaticType(false);
        start.setFinalType(false);
        
        ArrayList<ArgumentObject> startArguments = new ArrayList<>();
        ArgumentObject primaryStage = new ArgumentObject();
        primaryStage.setName("primaryStage");
        primaryStage.setType("Stage");
        startArguments.add(primaryStage);
        
        start.setArguments(startArguments);
        
        // SECOND METHOD
        MethodObject startWork = new MethodObject();
        startWork.setName("startWork");
        startWork.setScope(PUBLIC);
        startWork.setType("void");
        startWork.setStaticType(false);
        startWork.setFinalType(false);
        startWork.setArguments(new ArrayList<>());
        
        // THIRD METHOD
        MethodObject pauseWork = new MethodObject();
        pauseWork.setName("pauseWork");
        pauseWork.setScope(PUBLIC);
        pauseWork.setType("void");
        pauseWork.setStaticType(false);
        pauseWork.setFinalType(false);
        pauseWork.setArguments(new ArrayList<>());
        
        // FOURTH METHOD
        MethodObject doWork = new MethodObject();
        doWork.setName("doWork");
        doWork.setScope(PUBLIC);
        doWork.setType(BOOLEAN);
        doWork.setStaticType(false);
        doWork.setFinalType(false);
        doWork.setArguments(new ArrayList<>());
        
        // FIFTH METHOD
        MethodObject appendText = new MethodObject();
        appendText.setName("appendText");
        appendText.setScope(PUBLIC);
        appendText.setType("void");
        appendText.setStaticType(false);
        appendText.setFinalType(false);
        
        ArrayList<ArgumentObject> appendTextArguments = new ArrayList<>();
        ArgumentObject textToAppend = new ArgumentObject();
        textToAppend.setName("textToAppend");
        textToAppend.setType(STRING);
        appendTextArguments.add(textToAppend);
        
        appendText.setArguments(appendTextArguments);
        
        // SIXTH METHOD
        MethodObject sleep = new MethodObject();
        sleep.setName("sleep");
        sleep.setScope(PUBLIC);
        sleep.setType("void");
        sleep.setStaticType(false);
        sleep.setFinalType(false);
        
        ArrayList<ArgumentObject> sleepArguments = new ArrayList<>();
        ArgumentObject timeToSleep = new ArgumentObject();
        timeToSleep.setName("timeToSleep");
        timeToSleep.setType(INT);
        sleepArguments.add(timeToSleep);
        
        sleep.setArguments(sleepArguments);
        
        
        // SEVENTH METHOD
        MethodObject initLayout = new MethodObject();
        initLayout.setName("initLayout");
        initLayout.setScope(PRIVATE);
        initLayout.setType("void");
        initLayout.setStaticType(false);
        initLayout.setFinalType(false);
        initLayout.setArguments(new ArrayList<>());
        
        // EIGHTH METHOD
        MethodObject initHandlers = new MethodObject();
        initHandlers.setName("initHandlers");
        initHandlers.setScope(PRIVATE);
        initHandlers.setType("void");
        initHandlers.setStaticType(false);
        initHandlers.setFinalType(false);
        initHandlers.setArguments(new ArrayList<>());
        
        // NINTH METHOD
        MethodObject initWindow = new MethodObject();
        initWindow.setName("initWindow");
        initWindow.setScope(PRIVATE);
        initWindow.setType("void");
        initWindow.setStaticType(false);
        initWindow.setFinalType(false);
        
        ArrayList<ArgumentObject> initWindowArguments = new ArrayList<>();
        ArgumentObject initPrimaryStage = new ArgumentObject();
        initPrimaryStage.setName("initPrimaryStage");
        initPrimaryStage.setType("Stage");
        initWindowArguments.add(initPrimaryStage);
        
        initWindow.setArguments(initWindowArguments);
        
        // TENTH METHOD
        MethodObject initThreads = new MethodObject();
        initThreads.setName("initThreads");
        initThreads.setScope(PRIVATE);
        initThreads.setType("void");
        initThreads.setStaticType(false);
        initThreads.setFinalType(false);
        initThreads.setArguments(new ArrayList<>());
        
        // ELEVENTH METHOD
        MethodObject main = new MethodObject();
        main.setName("main");
        main.setType("void");
        main.setScope(PUBLIC);
        main.setStaticType(true);
        main.setFinalType(false);
        
        ArrayList<ArgumentObject> mainArguments = new ArrayList<>();
        ArgumentObject args = new ArgumentObject();
        args.setName("args");
        args.setType("String[]");
        mainArguments.add(args);
        
        main.setArguments(mainArguments);
        
        // FINISHED HARD CODING THE METHODS
        ArrayList<MethodObject> methods = new ArrayList<>();
        methods.add(start);
        methods.add(startWork);
        methods.add(pauseWork);
        methods.add(doWork);
        methods.add(appendText);
        methods.add(sleep);
        methods.add(initLayout);
        methods.add(initHandlers);
        methods.add(initWindow);
        methods.add(initThreads);
        methods.add(main);
        
        return methods;
    }
    
    /**
     * Helper method to initialize a new box.
     */
    private Box initializeNewBox()
    {
        // x and y values where the box will be origined
        int x = (int) ((Math.random() * 800));
        int y = 300;
        
        Box box = new Box();
        box.getMainVBox().setTranslateX(x);
        box.getMainVBox().setTranslateY(y);
        
        return box;
    }
    
    private void addClassNameText(String className, Box box)
    {
        Text text = new Text(className);
        text.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        box.getClassVBox().getChildren().add(text);
    }
}
