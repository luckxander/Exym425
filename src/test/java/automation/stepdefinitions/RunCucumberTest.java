package automation.stepdefinitions;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/automation/features",
        glue = "automation/stepdefinitions",
        strict = true,
        plugin = {"pretty"} 
        )

public class RunCucumberTest {

}
