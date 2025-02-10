package com.example.jython.samples;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.json.JSONObject;
import org.json.JSONException;
import java.io.InputStream;

public class JavaPythonIntegrationEg4 {
    public static void main(String[] args) {
        // Initialize the Python interpreter
        PythonInterpreter interpreter = new PythonInterpreter();

        // Construct the JSON data using org.json
        JSONObject jsonData = new JSONObject();
        try {
            JSONObject address = new JSONObject();
            address.put("street", "123 Main St");
            address.put("city", "Springfield");
            address.put("zipcode", "98765");

            JSONObject user = new JSONObject();
            user.put("id", 1);
            user.put("name", "John Doe");
            user.put("address", address);

            jsonData.put("user", user);
        } catch (JSONException e) {
            System.err.println("Error constructing JSON: " + e.getMessage());
            return;
        }

        try {
            // Load the Python file directly from the resources directory
            InputStream pyFile = JavaPythonIntegrationEg4.class.getResourceAsStream("/python-codes/transform5.py");
            if (pyFile == null) {
                System.err.println("Failed to load transform1.py from resources.");
                return;
            }
            interpreter.execfile(pyFile);

            // Set the JSON string as a variable in the interpreter
            interpreter.set("json_data", new PyString(jsonData.toString()));

            // Call the Python function (assumed to be defined as transform_json in the file)
            PyObject resultObj = interpreter.eval("transform_json(json_data)");
            String transformedJson = resultObj.toString();

            System.out.println("Transformed JSON: " + transformedJson);

            // Optionally, convert the result back to a Java JSONObject for pretty printing
            JSONObject jsonResult = new JSONObject(transformedJson);
            System.out.println("Java JSONObject:\n" + jsonResult.toString(2));
        } catch (Exception e) {
            System.err.println("Error during Python transformation: " + e.getMessage());
        }
    }
}
/*
OP
Transformed JSON: {"user": {"address": "123 Main St, Springfield, 98765", "name": "John Doe", "id": 1}}
Java JSONObject:
{"user": {
  "address": "123 Main St, Springfield, 98765",
  "name": "John Doe",
  "id": 1
}}
 */