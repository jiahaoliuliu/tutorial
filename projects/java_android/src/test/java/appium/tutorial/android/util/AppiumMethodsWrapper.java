package appium.tutorial.android.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Helper class which try to make the text much more cleaner.
 * Originally from the tutorial of Appium
 * http://appium.io/slate/en/tutorial/android.html?java#corrupt-ruby-gems
 * Modified by Jiahao Liu Liu
 */
public abstract class AppiumMethodsWrapper {

    protected static AndroidDriver driver;
    protected static URL serverAddress;
    private static WebDriverWait driverWait;

    /**
     * Initialize the webdriver. Must be called before using any helper methods. *
     */
    public void init(AndroidDriver webDriver, URL driverServerAddress) {
      driver = webDriver;
      serverAddress = driverServerAddress; 
      int timeoutInSeconds = 60;
      // must wait at least 60 seconds for running on Sauce.
      // waiting for 30 seconds works locally however it fails on Sauce.
      driverWait = new WebDriverWait(webDriver, timeoutInSeconds);
    }

    /**
     * Set implicit wait in seconds *
     */
    public void setWaitingTimeInSeconds(int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

   /**
    * Return a list of elements by locator *
    */
    public List<WebElement> findElementsByKey(By locator) {
        return driver.findElements(locator);
    }

    /**
     * Press the back button *
     */
    public void back() {
        driver.navigate().back();
    }

    /**
     * Return a list of elements by tag name *
     */
    public List<WebElement> findElementsByTags(String tagName) {
        return findElementsByKey(setKeyByTagName(tagName));
    }

    /**
     * Return a tag name locator *
     */
    public By setKeyByTagName(String tagName) {
        return By.className(tagName);
    }

    /**
     * Return a static text element by xpath index *
     */
    public WebElement s_text(int xpathIndex) {
        return driver.findElement(setKeyByIndex(xpathIndex));
    }

    /**
     * Return a static text locator by xpath index *
     */
    public By setKeyByIndex(int xpathIndex) {
        return By.xpath("//android.widget.TextView[" + xpathIndex + "]");
    }

    /**
     * Return a static text element that contains text
     * @param text
     *     The text contained in the element
     */
    public WebElement findElementContainsText(String text) {
        return driver.findElement(setKeyByContainsText(text));
    }

    /**
     * Return a static text locator that contains text.
     * 
     */
    public By setKeyByContainsText(String text) {
        return By.xpath("//android.widget.TextView[contains(@text, '" + text + "')]");
    }

   /**
    * Return a static text element by exact text *
    */
    public WebElement findElementMatchesText(String text) {
        return driver.findElement(setKeyByMatchesText(text));
    }

    /**
     * Return a static text locator by exact text *
     */
    public By setKeyByMatchesText(String text) {
        return By.xpath("//android.widget.TextView[@text='" + text + "']");
    }

    public By for_find(String value) {
        String xPathExpression = 
                "//*[@content-desc=\"" + value + "\" or "
                  + "@resource-id=\"" + value + "\" or "
                  + "@text=\"" + value + "\"] |"
              + "//*[contains(translate(@content-desc,\"" + value + "\",\"" + value + "\"), \"" + value + "\") or "
                  + "contains(translate(@text,\"" + value + "\",\"" + value + "\"), \"" + value + "\") or "
                  + "@resource-id=\"" + value + "\"]";
        System.out.println("xPathExpression " + xPathExpression);
        return By.xpath(xPathExpression);
    }

    public WebElement find(String value) {
        return driver.findElement(for_find(value));
    }

    /**
     * Wait 30 seconds for locator to find an element *
     */
    public WebElement waitForKey(By locator) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait 60 seconds for locator to find all elements *
     */
    public List<WebElement> waitAll(By locator) {
        return driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Wait 60 seconds for locator to not find a visible element *
     */
    public boolean waitInvisible(By locator) {
        return driverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Return an element that contains name or text *
     */
    public WebElement scroll_to(String value) {
        return driver.scrollTo(value);
    }

    /**
     * Return an element that exactly matches name or text *
     */
    public WebElement scroll_to_exact(String value) {
        return driver.scrollToExact(value);
  }
}