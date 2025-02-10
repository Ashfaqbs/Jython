# transform3.py

def filter_active(data):
    """
    Filters out any records in the list whose status is not 'active'.
    :param data: List of dictionaries representing JSON objects.
    :return: Filtered list with only active records.
    """
    return [item for item in data if item.get("status") == "active"]
