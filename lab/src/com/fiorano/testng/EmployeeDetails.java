package com.fiorano.testng;
/**
 * Created by Praveen on 12/16/2016.
 */
import org.testng.annotations.Test;
public class EmployeeDetails
{
    private String name;
    private int id;

    public void setName(String name)
    {
        this.name=name;
    }
    public void setId(int id)
    {
        this.id=id;
    }
    public String getName()
    {
        return name;
    }
    public int getId()
    {
        return id;
    }
}
