package com.firstcry.test;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;
import com.firstcry.base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Login extends BaseTest {

    @Test
    public void firstCryFullFlow() {
        ExtentTest test1 = extent.createTest("TC_FC_001 - Open Website & Login");
        ExtentTest test2 = extent.createTest("TC_FC_002 - Search Product");
        ExtentTest test3 = extent.createTest("TC_FC_003 - Add to Wishlist & Cart");
        ExtentTest test4 = extent.createTest("TC_FC_004 - Open Cart & Wishlist");
        ExtentTest test5 = extent.createTest("TC_FC_005 - Remove Item from Wishlist");

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // ------------------------------------------------------------
            // 1️⃣ Open website and login
            // ------------------------------------------------------------
            test1.info("📝 Step 1: Navigating to FirstCry homepage...");
            driver.get("https://www.firstcry.com/");
            driver.manage().window().maximize();
            test1.pass("✅ Website opened successfully.");
            captureScreenshotWithTest(test1, "Homepage_Opened");

            WebElement loginIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class,'poplogin_main')]")));
            js.executeScript("arguments[0].click();", loginIcon);
            test1.pass("✅ Clicked Login/Register button.");
            captureScreenshotWithTest(test1, "Login_Button_Clicked");

            WebElement mobileInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("lemail")));
            mobileInput.clear();
            mobileInput.sendKeys("9894652765");
            test1.pass("✅ Entered mobile number.");
            captureScreenshotWithTest(test1, "Mobile_Number_Entered");

            WebElement continueBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[normalize-space()='CONTINUE']")));
            js.executeScript("arguments[0].click();", continueBtn);
            test1.pass("✅ Clicked Continue.");
            test1.warning("⚠️ Please enter OTP manually in the browser.");

            WebDriverWait otpWait = new WebDriverWait(driver, Duration.ofSeconds(90));
            otpWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='search_box']")));
            test1.pass("✅ OTP entered and login successful.");
            captureScreenshotWithTest(test1, "After_Login");

            // ------------------------------------------------------------
            // 2️⃣ Search for product — Enhanced Section
            // ------------------------------------------------------------
            long searchStart = System.currentTimeMillis();

            test2.info("📝 Step 1: Verifying search box is displayed...");
            WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("search_box")));
            test2.pass("✅ Search box is visible on the homepage.");

            test2.info("📝 Step 2: Entering product name 'baby carrier'...");
            searchBox.clear();
            searchBox.sendKeys("baby carrier");
            test2.pass("✅ Entered product name successfully.");
            captureScreenshotWithTest(test2, "Entered_Search_Keyword");

            test2.info("📝 Step 3: Waiting for autosuggestions to appear...");
            try {
                WebElement suggestionBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//ul[contains(@class,'search_suggestion')]")));
                List<WebElement> suggestions = suggestionBox.findElements(By.tagName("li"));
                if (suggestions.size() > 0) {
                    test2.pass("✅ " + suggestions.size() + " suggestions displayed under search box.");
                    captureScreenshotWithTest(test2, "Search_Suggestions");
                } else {
                    test2.warning("⚠️ No suggestions appeared for the given search text.");
                }
            } catch (TimeoutException e) {
                test2.warning("⚠️ No suggestion dropdown appeared before timeout.");
            }

            test2.info("📝 Step 4: Clicking on Search button...");
            WebElement searchBtn = driver.findElement(By.xpath("//span[@class='search-button']"));
            js.executeScript("arguments[0].click();", searchBtn);
            test2.pass("✅ Clicked Search button successfully.");

            test2.info("📝 Step 5: Waiting for search results page to load...");
            wait.until(ExpectedConditions.urlContains("search"));
            captureScreenshotWithTest(test2, "Search_Results_Page");

            test2.info("📝 Step 6: Verifying results are displayed...");
            List<WebElement> results = driver.findElements(By.xpath("//div[contains(@class,'cat_item')]"));
            if (results.size() > 0) {
                test2.pass("✅ Search returned " + results.size() + " products for 'baby carrier'.");
            } else {
                test2.fail("❌ No search results found for 'baby carrier'.");
            }

            long searchEnd = System.currentTimeMillis();
            test2.info("⏱ Search completed in " + (searchEnd - searchStart) + " ms");

            // ------------------------------------------------------------
            // 3️⃣ Click first product & add to wishlist/cart
            // ------------------------------------------------------------
            WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//a[contains(@href,'babyhug-harmony')])[1]")));
            js.executeScript("arguments[0].click();", firstProduct);
            test3.pass("✅ Opened first product successfully.");
            captureScreenshotWithTest(test3, "Product_Opened");

            // Switch tab
            List<String> tabs = new ArrayList<>(driver.getWindowHandles());
            driver.switchTo().window(tabs.get(tabs.size() - 1));
            driver.manage().window().maximize();

            wait.until(ExpectedConditions.urlContains("product-detail"));
            WebElement wishlist = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//label[@data-fc-ricon='y']")));
            js.executeScript("arguments[0].scrollIntoView(true);", wishlist);
            Thread.sleep(1000);
            js.executeScript("arguments[0].click();", wishlist);
            test3.pass("✅ Added product to wishlist.");
            captureScreenshotWithTest(test3, "Added_To_Wishlist");

            js.executeScript("window.scrollBy(0, 400);");
            WebElement addToCartBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(translate(text(),'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ADD TO CART')]")));
            js.executeScript("arguments[0].scrollIntoView(true);", addToCartBtn);
            Thread.sleep(1000);
            js.executeScript("arguments[0].click();", addToCartBtn);
            test3.pass("✅ Added product to cart.");
            captureScreenshotWithTest(test3, "Added_To_Cart");

            // ------------------------------------------------------------
            // 4️⃣ Go to cart and wishlist
            // ------------------------------------------------------------
            WebElement viewCartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//span[contains(@class,'cart-icon')]")));
            js.executeScript("arguments[0].click();", viewCartBtn);
            test4.pass("✅ Opened Cart page.");
            captureScreenshotWithTest(test4, "Cart_Page");

            Thread.sleep(4000);
            WebElement heartIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='ShortlistTab1']/a/span[1]")));
            js.executeScript("arguments[0].scrollIntoView(true);", heartIcon);
            Thread.sleep(1000);
            js.executeScript("arguments[0].click();", heartIcon);
            test4.pass("✅ Opened Wishlist page.");
            captureScreenshotWithTest(test4, "Wishlist_Page");

            // ------------------------------------------------------------
            // 5️⃣ Remove from wishlist
            // ------------------------------------------------------------
            Thread.sleep(4000);
            WebElement removeWishlistItem = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id='listing']/div/div/div/div/div[3]/div/section/div[2]/p/label/img")));
            js.executeScript("arguments[0].scrollIntoView(true);", removeWishlistItem);
            Thread.sleep(1000);
            js.executeScript("arguments[0].click();", removeWishlistItem);
            test5.pass("✅ Removed item from Wishlist successfully.");
            captureScreenshotWithTest(test5, "Wishlist_Item_Removed");

        } catch (Exception e) {
            captureScreenshot("FirstCry_Flow_Error");
            test1.fail("❌ Test failed: " + e.getMessage());
            test2.fail("❌ Test failed due to previous step.");
            test3.fail("❌ Test failed due to previous step.");
            test4.fail("❌ Test failed due to previous step.");
            test5.fail("❌ Test failed due to previous step.");
            e.printStackTrace();
        }
    }

    // ----------------------------------------------------------------
    // 🔹 Screenshot helper for failure reports
    // ----------------------------------------------------------------
    private void captureScreenshot(String name) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] src = ts.getScreenshotAs(OutputType.BYTES);
            String base64 = java.util.Base64.getEncoder().encodeToString(src);
            extent.createTest("Screenshot: " + name)
                    .log(Status.INFO, "📸 Captured Screenshot")
                    .addScreenCaptureFromBase64String(base64, name);
        } catch (Exception e) {
            extent.createTest("Screenshot Failed").warning("⚠️ Screenshot error: " + e.getMessage());
        }
    }

    // ----------------------------------------------------------------
    // 🔹 Screenshot helper tied to individual ExtentTest
    // ----------------------------------------------------------------
    private void captureScreenshotWithTest(ExtentTest test, String name) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] src = ts.getScreenshotAs(OutputType.BYTES);
            String base64 = java.util.Base64.getEncoder().encodeToString(src);
            test.addScreenCaptureFromBase64String(base64, name);
        } catch (Exception e) {
            test.warning("⚠️ Failed to capture screenshot: " + e.getMessage());
        }
    }
}
