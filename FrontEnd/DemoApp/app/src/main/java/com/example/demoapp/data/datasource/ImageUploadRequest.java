package com.example.demoapp.data.datasource;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.demoapp.util.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ImageUploadRequest extends Request<NetworkResponse>
{
    private final Response.ErrorListener errorListener;
    private final Response.Listener<String> successListener;

    private final String boundary = "memo-" + System.currentTimeMillis();
    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final ImageLoader imageLoader;
    private final String JWToken;

    public ImageUploadRequest(int method, String url, Uri image, String JWToken, Response.Listener<String> successListener, @Nullable @org.jetbrains.annotations.Nullable Response.ErrorListener errorListener)
    {
        super(method, url, errorListener);
        this.errorListener = errorListener;
        this.successListener = successListener;
        this.JWToken = JWToken;
        imageLoader = ImageLoader.getInstance();
        imageLoader.load(image);
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
        }
        catch (Exception e)
        {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response)
    {
        successListener.onResponse(new String(response.data, StandardCharsets.UTF_8));
    }

    @Override
    public void deliverError(VolleyError error)
    {
        errorListener.onErrorResponse(error);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError
    {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + JWToken);
        return headers;
    }

    @Override
    public String getBodyContentType()
    {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody()
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

        StringBuilder builder = new StringBuilder();
        builder.append(twoHyphens).append(boundary).append(lineEnd);
        builder.append("Content-Disposition: form-data; name=\"file\"; filename=\"qwe\"" + lineEnd);
        builder.append("Content-Type: ").append(imageLoader.getMimeType()).append(lineEnd);
        builder.append(lineEnd);

        try
        {
            outputStream.write(builder.toString().getBytes());
            outputStream.write(imageLoader.getByteData());
            outputStream.write(lineEnd.getBytes());
            builder.setLength(0);
            builder.append(twoHyphens).append(boundary).append(twoHyphens).append(lineEnd);
            outputStream.write(builder.toString().getBytes());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
