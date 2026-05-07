/**
 * ============================================================================
 * PROJECT: Image Type Recognizer (Unique Number Scanner)
 * AUTHOR: Anas Abdelraouf Saeed Ismail
 * ============================================================================
 *
 * * DESCRIPTION:
 * This program is an image Type Recognizer tool designed to bypass
 * standard file extensions and it accurately guarantees the true file format regardless of its external naming convention. It identifies
 * files by reading their "Unique Numbers" (File Signatures) at the raw byte level.

 * * HOW TO USE (TERMINAL INSTRUCTIONS):
 * 1. Compile the program:
 * javac ImageTypeRecognizer.java
 * * 2. Run the program:
 * java ImageTypeRecognizer
 * * 3. When prompted, paste the absolute path to the file you want to test.
 * * 4. To stop the program, type 'exit' or 'quit'.
 * ============================================================================
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class ImageTypeRecognizer {

    // Unique Numbers (File Signatures)
    private static final int[] Unique_JPEG = {0xFF, 0xD8, 0xFF};
    private static final int[] Unique_PNG = {0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A};
    private static final int[] Unique_GIF87A = {0x47, 0x49, 0x46, 0x38, 0x37, 0x61};
    private static final int[] Unique_GIF89A = {0x47, 0x49, 0x46, 0x38, 0x39, 0x61};
    private static final int[] Unique_BMP = {0x42, 0x4D};
    private static final int[] Unique_TIFF_LE = {0x49, 0x49, 0x2A, 0x00};
    private static final int[] Unique_TIFF_BE = {0x4D, 0x4D, 0x00, 0x2A};
    private static final int[] Unique_RAW_CR2 = {0x49, 0x49, 0x2A, 0x00, 0x10, 0x00, 0x00, 0x00, 0x43, 0x52};
    private static final int[] Unique_WEBP = {0x52, 0x49, 0x46, 0x46};
    private static final int[] Unique_ICO = {0x00, 0x00, 0x01, 0x00}; 
    private static final int[] Unique_PSD = {0x38, 0x42, 0x50, 0x53}; 
    
    // Test for Unrelated extensions 
    private static final int[] Unique_EXE = {0x4D, 0x5A};
    private static final int[] Unique_ELF = {0x7F, 0x45, 0x4C, 0x46}; 
    private static final int[] Unique_PDF = {0x25, 0x50, 0x44, 0x46, 0x2D}; 
    private static final int[] Unique_ZIP = {0x50, 0x4B, 0x03, 0x04};
    private static final int[] Unique_RAR = {0x52, 0x61, 0x72, 0x21, 0x1A, 0x07, 0x00}; 
    private static final int[] Unique_RAR_V5 = {0x52, 0x61, 0x72, 0x21, 0x1A, 0x07, 0x01, 0x00};
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("--- Image Type Recognizer ---");
        System.out.println("--Type 'exit' or 'quit' at any time to stop--\n");

        while (true) {
            System.out.print("Enter the full path to the file to scan: ");
            String filePath = scanner.nextLine().trim();

            if (filePath.equalsIgnoreCase("exit") || filePath.equalsIgnoreCase("quit")) {
                System.out.println("Exiting program. Goodbye!");
                break;
            }

            // Clean the path from Windows double quotes
            filePath = filePath.replace("\"", "");
            File file = new File(filePath);

            // Error Handling: File validity
            if (!file.exists() || !file.isFile()) {
                System.err.println("Error: The file does not exist or is a directory.\n");
                continue;
            }

            // Error Handling: Read permissions
            if (!file.canRead()) {
                System.err.println("Error: Permission denied. Cannot read the file.\n");
                continue;
            }

            analyzeAndReport(file);
        }

        scanner.close();
    }

    private static void analyzeAndReport(File file) {
        byte[] rawHeader = new byte[10];
        int bytesRead = -1;

        // Safely read the first 10 bytes of the file
        try (FileInputStream fis = new FileInputStream(file)) {
            bytesRead = fis.read(rawHeader);
        } catch (IOException e) {
            System.err.println("An unexpected I/O error occurred: " + e.getMessage() + "\n");
            return;
        }

        if (bytesRead == -1) {
            System.err.println("Error: The file is completely empty.\n");
            return;
        }

        // Convert to unsigned integers and build the Hex String for the report
        int[] header = new int[bytesRead];
        StringBuilder hexSignature = new StringBuilder();
        for (int i = 0; i < bytesRead; i++) {
            header[i] = rawHeader[i] & 0xFF;
            // Format each byte as a 2-character uppercase Hexadecimal
            hexSignature.append(String.format("%02X ", header[i]));
        }

        // Determine the file type using our custom logic
        String actualType = identifyFileType(header);

        // Print the detailed Analysis Report
        System.out.println("\n==========================================");
        System.out.println("           FILE ANALYSIS REPORT           ");
        System.out.println("==========================================");
        System.out.println("[1] File Name          : " + file.getName());
        System.out.println("[2] File Size          : " + formatFileSize(file.length()));
        System.out.println("[3] Apparent Extension : " + getFileExtension(file.getName()));
        System.out.println("[4] Hex Signature      : " + hexSignature.toString().trim());
        System.out.println("------------------------------------------");
        System.out.println("[+] ACTUAL FILE TYPE   : " + actualType);
        System.out.println("==========================================\n");
    }

    // Identifies the file based entirely on the header array, ignoring the extensions
    private static String identifyFileType(int[] header) {
        if (matchesSignature(header, Unique_RAW_CR2)) {
            return "RAW Image File (Canon CR2)";
        } else if (matchesSignature(header, Unique_PNG)) {
            return "PNG Image File";
        } else if (matchesSignature(header, Unique_JPEG)) {
            return "JPEG Image File";
        } else if (matchesSignature(header, Unique_GIF87A) || matchesSignature(header, Unique_GIF89A)) {
            return "GIF Image File";
        } else if (matchesSignature(header, Unique_TIFF_LE) || matchesSignature(header, Unique_TIFF_BE)) {
            return "TIFF Image File";
        } else if (matchesSignature(header, Unique_BMP)) {
            return "BMP Image File";
        } else if (matchesSignature(header, Unique_EXE)) {
            return "Windows Executable / DLL File";
        } else if (matchesSignature(header, Unique_ELF)) {
            return "Linux Executable (ELF)";
        } else if (matchesSignature(header, Unique_PDF)) {
            return "PDF Document";
        } else if (matchesSignature(header, Unique_ZIP)) {
            return "ZIP Archive / MS Office Open XML (DOCX/XLSX)";
        } else if (matchesSignature(header, Unique_RAR) || matchesSignature(header, Unique_RAR_V5)) {
            return "RAR Archive File";
        } else if (matchesSignature(header, Unique_WEBP)) {
            return "WebP Image File";
        } else if (matchesSignature(header, Unique_ICO)) {
            return "ICO Image File (Icon)";
        } else if (matchesSignature(header, Unique_PSD)) {
            return "PSD Image File (Adobe Photoshop)";
        }
        
        return "Unknown / Other Format";
    }

    //  Compare byte arrays
    private static boolean matchesSignature(int[] fileHeader, int[] signature) {
        if (fileHeader.length < signature.length) {
            return false;
        }
        for (int i = 0; i < signature.length; i++) {
            if (fileHeader[i] != signature[i]) {
                return false;
            }
        }
        return true;
    }

    //  Format bytes into readable KB/MB
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " Bytes";
        else if (bytes < 1024 * 1024) return String.format("%.2f KB", bytes / 1024.0);
        else return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }

    //  Extract the extension strictly for display purposes
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return "." + fileName.substring(lastDotIndex + 1).toUpperCase();
        }
        return "NONE";
    }
}
