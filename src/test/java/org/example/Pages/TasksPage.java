package org.example.Pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class TasksPage {
    private static final Logger logger = Logger.getLogger(TasksPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = "input[name='str']")
    private WebElement searchField;

    @FindBy(xpath = "//input[@value='Ok']")
    private WebElement searchButton;

    @FindBy(css = "th a[href*='ob=iq']")
    private WebElement complexityLink;

    @FindBy(xpath = "//a[text()='3' and contains(@href, 'page=2')]")
    private WebElement pageThreeLink;

    @FindBy(xpath = "//a[contains(text(), 'Стрелки')]")
    private WebElement taskLink;

    @FindBy(css = ".CodeMirror")
    private WebElement codeEditor;

    @FindBy(css = "input[value='Отправить']")
    private WebElement submitButton;

    public TasksPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void searchTask(String query) {
        logger.info("Searching for task: " + query);
        searchField.sendKeys(query);
        searchButton.click();
        logger.info("Search completed.");
    }

    public void navigateToTaskPage() {
        logger.info("Navigating to task page.");
        complexityLink.click();
        wait.until(elementToBeClickable(pageThreeLink)).click();
        wait.until(elementToBeClickable(taskLink)).click();
        logger.info("Task page navigation complete.");
    }

    public void submitTaskCode(String code) {
        logger.info("Submitting task code.");
        ((JavascriptExecutor) driver).executeScript("arguments[0].CodeMirror.setValue(arguments[1]);", codeEditor, code);
        submitButton.click();
        logger.info("Task code submitted successfully.");
    }
}
