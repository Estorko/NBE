# Android Appium Automation (Python)

This project demonstrates how to automate Android apps using Appium and Python.

## Prerequisites

1. Python 3.7 or higher installed
2. Java Development Kit (JDK) 8 or higher
3. Android Studio and Android SDK
4. Appium Server installed
5. A physical Android device or emulator

## Setup

1. Create a virtual environment (recommended):
```bash
python -m venv venv
source venv/bin/activate  # On Windows: venv\Scripts\activate
```

2. Install dependencies:
```bash
pip install -r requirements.txt
```

3. Install Appium Server:
```bash
npm install -g appium
```

4. Install Appium Doctor to verify your setup:
```bash
npm install -g appium-doctor
```

5. Run Appium Doctor to check your setup:
```bash
appium-doctor
```

6. Start the Appium server:
```bash
appium
```

## Configuration

Before running the tests, you need to modify the following in `test/android_test.py`:

1. `deviceName`: Your Android device name (can be found using `adb devices`)
2. `appPackage`: Your app's package name
3. `appActivity`: Your app's main activity

## Running Tests

```bash
pytest test/android_test.py -v
```

To generate an HTML report:
```bash
pytest test/android_test.py --html=report.html
```

## Finding Elements

You can find elements using various locators:
```python
# By Accessibility ID
driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='elementId')

# By ID
driver.find_element(by=AppiumBy.ID, value='elementId')

# By XPath
driver.find_element(by=AppiumBy.XPATH, value='//element[@attribute="value"]')

# By Class name
driver.find_element(by=AppiumBy.CLASS_NAME, value='className')
```

## Common Commands

- Click: `element.click()`
- Input text: `element.send_keys('text')`
- Get text: `element.text`
- Wait for element: `element.is_displayed()`
- Clear text: `element.clear()`
- Get attribute: `element.get_attribute('attribute_name')` 