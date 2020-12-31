package ParallelSuite;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.annotations.Test;
import testcases.BrowserStackRunner;
import testcases.EmailStatus;

import java.util.concurrent.TimeUnit;

public class Careers extends BrowserStackRunner{

    @Test
    public void test() throws Exception{
        EmailStatus emailStatus = new EmailStatus(BrowserStackRunner.username, BrowserStackRunner.accessKey);
        SessionId sessionId = ((RemoteWebDriver) driver).getSessionId();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        JavascriptExecutor jse = (JavascriptExecutor)driver;

        /** Navigating to Australian Super's Careers page **/

        driver.get("https://www.australiansuper.com/");

        Thread.sleep(3000);
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(3000);

        driver.findElement(By.linkText("CAREERS")).click();
        System.out.println(driver.getTitle());


        String title = driver.getTitle();

        /** Marking test status on BrowserStack **/

        if(title.contains("Careers | AustralianSuper")) {
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"passed\", \"reason\": \"Expected Result\"}}");
        }
        else{
            jse.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \"failed\", \"reason\": \"Unexpected Result\"}}");
        }
//        emailStatus.triggerEmail(sessionId);
    }
}
