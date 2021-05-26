package com.example.demoapp.data;

public class Event<T>
{
    private boolean isHandled;
    private T data;

    public Event(T data)
    {
        this.data = data;
        isHandled = false;
    }

    public boolean isHandled()
    {
        return isHandled;
    }

    public void setHandled(boolean handled)
    {
        isHandled = handled;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}
