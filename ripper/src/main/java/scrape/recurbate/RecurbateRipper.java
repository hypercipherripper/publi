package scrape.recurbate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.*;
import org.openqa.selenium.*;

public class RecurbateRipper 
{
    private WebDriver driver;
    private String pageSource;
    private String fileLocation = "/Users/hojamil/Downloads/hot real women/scrape/23-08/";

    public RecurbateRipper(WebDriver driver, String pageSource)
    {
        this.driver = driver;
        this.pageSource = pageSource;
    }

    public void scrapeSite() throws IOException {

        // driver.findElement(By.)

        // FileWriter fstream = new FileWriter(fileLocation);
        // BufferedWriter out = new BufferedWriter(fstream);

    }


    
}
