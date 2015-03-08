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
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class AppiumTest extends AppiumMethodsWrapper {

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

    static {
        // Disable annoying cookie warnings.
        // WARNING: Invalid cookie header
        LogFactory.getFactory().setAttribute(APACHE_LOGGING_LOG_KEY, APACHE_LOGGING_LOG_VALUE);
    }

    /** Page object references. Allows using 'home' instead of 'HomePage' **/
    protected HomePage home;

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
        driver = new AndroidDriver(serverAddress, capabilities);

        driver.manage().timeouts().implicitlyWait(defaultWaitingTime, defaultWaitingTimeUnit);
        init(driver, serverAddress);
    }

    /** Run after each test **/
    @After
    public void tearDown() throws Exception {
        if (driver != null) driver.quit();
    }

}