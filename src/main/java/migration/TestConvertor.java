package migration;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;
import java.util.UUID;

public class TestConvertor {
    public static void main(String[] args) {
        // Input Selenium file and output Katalon file paths
        String inputFilePath = "/Users/ghazalashahin/Documents/AIBLatest/src/test/java/StepDefinitions/LoginStep.java";
        String mainDirectoryPath = "/Users/ghazalashahin/Documents/AIBLatest/Demo/";
        String objectFilePath = "/Users/ghazalashahin/Documents/AIBLatest/src/test/java/Pages/DashboardPage.java"; // Object file

        try {
            // Process the input file
            File inputFile = new File(inputFilePath);
            String className = inputFile.getName().replace(".java", "");
            System.out.println("Processing: " + inputFile.getName());

            // Read the Selenium code from the file
            String seleniumCode = readFromFile(inputFile.getAbsolutePath());

            // Read the object file to get the element names
            Map<String, String> objectMap = readObjectFile(objectFilePath); // Load object map

            // Convert the Selenium code to Katalon Studio code
            String katalonCode = convertToKatalon(seleniumCode, objectMap);

            // Create the Scripts folder and className subfolder
            Path scriptsFolderPath = Paths.get(mainDirectoryPath, "Scripts", className);
            Files.createDirectories(scriptsFolderPath);

            // Generate a dynamic filename for the Groovy script
            String dynamicNumber = String.valueOf(System.currentTimeMillis());
            String groovyFileName = "Script" + dynamicNumber + ".groovy";
            Path groovyFilePath = scriptsFolderPath.resolve(groovyFileName);

            // Write the Groovy script
            writeToFile(groovyFilePath.toString(), katalonCode);

            // Create the .tc file in the output directory
            Path tcFilePath = Paths.get(mainDirectoryPath, "Test Cases", className + ".tc");
            String tcFileContent = generateTcFileContent(className);
            Files.createDirectories(tcFilePath.getParent()); // Ensure the parent folder exists
            writeToFile(tcFilePath.toString(), tcFileContent);

            System.out.println("Conversion complete.");
            System.out.println("Katalon test case saved at: " + groovyFilePath);
            System.out.println(".tc file saved at: " + tcFilePath);
        } catch (IOException e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    // Read the contents of the file into a string
    private static String readFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    // Write the contents to a file
    private static void writeToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    // Read the object file and create a map of object names to locators
    private static Map<String, String> readObjectFile(String objectFilePath) throws IOException {
        Map<String, String> objectMap = new HashMap<>();
        Pattern pattern = Pattern.compile("public By (\\w+) = By\\.(\\w+)\\(\"([^\"]+)\"\\);");

        String objectCode = readFromFile(objectFilePath);
        Matcher matcher = pattern.matcher(objectCode);

        while (matcher.find()) {
            String objectName = matcher.group(1);
            String locatorValue = matcher.group(3); // Only the locator value

            // Add object name to map with its locator value
            objectMap.put(objectName, locatorValue);
        }
        return objectMap;
    }

    // Convert Selenium code to Katalon Studio Groovy test case
    private static String convertToKatalon(String seleniumCode, Map<String, String> objectMap) {
        StringBuilder katalonCode = new StringBuilder();

        // Add the necessary static imports for Katalon Studio
        katalonCode.append("""
            import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
            import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
            import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
            import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
            import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
            import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
            import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
            import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
            import com.kms.katalon.core.model.FailureHandling as FailureHandling
            import com.kms.katalon.core.testcase.TestCase as TestCase
            import com.kms.katalon.core.testdata.TestData as TestData
            import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
            import com.kms.katalon.core.testobject.TestObject as TestObject
            import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
            import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
            import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
            import internal.GlobalVariable as GlobalVariable
        """);

        // Regex for WebElements and associated actions
        Pattern elementPattern = Pattern.compile("(\\w+)\\s*=\\s*driver\\.findElement\\(By\\.(\\w+)\\(\"([^\"]+)\"\\)\\);");
        Pattern actionPattern = Pattern.compile("(\\w+)\\.(sendKeys|click|isDisplayed)\\(([^)]*)\\);");
        Pattern conditionPattern = Pattern.compile("(if|while)\\s*\\(([^)]+\\.isDisplayed\\(\\))\\)\\s*\\{");
        Pattern printPattern = Pattern.compile("System\\.out\\.println\\(\"([^\"]+)\"\\);");

        // Process each line of the Selenium code
        String[] lines = seleniumCode.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();

            // Handle WebElement declarations
            Matcher elementMatcher = elementPattern.matcher(line);
            if (elementMatcher.find()) {
                String variableName = elementMatcher.group(1);
                String objectName = elementMatcher.group(3); // Object name from the findElement statement
                // Look up the locator from the object map
                String locator = objectMap.get(objectName);
                if (locator != null) {
                    katalonCode.append(String.format("TestObject %s = findTestObject('%s')\n", variableName, locator));
                }
                continue;
            }

            // Handle actions like sendKeys, click
            Matcher actionMatcher = actionPattern.matcher(line);
            if (actionMatcher.find()) {
                String variableName = actionMatcher.group(1);
                String action = actionMatcher.group(2);
                String argument = actionMatcher.group(3);

                if ("sendKeys".equals(action)) {
                    // Ensure the action uses 'findTestObject' for the object name
                    katalonCode.append(String.format("WebUI.setText(findTestObject('%s'), %s)\n", variableName, argument));
                } else if ("click".equals(action)) {
                    // Ensure the action uses 'findTestObject' for the object name
                    katalonCode.append(String.format("WebUI.click(findTestObject('%s'))\n", variableName));
                }
                continue;
            }

            // Handle conditions (if/while) with isDisplayed
            Matcher conditionMatcher = conditionPattern.matcher(line);
            if (conditionMatcher.find()) {
                String conditionVariable = conditionMatcher.group(2).replace(".isDisplayed()", "").trim(); // Extract variable name
                katalonCode.append(String.format("WebUI.verifyElementPresent(findTestObject('%s'), 1000)\n", conditionVariable));
                continue;
            }

            // Handle System.out.println
            Matcher printMatcher = printPattern.matcher(line);
            if (printMatcher.find()) {
                katalonCode.append(String.format("println('%s')\n", printMatcher.group(1)));
                continue;
            }
        }
        return katalonCode.toString();
    }

    // Generate the content for the .tc file


    // Generate the content for the .tc file
    private static String generateTcFileContent(String className) {
        String uuid = UUID.randomUUID().toString();
        return String.format("""
            <?xml version="1.0" encoding="UTF-8"?>
            <TestCaseEntity>
               <description></description>
               <name>%s</name>
               <tag></tag>
               <comment></comment>
               <recordOption>OTHER</recordOption>
               <testCaseGuid>%s</testCaseGuid>
            </TestCaseEntity>
            """, className, uuid);
    }
}
