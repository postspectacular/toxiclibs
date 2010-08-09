package toxi.test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class RunnableImplObject {

    public static void main(String[] args) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");

        // JavaScript code in a String
        String script =
                "importPackage(java.lang); var obj = new Object(); obj.run = function() { while(true) { println('run method called'); Thread.sleep(1000); } }";

        // evaluate script
        engine.eval(script);

        // get script object on which we want to implement the interface with
        Object obj = engine.get("obj");

        Invocable inv = (Invocable) engine;

        // get Runnable interface object from engine. This interface methods
        // are implemented by script methods of object 'obj'
        Runnable r = inv.getInterface(obj, Runnable.class);

        // start a new thread that runs the script implemented
        // runnable interface
        Thread th = new Thread(r);
        th.start();
    }
}