package com.example.demoapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

public class ImageLoader
{
    private static ImageLoader instance;
    private static Context ctx;

    private Uri buffer;

    private ImageLoader(Context context)
    {
        ctx = context;
    }

    public static void initialize(Context context)
    {
        instance = new ImageLoader(context);
    }

    public static synchronized ImageLoader getInstance()
    {
        return instance;
    }

    public void load(Uri uri)
    {
        buffer = uri;
    }

    public String getMimeType()
    {
        return ctx.getContentResolver().getType(buffer);
    }

    public byte[] getByteData()
    {
        try
        {
            return Glide.with(ctx).as(byte[].class).load(buffer).submit().get();
        }
        catch (ExecutionException | InterruptedException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
