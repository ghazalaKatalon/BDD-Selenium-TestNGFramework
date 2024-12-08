package converter;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;
import java.util.regex.*;
import java.util.stream.Stream;

public class ObjectsConverter {

    public static void main(String[] args) {
        System.out.println("Starting Conversion");

        // Input directory containing Java files to process
        String inputDirectoryPath = "src/test/java/";

        // Katalon directory where the output will be saved
        String katalonDir = "/Users/ghazalashahin/Documents/AIBLatest/Demo";
       
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

            // Updated regex to match multiple locator types (xpath, name, id, cssSelector, className, linkText, partialLinkText, tagName)
            Pattern pattern = Pattern.compile(
                    "public\\s+By\\s+(\\w+)\\s*=\\s*By\\.(name|id|xpath|cssSelector|className|linkText|partialLinkText|tagName)\\([\"'](.+?)[\"']\\)\\s*;"
            );
            Matcher matcher = pattern.matcher(data);

            while (matcher.find()) {
                String elementName = matcher.group(1);
                String locatorType = matcher.group(2);
                String locatorValue = matcher.group(3);
                String guid = UUID.randomUUID().toString();

                // Convert locator type to a valid Katalon XML structure with XPATH
                String katalonObjectXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<WebElementEntity>\n" +
                        "   <description></description>\n" +
                        "   <name>" + elementName + "</name>\n" +
                        "   <tag></tag>\n" +
                        "   <elementGuidId>" + guid + "</elementGuidId>\n" +
                        "   <selectorCollection>\n";

                // Generate XPath value based on the locator type
                String xpathValue = generateXpathFromLocator(locatorValue, locatorType);
                katalonObjectXML +=
                        "      <entry>\n" +
                        "         <key>XPATH</key>\n" +
                        "         <value>" + xpathValue + "</value>\n" +
                        "      </entry>\n";

                katalonObjectXML +=
                        "   </selectorCollection>\n" +
                        "   <selectorMethod>XPATH</selectorMethod>\n" +
                        "   <smartLocatorCollection>\n" +
                        "      <entry>\n" +
                        "         <key>SMART_LOCATOR</key>\n" +
                        "         <value>internal:attr=[placeholder=&quot;Username&quot;i]</value>\n" +
                        "      </entry>\n" +
                        "   </smartLocatorCollection>\n" +
                        "   <smartLocatorEnabled>false</smartLocatorEnabled>\n" +
                        "   <useRalativeImagePath>true</useRalativeImagePath>\n" +
                        "   <webElementProperties>\n" +
                        "      <isSelected>false</isSelected>\n" +
                        "      <matchCondition>equals</matchCondition>\n" +
                        "      <name>tag</name>\n" +
                        "      <type>Main</type>\n" +
                        "      <value>input</value>\n" +
                        "      <webElementGuid>" + UUID.randomUUID().toString() + "</webElementGuid>\n" +
                        "   </webElementProperties>\n";

                katalonObjectXML += "</WebElementEntity>";

                // Save the Katalon object to the output directory
                Path outputFilePath = Paths.get(projectFolderPath, elementName + ".rs");
                Files.write(outputFilePath, katalonObjectXML.getBytes());
                System.out.println("Generated: " + outputFilePath);
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + inputFilePath + " - " + e.getMessage());
        }
    }

    /**
     * Generate an XPath locator based on other locator types if XPath is not the primary locator.
     */
    private static String generateXpathFromLocator(String locatorValue, String locatorType) {
        switch (locatorType) {
            case "name":
                return "//input[@name='" + locatorValue + "']";
            case "id":
                return "//*[@id='" + locatorValue + "']";
            case "className":
                return "//*[@class='" + locatorValue + "']";
            case "cssSelector":
                return locatorValue; // Directly use the CSS selector if given
            case "xpath":
                return locatorValue; // Already in XPATH format
            case "linkText":
                return "//a[text()='" + locatorValue + "']";
            case "partialLinkText":
                return "//a[contains(text(),'" + locatorValue + "')]";
            case "tagName":
                return "//" + locatorValue;
            default:
                return null;
        }
    }

    private static String escapeXml(String locatorValue) {
        return locatorValue.replace("&", "&amp;")
                           .replace("\"", "&quot;")
                           .replace("'", "&apos;")
                           .replace("<", "&lt;")
                           .replace(">", "&gt;");
    }
}
