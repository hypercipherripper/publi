package scrape.sex.scrape;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

public class SexScraper {
    private static String urlInQuestion = "https://www.sex.com/user/lanaluvblowin/hotbunnies/";
    
    public static void scrapeSex()
    {
        ChromeOptions options = new ChromeOptions();
        options.setBinary("/Users/hojamil/Downloads/hot real women/chrome driver/chrome-mac-x64/Google Chrome for Testing.app");
        options.addArguments("user-data-dir=/Users/hojamil/Library/Application Support/Google/Chrome/Default");
        options.addArguments("--start-maximized");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        WebDriver driver = new ChromeDriver(options);
        driver.get(urlInQuestion);

        List<String> skipImgs = new ArrayList<String>();
        skipImgs.add("29989909.gif");
        skipImgs.add("29912678.gif");
        skipImgs.add("29910894.gif");

        skipImgs.add("21503004.gif");
        skipImgs.add("29983833.gif");
        skipImgs.add("29975372.gif");

        skipImgs.add("29749012.gif");
        skipImgs.add("29975620.jpg");
        skipImgs.add("29998550.gif");
        skipImgs.add("29998460.gif");
        skipImgs.add("29989517.gif");
        skipImgs.add("29987883.gif");
        skipImgs.add("29987590.gif");
        skipImgs.add("29987244.gif");
        skipImgs.add("29971817.gif");
        skipImgs.add("29966675.gif");
        skipImgs.add("29967662.gif");
        skipImgs.add("29953455.gif");

        try {
            elementWaiter(driver, 5);

            String pageSource = driver.getPageSource();
            Document document = Jsoup.parse(pageSource, urlInQuestion);

            new File("/Volumes/My Passport for Mac/scrape/23-09/sex/" + document.title() + "/").mkdir();
            File myFile = new File("/Volumes/My Passport for Mac/scrape/23-09/sex/" + document.title() + "/" + document.title() + ".html");
            
            myFile.createNewFile();
            FileWriter myWriter = new FileWriter(myFile, false);

            String imagesDirectory = "/Volumes/My Passport for Mac/scrape/sex/images/";
            String miscDirectory = "/Volumes/My Passport for Mac/scrape/sex/misc/";
            String adDirectory = "/Volumes/My Passport for Mac/scrape/sex/ads/";

            // get all a tag elements
            List<Element> aTagElements = document.getElementsByTag("a");
            for (Element a: aTagElements)
            {
                String aFullHref = a.attr("abs:href");
                a.attr("href", aFullHref);
            }

            // get all img tag elements
            List<Element> imgTagElements = document.getElementsByTag("img");
            for (Element img: imgTagElements)
            {
                // make img src url into full url; except sex logo, flags
                // download img; except sex logo, flags
                // change src to point to downloaded item locally; except sex logo, flags
                // for sex logo, flags: change src to reflect item in global ('misc') directory.

                boolean isSexLogo = img.hasAttr("itemprop") && img.attr("itemprop").equals("logo");
                boolean isFlag = img.attr("src").startsWith("/images/flags/");

                if (isSexLogo || isFlag)
                {
                    String imgSrcFileName = FilenameUtils.getName(img.attr("src"));
                    img.attr("src", miscDirectory + imgSrcFileName);
                    continue;
                }
                
                String fileName = img.attr("abs:src");
                
                // check if a main image.
                // is main image (-1 means does not end with .gif, .jpg, ...)
                fileName = fileNameJunkStripper(FilenameUtils.getName(fileName));
                System.out.println("fileName: " + fileName);

                if (skipImgs.contains(fileName))
                {
                    img.attr("src", "");
                    img.attr("data-src", "");
                    continue;
                }
                // main image
                if (img.hasAttr("alt") && !img.attr("alt").equals("") && !img.hasClass("image lazy-loaded") && img.attr("src").startsWith("https://cdn.sex.com/images/pinporn/"))
                    downloadItem(document, myWriter, img, "src", true, imagesDirectory, fileName);
                // not main image, is related
                else if (img.hasClass("image lazy-loaded") && img.attr("src").startsWith("https://cdn.sex.com/images/pinporn/"))
                {
                    downloadItem(document, myWriter, img, "src", true, imagesDirectory, fileName);
                    img.attr("data-src", imagesDirectory + fileName);
                }
                // not main image, not related, is other account image
                else if (img.attr("src").startsWith("https://cdn.sex.com/images/pinporn/"))
                    downloadItem(document, myWriter, img, "src", true, imagesDirectory, fileName);
                else
                    downloadItem(document, myWriter, img, "src", true, adDirectory, fileName);
            }

            List<Element> linkTagElements = document.getElementsByTag("link");
            boolean faviconAppearedYet = false;
            for (Element link: linkTagElements)
            {
                boolean isFaviconIco = false;
                boolean isDefaultCss = false;
                // check if favicon.ico or default.css
                if (!faviconAppearedYet)
                    isFaviconIco = link.attr("href").startsWith("/images/sex/favicon.ico");
                    faviconAppearedYet = true;
                isDefaultCss = link.attr("href").startsWith("/build/default.css");
                
                if (isFaviconIco || isDefaultCss)
                {
                    String linkHrefFileName = FilenameUtils.getName(link.attr("abs:href"));
                    linkHrefFileName = fileNameJunkStripper(linkHrefFileName);
                    link.attr("href", miscDirectory + linkHrefFileName);
                    continue;
                }

                String linkFullHref = link.attr("abs:href");
                link.attr("href", linkFullHref);
            }

            Element defaultScriptElement = document.selectFirst("head > script[src^='/build/default.js']");
            defaultScriptElement.attr("src", "/Volumes/My Passport for Mac/scrape/sex/misc/default.js");

            myWriter.write(document.toString());
            myWriter.close();
            System.out.println(document.title());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void downloadItem(Document document, FileWriter myWriter, Element element, String attribute, boolean abs,
        String directoryPath, String fileName)
    {
        try 
        {
            URL urlImage;
            //open the stream from URL
            if (abs) { 
                String url = element.attr("abs:" + attribute);
                System.out.println(url);
                urlImage = new URL(url); 
            }
            else { 
                String url = element.attr(attribute);
                System.out.println(url);
                urlImage = new URL(url); 
            }

            boolean check = new File(directoryPath, fileName).exists();
            System.out.println(check);
            if (check)
            {
                element.attr(attribute, directoryPath + fileName);
                return;
            }

            HttpURLConnection httpURLConnection = (HttpURLConnection) urlImage.openConnection();
            httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
            httpURLConnection.setRequestProperty("REFERER", urlInQuestion);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Sec-Ch-Ua", "\"Not)A;Brand\";v=\"24\", \"Chromium\";v=\"116\"");
            httpURLConnection.setRequestProperty("Sec-Ch-Ua-Mobile", "?0");
            httpURLConnection.setRequestProperty("Sec-Ch-Ua-Platform", "macOS");
            httpURLConnection.connect();

            try(InputStream in = httpURLConnection.getInputStream())
            {

            
                byte[] buffer = new byte[4096];
                int n = -1;
                
                OutputStream os = 
                    new FileOutputStream ( directoryPath + fileName);

                //write bytes to the output stream
                while ( (n = in.read(buffer)) != -1 ){
                    os.write(buffer, 0, n);
                }
                
                //close the stream
                os.close();
                
                System.out.println("Image saved");

                element.attr(attribute, directoryPath + fileName);
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String fileNameJunkStripper(String fileName)
    {
        int indexOfQuestMark = fileName.indexOf("?");
        if (indexOfQuestMark != -1)
            fileName = fileName.substring(0, indexOfQuestMark);
        return fileName;
    }

    public static void elementWaiter(WebDriver driver, int numTimes)
    {
        Actions actions = new Actions(driver);
        try {
            for (int i = 0; i < numTimes; i++)
            {
                actions.sendKeys(Keys.PAGE_DOWN).build().perform();
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
