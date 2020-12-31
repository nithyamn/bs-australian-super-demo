package ParallelSuite;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;
import testcases.BrowserStackRunner;
import testcases.EmailStatus;

import java.util.concurrent.TimeUnit;

public class InsuranceTest extends BrowserStackRunner {
    @Test
    public void test() throws Exception{
        EmailStatus emailStatus = new EmailStatus(BrowserStackRunner.username, BrowserStackRunner.accessKey);
        SessionId sessionId = ((RemoteWebDriver) driver).getSessionId();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        /* Navigating to Australian Super's website */

        driver.get("https://www.australiansuper.com/");
        driver.findElement(By.linkText("Insurance")).click();
        System.out.println(driver.getTitle());

        Thread.sleep(3000);

        JavascriptExecutor jse = (JavascriptExecutor)driver;
        String title = driver.getTitle();
        if(title.contains("Super Insurance | Insurance Through Super | AustralianSuper")) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
        }
        else{
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
        }
//        emailStatus.triggerEmail(sessionId);
    }
}
