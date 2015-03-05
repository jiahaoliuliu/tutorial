package appium.tutorial.android;

import appium.tutorial.android.util.AppiumTest;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AutomatingASimpleActionTest extends AppiumTest {

//    @Test
//    public void one() throws Exception {
//    	// Find a text view with the name "Accessibility" and then click
//    	findElementContainsText("Accessibility").click();
//    	findElementMatchesText("Accessibility Node Provider");
//    }
//
//    @Test
//    public void two() throws Exception {
//    	waitForKey(setKeyByContainsText("Accessibility")).click();
//    	waitForKey(setKeyByMatchesText("Accessibility Node Provider"));
//    }
//
//    @Test
//    public void three() throws Exception {
//    	waitForKey(setKeyByIndex(2)).click();
//        find("Custom Evaluator");
//    }
//
    @Test
    public void four() throws Exception {
    	setWaitingTimeInSeconds(0);

        List<String> cell_names = new ArrayList<String>();

        for (WebElement cell : findElementsByTags("android.widget.TextView")) {
            cell_names.add(cell.getAttribute("name"));
        }

        // delete title cell
        cell_names.remove(0);

        for (String cell_name : cell_names) {
            scroll_to_exact(cell_name).click();
            waitInvisible(setKeyByMatchesText(cell_name));
            back();
            waitForKey(for_find("Accessibility"));
            waitForKey(for_find("Animation"));
        }

        setWaitingTimeInSeconds(30); // restore old implicit wait
    }
}