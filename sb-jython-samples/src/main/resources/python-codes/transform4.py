# transform4.py

def compute_average_scores(data):
    """
    Computes the average score for each student in the input data.

    :param data: Dictionary with a key "students" that is a list of student dictionaries.
                 Each student dictionary contains an "id", "name", and "scores" (a list of numbers).
    :return: A new dictionary with the key "students", where each student dictionary contains "id",
             "name", and "average_score" (rounded to two decimals).
    """
    students = data.get("students", [])
    result = {"students": []}

    for student in students:
        scores = student.get("scores", [])
        if scores:
            average = sum(scores) / len(scores)
            average = round(average, 2)
        else:
            average = 0
        result["students"].append({
            "id": student.get("id"),
            "name": student.get("name"),
            "average_score": average
        })
    return result
