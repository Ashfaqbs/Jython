# transform1.py
import json

def transform_json(json_data):
    """
    Receives a JSON string, parses it, and transforms the 'address' object of the user
    into a formatted string.
    """
    # Convert JSON string to a dictionary
    data = json.loads(json_data)

    # Get the 'user' object and its 'address'
    user = data.get("user", {})
    address = user.get("address", {})

    # Transform the address by combining fields (if available)
    if address:
        formatted_address = "{street}, {city}, {zipcode}".format(
            street=address.get("street", ""),
            city=address.get("city", ""),
            zipcode=address.get("zipcode", "")
        )
        # Replace the address object with the formatted string
        user["address"] = formatted_address
    data["user"] = user

    # Return the new JSON string
    return json.dumps(data)
