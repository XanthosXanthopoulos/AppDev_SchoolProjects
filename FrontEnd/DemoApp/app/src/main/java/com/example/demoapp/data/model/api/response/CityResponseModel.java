package com.example.demoapp.data.model.api.response;

import java.util.List;

public class CityResponseModel
{
    private boolean error;
    private String msg;
    private List<String> data;

    public CityResponseModel(boolean error, String msg, List<String> data)
    {
        this.error = error;
        this.msg = msg;
        this.data = data;
    }

    public boolean isError()
    {
        return error;
    }

    public void setError(boolean error)
    {
        this.error = error;
    }

    public List<String> getData()
    {
        return data;
    }

    public void setData(List<String> data)
    {
        this.data = data;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }
}
