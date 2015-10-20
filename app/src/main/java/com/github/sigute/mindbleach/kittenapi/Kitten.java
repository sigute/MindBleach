package com.github.sigute.mindbleach.kittenapi;

public class Kitten
{
    private String imageURL;
    private String imageID;
    private String sourceURL;

    public Kitten(String imageURL, String imageID, String sourceURL)
    {
        this.imageURL = imageURL;
        this.imageID = imageID;
        this.sourceURL = sourceURL;
    }

    public String getImageURL()
    {
        return imageURL;
    }

    public String getImageID()
    {
        return imageID;
    }

    public String getSourceURL()
    {
        return sourceURL;
    }
}
