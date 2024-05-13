package com.tathvatech.equipmentcalibration.common;

import java.util.ArrayList;
import java.util.List;

public class BaseResponseBean
{
    public static enum ResponseStatus{success, fail, warn};

    private ResponseStatus responseStatus;
    private List<String> messages = new ArrayList<>();
    public ResponseStatus getResponseStatus()
    {
        return responseStatus;
    }
    public void setResponseStatus(ResponseStatus responseStatus)
    {
        this.responseStatus = responseStatus;
    }
    public List<String> getMessages()
    {
        return messages;
    }
    public void setMessages(List<String> messages)
    {
        this.messages = messages;
    }

    public void addMessage(String message)
    {
        messages.add(message);
    }
}
