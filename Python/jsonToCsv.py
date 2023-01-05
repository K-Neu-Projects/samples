import csv
import json

# Open the JSON file and load the data
with open('data.json', 'r', encoding='utf-8') as f:
    data = json.load(f)

# Open a CSV file for writing
with open('data.csv', 'w', newline='', encoding='utf-8') as f:
    # Create a CSV writer
    writer = csv.writer(f)

    # Write the header row
    writer.writerow(data[0].keys())

    # Write the data rows
    for row in data:
        writer.writerow(row.values())
