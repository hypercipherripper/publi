package scrape.google_images.scrape;

import java.awt.Robot;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.Duration;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.sun.glass.events.KeyEvent;

public class GoogleImagesScraper {
    private static String urlInQuestion = "https://www.google.com/search?sca_esv=563271944&sxsrf=AB5stBhoa4CxgZAP8nDltIDY88ahsHfwkw:1694052450044&q=strawberrysquirtcake+deepthroat&tbm=isch&source=lnms&sa=X&ved=2ahUKEwip24-UtZeBAxXBFVkFHdX2ADsQ0pQJegQIDBAB&biw=1680&bih=950&dpr=2";
    
    public static void scrapeGoogleImages()
    {
        // FirefoxOptions optionsF= new FirefoxOptions();
        // optionsF.addArguments("user-data-dir=/Users/hojamil/Library/Application Support/Firefox/Profiles/srtbvnrd.Acorn");
        // // optionsF.setBinary("/Applications/Firefox.app");
        // // optionsF.addArguments("--start-maximized");
        // // optionsF.addArguments("--remote-allow-origins=*");
        // // optionsF.addArguments("--disable-blink-features=AutomationControlled");
        // // optionsF.addArguments("--disable-extensions");
        // WebDriver driver = new FirefoxDriver(optionsF);
        // driver.get(urlInQuestion);

        // /Users/hojamil/Downloads/hot real women/geckodriver
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/Users/hojamil/Downloads/hot real women/chrome driver/chrome-mac-x64/Google Chrome for Testing.app");
        options.addArguments("user-data-dir=/Users/hojamil/Library/Application Support/Google/Chrome/Default");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get(urlInQuestion);

        try 
        {
            // WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[aria-label='Quick Settings']")));
            Thread.sleep(5000);
            
            

            // Actions actions = new Actions(driver);
            // actions.keyDown(Keys.COMMAND)
            //        .sendKeys("s")
            //        .build()
            //        .perform();

            // Thread.sleep(3000);
            
            // actions.sendKeys(Keys.ENTER).build().perform();

            // WebElement elem = new WebDriverWait(driver, Duration.ofSeconds(10))
            //                   .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#yDmH0d")));
            // elem.click();

            // new Actions(driver).moveToElement(elem)
            //                    .sendKeys(elem, Keys.chord(Keys.CONTROL, "s"))
            //                    .perform();

            // actions = new Actions(driver);
            // actions.sendKeys(Keys.ENTER)
            //        .build()
            //        .perform();

            // // create object of robot class
            // Robot robot = new Robot();

            // // simulate command + s action to open save as dialog, then release
            // robot.keyPress(768);
            // robot.delay(1000);
            // robot.keyPress(83);
            // // robot.keyRelease(KeyEvent.VK_COMMAND);
            // // robot.keyRelease(KeyEvent.VK_S);

            // robot.delay(2000);

            // // press enter to save file, then release
            // robot.keyRelease(768);
            // robot.keyRelease(83);

            // robot.delay(2000);

            // robot.keyPress(10);
            // robot.delay(1000);
            // robot.keyRelease(10);


            JavascriptExecutor js = (JavascriptExecutor) driver;
            long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

            while (true)
            {
                js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                Thread.sleep(2000);

                long newHeight = (long) js.executeScript("return document.body.scrollHeight");

                if (newHeight == lastHeight)
                {
                    List<WebElement> elems = driver.findElements(By.cssSelector("input[value='Show more results']"));
                    if (!elems.isEmpty())
                    {
                        try
                        {
                            elems.get(0).click();
                        }
                        catch(Exception e)
                        {
                            break;
                        }
                        Thread.sleep(2000);
                        continue;
                    }
                    else
                    {
                        break;
                    }
                }

                lastHeight = newHeight;
            }

            // String pageSource = (String) js.executeScript("return document.documentElement.outerHTML");
            // System.out.println(pageSource);

            String pageSource = driver.getPageSource();
            Document document = Jsoup.parse(pageSource, urlInQuestion);

            new File("/Volumes/My Passport for Mac/scrape/23-09/google/" + document.title() + "/").mkdir();
            File myFile = new File("/Volumes/My Passport for Mac/scrape/23-09/google/" + document.title() + "/" + document.title() + ".html");
            myFile.createNewFile();
            
            FileWriter myWriter = new FileWriter(myFile, true);
            BufferedWriter bufferFileWriter = new BufferedWriter(myWriter);
            bufferFileWriter.append("<!DOCTYPE html>\n");
            bufferFileWriter.append(document.toString());
            // myWriter.append("<!DOCTYPE html>\n");
            // myWriter.append(document.toString());
            // myWriter.close();
            bufferFileWriter.close();
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
