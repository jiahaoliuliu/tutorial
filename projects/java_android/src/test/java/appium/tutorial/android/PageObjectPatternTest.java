package appium.tutorial.android;

import appium.tutorial.android.util.AppiumTest;

public class PageObjectPatternTest extends AppiumTest {

    @org.junit.Test
    public void pageObject() throws Exception {
        home.accessibilityClick();
        back();

        home.animationClick();
        back();
    }
}