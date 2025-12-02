import csv
import os

# Define file paths
csv_file = 'data/forecast_sample.csv'
seed_sql_file = 'seed_test_data.sql'

def generate_scripts():
    seed_rows = []

    try:
        # Check if the CSV directory exists
        if not os.path.exists(csv_file):
            print(f"Error: File not found at {csv_file}")
            return

        with open(csv_file, mode='r', encoding='utf-8') as f:
            # Read CSV and map to dictionary based on header row
            reader = csv.DictReader(f)
            for row in reader:
                # Collect data to create the Seed SQL script
                # Mapping CSV columns: item_name, fy2023, fy2024, fy2025
                values = f"('{row['item_name']}', {row['fy2023']}, {row['fy2024']}, {row['fy2025']})"
                seed_rows.append(values)

        if not seed_rows:
            print("No data found in CSV to generate SQL.")
            return

        # --- Generate Seed SQL (Import) ---
        seed_content = [
            "-- Auto-generated seed script for LivePlan testing\n",
            "INSERT INTO revenue_items (name, fy2023_amount, fy2024_amount, fy2025_amount) VALUES \n",
            ",\n".join(seed_rows) + ";"
        ]
        
        with open(seed_sql_file, 'w', encoding='utf-8') as f:
            f.writelines(seed_content)
            
        print(f"Success! Generated {seed_sql_file} based on {csv_file}")

    except Exception as e:
        # Catch and print any unexpected errors during processing
        print(f"An error occurred: {e}")

if __name__ == "__main__":
    generate_scripts()