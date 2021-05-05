package com.example.demoapp.util;

import java.util.HashMap;
import java.util.Map;

public final class ApiRoutes
{
    private static final String BASE = "http://192.168.1.109:5000/api";

    public static final String LOGIN = "/account/login";
    public static final String REGISTER = "/account/register";
    public static final String PROFILE_INFO = "/account/profileinfo";
    public static final String UPDATE_PROFILE = "/account/updateprofile";
    public static final String SEARCH = "/activity/activities";
    public static final String FEED = "/activity/feed";
    public static final String IMAGE_DOWNLOAD = "/file/download";
    public static final String NOTIFICATION_HUB = "/notifications";

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

    public enum Route
    {
        LOGIN,
        LOGOUT,
        REGISTER,
        PROFILE_INFO,
        UPDATE_PROFILE,
        SEARCH,
        FEED,
        IMAGE_DOWNLOAD,
        NOTIFICATION_HUB
    }
}
