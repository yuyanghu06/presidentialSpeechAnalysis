#parses throuugh files without audio and saves it onto my local memory using selenium

import os
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time

# Full path to the "noaudiospeechlinks.ini" file
file_path = r"C:\Users\Tange\OneDrive\Desktop\CodeProj\Python\PresidentialSpeechAnalysis\PresidentialSpeechRaws\missednoaudiolinks.ini"

# Check if the file exists
if not os.path.exists(file_path):
    print(f"File not found: {file_path}")
    exit(1)

# Read all links from the "noaudiospeechlinks.ini" file
with open(file_path, "r") as file:
    speech_links = [line.strip() for line in file if line.strip()]  # Remove empty lines and whitespace

# Use the specified folder to save the files
save_directory = r"C:\Users\Tange\OneDrive\Desktop\CodeProj\Python\PresidentialSpeechAnalysis\PresidentialSpeechRaws\Presidential_Speeches_Pre1932"

# Check if the folder exists
if not os.path.exists(save_directory):
    os.makedirs(save_directory)  # Create the directory if it doesn't exist

# Set up Selenium WebDriver
driver = webdriver.Chrome()

# Loop through all links in the file
for link in speech_links:
    print(f"Processing: {link}")

    try:
        # Open the link in the browser
        driver.get(link)
        time.sleep(3)  # Wait for the page to load

        # Extract the "About this speech" section
        about_text = None  # Initialize as None to handle missing data
        try:
            about_section = WebDriverWait(driver, 20).until(  # Wait for the element to load
                EC.presence_of_element_located((By.CSS_SELECTOR, "div.about-this-episode"))
            )
            about_text = about_section.text.strip()
            print("About this Speech:", about_text)  # Debugging output
        except Exception as e:
            print(f"Failed to extract 'About this speech' section for {link}: {e}")

        # Extract the static transcript from the "div.view-transcript" class
        transcript_text = None  # Initialize as None to handle missing data
        try:
            transcript_section = WebDriverWait(driver, 20).until(  # Wait for the element to load
                EC.presence_of_element_located((By.CSS_SELECTOR, "div.view-transcript"))
            )
            transcript_text = transcript_section.text.strip()
            print("Transcript (first 100 chars):", transcript_text[:100] if transcript_text else "None", "...")  # Debugging output
        except Exception as e:
            print(f"Failed to retrieve transcript for {link}: {e}")

        # Skip saving if both sections are missing
        if not about_text and not transcript_text:
            print(f"No content extracted for {link}. Skipping...")
            continue

        # Generate the file name based on "About this speech" content or fallback to a default name
        try:
            president_name = about_text.split("\n")[1] if about_text else "Unknown_President"
            date_addressed = about_text.split("\n")[2] if about_text else "Unknown_Date"
            print("President Name:", president_name)  # Debugging output
            print("Date Addressed:", date_addressed)  # Debugging output
            filename = f"{president_name}_{date_addressed}.txt".replace(" ", "_").replace("/", "-")
            filepath = os.path.join(save_directory, filename)
        except Exception as e:
            print(f"Failed to generate filename for {link}: {e}")
            continue

        # Save the content into a file
        try:
            print(f"Saving file to: {filepath}")  # Debugging output
            with open(filepath, "w", encoding="utf-8") as file:
                if about_text:
                    file.write("About this Speech:\n")
                    file.write(about_text)
                    file.write("\n\n")
                if transcript_text:
                    file.write("Transcript:\n")
                    file.write(transcript_text)
            print(f"Saved: {filepath}")
        except Exception as e:
            print(f"Failed to save the file for {link}: {e}")
            continue

    except Exception as e:
        print(f"Error processing {link}: {e}")
        continue

# Close the browser
driver.quit()

print("All speeches have been processed.")
