package com.example.jython.samples;

import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

public class JavaPythonIntegrationEg3 {
    public static void main(String[] args) {
        PythonInterpreter interpreter = new PythonInterpreter();

        // Sample JSON data as a Java JSONObject
        JSONObject jsonData = new JSONObject();
        try {
            JSONArray courses1 = new JSONArray();
            courses1.put(new JSONObject().put("name", "Math").put("grade", "A").put("credits", 3));
            courses1.put(new JSONObject().put("name", "Physics").put("grade", "B").put("credits", 4));

            JSONObject student1 = new JSONObject();
            student1.put("id", 101);
            student1.put("name", "Alice");
            student1.put("courses", courses1);

            JSONArray courses2 = new JSONArray();
            courses2.put(new JSONObject().put("name", "Chemistry").put("grade", "C").put("credits", 3));
            courses2.put(new JSONObject().put("name", "Biology").put("grade", "A").put("credits", 4));

            JSONObject student2 = new JSONObject();
            student2.put("id", 102);
            student2.put("name", "Bob");
            student2.put("courses", courses2);

            JSONArray students = new JSONArray();
            students.put(student1);
            students.put(student2);

            // Now jsonData holds the complete JSON array of students

            // Define the transform_json function in Python (modified to receive a PyJsonObject)
            interpreter.exec("import json\n" +
                    "def calculate_gpa(courses):\n" +
                    "    grade_points = {\"A\": 4, \"B\": 3, \"C\": 2, \"D\": 1, \"F\": 0}\n" +
                    "    total_credits = 0\n" +
                    "    weighted_sum = 0\n" +
                    "    for course in courses:\n" +
                    "        grade = course[\"grade\"]\n" +
                    "        credits = course[\"credits\"]\n" +
                    "        if grade in grade_points:\n" +
                    "            weighted_sum += grade_points[grade] * credits\n" +
                    "            total_credits += credits\n" +
                    "    if total_credits > 0:\n" +
                    "        return round(weighted_sum / float(total_credits), 2)\n" +
                    "    else:\n" +
                    "        return 0.0\n" +
                    "\n" +
                    "def transform_json(students_json):\n" +
                    "    students = json.loads(str(students_json))\n" +  // Convert PyJsonObject to string then load with json
                    "    transformed_students = []\n" +
                    "    for student in students:\n" +
                    "        gpa = calculate_gpa(student[\"courses\"])\n" +
                    "        transformed_student = {\n" +
                    "            \"id\": student[\"id\"],\n" +
                    "            \"name\": student[\"name\"],\n" +
                    "            \"gpa\": gpa\n" +
                    "        }\n" +
                    "        transformed_students.append(transformed_student)\n" +
                    "    return json.dumps(transformed_students)");

            // Pass the Java JSONObject to Python
            interpreter.set("json_data", new PyString(students.toString())); // Pass as string
            PyObject pyObj = interpreter.eval("transform_json(json_data)");

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

        } catch (JSONException e) {
            e.printStackTrace();
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