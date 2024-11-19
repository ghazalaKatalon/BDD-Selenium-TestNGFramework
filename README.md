# BDD-Selenium-TestNGFramework to Katalon Studio BDD Cucumber Test
# Conversion from Selenium BDD to Katalon Studio

This guide walks you through the process of converting your Selenium BDD tests (including feature files, page objects, and step definitions) into Katalon Studio test cases using a simple Java program.

Prerequisites
Before proceeding, make sure you have the following installed:

Katalon Studio (latest version)
Java JDK (version 8 or higher)
Selenium WebDriver
Cucumber (for BDD testing)
TestNG (for test execution)
Overview
We’ve created three dedicated Java classes that automate the process of converting your existing Selenium BDD project into Katalon Studio-compatible format. These classes will:

Convert feature files (written in Gherkin) into Katalon test cases.
Convert page object models (Selenium objects) into Katalon test objects.
Convert step definitions from Cucumber into Katalon step definitions.
You will only need to run these classes once, and they will handle the conversion for you.

Conversion Process
Step 1: Prepare Your Files
Ensure that the following files are present in your Selenium BDD project:

Feature Files (*.feature)
Located in the src/test/resources folder.
Page Objects (*.java files with @FindBy annotations)
Typically found in the src/main/java folder.
Step Definitions (*.java files with step definition methods like @Given, @When, @Then)
Found in the src/test/java folder.
Step 2: Running the Conversion Classes
There are three key classes to run for the conversion:

1. FeatureFileConverter
This class converts your Gherkin feature files into Katalon Studio-compatible test cases.

How to use:

Ensure the source feature file is located in your project (e.g., src/test/resources/login.feature).
Run the FeatureFileConverter class.
The converted test case will be saved in the Katalon project’s Test Cases folder.
2. ObjectsConverter
This class converts your Selenium Page Object models into Katalon test objects.

How to use:

Make sure the Selenium Page Object (e.g., LoginPage.java) is located in your project (e.g., src/main/java).
Run the ObjectsConverter class.
The converted test object will be placed into the Test Objects folder in Katalon Studio.
3. StepDefinitionsConverter
This class will convert your Cucumber step definitions into Katalon step definitions.

How to use:

Ensure your Cucumber step definitions (e.g., LoginSteps.java) are available in the project (e.g., src/test/java).
Run the StepDefinitionsConverter class.
The converted step definitions will be placed in the Keywords or Test Cases folder in Katalon Studio.
Step 3: Running the Classes
Once your files are in place, follow these simple steps to perform the conversion:

Compile the Java classes: Ensure your project is compiled and ready to run.
Run each conversion class:
Run the FeatureFileConverter class to convert your feature files.
Run the ObjectsConverter class to convert your page objects.
Run the StepDefinitionsConverter class to convert your step definitions.
Review the Output: After running each class, check the Katalon Studio folders (e.g., Test Cases, Test Objects, Keywords) for the converted files.
Step 4: Import Converted Files into Katalon Studio
Once the conversion classes have been run, you will need to:

Import the converted test cases, test objects, and step definitions into your Katalon Studio project. You can do this by:
Going to Katalon Studio.
Clicking File > Import and selecting the appropriate files to import into your project.
Step 5: Running Tests in Katalon Studio
Once the files are successfully imported:

Navigate to Test Cases in Katalon Studio.
Select the converted test case (e.g., LoginTestCase).
Click Run to execute the tests.
Example Workflow
Source Files:

login.feature (located in src/test/resources/).
LoginPage.java (located in src/main/java/).
LoginSteps.java (located in src/test/java/).
Run the following classes:

FeatureFileConverter — Converts login.feature into a Katalon test case.
ObjectsConverter — Converts LoginPage.java into a Katalon test object.
StepDefinitionsConverter — Converts LoginSteps.java into Katalon step definitions.
Output:

A new Katalon test case for the login feature.
A new Katalon test object for the login page.
Step definitions for the login steps.
Conclusion
By running the FeatureFileConverter, ObjectsConverter, and StepDefinitionsConverter classes, you can quickly migrate your existing Selenium BDD tests to Katalon Studio without having to manually re-create your test cases, page objects, and step definitions. This approach saves time and ensures seamless integration into Katalon Studio's test management environment.
