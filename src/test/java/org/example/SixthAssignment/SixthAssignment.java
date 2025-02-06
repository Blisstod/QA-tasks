package org.example.SixthAssignment;

import org.apache.poi.ss.usermodel.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class SixthAssignment {

    private static final String BROWSERSTACK_USERNAME = "bsuser_OZ7vg2";
    private static final String BROWSERSTACK_ACCESS_KEY = "mFXHCAyopqFZKJyCNfor";
    private static final String BROWSERSTACK_URL = "https://" + BROWSERSTACK_USERNAME + ":" + BROWSERSTACK_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    private static WebDriver driver;
    private static WebDriverWait wait;
    private static ExcelUtils excelUtils;
    private static final String EXCEL_PATH = "./src/test/java/org/example/SixthAssignment/TestData.xlsx";

    @BeforeClass
    public static void setUpClass() throws Exception {
        excelUtils = new ExcelUtils(EXCEL_PATH);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browserName", "Chrome");
        capabilities.setCapability("browserVersion", "latest");
        capabilities.setCapability("os", "Windows");
        capabilities.setCapability("osVersion", "10");
        capabilities.setCapability("name", "ACMP Tests"); // Имя теста
        capabilities.setCapability("browserstack.debug", "true");

        // Инициализируем RemoteWebDriver для BrowserStack
        driver = new RemoteWebDriver(new URL(BROWSERSTACK_URL), capabilities);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://acmp.ru/");
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void testSelectDropdownWithFluentWait() {
        loginIfNeeded();

        driver.get("https://acmp.ru/index.asp?main=tasks");

        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        WebElement dropdown = fluentWait.until(d -> d.findElement(By.name("id_type")));

        Select select = new Select(dropdown);
        String dropdownValue = excelUtils.getCellValue("TestData", 1, 0);
        select.selectByVisibleText(dropdownValue);

        WebElement okButton = fluentWait.until(d -> d.findElement(By.cssSelector("input[value='Ok']")));
        okButton.click();

        WebElement firstResultTheme = fluentWait.until(d -> d.findElement(
                By.xpath("//tr[@class='white'][1]/td[contains(text(),'" + dropdownValue + "')]")));

        assertTrue("Тема первого результата не соответствует выбранному значению.",
                firstResultTheme.getText().contains(dropdownValue));
    }

    @Test
    public void testHoverOnFirstTask() throws InterruptedException {
        loginIfNeeded();

        driver.get("https://acmp.ru/index.asp?main=tasks");

        WebElement firstTaskName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//tr[@class='white'][1]/td[3]/a")));

        Actions actions = new Actions(driver);
        actions.moveToElement(firstTaskName).perform();

        Thread.sleep(3000);

        assertTrue("Название задачи не отображается после наведения.", firstTaskName.isDisplayed());
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        if (driver != null) {
            driver.quit();
        }
        if (excelUtils != null) {
            excelUtils.close();
        }
    }

    private void loginIfNeeded() {
        try {
            // Если элемент выхода (logout) найден, значит пользователь уже вошёл
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='exit']")));
        } catch (Exception e) {
            // Читаем данные для входа из Excel
            String username = excelUtils.getCellValue("LoginData", 1, 0);
            String password = excelUtils.getCellValue("LoginData", 1, 1);

            WebElement loginField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='lgn']")));
            setInputValue(loginField, username);

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[name='password']")));
            setInputValue(passwordField, password);

            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[value='Ok']")));
            loginButton.click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[href*='exit']")));
        }
    }

    private void setInputValue(WebElement element, String value) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='" + value + "';", element);
    }

    public static class ExcelUtils {
        private Workbook workbook;

        public ExcelUtils(String excelPath) throws IOException {
            FileInputStream fis = new FileInputStream(new File(excelPath));
            workbook = WorkbookFactory.create(fis);
            fis.close();
        }

        public String getCellValue(String sheetName, int rowIndex, int colIndex) {
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(rowIndex);
            Cell cell = row.getCell(colIndex);
            if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {
                return String.valueOf(cell.getNumericCellValue());
            } else if (cell.getCellType() == CellType.BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue());
            } else {
                return "";
            }
        }

        public void close() throws IOException {
            workbook.close();
        }
    }
}
