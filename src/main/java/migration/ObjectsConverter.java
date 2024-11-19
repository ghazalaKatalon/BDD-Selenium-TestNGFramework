package migration;



import java.io.*;
import java.nio.file.*;
import java.util.UUID;
import java.util.regex.*;

public class ObjectsConverter {



	    public static void main(String[] args) {
	        System.out.println("Starting Conversion");

	        String inputFile = "/Users/ghazalashahin/Documents/acc/new/src/test/java/Pages/DashboardPage.java";
	    	


    		// Path to your Selenium object
    String outputDir = "/Users/ghazalashahin/Documents/acc/migration/Object Repository";

	        // Create output directory if it does not exist
	        try {
	            Files.createDirectories(Paths.get(outputDir));
	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        try {
	            String data = new String(Files.readAllBytes(Paths.get(inputFile)));

	            // Updated regex to match multiple locator types (xpath, name, id, cssSelector, className)
	            Pattern pattern = Pattern.compile(
	                    "public\\s+By\\s+(\\w+)\\s*=\\s*By\\.(name|id|xpath|cssSelector|className)\\([\"'](.+?)[\"']\\)\\s*;"
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

	                // Convert unsupported locator types to XPATH
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

	                Path outputFilePath = Paths.get(outputDir, elementName + ".rs");
	                Files.write(outputFilePath, katalonObjectXML.getBytes());
	                System.out.println("Generated: " + outputFilePath);
	            }
	        } catch (IOException e) {
	            System.err.println("Error reading file: " + e.getMessage());
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
	            default:
	                return null;
	        }
	    }

	    /**
	     * Helper function to escape special XML characters in locator values
	     */
	    private static String escapeXml(String locatorValue) {
	        return locatorValue.replace("&", "&amp;")
	                           .replace("\"", "&quot;")
	                           .replace("'", "&apos;")
	                           .replace("<", "&lt;")
	                           .replace(">", "&gt;");
	    }
	}
