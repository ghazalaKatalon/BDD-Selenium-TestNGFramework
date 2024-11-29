package migration;
import java.io.*;
import java.nio.file.*;
import java.util.regex.*;

public class FeatureToStepDef {

    public static void main(String[] args) {
        // Input feature file path
        String inputPath = "/Users/ghazalashahin/Documents/AIBLatest/Demo/Include/features/Login.feature";

        // Output directory for step definition
        String outputDirectory = "/Users/ghazalashahin/Documents/AIBLatest/Demo/Include/scripts/groovy/common/";

        try {
            // Extract feature file name without extension
            String featureFileName = Paths.get(inputPath).getFileName().toString();
            String featureName = featureFileName.replace(".feature", "");

            // Read feature file content
            String featureContent = Files.readString(Paths.get(inputPath));

            // Generate step definition content
            String stepDefContent = generateStepDefinition(featureContent, featureName);

            // Ensure the output directory exists
            Files.createDirectories(Paths.get(outputDirectory));

            // Save step definition file
            String outputPath = outputDirectory + featureName + ".groovy";
            Files.writeString(Paths.get(outputPath), stepDefContent);

            System.out.println("Step definition created at: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateStepDefinition(String featureContent, String className) {
        StringBuilder groovyContent = new StringBuilder();

        // Add package and imports
        groovyContent.append("""
                package common
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

            // Add method for the step
            groovyContent.append("\n\t@").append(keyword).append("(\"").append(step).append("\")\n")
                         .append("\tdef ").append(methodName).append("() {\n")
                         .append("\t\t// TODO: Implement step logic for '").append(step).append("'\n")
                         .append("\t}\n");
        }

        // Close the class
        groovyContent.append("}\n");

        return groovyContent.toString();
    }
}
