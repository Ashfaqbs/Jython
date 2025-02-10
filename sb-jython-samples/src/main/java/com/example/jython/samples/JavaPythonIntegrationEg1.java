package com.example.jython.samples;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;


public class JavaPythonIntegrationEg1 {
    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter();

        // Sample JSON data as a Java String
        String jsonData = "{\"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"skills\": [\"Python\", \"Java\", \"JavaScript\"]}";

        // Execute Python code (define the transformation function)
        interpreter.exec("""
import json

def transform_json(json_string):
    data = json.loads(json_string)
    transformed_data = {
        "id": data["id"],
        "name": data["name"],
        "email": data["email"],
        "skills_count": len(data["skills"])
    }
    return json.dumps(transformed_data)
""");


        // Call the Python function with JSON data
        PyObject pyObj = interpreter.eval("transform_json('" + jsonData.replace("'", "\\'") + "')");

        // Convert the result back to a Java String
        String result = pyObj.toString();

        System.out.println("Transformed JSON: " + result);

        // (Optional) Convert the JSON string to a Java JSONObject
        try {
            JSONObject jsonResult = new JSONObject(result);
            System.out.println("Java JSONObject: " + jsonResult.toString(2)); // Use toString(2) for pretty printing
        } catch (JSONException e) {
            System.err.println("Error parsing JSON result: " + e.getMessage());
        }
    }
}
/*
OP

Java JSONObject: {
  "skills_count": 3,
  "name": "John Doe",
  "id": 1,
  "email": "john.doe@example.com"
}

 */