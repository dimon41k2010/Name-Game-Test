package com.willowtreeapps;

import org.assertj.swing.assertions.Assertions;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;


import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Created on 5/23/17.
 */
public class BasePage {

    public WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public BasePage validateAttribute(String css, String attr, String regex) {
        return validateAttribute(By.cssSelector(css), attr, regex);
    }

    public BasePage validateAttribute(By by, String attr, String regex) {
        return validateAttribute(driver.findElement(by), attr, regex);
    }

    public BasePage validateAttribute(WebElement element, String attr, String regex) {
        String actual = null;
        try {
            actual = element.getAttribute(attr);
            if (actual.equals(regex)) {
                return this; // test passes
            }
        } catch (Exception e) {
            Assertions.fail(String.format("Attribute not fount! [Attribute: %s] [Desired value: %s] [Actual value: %s] [Element: %s] [Message: %s]",
                    attr,
                    regex,
                    actual,
                    element.toString(),
                    e.getMessage()), e);
        }

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(actual);

        Assertions.assertThat(m.find())
                .withFailMessage("Attribute doesn't match! [Attribute: %s] [Desired value: %s] [Actual value: %s] [Element: %s]",
                        attr,
                        regex,
                        actual,
                        element.toString())
                .isTrue();
        return this;
    }

    public BasePage validateText(String css, String text) {
        return validateText(By.cssSelector(css), text);
    }

    /**
     * Validate Text ignores white spaces
     */
    public BasePage validateText(By by, String text) {
        Assertions.assertThat(text).isEqualToIgnoringWhitespace(getText(by));
        return this;
    }

    public String getText(By by) {
        WebElement e = driver.findElement(by);
        return e.getTagName().equalsIgnoreCase("input")
                || e.getTagName().equalsIgnoreCase("select")
                || e.getTagName().equalsIgnoreCase("textarea")
                ? e.getAttribute("value")
                : e.getText();
    }

    public BasePage validatePresent(String css) {
        return validatePresent(By.cssSelector(css));
    }

    public BasePage validatePresent(By by) {
        Assertions.assertThat(driver.findElements(by).size())
                .withFailMessage("Element not present: [Element: %s]", by.toString())
                .isGreaterThan(0);
        return this;
    }

    public void sleep(int timeInMillis) {
        try {
            Thread.sleep(timeInMillis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //________________________________________________________________________________________________//

    //opens the main page and waits for the page to be loaded;
    public void open(String URL) {
        driver.get(URL);
        sleep(5000);
    }

    //fluent wait for element to be clickable;
    public void waitForElementToBeClickable(WebElement element) {
        new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(3, TimeUnit.SECONDS)
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    //clicking on the clickable element;
    public void clickElement(WebElement element) {
        waitForElementToBeClickable(element);
        element.click();
    }

    //method, that returns the index of correct element;
    public int indexOfCorrectElement(By requestedName, By listOfNames) {

        WebElement element = driver.findElement(requestedName);
        List<WebElement> list = driver.findElements(listOfNames);
        for (int i = 0; i < list.size(); i++) {
            if (element.getText().equals(list.get(i).getText())) {
                return list.indexOf(list.get(i));
            }
        }
        return 0;
    }

    //method, that returns the index of the wrong element;
    public int indexOfWrongElement(By requestedName, By listOfNames) {

        WebElement element = driver.findElement(requestedName);
        List<WebElement> list = driver.findElements(listOfNames);
        for (int i = 0; i < list.size(); i++) {
            if (!element.getText().equals(list.get(i).getText())) {
                return list.indexOf(list.get(i));
            }
        }
        return 0;
    }

    //clicking on correct element;
    public void clickOnCorrectElement(By requestedName, By listOfNames, By listOfPhotos) {

        List<WebElement> list = driver.findElements(listOfPhotos);
        int correctIndex = indexOfCorrectElement(requestedName, listOfNames);
        clickElement(list.get(correctIndex));

    }

    //clicking on wrong element;
    public void clickOnWrongElement(By requestedName, By listOfNames, By listOfPhotos) {

        List<WebElement> list = driver.findElements(listOfPhotos);
        int wrongIndex = indexOfWrongElement(requestedName, listOfNames);
        clickElement(list.get(wrongIndex));
    }

    //repeats correct selection repNum times;
    public void repeatCorrectSelection(By requestedName, By listOfNames, By listOfPhotos, int repNum) {
        for (int i = 0; i < repNum; i++) {
            clickOnCorrectElement(requestedName, listOfNames, listOfPhotos);
            sleep(6000);
        }
    }

    //validfation of planed and actual counter;
    public BasePage counterAssertion(int num, By by) {
        int counterPlan = num;
        int counterActual = Integer.parseInt(driver.findElement(by).getText());
        Assert.assertEquals(counterPlan, counterActual);
        return this;

    }

    //random 10 times selection;
    public void clickRandomSelectionWithAssertions(By listOfPhotos, By triesCounter, By correctCounter) {
        int attempts = 0;
        int correctPlan = 0;
        while (attempts < 10) {
            sleep(1000);
            List<WebElement> list = driver.findElements(listOfPhotos);
            Random random = new Random();
            int index = random.nextInt(5);
            clickElement(list.get(index));
            String class_name = list.get(index).getAttribute("class");
            attempts = Integer.parseInt(driver.findElement(triesCounter).getText());
            if (class_name.contains("photo correct")) {
                sleep(3000);
                correctPlan += 1;
            }
        }
        sleep(1000);
        counterAssertion(10, triesCounter);
        counterAssertion(correctPlan, correctCounter);
    }
}


