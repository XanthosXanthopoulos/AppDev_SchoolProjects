package com.example.demoapp.data.viewmodel;

import android.graphics.Bitmap;

public class ItemUpdate
{
    private int position;
    private Bitmap image;

    public ItemUpdate()
    {
    }

    public ItemUpdate(int position, Bitmap image)
    {
        this.position = position;
        this.image = image;
    }

    public int getPosition()
    {
        return position;
    }

    public void setPosition(int position)
    {
        this.position = position;
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
