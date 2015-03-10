package appium.tutorial.android.util;

import io.appium.java_client.android.AndroidDriver;

import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class AppiumTest {

    /*
     * The follow list of values are the one set by default.
     * Note all the keys are "private static final". This is because the keys are immutable.
     * 
     * In the case of values, there are values which are "private static final". Those values
     * are immutable.
     * 
     * There are other values which are "protected". Those values could be modified by the classes
     * which inherited from this class. To do so, add the follow code to the class:
     * 
     *     @Override
     *     public void setUp() throws Exception {
     *         // Here is where you modify the value. e.g. platformVersion = "4.4";
     *         super.setUp();
     *     }
     *
     */
    private static final String APACHE_LOGGING_LOG_KEY = "org.apache.commons.logging.Log";
    private static final String APACHE_LOGGING_LOG_VALUE = "org.apache.commons.logging.impl.NoOpLog";

    // Desired capabilities
    //    Appium Version
    private static final String APPIUM_VERSION_KEY = "appium-version";
    protected String appiumVersionValue = "1.1.0";

    //    Platform name
    private static final String PLATFORM_NAME_KEY = "platformName";
    private static final String PLATFORM_NAME_VALUE = "Android";

    //    Device name
    private static final String DEVICE_NAME_KEY = "deviceName";
    private static final String DEVICE_NAME_VALUE = "Android";

    //    Platform Version
    private static final String PLATFORM_VERSION_KEY = "platformVersion";
    protected String platformVersionValue = "4.3";

    //    Name
    private static final String NAME_KEY = "name";
    protected static final String NAME_VALUE = "Appium tutorial";

    //    App
    private static final String APP_KEY = "app";
    private static final String LOCAL_APP_NAME = "api.apk";

    // Other properties
    private static final String SERVER_ADDRESS = "http://127.0.0.1:4723/wd/hub";

    protected int defaultWaitingTime = 30;
    protected TimeUnit defaultWaitingTimeUnit = TimeUnit.SECONDS;

    // The test must wait at most 60 seconds for running on Sauce.
    // waiting for 30 seconds works locally however it fails on Sauce.
    protected int maximumWaitingTime = 60;

    static {
        // Disable annoying cookie warnings.
        // WARNING: Invalid cookie header
        LogFactory.getFactory().setAttribute(APACHE_LOGGING_LOG_KEY, APACHE_LOGGING_LOG_VALUE);
    }

    // The follow elements are protected to allow the class which extends this class
    // to use other functions if needed
    protected AndroidDriver driver;
    protected WebDriverWait driverWait;

    @Rule
    public TestRule printTests = new TestWatcher() {
        protected void starting(Description description) {
            System.out.print("  Appium test: " + description.getMethodName() + "\n");
        }
        
        protected void finished(Description description) {
            System.out.println();
        }
    };

    /** Keep the same date prefix to identify job sets. **/
    private static Date date = new Date();

    /** Run before each test **/
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(APPIUM_VERSION_KEY, appiumVersionValue);
        capabilities.setCapability(PLATFORM_NAME_KEY, PLATFORM_NAME_VALUE);
        capabilities.setCapability(DEVICE_NAME_KEY, DEVICE_NAME_VALUE);
        capabilities.setCapability(PLATFORM_VERSION_KEY, platformVersionValue);

        // Set job name on Sauce Labs
        capabilities.setCapability(NAME_KEY, NAME_VALUE + " " + date);
        String userDir = System.getProperty("user.dir");

        URL serverAddress;
        String localApp = LOCAL_APP_NAME;
        String appPath = Paths.get(userDir, localApp).toAbsolutePath().toString();
        capabilities.setCapability(APP_KEY, appPath);
        serverAddress = new URL(SERVER_ADDRESS);
        driver = new AndroidDriver(serverAddress, capabilities);
        driver.manage().timeouts().implicitlyWait(defaultWaitingTime, defaultWaitingTimeUnit);

        // Create special driver for waiting
        driverWait = new WebDriverWait(driver, maximumWaitingTime);
    }

    /** Run after each test **/
    @After
    public void tearDown() throws Exception {
        if (driver != null) {
        	driver.quit();
        }
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
     * Press the back button. Note that if we are on the main page,
     * this might close the app.
     */
    public void back() {
    	driver.navigate().back();
    }

    /**
     * Return a list of elements by class name.
     * @param className The name of the class to look for.
     * @return The list of elements that contains that class
     */
    public List<WebElement> findElementsByClassName(String className) {
        return driver.findElements(By.className(className));
    }

    /**
     * Return a static text element by xpath index. Note since there is an error
     * clicking on the element which contains the xPath index 1, a specific method
     * is used in this case.
     * 
     * @param xPathIndex The idex of the element to be found
     * @return The first element matches to the criteria.
     */
    public WebElement findElementByIndex(int xPathIndex) {
        // Set a specific case when the xPathIndex is one
        if (xPathIndex == 1) {
            return findFirstElementOfList();
        }

        return driver.findElement(setKeyByIndex(xPathIndex));
    }

    /**
     * Since there is a bug interacting with the first element of the list, 
     * a specific method is used in this case.
     * It gets the name of the element and use the method scrollToExact,
     * which works better
     * 
     * @return The first WebElement of the list
     */
    public WebElement findFirstElementOfList() {
      List<String> cellNames = new ArrayList<String>();

      for (WebElement cell : findElementsByClassName("android.widget.TextView")) {
      	cellNames.add(cell.getAttribute("name"));
      }

      // delete title cell
      cellNames.remove(0);
      return scrollToExact(cellNames.get(0));
    }

    /**
     * Return a text key by matching the same by xpath index. Note that actually there is a bug
     * by interacting with the first element of the list (xPathIndex = 1), so an IllegalArgumentException
     * will be launched.
     * @param xPathIndex The idex of the text to be found.
     * @return The key for the corresponding xPath.
     * @throws IllegalArgumentException if the xPathIndex is 1.
     */
    public By setKeyByIndex(int xPathIndex) {
        if (xPathIndex == 1) {
            throw new IllegalArgumentException("There is a bug by interacting with the first element of the list. Please do not use"
            		+ "this method to find it. Instead, use the method findFirstElementOfList()");
        }
        return By.xpath("//android.widget.TextView[" + xPathIndex + "]");
    }

    /**
     * Return a static text element that contains text
     * @param text
     *     The text contained in the element
     * @return The first element which contains that text.
     */
    public WebElement findElementContainsText(String text) {
        return driver.findElement(setKeyByContainsText(text));
    }

    /**
     * Return a static text locator that contains text.
     * @param text
     *     The text that the element should contain
     * @return the key which corresponding criteria
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
     * Wait N seconds for locator to not find a visible element.
     * @param locator
     *     The key to find the elements
     * @return
     *     True if the element became invisible or not exist.
     */
    public boolean waitInvisible(By locator) {
        return driverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Return an element that contains name or text
     * @param
     *     The value to scroll to.
     * @return
     *     The element which has already scrolled to such value
     */
    public WebElement scrollTo(String value) {
        return driver.scrollTo(value);
    }

    /**
     * Return an element that exactly matches name or text
     * @param
     *     The value to scroll to
     * @return
     *     The element which has already scrolled to such value
     * 
     */
    public WebElement scrollToExact(String value) {
        return driver.scrollToExact(value);
  }
}