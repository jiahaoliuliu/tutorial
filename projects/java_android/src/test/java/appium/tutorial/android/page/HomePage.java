package appium.tutorial.android.page;

import appium.tutorial.android.util.AppiumTest;

/** Page object for the home page **/
public abstract class HomePage extends AppiumTest{

    /** Verify the home page has loaded.
     *  Click the accessibility button.
     *  Verify the accessibility page has loaded. **/
    public void accessibilityClick() {
        loaded();
        find("Accessibility").click();
//        AccessibilityPage.loaded();
    }

    /** Verify the home page has loaded.
     *  Click the animation button.
     *  Verify the animation page has loaded. **/
    public static void animationClick() {
        loaded();
//        find("Animation").click();
//        AnimationPage.loaded();
    }

    /** Verify the home page has loaded **/
    public static void loaded() {
//        find("NFC");
    }
}