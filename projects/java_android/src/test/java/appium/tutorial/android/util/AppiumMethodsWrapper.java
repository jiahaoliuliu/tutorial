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

    private static AndroidDriver driver;
    private static URL serverAddress;
    private static WebDriverWait driverWait;

    /**
     * Initialize the WebDriver. Must be called before using any helper methods.
     * @param androidDriver
     *     The driver created based on the set of parameters such as the server address and
     *     the list of capabilities
     * @param driverServerAddress
     */
    public void init(AndroidDriver androidDriver) {
        if (androidDriver != null) {
            throw new IllegalArgumentException("The android Driver cannot be null");
        }

        driver = androidDriver;
        serverAddress = driver.getRemoteAddress(); 
        int timeoutInSeconds = 60;
        // must wait at most 60 seconds for running on Sauce.
        // waiting for 30 seconds works locally however it fails on Sauce.
        driverWait = new WebDriverWait(androidDriver, timeoutInSeconds);
    }

    /**
     * Set implicit wait in seconds.
     * @param seconds The number of seconds to wait.
     */
    public void setWaitingTimeInSeconds(int seconds) {
        driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
    }

    /**
     * Return a list of elements by locator.
     * @param key The key used to find the element
     * @return The list of elements that matches or an empty list if nothing matches
     *         See {@link driver.findElements()}
     */
//    public List<WebElement> findElementsByKey(By key) {
//        return driver.findElements(key);
//    }

    /**
     * Press the back button *
     */
    public void back() {
        driver.navigate().back();
    }

    /**
     * Return a list of elements by tag name *
     */
    public List<WebElement> findElementsByClassName(String className) {
        return driver.findElements(By.className(className));
    }

    /**
     * Return a static text element by xpath index *
     */
    public WebElement findElementByIndex(int xpathIndex) {
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
     * @param text
     *     The text that the element should contain.
     */
    public By setKeyByContainsText(String text) {
        return By.xpath("//android.widget.TextView[contains(@text, '" + text + "')]");
    }

   /**
    * Return a static text element by exact text.
    * @param text
    *     The text that the element must match
    * @return
    *     The first element that matches with the criteria
    */
    public WebElement findElementMatchesText(String text) {
        return driver.findElement(setKeyByMatchesText(text));
    }

    /**
     * Return a static text locator by exact text.
     * @param text
     *     The text that the element must match
     * @return
     *     They key (locator) which looks for element(s) that matches with the text.
     */
    public By setKeyByMatchesText(String text) {
        return By.xpath("//android.widget.TextView[@text='" + text + "']");
    }

    /**
     * Set the key to look for elements that contains the text as any kind of resources.
     * @param text
     *     The text that is contained as resource of any kind of resources.
     * @return
     *     The key (locator) which looks for element(s) that contains the text as resource.
     */
    public By setKeyByResources(String text) {
        String xPathExpression = 
                "//*[@content-desc=\"" + text + "\" or "
                  + "@resource-id=\"" + text + "\" or "
                  + "@text=\"" + text + "\"] |"
              + "//*[contains(translate(@content-desc,\"" + text + "\",\"" + text + "\"), \"" + text + "\") or "
                  + "contains(translate(@text,\"" + text + "\",\"" + text + "\"), \"" + text + "\") or "
                  + "@resource-id=\"" + text + "\"]";
        return By.xpath(xPathExpression);
    }

    /**
     * Find element that contains the text as any kind of resources
     * @param text
     *     The text to look for as any kind of resources.
     * @return
     *     The first element that matches with the criteria.
     */
    public WebElement findElementByResources(String text) {
        return driver.findElement(setKeyByResources(text));
    }

    /**
     * Wait N seconds for locator to find an element to be visible and with height and weight bigger than zero.
     * @param locator
     *     The key (locator) used.
     * @return
     *     The element found after N seconds.
     */
    public WebElement waitForKey(By locator) {
        return driverWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait N seconds for locator to find all elements visible and with height and weight bigger than zero.
     * @param locator
     *     The key (locator) used
     * @return
     *     The list of elements found after N seconds.
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
    public WebElement scrollTo(String value) {
        return driver.scrollTo(value);
    }

    /**
     * Return an element that exactly matches name or text *
     */
    public WebElement scrollToExact(String value) {
        return driver.scrollToExact(value);
  }
}