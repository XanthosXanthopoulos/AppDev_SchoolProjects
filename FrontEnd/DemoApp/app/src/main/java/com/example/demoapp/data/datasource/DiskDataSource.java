package com.example.demoapp.data.datasource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.demoapp.data.model.datasource.DataSourceResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class DiskDataSource
{
    private ConcurrentHashMap<String, String> files;
    private ExecutorService executor;
    private File directory;
    private Handler handler;

    private static DiskDataSource instance;

    private DiskDataSource(Context context, ExecutorService executor)
    {
        files = new ConcurrentHashMap<>();

        this.executor = executor;

        directory = context.getDir("images", Context.MODE_PRIVATE);
        handler = new Handler(Looper.getMainLooper());
    }

    public static void init(Context context, ExecutorService executor)
    {
        instance = new DiskDataSource(context, executor);
    }

    public static DiskDataSource getInstance()
    {
        return instance;
    }

    public void storeImage(String imageID, String filetype, Bitmap image)
    {
        Runnable storeTask = () ->
        {
            File filepath = new File(directory, imageID + filetype);

            FileOutputStream outputStream = null;
            try
            {
                outputStream = new FileOutputStream(filepath);
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                files.put(imageID, filepath.getAbsolutePath());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
                    outputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };

        executor.execute(storeTask);
    }

    public LiveData<DataSourceResponse<Bitmap>> loadImage(String imageID)
    {
        MutableLiveData<DataSourceResponse<Bitmap>> result = new MutableLiveData<>();

        Runnable retrieveTask = () ->
        {
            if (!files.containsKey(imageID))
            {
                result.postValue(new DataSourceResponse<>("File not found."));
                return;
            }

            try
            {
                File filepath = new File(files.get(imageID));
                Bitmap image = BitmapFactory.decodeStream(new FileInputStream(filepath));
                publishResult(result, image, imageID);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        };

        executor.execute(retrieveTask);

        return result;
    }

    private void publishResult(MutableLiveData<DataSourceResponse<Bitmap>> result, Bitmap image, String imageID)
    {
        handler.post(() ->
        {
            result.setValue(new DataSourceResponse<>(image));
            Log.i("IMAGE ID", imageID);
        });
    }
}
