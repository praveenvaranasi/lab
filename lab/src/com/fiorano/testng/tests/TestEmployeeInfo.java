package com.fiorano.testng.tests;

/**
 * Created by Praveen on 12/16/2016.
 */
import com.fiorano.testng.EmployeeDetails;
import com.fiorano.testng.EmployeeLogic;
import org.testng.annotations.Test;
import org.testng.Assert;
public class TestEmployeeInfo
{
    EmployeeDetails employeeDetails = new EmployeeDetails();
    EmployeeLogic employeeLogic = new EmployeeLogic();

    @Test
    public  void testNumber()
    {
        employeeDetails.setId(372);
        int idesh=employeeLogic.number(employeeDetails);
        Assert.assertEquals(372,idesh);
    }
    public void testfn()
    {
        employeeDetails.setName("ravi");
        String result=employeeLogic.fn(employeeDetails);
        Assert.assertEquals("ravi",result);
    }
}
