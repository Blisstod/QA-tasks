package org.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class ThirdAssignment {
    private static final String CHROME_DRIVER_PATH = "C:\\Users\\onete\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe";
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://acmp.ru/");
        wait = new WebDriverWait(driver, 2);
    }

    @Test
    public void testSelectDropdownWithFluentWait() {
        loginIfNeeded();

        driver.get("https://acmp.ru/index.asp?main=tasks");

        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10)) // Total wait time
                .pollingEvery(Duration.ofSeconds(1)) // Polling interval
                .ignoring(NoSuchElementException.class);

        WebElement dropdown = fluentWait.until(driver -> driver.findElement(By.name("id_type")));

        Select select = new Select(dropdown);
        select.selectByVisibleText("Геометрия");

        WebElement okButton = fluentWait.until(driver -> driver.findElement(By.cssSelector("input[value='Ok']")));
        okButton.click();

        WebElement firstResultTheme = fluentWait.until(driver -> driver.findElement(By.xpath("//tr[@class='white'][1]/td[contains(text(),'Геометрия')]")));

        assertTrue("The first result theme does not match the selected dropdown value.", firstResultTheme.getText().equals("Геометрия"));
    }


    @Test
    public void testHoverOnFirstTask() throws InterruptedException {
        loginIfNeeded();

        driver.get("https://acmp.ru/index.asp?main=tasks");

        WebElement firstTaskName = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@class='white'][1]/td[3]/a")));

        Actions actions = new Actions(driver);

        actions.moveToElement(firstTaskName).perform();

        Thread.sleep(3000);

        assertTrue("Task name is not displayed after hover.", firstTaskName.isDisplayed());
    }


    @AfterClass
    public static void tearDownClass() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void loginIfNeeded() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='exit']")));
        } catch (Exception e) {
            WebElement loginField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='lgn']")));
            setInputValue(loginField, "nurik21352");

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='password']")));
            setInputValue(passwordField, "Ilovekopatel2135");

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Ok']")));
            loginButton.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='exit']")));
        }
    }

    private void setInputValue(WebElement element, String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='" + value + "';", element);
    }

    private void setCodeMirrorValue(WebElement codeMirror, String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].CodeMirror.setValue(arguments[1]);", codeMirror, value);
    }
}
