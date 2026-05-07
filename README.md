# Advanced File Type Recognizer (Magic Number Scanner)

**Author:** Anas Abdelraouf Saeed Ismail  
**Institution:** Faculty of Science, Alexandria University  

## Description
This program is a systems-level file identification tool designed to bypass standard file extensions. It operates entirely in the terminal and identifies files by reading their "Magic Numbers" (File Signatures) at the raw byte level. 

## Academic Constraints Met
1. Written in pure Java.
2. Does NOT use any pre-built file identification libraries (e.g., `Files.probeContentType`).
3. Does NOT rely on the file extension (e.g., `.jpg`, `.pdf`) to determine the type.
4. Terminal-based interface (No GUI).
5. Custom byte-matching logic to ensure structural originality.
6. Includes robust error handling for missing files, empty files, and permission issues.

## Supported File Signatures
* JPEG / JPG
* PNG
* GIF (87a and 89a formats)
* BMP
* TIFF (Little-Endian and Big-Endian)
* RAW (Canon CR2 format)

## How to Use

1. **Compile the program:**
   ```bash
   cd src
   javac FileTypeRecognizer.java
   
   Run the program:

Bash
java FileTypeRecognizer

When prompted, paste the absolute path to the file you want to test. (The program automatically handles paths wrapped in Windows double quotes).

To stop the program, type exit or quit.
