package com.example.jython.samples;


import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JavaPythonMappingNestedArrays {
    public static void main(String[] args) {
        // Initialize the Python interpreter
        PythonInterpreter interpreter = new PythonInterpreter();

        // Load the Python script from the classpath (src/main/resources)
        InputStream pyFile = JavaPythonMappingNestedArrays.class
                .getClassLoader()
                .getResourceAsStream("python-codes/transform4.py");

        if (pyFile == null) {
            System.err.println("Failed to load python file from resources.");
            return;
        }
        interpreter.execfile(pyFile);

        // Build the JSON-like structure as a Java Map
        Map<String, Object> student1 = new HashMap<>();
        student1.put("id", 101);
        student1.put("name", "Tom");
        student1.put("scores", Arrays.asList(85, 90, 78));

        Map<String, Object> student2 = new HashMap<>();
        student2.put("id", 102);
        student2.put("name", "Emma");
        student2.put("scores", Arrays.asList(92, 88, 85));

        List<Map<String, Object>> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);

        Map<String, Object> inputJson = new HashMap<>();
        inputJson.put("students", students);

        // Convert the Java Map to a Python object (a dictionary)
        PyObject pyInput = Py.java2py(inputJson);

        // Retrieve the Python function defined in the script
        PyObject computeFunction = interpreter.get("compute_average_scores");
        if (computeFunction == null) {
            System.err.println("Function 'compute_average_scores' not found in the Python script.");
            return;
        }

        // Call the Python function with the input object
        PyObject pyResult = computeFunction.__call__(pyInput);

        // Convert the result back to a Java Map
        Map result = (Map) pyResult.__tojava__(Map.class);

        // Print the transformed JSON (as a Java Map)
        System.out.println("Transformed JSON: " + result);
    }
}
/*
IP
 "students": [
    { "id": 101, "name": "Tom", "scores": [85, 90, 78] },
    { "id": 102, "name": "Emma", "scores": [92, 88, 85] }
  ]
}

OP

Transformed JSON: {'students': [{'average_score': 84.0, 'name': u'Tom', 'id': 101}, {'average_score': 88.0, 'name': u'Emma', 'id': 102}]}

 */