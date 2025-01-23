package org.example;

import org.apache.log4j.Logger;
import org.example.Pages.LoginPage;
import org.example.Pages.TasksPage;
import org.testng.annotations.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.testng.Assert.assertTrue;

public class FifthAssignment {
    private static final Logger logger = Logger.getLogger(FourthAssignment.class);
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static LoginPage loginPage;
    private static TasksPage tasksPage;

    @BeforeClass
    public static void setUpClass() {
        logger.info("Setting up WebDriver and initializing pages.");
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\onete\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 3);

        loginPage = new LoginPage(driver, wait);
        tasksPage = new TasksPage(driver, wait);
        logger.info("Setup complete.");
    }

    @BeforeMethod
    public void setUp() {
        logger.info("\n\n=====================================");
        logger.info("Setting up before test.");
        driver.get("https://acmp.ru/");

        if (loginPage.isLoggedIn()) {
            logger.info("Already logged in. Logging out to reset state.");
            loginPage.logout();
        }

        logger.info("Logging in.");
        loginPage.login("nurik21352", "Ilovekopatel2135");
    }

    @AfterMethod
    public void tearDown() {
        logger.info("Tearing down after test.");
        if (loginPage.isLoggedIn()) {
            loginPage.logout();
        }
    }

    @Test
    public void testLoginLogoutFunctionality() {
        logger.info("Testing login and logout functionality.");
        assertTrue(loginPage.isLoggedIn(), "Login failed.");
        loginPage.logout();
        assertTrue(!loginPage.isLoggedIn(), "Logout failed.");
    }

    @Test
    public void testSearchFunctionality() {
        logger.info("Testing search functionality.");
        driver.get("https://acmp.ru/index.asp?main=tasks");
        tasksPage.searchTask("Дороги");
    }

    @Test
    public void testTaskFunctionality() {
        logger.info("Testing task functionality.");
        driver.get("https://acmp.ru/index.asp?main=tasks");
        tasksPage.navigateToTaskPage();
        tasksPage.submitTaskCode("""
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
    }

    @AfterClass
    public static void tearDownClass() {
        logger.info("Tearing down WebDriver.");
        if (driver != null) {
            driver.quit();
        }
    }
}