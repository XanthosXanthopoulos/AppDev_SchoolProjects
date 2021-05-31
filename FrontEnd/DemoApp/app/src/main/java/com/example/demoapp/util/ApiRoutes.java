package com.example.demoapp.util;

import java.util.HashMap;
import java.util.Map;

public final class ApiRoutes
{
    private static final String BASE = "http://192.168.1.109:5000/api";
    private static final String GEO_BASE = "https://api.opencagedata.com/geocode/v1/json";

    public static final String LOGIN = "/account/login";
    public static final String LOGOUT = "/account/logout";
    public static final String REGISTER = "/account/register";
    public static final String PROFILE_INFO = "/account/profileinfo";
    public static final String UPDATE_PROFILE = "/account/updateprofile";
    public static final String SEARCH_ACTIVITY = "/activity/searchactivities";
    public static final String SEARCH_POST = "/post/searchposts";
    public static final String DELETE_POST = "/post/deletepost";
    public static final String SEARCH_USER = "/relationship/searchusers";
    public static final String GET_FOLLOWEES = "/relationship/getfollowees";
    public static final String GET_FOLLOWERS = "/relationship/getfollowers";
    public static final String FEED = "/post/getpostsummaries";
    public static final String IMAGE_DOWNLOAD = "/file/download";
    public static final String IMAGE_UPLOAD = "/file/uploadfile";
    public static final String NOTIFICATION_HUB = "/notifications";
    public static final String GET_POST = "/post/getpost";
    public static final String GET_POST_ACTIVITIES = "/activity/getactivities";
    public static final String UPLOAD_ACTIVITY = "/activity/uploadactivity";
    public static final String UPLOAD_POST = "/post/uploadpost";

    public static String getRoute(Route route)
    {
        try
        {
            return BASE + (String) ApiRoutes.class.getField(route.name()).get(null);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getRoute(Route route, HashMap<String, String> params)
    {
        try
        {
            StringBuilder url = new StringBuilder();
            url.append(BASE + (String) ApiRoutes.class.getField(route.name()).get(null));

            if (!params.isEmpty()) url.append("?");

            for (Map.Entry<String, String> entry : params.entrySet())
            {
                url.append(entry.getKey() + "=" + entry.getValue() + "&");
            }

            if (!params.isEmpty()) url.deleteCharAt(url.length() - 1);

            return url.toString();
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    public static String getGeoRoute(HashMap<String, String> params)
    {
        StringBuilder url = new StringBuilder();
        url.append(GEO_BASE);

        if (!params.isEmpty()) url.append("?");

        for (Map.Entry<String, String> entry : params.entrySet())
        {
            url.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        if (!params.isEmpty()) url.deleteCharAt(url.length() - 1);

        return url.toString();
    }

    public enum Route
    {
        LOGIN,
        LOGOUT,
        REGISTER,
        PROFILE_INFO,
        UPDATE_PROFILE,
        SEARCH_ACTIVITY,
        FEED,
        IMAGE_DOWNLOAD,
        IMAGE_UPLOAD,
        NOTIFICATION_HUB,
        GET_POST,
        GET_POST_ACTIVITIES,
        UPLOAD_ACTIVITY,
        UPLOAD_POST,
        SEARCH_POST,
        SEARCH_USER,
        GET_FOLLOWEES,
        DELETE_POST,
        GET_FOLLOWERS
    }
}
