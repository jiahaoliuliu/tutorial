package appium.tutorial.android;

import appium.tutorial.android.util.AppiumTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AutomatingASimpleActionTest extends AppiumTest {

//    @Test
//    public void one() throws Exception {
//        // Find a text view with the name "Accessibility" and then click
//        findElementContainsText("Accessibility").click();
//        // Check that the element exists
//        findElementMatchesText("Accessibility Node Provider");
//    }

//    @Test
//    public void two() throws Exception {
//        waitForKey(setKeyByContainsText("Accessibility")).click();
//        waitForKey(setKeyByMatchesText("Accessibility Node Provider"));
//    }

    @Test
    public void three() throws Exception {
        // Wait to click on the second element of the list
        //waitForKey(setKeyByIndex(3)).click();
    	for (int i = 1; i <= 11; i++) {
    		WebElement webElement = findElementByIndex(i);
    		System.out.println(webElement.toString());
//	        WebElement newWebElement = mDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
//	            By.xpath("//android.widget.TextView[" + i + "]")));
//	        System.out.println(newWebElement.toString());
    	}
//        findElementByResources("Custom View").click();
    }

//    @Test
//    public void four() throws Exception {
//    	setWaitingTimeInSeconds(0);
//
//        List<String> cellNames = new ArrayList<String>();
//
//        for (WebElement cell : findElementsByClassName("android.widget.TextView")) {
//        	cellNames.add(cell.getAttribute("name"));
//        }
//
//        for (int i = 0; i < cellNames.size(); i++) {
//            System.out.println("Position " + i + ":" + cellNames.get(i));
//        }
//
//        // delete title cell
//        cellNames.remove(0);
//
//        for (String cellName : cellNames) {
//            System.out.println("cell name:" + cellName);
//            WebElement webElement = scrollToExact(cellName);
//            System.out.println(webElement.toString());
//            webElement.click();
//            // Testing the first element, which is not possible to click on the test three
////            scrollToExact(cellNames.get(0)).click();
//            waitInvisible(setKeyByMatchesText(cellName));
//            back();
//            waitForKey(setKeyByResources("Accessibility"));
//            waitForKey(setKeyByResources("Animation"));
//        }
//
//        setWaitingTimeInSeconds(30); // restore old implicit wait
//    }
}