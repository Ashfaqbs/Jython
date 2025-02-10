# transform2.py

def transform_order(order_json):
    # Extract the order object
    order = order_json.get("order", {})
    order_id = order.get("order_id")
    items = order.get("items", [])

    # Compute total quantity and amount
    total_quantity = sum(item.get("quantity", 0) for item in items)
    total_amount = sum(item.get("quantity", 0) * item.get("price", 0) for item in items)

    # Return the transformed JSON as a new dictionary
    return {
        "order_id": order_id,
        "total_quantity": total_quantity,
        "total_amount": total_amount
    }
