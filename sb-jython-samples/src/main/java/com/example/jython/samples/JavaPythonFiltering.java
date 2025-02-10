package com.example.jython.samples;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JavaPythonFiltering {
    public static void main(String[] args) {
        // Initialize the Python interpreter
        PythonInterpreter interpreter = new PythonInterpreter();

        // Load the Python script from the resources folder
        InputStream pyFile = JavaPythonFiltering.class
                .getClassLoader()
                .getResourceAsStream("python-codes/transform3.py");

        if (pyFile == null) {
            System.err.println("Failed to load python file from resources.");
            return;
        }
        interpreter.execfile(pyFile);

        // Build a Java List representing the JSON array
        List<Map<String, Object>> dataList = new ArrayList<>();

        Map<String, Object> record1 = new HashMap<>();
        record1.put("id", 1);
        record1.put("name", "John");
        record1.put("status", "active");

        Map<String, Object> record2 = new HashMap<>();
        record2.put("id", 2);
        record2.put("name", "Jane");
        record2.put("status", "inactive");

        Map<String, Object> record3 = new HashMap<>();
        record3.put("id", 3);
        record3.put("name", "Alice");
        record3.put("status", "active");

        dataList.add(record1);
        dataList.add(record2);
        dataList.add(record3);

        // Convert the Java List to a Python object (automatically converted to a list of dictionaries)
        PyObject pyData = Py.java2py(dataList);

        // Retrieve the Python function from the script
        PyObject filterFunction = interpreter.get("filter_active");
        if (filterFunction == null) {
            System.err.println("Function 'filter_active' not found in the Python script.");
            return;
        }

        // Call the Python function with the data
        PyObject pyResult = filterFunction.__call__(pyData);

        // Convert the result back to a Java List
        List filteredData = (List) pyResult.__tojava__(List.class);

        // Print the filtered JSON data
        System.out.println("Filtered Data: " + filteredData);
    }
}

/*
IP
[
  { "id": 1, "name": "John", "status": "active" },
  { "id": 2, "name": "Jane", "status": "inactive" },
  { "id": 3, "name": "Alice", "status": "active" }
] 
OP
Filtered Data: [{u'name': u'John', u'id': 1, u'status': u'active'}, {u'name': u'Alice', u'id': 3, u'status': u'active'}]

 */