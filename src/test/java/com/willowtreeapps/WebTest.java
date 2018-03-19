package com.willowtreeapps;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebTest {

    private WebDriver driver;

    /**
     * Change the prop if you are on Windows or Linux to the corresponding file type
     * The chrome WebDrivers are included on the root of this project, to get the
     * latest versions go to https://sites.google.com/a/chromium.org/chromedriver/downloads
     */
    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.mac");
        Capabilities capabilities = DesiredCapabilities.chrome();
        driver = new ChromeDriver(capabilities);
        //driver.manage().window().maximize();

    }

    @After
    public void teardown() {
        driver.quit();
        System.clearProperty("webdriver.chrome.driver");
    }

    @Test
    public void test_validate_title_is_present() {
        new HomePage(driver)
                .validateTitleIsPresent();
    }

    @Test
    public void test_clicking_photo_increases_tries_counter() {
        new HomePage(driver)
                .validateClickingFirstPhotoIncreasesTriesCounter();
    }

    //_________________________________________________________________________________________//

    //Test #1 Verify the "streak" counter is incrementing on correct selections;
    @Test
    public void test_clicking_correct_photo_increases_streak_counter() {
        new HomePage(driver)
                .validateClickingCorrectPhotoIncreasesStreakCounter();
    }

    //Test #2 Verify the a multiple вЂњstreakвЂќ counter resets after getting an incorrect answer;
    @Test
    public void test_multiple_streak_counter_resets_after_incorrect_answer() {
        new HomePage(driver)
                .validateClickingWrongPhotoResetsMultipleStreakCounter();
    }

    //Test #3 Verify that after 10 random selections the correct counters are being incremented for tries and correct counters;
    @Test
    public void test_10_random_selections_increment_counters () {
        new HomePage(driver)
                .validateRandomSelectionsIncrementCounters();
    }


    //Test #4 Verify name and displayed photos change after selecting the correct answer;
    @Test
    public void test_name_and_photos_change_after_correct_selection() {
        new HomePage(driver)
                .validateClickingCorrectPhotoChangesNameAndPhotos();
    }

}
