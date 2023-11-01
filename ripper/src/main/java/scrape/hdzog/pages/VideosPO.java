package scrape.hdzog.pages;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import scrape.AbstractCommonPO;

public class VideosPO extends AbstractCommonPO
{

    @FindBy(css="a[href='/top-country/']")
    private WebElement linkCountry;

    @FindBy(css="a.desktop-menu-item.item-primary")
    private List<WebElement> linkPrimaryTags;
    
    @FindBy(css="a.desktop-menu-item:not(a.item-primary)")
    private List<WebElement> linkPopularTags;

    @FindBy(css="h1.title-h1")
    private WebElement labelTitle;

    @FindBy(css=".videoplayer")
    private WebElement videoPlayer;

    @FindBy(css="div.jw-icon-playback[aria-label='Play']")
    private WebElement videoPlay;

    @FindBy(css="video.jw-video")
    private WebElement videoSource;

    @FindBy(css=".info-block__controllers-wrapper:nth-child(1) > .info-block__controllers-control.has-tooltip:nth-child(1)")
    private WebElement buttonLikeCount;

    @FindBy(css=".info-block__controllers-wrapper:nth-child(1) > .info-block__controllers-control.has-tooltip:nth-child(2)")
    private WebElement buttonDislikeCount;

    @FindBy(css=".info-block__controllers-wrapper:nth-child(2) > .info-block__controllers-control.has-tooltip:nth-child(1)")
    private WebElement labelViewCount;

    @FindBy(css=".info-block__controllers-wrapper:nth-child(3) > .info-block__controllers-control.has-tooltip:nth-child(1)")
    private WebElement buttonCommentCount;

    @FindBy(css=".info-block__info_show > ul")
    private WebElement frameInfoList;

    @FindBy(css="li.info-block__list-item > a.info-block__tag")
    private WebElement buttonAuthor;

    @FindBy(css=".info-block__tags > a")
    private List<WebElement> tableVideoTags;

    @FindBy(css=".listing_related")
    private WebElement sectionRelatedVideos;

    @FindBy(css=".listing__item.listing__item_video.item-video")
    private List<WebElement> tableVideoPreviews;

    @FindBy(css=".pagination__arrow-next")
    private WebElement buttonPaginationNext;

    public VideosPO(WebDriver driver)
    {
        super(driver);
        waitForElementToBeVisible(buttonAuthor);
    }

    public String retrieveTextCountry()
    {
        return retrieveTextFromElement(this.linkCountry);
    }

    public String retrieveHrefCountry()
    {
        return retrieveAttributeValueFromElement(linkCountry, "href");
    }

    public List<String> retrieveTextPrimaryTags()
    {
        return retrieveTextsFromElements(this.linkPrimaryTags);
    }

    public List<String> retrieveHrefPrimaryTags()
    {
        return retrieveAttributeValuesFromElements(linkPrimaryTags, "href");
    }

    public List<String> retrieveTextPopularTags()
    {
        return retrieveTextsFromElements(linkPopularTags);
    }

    public List<String> retrieveHrefPopularTags()
    {
        return retrieveAttributeValuesFromElements(linkPopularTags, "href");
    }

    public String retrieveTextTitle()
    {
        return retrieveTextFromElement(labelTitle);
    }

    public String retrieveSrcSource()
    {
        return retrieveAttributeValueFromElement(this.videoSource, "src");
    }

    public String retrieveTextLikeCount()
    {
        return retrieveTextFromElement(this.buttonLikeCount);
    }

    public String retrieveTextDislikeCount()
    {
        return retrieveTextFromElement(this.buttonDislikeCount);
    }

    public String retrieveTextViewCount()
    {
        return retrieveTextFromElement(this.labelViewCount);
    }

    public String retrieveTextCommentCount()
    {
        return retrieveTextFromElement(this.buttonCommentCount);
    }

    public String retrieveTextAuthor()
    {
        return retrieveTextFromElement(this.buttonAuthor);
    }

    public String retrieveHrefAuthor()
    {
        return retrieveAttributeValueFromElement(buttonAuthor, "href");
    }

    public List<String> retrieveTextVideoTags()
    {
        return retrieveTextsFromElements(this.tableVideoTags);
    }

    public List<String> retrieveHrefVideoTags()
    {
        return retrieveAttributeValuesFromElements(this.tableVideoTags, "href");
    }

    public List<String> retrieveTitleVideoPreviews()
    {
        return retrieveTextsFromElements(this.tableVideoPreviews);
    }

    public List<String> retrieveAltVideoPreviews()
    {
        return retrieveAttributeValuesFromElements(this.tableVideoPreviews, "alt");
    }

    public List<String> retrieveBackgroundImageVideoPreviews()
    {
        return retrieveCssPropertyFromElements(tableVideoPreviews, "background-image");
    }

    // retrieveHrefVideoPreviews

    // retrieveSrcVideoPreviews

    public void hoverOverVideoPlayer()
    {
        hoverOverElement(videoPlayer);
    }

    public void clickVideoPlayButton()
    {
        clickElement(videoPlay);
    }

    public void scrollToRelatedVideosSection()
    {
        scrollToElement(sectionRelatedVideos);
    }

    public boolean clickPaginationNext()
    {
        return clickElement(buttonPaginationNext);
    }



    
}
