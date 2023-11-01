package scrape.sex.scrape;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SexUserScraper {
    private static String urlInQuestion = "https://www.sex.com/user/lanaluvblowin/hotbunnies/";
    private static String urlStarter = "https://www.sex.com/";
    private static Logger logger = Logger.getLogger(SexUserScraper.class.getName());
    
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

        try
        {
            File skipTxt = new File("/Users/hojamil/Downloads/hot real women/scrape/scraper/ripper/src/main/java/scrape/sex/resources/skip.txt");
            Scanner myReader = new Scanner(skipTxt);
            
            while(myReader.hasNextLine())
            {
                String skipFileName = myReader.nextLine();
                skipImgs.add(skipFileName);
            }

        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        try {
            String profileName = urlInQuestion.replace("https://www.sex.com/user/", "")
                                              .replace("/", "");

            String profileDirectoryString = "/Volumes/My Passport for Mac/scrape/sex/pages/profile/";

            // directory string for user profile folder containing all snapshots
            File profileDirectory = new File(profileDirectoryString + profileName + "/");

            File snapshotDirectory = generateSnapshotDirectory(profileDirectory);

            // file for storing pfp links
            // check if file exists, if not make it
            File pfpLinks = new File("/Users/hojamil/Downloads/hot real women/scrape/scraper/ripper/src/main/java/scrape/sex/resources/pfpLinks.txt");
            if (!pfpLinks.exists())
            {
                try 
                {
                    pfpLinks.createNewFile();
                }
                catch(IOException e)
                {
                    logger.warning("Failed to create pfp links file. Exiting...");
                    e.printStackTrace();
                    System.exit(1);
                }
            }

            // file for storing board links
            // check if file exists, if not make it
            File boardLinks = new File("/Users/hojamil/Downloads/hot real women/scrape/scraper/ripper/src/main/java/scrape/sex/resources/boardLinks.txt");
            if (!pfpLinks.exists())
            {
                try 
                {
                    boardLinks.createNewFile();
                }
                catch(IOException e)
                {
                    logger.warning("Failed to create board links file. Exiting...");
                    e.printStackTrace();
                    System.exit(1);
                }
            }

            // links in user page
            WebElement linkFollowing = driver.findElement(By.cssSelector(".create_board_box a[href*='following']"));
            WebElement linkPins = driver.findElement(By.cssSelector(".create_board_box a[href*='pins']:not(a[href*='repins'])"));
            WebElement linkRepins = driver.findElement(By.cssSelector(".create_board_box a[href*='repins']"));
            WebElement linkLikes = driver.findElement(By.cssSelector(".create_board_box a[href*='likes']"));

            // boards page (user page is same as boards page)
            scrapeTab(driver, skipImgs, snapshotDirectory, "boards");
            WebElement pfp = driver.findElement(By.cssSelector(".user_profile_picture > a"));
            String pfpImgLink = urlStarter + pfp.getAttribute("href");

            List<WebElement> userBoards = driver.findElements(By.cssSelector("#masonry_container > .masonry_box.small_board_box > a"));
            List<String> boards = userBoards.stream().map(board -> urlStarter + board.getAttribute("href"))
                                                     .collect(Collectors.toList());

            FileWriter myWriterBoards = new FileWriter(boardLinks, true);
            boards.forEach(board -> {
                try 
                {
                    myWriterBoards.append(board);
                } catch (IOException e) {
                    logger.warning("Could not append board link to board links file. Exiting...");
                    e.printStackTrace();
                    System.exit(1);
                }
            });
            myWriterBoards.close();
            
            FileWriter myWriterPfps = new FileWriter(pfpLinks, true);
            try 
            {
                myWriterPfps.append(pfpImgLink);
            } catch (IOException e) {
                logger.warning("Could not append pfp link to pfp links file. Exiting...");
                e.printStackTrace();
                System.exit(1);
            }
            myWriterPfps.close();

            // TODO: make sure there is no next page
            // TODO: if there is next page, keep scraping, appending links of next pages until reach end
            // may need to adjust above code to account for this
              
            // go to following tab
            linkFollowing.click();
            scrapeTab(driver, skipImgs, snapshotDirectory, "following");

            // TODO: collect following boards links
            // TODO: if there is next page, keep scraping, appending links of next pages until reach end
            // may need to adjust above code to account for this

            // go to pins tab
            linkPins.click();
            scrapeTab(driver, skipImgs, snapshotDirectory, "pins");

            // TODO: collect pins page links
            // TODO: if there is next page, keep scraping, appending links of next pages until reach end
            // may need to adjust above code to account for this

            // go to repins tab
            linkRepins.click();
            scrapeTab(driver, skipImgs, snapshotDirectory, "repins");

            // TODO: collect repins page links
            // TODO: if there is next page, keep scraping, appending links of next pages until reach end
            // may need to adjust above code to account for this

            // go to likes tab
            linkLikes.click();
            scrapeTab(driver, skipImgs, snapshotDirectory, "likes");

            // TODO: collect likes page links
            // TODO: if there is next page, keep scraping, appending links of next pages until reach end
            // may need to adjust above code to account for this





        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void scrapeTab(WebDriver driver, List<String> skipImgs, File snapshotDirectory, 
        String documentFileName) 
    {
        try
        {
            elementWaiter(driver);

            String pageSource = driver.getPageSource();
            Document document = Jsoup.parse(pageSource, urlInQuestion);

            File myFile = new File(snapshotDirectory.getAbsolutePath() + "/" + documentFileName + ".html");
            
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

    public static void elementWaiter(WebDriver driver)
    {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        long lastHeight = (long) js.executeScript("return document.body.scrollHeight");

        while (true)
            {
                js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    else
                    {
                        break;
                    }
                }

                lastHeight = newHeight;
            }
    }

    public static String oldProfileDirectoryGenerator(String profileName)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm_ss");
        LocalDateTime now = LocalDateTime.now();
        return "/Volumes/My Passport for Mac/scrape/sex/pages/individual/" 
                                                + "old " + dtf.format(now) + profileName + "/";
    }

    private static File generateSnapshotDirectory(File profileDirectory) throws IOException
    {

        // if directory doesn't exist, make it
        if (!profileDirectory.exists())
        {
            if (profileDirectory.mkdir() == true)
                logger.log(Level.INFO, "Profile Directory was successfully created.");
            else
            {
                logger.log(Level.SEVERE, "Profile directory was not able to be created!");
                throw new IOException();
            }
        }
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();

        // TODO: if snapshot directory already exists, then don't make directory
        // log that the snapshot directory exists
        // save this link for later, and continue to next link

        File snapshotDirectory = new File(profileDirectory + dtf.format(now) + "/");
        snapshotDirectory.mkdir();

        return snapshotDirectory;
    }
}
