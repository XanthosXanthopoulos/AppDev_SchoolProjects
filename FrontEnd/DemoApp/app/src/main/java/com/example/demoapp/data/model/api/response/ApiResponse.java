package com.example.demoapp.data.model.api.response;

public class ApiResponse<T>
{
    private boolean successful;

    private String errorMessage;

    private T response;

    public ApiResponse() {}

    public ApiResponse(boolean successful, String errorMessage, T response)
    {
        this.successful = successful;
        this.errorMessage = errorMessage;
        this.response = response;
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
