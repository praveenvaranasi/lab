package com.fiorano.testng;

/**
 * Created by Praveen on 12/16/2016.
 */
import com.fiorano.testng.EmployeeDetails;

public class EmployeeLogic
{
    public int number(EmployeeDetails employeeDetails)
    {
        int number=employeeDetails.getId();
        return number;
    }
    public String fn(EmployeeDetails employeeDetails)
    {
        String fn=employeeDetails.getName();
        return fn;
    }
}
