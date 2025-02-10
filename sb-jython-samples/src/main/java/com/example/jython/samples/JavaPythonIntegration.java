package com.example.jython.samples;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

public class JavaPythonIntegration {
    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter();

        // Java String to be passed to Python
        String javaString = "example text";

        // Execute Python code
        interpreter.exec("def to_upper(s): return s.upper()");

        // Call the Python function with Java data
        PyObject pyobj = interpreter.eval("to_upper('" + javaString + "')");

        // Convert back to Java String
        String result = ((PyString)pyobj).asString();

        System.out.println("Result: " + result);
    }
}