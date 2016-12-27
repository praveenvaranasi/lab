package com.fiorano.testng;

/**
 * Created by Praveen on 12/23/2016.
 */
import com.fiorano.MessageUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.AfterGroups;
public class ExecutionProcedure
{
    String msg="Do it!!!";
    MessageUtil messageUtil=new MessageUtil(msg);
    @BeforeSuite
    public void beforeSuite()
    {
        System.out.println("Before Suite");
    }
    @AfterSuite
    public void afterSuite()
    {
        System.out.println("After Suite");
    }
    @BeforeClass
    public void beforeClass()
    {
        System.out.println("Before class");
    }
    @AfterClass
    public void afterClass()
    {
        System.out.println("After class");
    }
    @BeforeTest
    public void beforeTest()
    {
        System.out.println("before test");
    }
    @AfterTest
    public void afterTest()
    {
        System.out.println("after Test");
    }
    @BeforeMethod
    public void beforeMethod()
    {
        System.out.println("before method");
    }
    @AfterMethod
    public void afterMethod()
    {
        System.out.println("after Method");
    }
    @Test
    public void test()
    {
        System.out.println(msg);
        Assert.assertEquals(msg,messageUtil.getMessageUtil(),"done");
    }
}
