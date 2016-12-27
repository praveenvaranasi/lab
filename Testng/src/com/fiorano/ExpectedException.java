package com.fiorano;

/**
 * Created by Praveen on 12/24/2016.
 */
public class ExpectedException
{
    private String name;
    private String firstName;
    public void setName(String name)
    {
        this.name=name;
        int a=0,b;
        b=1/a;
    }
    public String getName()
    {
        return name;
    }
    public void setFirstName(String firstName)
    {
        this.firstName=firstName;
    }
    public String getFirstName()
    {
        return firstName;
    }
}
