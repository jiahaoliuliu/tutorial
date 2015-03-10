package appium.tutorial.android;

import appium.tutorial.android.util.AppiumTest;

import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AutomatingASimpleActionTest extends AppiumTest {

    @Test
    public void one() throws Exception {
        // Find a text view with the name "Accessibility" and then click
        findElementContainsText("Accessibility").click();
        // Check that the element exists
        findElementMatchesText("Accessibility Node Provider");
    }

    @Test
    public void two() throws Exception {
        waitForKey(setKeyByContainsText("Accessibility")).click();
        waitForKey(setKeyByMatchesText("Accessibility Node Provider"));
    }

    @Test
    public void three() throws Exception {
        // Wait to click on the second element of the list
        waitForKey(setKeyByIndex(2)).click();
        findElementByResources("Custom Evaluator");
    }

    @Test
    public void four() throws Exception {
    	setWaitingTimeInSeconds(0);

        List<String> cellNames = new ArrayList<String>();

        for (WebElement cell : findElementsByClassName("android.widget.TextView")) {
        	cellNames.add(cell.getAttribute("name"));
        }

        // delete title cell
        cellNames.remove(0);

        for (String cellName : cellNames) {
            WebElement webElement = scrollToExact(cellName);
            webElement.click();
            waitInvisible(setKeyByMatchesText(cellName));
            back();
            waitForKey(setKeyByResources("Accessibility"));
            waitForKey(setKeyByResources("Animation"));
        }

        setWaitingTimeInSeconds(30); // restore old implicit wait
    }
}