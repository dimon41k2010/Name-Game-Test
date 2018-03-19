package com.willowtreeapps;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created on 5/23/17.
 */
public class HomePage extends BasePage {


    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void validateTitleIsPresent() {
        open("http://www.ericrochester.com/name-game/");
        WebElement title = driver.findElement(By.cssSelector("h1"));
        Assert.assertTrue(title != null);
    }

    public void validateClickingFirstPhotoIncreasesTriesCounter() {
        open("http://www.ericrochester.com/name-game/");
        int count = Integer.parseInt(driver.findElement(By.className("attempts")).getText());
        WebElement element = driver.findElement(By.className("photo"));
        clickElement(element);
        int countAfter = Integer.parseInt(driver.findElement(By.className("attempts")).getText());
        Assert.assertTrue(countAfter > count);
    }

    public void validateClickingCorrectPhotoIncreasesStreakCounter() {

        open("http://www.ericrochester.com/name-game/");
        int count = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        new BasePage(driver).clickOnCorrectElement(By.id("name"), By.className("name"), By.className("photo"));
        int countAfter = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        Assert.assertTrue(countAfter > count);
        sleep(5000);
        new BasePage(driver).clickOnCorrectElement(By.id("name"), By.className("name"), By.className("photo"));
        int countAfterAfter = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        Assert.assertTrue(countAfterAfter > countAfter);
    }

    public void validateClickingWrongPhotoResetsMultipleStreakCounter() {

        open("http://www.ericrochester.com/name-game/");
        new BasePage(driver).repeatCorrectSelection(By.id("name"), By.className("name"), By.className("photo"), 3);
        int count = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        new BasePage(driver).clickOnWrongElement(By.id("name"), By.className("name"), By.className("photo"));
        int countAfter = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        Assert.assertTrue(count > countAfter);
        Assert.assertEquals(0, countAfter);
    }

    public void validateClickingCorrectPhotoChangesNameAndPhotos() {

        open("http://www.ericrochester.com/name-game/");
        String name = getText(By.id("name"));
        List<WebElement> list = driver.findElements(By.className("photo"));
        new BasePage(driver).clickOnCorrectElement(By.id("name"), By.className("name"), By.className("photo"));
        sleep(6000);
        String nameAfter = getText(By.id("name"));
        List<WebElement> listAfter = driver.findElements(By.className("photo"));
        Assert.assertNotEquals(name, nameAfter);
        Assert.assertNotEquals(list, listAfter);
    }

    public void validateRandomSelectionsIncrementCounters() {
        open("http://www.ericrochester.com/name-game/");
        new BasePage(driver)
                .clickRandomSelectionWithAssertions(By.className("photo"), By.className("attempts")
                        , By.className("correct"));

    }

}


