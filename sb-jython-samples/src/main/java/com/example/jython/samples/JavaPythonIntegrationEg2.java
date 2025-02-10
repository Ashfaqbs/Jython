package com.example.jython.samples;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;

public class JavaPythonIntegrationEg2 {
    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter();

        // Sample JSON data as a Java String (representing a list of students)
        String jsonData = "[" +
                "{" +
                "\"id\": 101," +
                "\"name\": \"Alice\"," +
                "\"courses\": [" +
                "{\"name\": \"Math\", \"grade\": \"A\", \"credits\": 3}," +
                "{\"name\": \"Physics\", \"grade\": \"B\", \"credits\": 4}" +
                "]" +
                "}," +
                "{" +
                "\"id\": 102," +
                "\"name\": \"Bob\"," +
                "\"courses\": [" +
                "{\"name\": \"Chemistry\", \"grade\": \"C\", \"credits\": 3}," +
                "{\"name\": \"Biology\", \"grade\": \"A\", \"credits\": 4}" +
                "]" +
                "}" +
                "]";

        // Define the transform_json function in Python
        interpreter.exec("""
import json
def calculate_gpa(courses):
    grade_points = {"A": 4, "B": 3, "C": 2, "D": 1, "F": 0}
    total_credits = 0
    weighted_sum = 0
    for course in courses:
        grade = course["grade"]
        credits = course["credits"]
        if grade in grade_points:
            weighted_sum += grade_points[grade] * credits
            total_credits += credits
    if total_credits > 0:
        return round(weighted_sum / float(total_credits), 2)
    else:
        return 0.0

def transform_json(json_string):
    students = json.loads(json_string)
    transformed_students = []
    for student in students:
        gpa = calculate_gpa(student["courses"])
        transformed_student = {
            "id": student["id"],
            "name": student["name"],
            "gpa": gpa
        }
        transformed_students.append(transformed_student)
    return json.dumps(transformed_students)""");


        // Call the Python function with JSON data
        PyObject pyObj = interpreter.eval("transform_json('" + jsonData.replace("'", "\\'") + "')");

        // Convert the result back to a Java String
        String result = pyObj.toString();

        System.out.println("Transformed JSON: " + result);

        // (Optional) Convert the JSON string to a Java JSONArray
        try {
            JSONArray jsonResult = new JSONArray(result);
            System.out.println("Java JSONArray: " + jsonResult.toString(2)); // Use toString(2) for pretty printing
        } catch (JSONException e) {
            System.err.println("Error parsing JSON result: " + e.getMessage());
        }
    }
}

/*
Transformed JSON: [{"name": "Alice", "gpa": 3.43, "id": 101}, {"name": "Bob", "gpa": 3.14, "id": 102}]
Java JSONArray: [
  {
    "name": "Alice",
    "gpa": 3.43,
    "id": 101
  },
  {
    "name": "Bob",
    "gpa": 3.14,
    "id": 102
  }
]

 */