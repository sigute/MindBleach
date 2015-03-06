package com.github.sigute.mindbleach.kittenapi;

import android.graphics.Bitmap;

/**
 * Created by sigute on 04/03/2015.
 */
public class Kitten
{
    private Bitmap kittenImage;
    private String imageURL;
    private String imageID;
    private String sourceURL;
    private String description;

    public Kitten(Bitmap kittenImage, String imageURL, String imageID, String sourceURL,
            String description)
    {
        this.kittenImage = kittenImage;
        this.imageURL = imageURL;
        this.imageID = imageID;
        this.sourceURL = sourceURL;
        this.description = description;
    }

    public Bitmap getKittenImage()
    {
        return kittenImage;
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

    public String getDescription()
    {
        return description;
    }
}
