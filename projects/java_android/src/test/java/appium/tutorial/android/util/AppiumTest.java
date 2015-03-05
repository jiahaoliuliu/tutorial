package appium.tutorial.android.util;

import appium.tutorial.android.page.HomePage;
import com.saucelabs.common.SauceOnDemandAuthentication;
import com.saucelabs.common.SauceOnDemandSessionIdProvider;
import com.saucelabs.junit.SauceOnDemandTestWatcher;
import com.saucelabs.saucerest.SauceREST;
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

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static appium.tutorial.android.util.Helpers.driver;

public class AppiumTest implements SauceOnDemandSessionIdProvider {

    private static final String APACHE_LOGGING_LOG_KEY = "org.apache.commons.logging.Log";
    private static final String APACHE_LOGGING_LOG_VALUE = "org.apache.commons.logging.impl.NoOpLog";

    // Desired capabilities
    //    Appium Version
    private static final String APPIUM_VERSION_KEY = "appium-version";
    private static final String APPIUM_VERSION_VALUE = "1.1.0";

    //    Platform name
    private static final String PLATFORM_NAME_KEY = "platformName";
    private static final String PLATFORM_NAME_VALUE = "Android";

    //    Device name
    private static final String DEVICE_NAME_KEY = "deviceName";
    private static final String DEVICE_NAME_VALUE = "Android";

    //    Platform Version
    private static final String PLATFORM_VERSION_KEY = "platformVersion";
    private static final String PLATFORM_VERSION_VALUE = "4.3";

    //    Name
    private static final String NAME_KEY = "name";
    private static final String NAME_VALUE = "Appium tutorial";

    static {
        // Disable annoying cookie warnings.
        // WARNING: Invalid cookie header
        LogFactory.getFactory().setAttribute(APACHE_LOGGING_LOG_KEY, APACHE_LOGGING_LOG_VALUE);
    }

    /** Page object references. Allows using 'home' instead of 'HomePage' **/
    protected HomePage home;

    /** wait wraps Helpers.wait **/
    public static WebElement wait(By locator) {
        return Helpers.wait(locator);
    }

    private boolean runOnSauce = System.getProperty("sauce") != null;

    /** Authenticate to Sauce with environment variables SAUCE_USER_NAME and SAUCE_API_KEY **/
    private SauceOnDemandAuthentication auth = new SauceOnDemandAuthentication();

    /** Report pass/fail to Sauce Labs **/
    // false to silence Sauce connect messages.
    public @Rule
    SauceOnDemandTestWatcher reportToSauce = new SauceOnDemandTestWatcher(this, auth, false);

    @Rule
    public TestRule printTests = new TestWatcher() {
        protected void starting(Description description) {
            System.out.print("  test: " + description.getMethodName());
        }

        protected void finished(Description description) {
            final String session = getSessionId();

            if (session != null) {
                System.out.println(" " + "https://saucelabs.com/tests/" + session);
            } else {
                System.out.println();
            }
        }
    };

    private String sessionId;

    /** Keep the same date prefix to identify job sets. **/
    private static Date date = new Date();

    /** Run before each test **/
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(APPIUM_VERSION_KEY, APPIUM_VERSION_VALUE);
        capabilities.setCapability(PLATFORM_NAME_KEY, PLATFORM_NAME_VALUE);
        capabilities.setCapability(DEVICE_NAME_KEY, DEVICE_NAME_VALUE);
        capabilities.setCapability(PLATFORM_VERSION_KEY, PLATFORM_VERSION_VALUE);

        // Set job name on Sauce Labs
        capabilities.setCapability(NAME_KEY, NAME_VALUE + " " + date);
        String userDir = System.getProperty("user.dir");

        URL serverAddress;
        String localApp = "api.apk";
        if (runOnSauce) {
            String user = auth.getUsername();
            String key = auth.getAccessKey();

            // Upload app to Sauce Labs
            SauceREST rest = new SauceREST(user, key);

            rest.uploadFile(new File(userDir, localApp), localApp);

            capabilities.setCapability("app", "sauce-storage:" + localApp);
            serverAddress = new URL("http://" + user + ":" + key + "@ondemand.saucelabs.com:80/wd/hub");
            driver = new AndroidDriver(serverAddress, capabilities);
        } else {
            String appPath = Paths.get(userDir, localApp).toAbsolutePath().toString();
            capabilities.setCapability("app", appPath);
            serverAddress = new URL("http://127.0.0.1:4723/wd/hub");
            driver = new AndroidDriver(serverAddress, capabilities);
        }

        sessionId = driver.getSessionId().toString();

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        Helpers.init(driver, serverAddress);
    }

    /** Run after each test **/
    @After
    public void tearDown() throws Exception {
        if (driver != null) driver.quit();
    }

    /** If we're not on Sauce then return null otherwise SauceOnDemandTestWatcher will error. **/
    public String getSessionId() {
        return runOnSauce ? sessionId : null;
    }
}