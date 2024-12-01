from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Set up Selenium WebDriver
driver = webdriver.Chrome()

# Open the main page
url = "https://millercenter.org/the-presidency/presidential-speeches"
driver.get(url)

# Adjust browser zoom for better visibility
driver.execute_script("document.body.style.zoom='75%'")

# Wait for the page to load
time.sleep(5)

# Scroll and collect all links
last_height = driver.execute_script("return document.body.scrollHeight")
speech_links = []
container_selector = "div.views-infinite-scroll-content-wrapper.clearfix"
row_selector = "div.views-row"

while True:
    # Wait for the container to be present
    try:
        WebDriverWait(driver, 10).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, container_selector))
        )
    except Exception as e:
        print(f"Error: Could not find container {container_selector}: {e}")
        break

    # Scroll down to the bottom
    driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
    time.sleep(5)  # Increased wait time to ensure elements load

    # Collect all speech links in the rows
    try:
        rows = driver.find_elements(By.CSS_SELECTOR, row_selector)
        for row in rows:
            link = row.find_element(By.CSS_SELECTOR, "a")
            href = link.get_attribute("href")
            if href not in speech_links:
                speech_links.append(href)
    except Exception as e:
        print(f"Error while collecting links: {e}")

    # Check if the scroll reached the bottom
    new_height = driver.execute_script("return document.body.scrollHeight")
    if new_height == last_height:
        print("Reached the bottom of the page.")
        break
    last_height = new_height

# Print all collected speech links to the console
print("Collected speech links:")
for speech_link in speech_links:
    print(speech_link)

# Close the browser
driver.quit()
