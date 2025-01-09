package org.example.FourthAssignment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(css = "input[name='lgn']")
    private WebElement usernameField;

    @FindBy(css = "input[name='password']")
    private WebElement passwordField;

    @FindBy(css = "input[value='Ok']")
    private WebElement loginButton;

    @FindBy(css = "a[href*='exit']")
    private WebElement logoutButton;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        System.out.println("Attempting login...");

        usernameField.clear();
        usernameField.sendKeys(username);

        passwordField.clear();
        passwordField.sendKeys(password);

        loginButton.click();

        wait.until(visibilityOf(logoutButton));
        System.out.println("Login successful.");
    }

    public boolean isLoggedIn() {
        try {
            return logoutButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void logout() {
        System.out.println("Attempting logout...");
        logoutButton.click();
        wait.until(visibilityOf(usernameField));
        System.out.println("Logout successful.");
    }
}
