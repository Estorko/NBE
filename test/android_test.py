from appium import webdriver
from appium.webdriver.common.appiumby import AppiumBy
import pytest

@pytest.fixture
def driver():
    # Desired capabilities
    desired_caps = {
        'platformName': 'Android',
        'automationName': 'UiAutomator2',
        'deviceName': 'Android Emulator',  # Change this to your device name
        'appPackage': 'com.example.app',   # Change this to your app's package name
        'appActivity': '.MainActivity',    # Change this to your app's main activity
        'noReset': True
    }
    
    # Initialize the driver
    driver = webdriver.Remote('http://localhost:4723', desired_caps)
    yield driver
    driver.quit()

def test_example(driver):
    try:
        # Example: Find and click an element using accessibility ID
        element = driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='elementId')
        element.click()

        # Example: Input text
        input_field = driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='inputFieldId')
        input_field.send_keys('Hello World')

        # Example: Wait for an element to be visible
        visible_element = driver.find_element(by=AppiumBy.ACCESSIBILITY_ID, value='visibleElementId')
        visible_element.is_displayed()

    except Exception as e:
        print(f"Error during automation: {e}")
        raise 