package converter;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.stream.Stream;
import javax.swing.*;

public class ObjectsConverter {

    public static void main(String[] args) {

        System.out.println("Starting Conversion");

        String inputDirectoryPath = System.getProperty("inputDirectoryPath");
        inputDirectoryPath = Paths.get(inputDirectoryPath, "src", "test","java").toString();
        String katalonDir = System.getProperty("katalonDir");

        if (inputDirectoryPath == null || katalonDir == null) {
            System.out.println("Directories not provided. Exiting program.");
            System.exit(0);
        }

        String projectFolderPath = Paths.get(katalonDir, "Object Repository").toString();

        // Create the project folder if it does not exist
        try {
            Files.createDirectories(Paths.get(projectFolderPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Stream<Path> filePaths = Files.walk(Paths.get(inputDirectoryPath))) {
            // Process each `.java` file in the input directory
            filePaths.filter(Files::isRegularFile)
                     .filter(path -> path.toString().endsWith(".java"))
                     .forEach(filePath -> processFile(filePath, projectFolderPath));
        } catch (IOException e) {
            System.err.println("Error reading files from input directory: " + e.getMessage());
        }
    }

    /**
     * Processes a single `.java` file to extract Selenium locators and generate Katalon objects.
     */
    private static void processFile(Path inputFilePath, String projectFolderPath) {
        System.out.println("Processing file: " + inputFilePath);

        try {
            // Read the file content
            String data = new String(Files.readAllBytes(inputFilePath));

            // Match any usage of By locators
            Pattern byLocatorPattern = Pattern.compile(
                "(?:private|public)?\\s*By\\s+(\\w+)\\s*=\\s*By\\.(id|name|xpath|cssSelector|className|linkText|partialLinkText|tagName)\\([\\\"'](.+?)[\\\"']\\)\\s*;"
            );

            // Match inline driver.findElement(By.*)
            Pattern inlineByPattern = Pattern.compile(
                "driver\\.findElement\\(By\\.(id|name|xpath|cssSelector|className|linkText|partialLinkText|tagName)\\([\\\"'](.+?)[\\\"']\\)\\)"
            );

            Matcher byLocatorMatcher = byLocatorPattern.matcher(data);
            Matcher inlineByMatcher = inlineByPattern.matcher(data);

            // Process `By` locators
            while (byLocatorMatcher.find()) {
                String elementName = byLocatorMatcher.group(1);
                String locatorType = byLocatorMatcher.group(2);
                String locatorValue = byLocatorMatcher.group(3);
                generateAndSaveKatalonObject(elementName, locatorType, locatorValue, projectFolderPath);
            }

            // Process inline `By` locators
            int inlineCount = 0;
            while (inlineByMatcher.find()) {
                String locatorType = inlineByMatcher.group(1);
                String locatorValue = inlineByMatcher.group(2);
                String elementName = "InlineElement" + (++inlineCount);
                generateAndSaveKatalonObject(elementName, locatorType, locatorValue, projectFolderPath);
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + inputFilePath + " - " + e.getMessage());
        }
    }

    /**
     * Generates a Katalon object XML file and saves it to the specified directory.
     */
    private static void generateAndSaveKatalonObject(String elementName, String locatorType, String locatorValue, String projectFolderPath) {
        String guid = UUID.randomUUID().toString();
        String katalonObjectXML = generateKatalonObjectXML(elementName, locatorType, locatorValue, guid);

        // Save the Katalon object XML
        Path outputFilePath = Paths.get(projectFolderPath, elementName + ".rs");
        try {
            Files.write(outputFilePath, katalonObjectXML.getBytes());
            System.out.println("Generated: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing Katalon object: " + e.getMessage());
        }
    }

    /**
     * Generates the XML structure for a Katalon object.
     */
    private static String generateKatalonObjectXML(String elementName, String locatorType, String locatorValue, String guid) {
        locatorValue = escapeXml(locatorValue);

        String xpathValue = locatorType.equals("xpath") ? locatorValue : generateXpathFromLocator(locatorValue, locatorType);

        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<WebElementEntity>\n" +
               "   <description></description>\n" +
               "   <name>" + elementName + "</name>\n" +
               "   <tag></tag>\n" +
               "   <elementGuidId>" + guid + "</elementGuidId>\n" +
               "   <selectorCollection>\n" +
               "      <entry>\n" +
               "         <key>XPATH</key>\n" +
               "         <value>" + xpathValue + "</value>\n" +
               "      </entry>\n" +
               "   </selectorCollection>\n" +
               "   <selectorMethod>XPATH</selectorMethod>\n" +
               "   <useRalativeImagePath>true</useRalativeImagePath>\n" +
               "</WebElementEntity>";
    }

    /**
     * Escapes special XML characters.
     */
    private static String escapeXml(String value) {
        return value.replace("&", "&amp;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;");
    }

    /**
     * Generates an XPath from other locator types if XPath is not directly given.
     */
    private static String generateXpathFromLocator(String locatorValue, String locatorType) {
        switch (locatorType) {
            case "id":
                return "//*[@id='" + locatorValue + "']";
            case "name":
                return "//input[@name='" + locatorValue + "']";
            case "className":
                return "//*[contains(@class, '" + locatorValue + "')]";
            case "cssSelector":
                return locatorValue;
            case "linkText":
                return "//a[text()='" + locatorValue + "']";
            case "partialLinkText":
                return "//a[contains(text(),'" + locatorValue + "')]";
            case "tagName":
                return "//" + locatorValue;
            default:
                return locatorValue;
        }
    }
}
