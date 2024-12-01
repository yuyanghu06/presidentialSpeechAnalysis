#This program  uses selenium to go into the website millercenter.org and scans and retrieves the links of all the links to the seperate speech browser
#For some reason the other code that automatically tapped through the websites trigger loading couldnt't retrieve it so I needed to make a seperate code file.
#goes through grabs all the links of speech browsers and returns it into the console 
#later I manually turned them into ini files


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

# Collect all speech links on the current page
speech_links = []
row_selector = "div.views-row"

try:
    # Wait for the rows to be present
    WebDriverWait(driver, 10).until(
        EC.presence_of_all_elements_located((By.CSS_SELECTOR, row_selector))
    )

    # Collect all links in the rows
    rows = driver.find_elements(By.CSS_SELECTOR, row_selector)
    for row in rows:
        link = row.find_element(By.CSS_SELECTOR, "a")
        href = link.get_attribute("href")
        if href not in speech_links:
            speech_links.append(href)
except Exception as e:
    print(f"Error while collecting links: {e}")

# Print all collected speech links to the console
print("Collected speech links from the current page:")
for speech_link in speech_links:
    print(speech_link)

# Close the browser
driver.quit()
