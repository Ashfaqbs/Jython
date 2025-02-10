package com.example.jython.samples;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import java.io.InputStream;


public class JavaPythonIntegrationWithFile {
    public static void main(String[] args) {
        // Initialize the Python interpreter
        PythonInterpreter interpreter = new PythonInterpreter();
        // Load the Python file from the classpath (src/main/resources)
        InputStream pyFile = JavaPythonIntegration.class
                .getClassLoader()
                .getResourceAsStream("python-codes/transform.py");

        if (pyFile == null) {
            System.err.println("Failed to load python file from resources.");
            return;
        }

        // Execute the python file, which defines the to_upper function
        interpreter.execfile(pyFile);

        // Java String to be passed to Python
        String javaString = "example text";

        // Retrieve the Python function defined in the file
        PyObject toUpperFunction = interpreter.get("to_upper");
        if (toUpperFunction == null) {
            System.err.println("Function 'to_upper' was not found in the Python script.");
            return;
        }

        // Call the Python function with the Java string as a PyString argument
        PyObject pyResult = toUpperFunction.__call__(new PyString(javaString));

        // Convert the result back to a Java String
        String result = pyResult.toString();

        System.out.println("Result: " + result);
    }
}
/*
OP
Result: EXAMPLE TEXT

 */