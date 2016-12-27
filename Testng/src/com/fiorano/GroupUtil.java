package com.fiorano;

/**
 * Created by Praveen on 12/24/2016.
 */
public class GroupUtil
{
    private String message;
    private String salutationMessage;
    private String goodByeMessage;

    public void setMessage(String message)
    {
        this.message=message;
    }
    public String getMessage()
    {
        return message;
    }
    public void setSalutationMessage(String salutationMessage)
    {
        this.salutationMessage=salutationMessage;
    }
    public String getSalutationMessage()
    {
        return salutationMessage;
    }
    public void setGoodByeMessage(String goodByeMessage)
    {
        this.goodByeMessage=goodByeMessage;
    }
    public String getGoodByeMessage()
    {
        return goodByeMessage;
    }
    public void setParameters(String company, String salutationMessage)
    {
        this.message=company;
        this.salutationMessage=salutationMessage;
    }
}
