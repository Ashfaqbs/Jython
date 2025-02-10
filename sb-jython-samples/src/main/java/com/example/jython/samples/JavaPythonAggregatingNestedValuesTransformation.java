package com.example.jython.samples;


import java.io.InputStream;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.python.core.Py;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

public class JavaPythonAggregatingNestedValuesTransformation {
    public static void main(String[] args) {
        // Initialize the Python interpreter
        PythonInterpreter interpreter = new PythonInterpreter();

        // Load the Python script from the classpath (src/main/resources)
        InputStream pyFile = JavaPythonAggregatingNestedValuesTransformation.class
                .getClassLoader()
                .getResourceAsStream("python-codes/transform1.py");

        if (pyFile == null) {
            System.err.println("Failed to load python file from resources.");
            return;
        }
        interpreter.execfile(pyFile);

        // Build the JSON-like structure as a Java Map
        Map<String, Object> item1 = new HashMap<>();
        item1.put("product_id", "A1");
        item1.put("quantity", 2);
        item1.put("price", 10);

        Map<String, Object> item2 = new HashMap<>();
        item2.put("product_id", "B2");
        item2.put("quantity", 1);
        item2.put("price", 20);

        List<Map<String, Object>> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Map<String, Object> order = new HashMap<>();
        order.put("order_id", "ORD12345");
        order.put("items", items);

        Map<String, Object> jsonInput = new HashMap<>();
        jsonInput.put("order", order);

        // Convert the Java Map into a PyObject (Python dictionary)
        PyObject pyInput = Py.java2py(jsonInput);

        // Retrieve the transformation function from the Python script
        PyObject transformFunction = interpreter.get("transform_order");
        if (transformFunction == null) {
            System.err.println("Function 'transform_order' not found in the Python script.");
            return;
        }

        // Call the Python function with the input object
        PyObject pyResult = transformFunction.__call__(pyInput);

        // Convert the result back to a Java Map
        Map result = (Map) pyResult.__tojava__(Map.class);

        // Print the transformed JSON (as a Java Map)
        System.out.println("Transformed JSON: " + result);
    }
}
/*

IP
{
  "order": {
    "order_id": "ORD12345",
    "items": [
      { "product_id": "A1", "quantity": 2, "price": 10 },
      { "product_id": "B2", "quantity": 1, "price": 20 }
    ]
  }
}
    

OP

Transformed JSON: {'total_amount': 40, 'total_quantity': 3, 'order_id': u'ORD12345'}

 */