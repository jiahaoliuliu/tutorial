package appium.tutorial.android.util;

import appium.tutorial.android.page.HomePage;
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

    /** Page object references. Allows using 'home' instead of 'HomePage' **/
    protected HomePage home;

    private AndroidDriver mDriver;
    private WebDriverWait driverWait;

    @Rule
    public TestRule printTests = new TestWatcher() {
        protected void starting(Description description) {
            System.out.print("  Appium test: " + description.getMethodName());
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
        mDriver = new AndroidDriver(serverAddress, capabilities);
        mDriver.manage().timeouts().implicitlyWait(defaultWaitingTime, defaultWaitingTimeUnit);

        // Create special driver for waiting
        driverWait = new WebDriverWait(mDriver, maximumWaitingTime);
    }

    /** Run after each test **/
    @After
    public void tearDown() throws Exception {
        if (mDriver != null) {
        	mDriver.quit();
        }
    }

    /**
     * Set implicit wait in seconds.
     * @param seconds The number of seconds to wait.
     */
    public void setWaitingTimeInSeconds(int seconds) {
    	mDriver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
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
    	mDriver.navigate().back();
    }

    /**
     * Return a list of elements by tag name *
     */
    public List<WebElement> findElementsByClassName(String className) {
        return mDriver.findElements(By.className(className));
    }

    /**
     * Return a static text element by xpath index *
     */
    public WebElement findElementByIndex(int xpathIndex) {
        return mDriver.findElement(setKeyByIndex(xpathIndex));
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
        return mDriver.findElement(setKeyByContainsText(text));
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
        return mDriver.findElement(setKeyByMatchesText(text));
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
        return mDriver.findElement(setKeyByResources(text));
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
     *     The element which has already scroller to such value
     */
    public WebElement scrollTo(String value) {
        return mDriver.scrollTo(value);
    }

    /**
     * Return an element that exactly matches name or text
     * @param
     * 
     */
    public WebElement scrollToExact(String value) {
        return mDriver.scrollToExact(value);
  }
}