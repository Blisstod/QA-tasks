package org.example;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class SecondAssignment {
    private static final String CHROME_DRIVER_PATH = "C:\\Users\\onete\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe";
    private static WebDriver driver;
    private static WebDriverWait wait;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://acmp.ru/");
        wait = new WebDriverWait(driver, 3);
    }

    @Test
    public void testLoginLogoutFunctionality() {
        loginIfNeeded();

        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='exit']")));
        assertTrue("Login failed or logout button not visible.", logoutButton.isDisplayed());

        logoutButton.click();

        WebElement loginAfterLogout = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='lgn']")));
        WebElement passwordAfterLogout = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='password']")));
        assertTrue("Logout failed or login fields are not visible.",
                loginAfterLogout.isDisplayed() && passwordAfterLogout.isDisplayed());
    }

    @Test
    public void testSearchFunctionality() {
        loginIfNeeded();

        driver.get("https://acmp.ru/index.asp?main=tasks");

        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='str']")));
        setInputValue(searchInput, "Дороги");

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Ok']")));
        searchButton.click();
    }

    @Test
    public void testTaskFunctionality() {
        loginIfNeeded();

        driver.get("https://acmp.ru/index.asp?main=tasks");

        WebElement complexityLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("th a[href*='ob=iq']")));
        complexityLink.click();

        WebElement pageThreeLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='3' and contains(@href, 'page=2')]")));
        pageThreeLink.click();

        WebElement taskLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Стрелки')]")));
        taskLink.click();

        WebElement codeEditor = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".CodeMirror")));
        setCodeMirrorValue(codeEditor, """
                #include<iostream>
                #include<string>
                using namespace std;
                main()
                {
                string s,t;
                int i,k=0;
                cin>>s;

                for(i=0;i<=int(s.size())-5;i++)
                {
                t=s.substr(i,5);
                if (t==">>-->"||t=="<--<<")
                k++;
                }
                cout<<k;
                }
                """);

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Отправить']")));
        submitButton.click();

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
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
