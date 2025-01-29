package converter;

import java.io.*;
import java.nio.file.*;
import java.util.UUID;

public class KatalonSetup {

    public static void main(String[] args) {
        try {
            // Define paths
            String basePath = System.getProperty("inputDirectoryPath");
            String profilesPath = basePath + "/Profiles";
            String scriptsPath = basePath + "/Scripts/setup";
            String testCasesPath = basePath + "/Test Cases";

            // Create directories
            createDirectory(profilesPath);
            createDirectory(scriptsPath);
            createDirectory(testCasesPath);

            // Generate GlobalVariable XML
            String globalVariableXML = generateGlobalVariableXML();
            saveFile(profilesPath + "/default.glbl", globalVariableXML);

            // Generate Groovy Script
            String groovyScript = generateGroovyScript();
            String scriptFileName = "Script" + System.currentTimeMillis() + ".groovy";
            saveFile(scriptsPath + "/" + scriptFileName, groovyScript);

            // Generate Test Case XML
            String testCaseXML = generateTestCaseXML();
            saveFile(testCasesPath + "/setup.tc", testCaseXML);

            System.out.println("Katalon Studio setup completed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createDirectory(String path) throws IOException {
        Files.createDirectories(Paths.get(path));
        System.out.println("Directory created: " + path);
    }

    private static String generateGlobalVariableXML() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<GlobalVariableEntities>\n" +
               "   <description></description>\n" +
               "   <name>default</name>\n" +
               "   <tag></tag>\n" +
               "   <defaultProfile>true</defaultProfile>\n" +
               "   <GlobalVariableEntity>\n" +
               "      <description></description>\n" +
               "      <initValue>'AppUnderTest'</initValue>\n" +
               "      <name>url</name>\n" +
               "   </GlobalVariableEntity>\n" +
               "</GlobalVariableEntities>\n";
    }


    private static String generateGroovyScript() {
        return """
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
                import org.openqa.selenium.Keys as Keys

                WebUI.openBrowser('')

                WebUI.navigateToUrl(GlobalVariable.url)

                WebUI.waitForPageLoad(1000)
                WebUI.maximizeWindow()
                """;
        	 
    }

    private static String generateTestCaseXML() {     
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
            """, "setup", uuid);
    }


    private static void saveFile(String filePath, String content) throws IOException {
        Files.writeString(Paths.get(filePath), content);
        System.out.println("File saved: " + filePath);
    }


}
