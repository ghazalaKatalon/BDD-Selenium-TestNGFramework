package converter;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class stepC {

    public static void main(String[] args) {
    	String inputDirectory = System.getProperty("katalonDir");
    	String projectName = System.getProperty("projectName");

    	if (inputDirectory != null && projectName != null) {
    	    inputDirectory = Paths.get(inputDirectory, "Include", "features", projectName).toString();
    	} else {
    	    throw new IllegalArgumentException("katalonDir or projectName is not set");
    	}

    	// Corrected output directory path
    	String outputDirectory = Paths.get(System.getProperty("katalonDir"), "Include", "scripts", "groovy", projectName).toString();

    	String converterFilePath = System.getProperty("inputDirectoryPath");
    	converterFilePath = Paths.get(converterFilePath, "src", "main", "java", "converter", "FeatureFileConverter.java").toString();

    	try {
    	    // Read package name from FeatureFileConverter.java
    	    String packageName = extractProjectName(converterFilePath);
    	    if (packageName == null) {
    	        System.out.println("Package name not found in FeatureFileConverter.java.");
    	        return;
    	    }

    	    // Create the project package directory under groovy folder
    	    Files.createDirectories(Paths.get(outputDirectory));

    	    // Get all .feature files in the input directory
    	    DirectoryStream<Path> featureFiles = Files.newDirectoryStream(Paths.get(inputDirectory), "Login.feature");

    	    for (Path featureFile : featureFiles) {
    	        // Extract feature file name without extension
    	        String featureFileName = featureFile.getFileName().toString();
    	        String featureName = featureFileName.replace(".feature", "");

    	        // Read feature file content
    	        String featureContent = Files.readString(featureFile);

    	        // Generate step definition content
    	        String stepDefContent = generateStepDefinition(featureContent, featureName, packageName);

    	        // Save step definition file in the project package folder
    	        String outputPath = Paths.get(outputDirectory, featureName + ".groovy").toString();
    	        Files.writeString(Paths.get(outputPath), stepDefContent);

    	        System.out.println("Step definition created for: " + featureFileName);
    	        System.out.println("Step definition saved at: " + outputPath);
    	    }

    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
    }


    // Method to extract the project name from FeatureFileConverter.java
    public static String extractProjectName(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        String projectName = null;
        
        // Regex pattern to find the project folder path line
        Pattern pattern = Pattern.compile("String projectFolderPath = .*\"(.*?)\";");
        
        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                // Extract the project name from the line
                projectName = matcher.group(1);  // The captured group for the project name
                break;
            }
        }
        
        reader.close();
        return projectName != null ? projectName : null;
    }

    private static String generateStepDefinition(String featureContent, String className, String packageName) {
        StringBuilder groovyContent = new StringBuilder();

        // Get project name or use a default value
        String PackageName = System.getProperty("projectName", "defaultPackage");
        System.out.println("Using package name: " + PackageName);  // Debugging

        // Add package declaration
        groovyContent.append("package ").append(PackageName).append("\n\n");
        // Add imports
        groovyContent.append("""
                import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
                import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
                import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
                import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

                import com.kms.katalon.core.annotation.Keyword
                import com.kms.katalon.core.checkpoint.Checkpoint
                import com.kms.katalon.core.checkpoint.CheckpointFactory
                import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
                import com.kms.katalon.core.model.FailureHandling
                import com.kms.katalon.core.testcase.TestCase
                import com.kms.katalon.core.testcase.TestCaseFactory
                import com.kms.katalon.core.testdata.TestData
                import com.kms.katalon.core.testdata.TestDataFactory
                import com.kms.katalon.core.testobject.ObjectRepository
                import com.kms.katalon.core.testobject.TestObject
                import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
                import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
                import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

                import internal.GlobalVariable

                import org.openqa.selenium.WebElement
                import org.openqa.selenium.WebDriver
                import org.openqa.selenium.By

                import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
                import com.kms.katalon.core.webui.driver.DriverFactory

                import com.kms.katalon.core.testobject.RequestObject
                import com.kms.katalon.core.testobject.ResponseObject
                import com.kms.katalon.core.testobject.ConditionType
                import com.kms.katalon.core.testobject.TestObjectProperty

                import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
                import com.kms.katalon.core.util.KeywordUtil

                import com.kms.katalon.core.webui.exception.WebElementNotFoundException

                import cucumber.api.java.en.And
                import cucumber.api.java.en.Given
                import cucumber.api.java.en.Then
                import cucumber.api.java.en.When

                """);

        groovyContent.append("class ").append(className).append(" {\n");

        // Extract steps using regex
        Pattern stepPattern = Pattern.compile("(Given|When|Then|And) (.+)");
        Matcher matcher = stepPattern.matcher(featureContent);

        while (matcher.find()) {
            String keyword = matcher.group(1); // Given, When, Then, And
            String step = matcher.group(2);   // Step description

    
            if (step.contains("<") && step.contains(">")) {
                // Replace placeholder with (.*) for regex matching
                String methodName = step.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]", "");
                String dynamicParameter = step.replaceAll(".*<([^>]+)>.*", "$1"); // Extract parameter name inside < >

                groovyContent.append("\n\t@").append(keyword)
                    .append("(\"").append(step.replaceAll("<[^>]+>", "(.*)")).append("\")\n")
                    .append("\tdef ").append(methodName).append("(String ").append(dynamicParameter).append(") {\n")
                    .append("\t\tWebUI.callTestCase(findTestCase(\"Test Cases/").append(methodName).append("\"), [ ('").append(dynamicParameter).append("') : ").append(dynamicParameter).append(" ], FailureHandling.STOP_ON_FAILURE)\n")
                    .append("\t}\n");
            } else {
                // Regular step without dynamic parameters
                String methodName = step.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]", "");

                groovyContent.append("\n\t@").append(keyword)
                    .append("(\"").append(step).append("\")\n")
                    .append("\tdef ").append(methodName).append("() {\n")
                    .append("\t\tWebUI.callTestCase(findTestCase(\"Test Cases/").append(methodName).append("\"), [:], FailureHandling.STOP_ON_FAILURE)\n")
                    .append("\t}\n");
            }
        }

        // Close the class
        groovyContent.append("}\n");

        return groovyContent.toString();
    }
}
