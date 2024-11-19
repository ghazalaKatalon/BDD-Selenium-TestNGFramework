package TestRunner;

@io.cucumber.junit.CucumberOptions(
    tags = "",
    features = {"src/test/resources/Features"},
    glue = {"StepDefinitions"},
    plugin = {"pretty", "html:target/htmlreport.html"}
)
public class CucumberTestRunner  {
}
