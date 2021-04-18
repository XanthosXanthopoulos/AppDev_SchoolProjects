package com.example.demoapp.util;

public final class ApiRoutes
{
    private static final String BASE = "https://192.168.1.109:5002/api";

    public static final String LOGIN = "/account/login";
    public static final String REGISTER = "/account/register";
    public static final String PROFILE_INFO = "/account/profileinfo";
    public static final String UPDATE_PROFILE = "/account/updateprofile";
    public static final String SEARCH = "/activity/activities";

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

    public enum Route
    {
        LOGIN,
        LOGOUT,
        REGISTER,
        PROFILE_INFO,
        UPDATE_PROFILE,
        SEARCH
    }
}
