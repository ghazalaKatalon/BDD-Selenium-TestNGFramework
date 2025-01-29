package converter;

import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class FeatureToStepDef {

    public static void main(String[] args) {
        // Input directory containing feature files
        String inputDirectory = "/Users/ghazalashahin/Documents/AIBLatest/Demo/Include/features/AIB";

        // Output directory for step definitions
        String outputDirectory = "/Users/ghazalashahin/Documents/AIBLatest/Demo/Include/scripts/groovy/";

        try {
            // Get all .feature files in the input directory
            DirectoryStream<Path> featureFiles = Files.newDirectoryStream(Paths.get(inputDirectory), "Login.feature");

            for (Path featureFile : featureFiles) {
                // Extract feature file name without extension
                String featureFileName = featureFile.getFileName().toString();
                String featureName = featureFileName.replace(".feature", "");

                // Read feature file content
                String featureContent = Files.readString(featureFile);

                // Generate step definition content
                String stepDefContent = generateStepDefinition(featureContent, featureName);

                // Ensure the output directory exists
                Files.createDirectories(Paths.get(outputDirectory));

                // Save step definition file
                String outputPath = outputDirectory + featureName + ".groovy";
                Files.writeString(Paths.get(outputPath), stepDefContent);

                System.out.println("Step definition created for: " + featureFileName);
                System.out.println("Step definition saved at: " + outputPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateStepDefinition(String featureContent, String className) {
        StringBuilder groovyContent = new StringBuilder();

        // Add package and imports
        groovyContent.append("""
                package operations
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

        // Add class declaration
        groovyContent.append("class ").append(className).append(" {\n");

        // Extract steps using regex
        Pattern stepPattern = Pattern.compile("(Given|When|Then|And) (.+)");
        Matcher matcher = stepPattern.matcher(featureContent);

        while (matcher.find()) {
            String keyword = matcher.group(1); // Given, When, Then, And
            String step = matcher.group(2);   // Step description

            // Generate method name by replacing spaces with underscores and removing special characters
            String methodName = step.replaceAll("\\s+", "_").replaceAll("[^a-zA-Z0-9_]", "");

            groovyContent.append("\n\t@").append(keyword).append("(\"").append(step).append("\")\n")
            .append("\tdef ").append(methodName).append("() {\n")
            .append("\t\tWebUI.callTestCase(findTestCase(\"Test Cases/").append(methodName).append("\"), [:], FailureHandling.STOP_ON_FAILURE)\n")
            .append("\t}\n");

}

        // Close the class
        groovyContent.append("}\n");

        return groovyContent.toString();
    }
}
