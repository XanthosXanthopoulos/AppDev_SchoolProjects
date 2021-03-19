package com.example.demoapp.data.model.datasource;

public class DataSourceResponse<T>
{

    private boolean successful;

    private String errorMessage;

    private T response;

    public DataSourceResponse(T response)
    {
        this.successful = true;
        this.errorMessage = null;
        this.response = response;
    }

    public DataSourceResponse(String errorMessage)
    {
        this.successful = false;
        this.errorMessage = errorMessage;
        this.response = null;
    }

    public boolean isSuccessful()
    {
        return successful;
    }

    public void setSuccessful(boolean successful)
    {
        this.successful = successful;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public T getResponse()
    {
        return response;
    }

    public void setResponse(T response)
    {
        this.response = response;
    }
}
