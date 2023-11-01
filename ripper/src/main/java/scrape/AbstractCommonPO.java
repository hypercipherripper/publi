package scrape;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractCommonPO 
{
    
    protected WebDriver driver;
    protected JavascriptExecutor js;

    public AbstractCommonPO(WebDriver driver)
    {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
    }

    protected void waitForElementToBeVisible(WebElement element)
    {
        new WebDriverWait(this.driver, Duration.ofSeconds(30)).
            until(ExpectedConditions.visibilityOf(element));
    }

    protected void waitForElementsToBeVisible(List<WebElement> elements)
    {
        new WebDriverWait(this.driver, Duration.ofSeconds(30)).
            until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    protected void waitForElementToBeVisibleLocation(String cssString)
    {
        new WebDriverWait(this.driver, Duration.ofSeconds(30)).
            until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(cssString)));
    }

    protected boolean waitForElementToBeClickable(WebElement element)
    {
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(30));
        
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (TimeoutException e) {
            System.out.println("ERROR: Clickable element not found!");
            e.printStackTrace();
        }

        return false;
    }

    protected void waitForElementToBeClickableLocation(String cssString)
    {
        new WebDriverWait(this.driver, Duration.ofSeconds(30)).
            until(ExpectedConditions.elementToBeClickable(By.cssSelector(cssString)));
    }

    protected void scrollToElement(WebElement element)
    {
        //js.executeScript("arguments[0].scrollIntoView(true)", element);
        Actions actionObj = new Actions(this.driver);
        waitForElementToBeVisible(element);
        actionObj.scrollToElement(element).perform();

    }

    protected void hoverOverElement(WebElement element)
    {
        Actions actionObj = new Actions(this.driver);
        waitForElementToBeVisible(element);
        scrollToElement(element);
        actionObj.moveToElement(element);
    }

    protected String retrieveTextFromElement(WebElement element)
    {
        waitForElementToBeVisible(element);
        return element.getText();
    }

    protected List<String> retrieveTextsFromElements(List<WebElement> elements)
    {
        waitForElementsToBeVisible(elements);
        List<String> texts = new ArrayList<String>();
        elements.forEach(e -> texts.add(e.getText()));
        return texts;
    }

    protected String retrieveAttributeValueFromElement(WebElement element, String attribute)
    {
        waitForElementToBeVisible(element);
        return element.getAttribute(attribute);
    }

    protected List<String> retrieveAttributeValuesFromElements(List<WebElement> elements, String attribute)
    {
        waitForElementsToBeVisible(elements);
        List<String> attributeVals = new ArrayList<String>();
        elements.forEach(e -> attributeVals.add(e.getAttribute(attribute)));
        return attributeVals;
    }

    protected String retrieveCssPropertyFromElement(WebElement element, String cssProperty)
    {
        waitForElementToBeVisible(element);
        return element.getCssValue(cssProperty);
    }

    protected List<String> retrieveCssPropertyFromElements(List<WebElement> elements, String cssProperty)
    {
        waitForElementsToBeVisible(elements);
        List<String> cssPropertyVals = new ArrayList<String>();
        elements.forEach(e -> cssPropertyVals.add(e.getCssValue(cssProperty)));
        return cssPropertyVals;
    }

    protected boolean clickElement(WebElement element)
    {
        boolean isClicked = waitForElementToBeClickable(element);
        
        if (!isClicked) { return false; }

        scrollToElement(element);
        element.click();

        return true;
    }


}
