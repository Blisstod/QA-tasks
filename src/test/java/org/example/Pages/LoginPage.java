package org.example.Pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class LoginPage {
    private static final Logger logger = Logger.getLogger(LoginPage.class);
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = "input[name='lgn']")
    private WebElement usernameField;

    @FindBy(css = "input[name='password']")
    private WebElement passwordField;

    @FindBy(css = "input[value='Ok']")
    private WebElement loginButton;

    @FindBy(css = "a[href='/?main=exit']")
    private WebElement logoutButton;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        logger.info("Attempting login...");
        usernameField.clear();
        usernameField.sendKeys(username);

        passwordField.clear();
        passwordField.sendKeys(password);

        loginButton.click();

        wait.until(visibilityOf(logoutButton));
        logger.info("Login successful.");
    }


    public boolean isLoggedIn() {
        try {
            wait.until(visibilityOf(logoutButton));
            logger.info("Login status: true");
            return logoutButton.isDisplayed();
        } catch (Exception e) {
            logger.warn("Login status: false");
            return false;
        }
    }

    public void logout() {
        if (isLoggedIn()) {
            logger.info("Attempting logout...");
            logoutButton.click();
            wait.until(visibilityOf(usernameField));
            logger.info("Logout successful.");
        } else {
            logger.warn("Cannot logout, user is not logged in.");
        }
    }
}
