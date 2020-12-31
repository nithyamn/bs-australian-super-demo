package testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;
import testcases.BrowserStackRunner;

public class LocalTest extends BrowserStackRunner {
    @Test
    public void test() throws Exception {
        EmailStatus emailStatus = new EmailStatus(BrowserStackRunner.username, BrowserStackRunner.accessKey);
        SessionId sessionId = ((RemoteWebDriver) driver).getSessionId();

        /*** Local website ***/
        driver.get("http://bs-local.com:45691/check");

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        String content = driver.findElement(By.xpath("/html/body")).getText();

        /** Marking test status on BrowserStack **/
        if(content.contains("Up and running")) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
        }
        else{
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
        }

        /** Trigger email for test status **/
        emailStatus.triggerEmail(sessionId);
    }
}
