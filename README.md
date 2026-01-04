# QA Automation Demo Project

This repository is a collection of my automation practices

## Project Structure
* `ui_automation_java/`: Selenium BDD framework with Page Object Model (POM).
* `liveplan_automation_demo/`: Playwright implementation focused on financial data visualization.
* `python_api_automation/`: Pytest scripts for backend API validation.
* `sql_scripts/`: SQL seed generators and sample test datasets.
* 
### `ui_automation_java/`: Offline Sync (BDD)
* **Scenario**: Verify data integrity and synchronization once the network connection is restored.
* **Tools**: Cucumber + Selenium **Robot Class**.
* **Key Solution**: Implemented the **Robot Class** for file uploads to bypass limitations where standard `sendKeys` was not working

### 3. Financial Forecast (Playwright)
* **Tool**: Playwright Java.
* **Features**:
    * **Visual Testing**: Integrated with **Percy** for visual regression detection.
    * **Data Seeding**: Includes a Python utility (`generator_tool.py`) that transforms CSV datasets into SQL seed scripts for automated DB initialization.

## Tech Stack
* **Languages**: Java, Python.
* **Frameworks**: Selenium, Playwright, Cucumber, Pytest.
* **Others**: Maven, Percy (Visual UI), Robot Class, SQL.
