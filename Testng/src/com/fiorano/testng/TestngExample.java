package com.fiorano.testng;

/**
 * Created by Praveen on 12/22/2016.
 */
import org.testng.annotations.Test;
import org.testng.Assert;
import com.fiorano.MessageUtil;
import com.fiorano.ExpectedException;

public class TestngExample
{
    String msg="Hello World";
    MessageUtil messageUtil=new MessageUtil(msg);
    ExpectedException expectedException=new ExpectedException();

    @Test()
    public void printMessage()
    {
        System.out.println(msg);
        Assert.assertEquals(msg,messageUtil.getMessageUtil());
    }
    @Test(expectedExceptions = ArithmeticException.class)
    public void check()
    {
        expectedException.setName(msg);
    }

}
