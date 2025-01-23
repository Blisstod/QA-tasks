package org.example;

import org.example.Pages.LoginPage;
import org.example.Pages.TasksPage;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;

public class FourthAssignment {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static LoginPage loginPage;
    private static TasksPage tasksPage;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\onete\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 3);

        loginPage = new LoginPage(driver, wait);
        tasksPage = new TasksPage(driver, wait);
    }

    @Before
    public void setUp() {
        System.out.println("Setting up test...");
        driver.get("https://acmp.ru/");

        if (loginPage.isLoggedIn()) {
            System.out.println("Already logged in. Logging out to reset state.");
            loginPage.logout();
        }

        System.out.println("Logging in...");
        loginPage.login("nurik21352", "Ilovekopatel2135");
    }


    @After
    public void tearDown() {
        if (loginPage.isLoggedIn()) {
            loginPage.logout();
        }
    }

    @Test
    public void testLoginLogoutFunctionality() {
        assertTrue("Login failed.", loginPage.isLoggedIn());
        loginPage.logout();
        assertTrue("Logout failed.", !loginPage.isLoggedIn());
    }

    @Test
    public void testSearchFunctionality() {
        driver.get("https://acmp.ru/index.asp?main=tasks");
        tasksPage.searchTask("Дороги");
    }

    @Test
    public void testTaskFunctionality() {
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
        if (driver != null) {
            driver.quit();
        }
    }
}
