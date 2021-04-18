package com.example.demoapp.data.model;

import android.graphics.Bitmap;

public class Image extends Item
{
    private Bitmap image;

    public Image(Bitmap image)
    {
        super(ContentType.IMAGE);

        this.image = image;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }
}
