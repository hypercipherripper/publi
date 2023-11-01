package scrape.hdzog.actions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.WebDriver;

import scrape.hdzog.pages.VideosPO;

public class VideosActions {

    private WebDriver driver;
    private String url;
    private String directory;
    private final String containingDir = "hdzog/";

    public VideosActions(WebDriver driver, String url, String directory)
    {
        this.driver = driver;
        this.url = url;
        this.directory = directory;
    }

    private void createDirectory(String fullDirectory)
    {
        //String fullDirectory = directory + containingDir + dirFileName;
        try {
            Boolean isDirCreated = new File(fullDirectory).mkdir();
            if(!isDirCreated) { throw new IOException(); }
        } catch (IOException e) {
            System.out.println("ERROR: Exception encountered when creating directory!");
            e.printStackTrace();
        }
    }

    private Optional<File> createFile(String fullDirectory, String fileName)
    {
        File myFile = new File(fullDirectory + "/" + fileName + ".txt");

        try {
            if ( myFile.createNewFile() ) { System.out.println("File created: " + myFile.getName()); }
            else { System.out.println("File already exists!"); }

            return Optional.of(myFile);
        } catch (IOException e) {
            System.out.println("ERROR: Exception encountered when creating file!");
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private void writeToFile(File file, String before, String content)
    {
        try( FileWriter myWriter = new FileWriter(file, true) ) {
            myWriter.write(before + "\n");
            myWriter.write(content + "\n");
            myWriter.write("\n");
        } catch (IOException e) {
            System.out.println("ERROR: Failed to write to file!");
            e.printStackTrace();
        }
    }

     private void writeToFile(File file, String before, String content1, String content2)
    {
        try( FileWriter myWriter = new FileWriter(file, true) ) {
            myWriter.write(before + "\n");
            myWriter.write("\t" + content1 + "\t" + "(" + content2 + ")" + "\n");
            myWriter.write("\n");
        } catch (IOException e) {
            System.out.println("ERROR: Failed to write to file!");
            e.printStackTrace();
        }
    }

    private void writeToFile(File file, String before, List<String> contents)
    {
        try( FileWriter myWriter = new FileWriter(file, false) ) 
        {
            myWriter.write(before + "\n");
            for (String piece : contents)
            {
                myWriter.write("\t" + piece + "\n");
            }
            myWriter.write("\n");
        } catch (IOException e) 
        {
            System.out.println("ERROR: Failed to write to file!");
            e.printStackTrace();
        }
    }

    private void writeToFile(File file, String before, List<String> contents1, 
        List<String> contents2)
    {
        try( FileWriter myWriter = new FileWriter(file, false) ) 
        {
            myWriter.write(before + "\n");

            for (int i = 0; i < contents1.size(); i++)
            {
                myWriter.write("\t" + contents1.get(i) + "\t" + "(" + contents2.get(i) + ")" + "\n");
            }
            myWriter.write("\n");
        } catch (IOException e) 
        {
            System.out.println("ERROR: Failed to write to file!");
            e.printStackTrace();
        }
    }

    private void pauseOrPlayVideo(VideosPO videosPO)
    {
        videosPO.hoverOverVideoPlayer();
        videosPO.clickVideoPlayButton();
    }

    private void writeToFile(File file, String before, List<String> contents1, 
        List<String> contents2, List<String> contents3)
    {
        try( FileWriter myWriter = new FileWriter(file, false) ) 
        {
            myWriter.write(before + "\n");

            for (int i = 0; i < contents1.size(); i++)
            {
                // write main bullet
                myWriter.write("\t" + "- " + "\"" + contents1.get(i) + "\"" + "\n");

                // write sub bullets
                myWriter.write("\t\t" + "- " + "\"" + contents2.get(i) + "\"" + "\n");
                myWriter.write("\t\t" + "- " + contents3.get(i) + "\n");
            }
            myWriter.write("\n");
        } catch (IOException e) 
        {
            System.out.println("ERROR: Failed to write to file!");
            e.printStackTrace();
        }
    }

    public void scrapePage()
    {
        // instantiate VideosPO
        VideosPO videosPO = new VideosPO(driver);

        // pause video
        pauseOrPlayVideo(videosPO);

        // get video title, add to directory string
        String videoTitle = videosPO.retrieveTextTitle();
        String fullDirectory = directory + containingDir + videoTitle;

        // make new text file
        createDirectory(fullDirectory);
        Optional<File> myFile = createFile(fullDirectory, videoTitle);
        
        if ( !myFile.isPresent() )
        {
            System.out.println("ERROR: File container is empty!");
            System.exit(0);
        }

        // write site url
        writeToFile(myFile.get(), "URL: ",  url);

        // write country tag, href
        String countryTag = videosPO.retrieveTextCountry();
        String countryHref = videosPO.retrieveHrefCountry();
        writeToFile(myFile.get(), "Country Tag: ", countryTag, countryHref);

        // write primary tags, href
        List<String> primaryTags = videosPO.retrieveTextPrimaryTags();
        List<String> primaryHrefs = videosPO.retrieveHrefPrimaryTags();
        writeToFile(myFile.get(), "Primary Tags: ", primaryTags, primaryHrefs);

        // write popular tags, href
        List<String> popularTags = videosPO.retrieveTextPopularTags();
        List<String> popularHrefs = videosPO.retrieveHrefPopularTags();
        writeToFile(myFile.get(), "Popular Tags: ", popularTags, popularHrefs);

        // write tags
        String vidTitle = videosPO.retrieveTextTitle();
        writeToFile(myFile.get(), "Video Title: ", vidTitle);

        // write like count
        String likeCount = videosPO.retrieveTextLikeCount();
        writeToFile(myFile.get(), "Like Count: ", likeCount);

        // write dislike count
        String dislikeCount = videosPO.retrieveTextDislikeCount();
        writeToFile(myFile.get(), "Dislike Count: ", dislikeCount);

        // write view count
        String viewCount = videosPO.retrieveTextViewCount();
        writeToFile(myFile.get(), "View Count: ", viewCount);

        // write comment count
        String commentCount = videosPO.retrieveTextCommentCount();
        writeToFile(myFile.get(), "Comment Count: ", commentCount);

        // write author, href
        String authorName = videosPO.retrieveTextAuthor();
        String authorHref = videosPO.retrieveHrefAuthor();
        writeToFile(myFile.get(), "Author: ", authorName, authorHref);

        // write video tags, hrefs
        List<String> videoTags = videosPO.retrieveTextVideoTags();
        List<String> videoHrefs = videosPO.retrieveHrefVideoTags();
        writeToFile(myFile.get(), "Video Tags: ", videoTags, videoHrefs);

        // write related video info
        videosPO.scrollToRelatedVideosSection();

        int count = 1;
        while (videosPO.clickPaginationNext()) 
        {
            List<String> relatedVideoTitles = videosPO.retrieveTitleVideoPreviews();
            List<String> relatedVideoAlts = videosPO.retrieveAltVideoPreviews();
            List<String> relatedVideoUrls = videosPO.retrieveBackgroundImageVideoPreviews();
            writeToFile(myFile.get(), "Related Videos " + count + ": ", relatedVideoTitles, 
                relatedVideoAlts, relatedVideoUrls);
            count++;
        }

        // write download video link, download video
        String videoUrl = videosPO.retrieveSrcSource();
        writeToFile(myFile.get(), "Download: ", videoUrl);
        






        
        













        


        // write link to file

        
    }
    
}
