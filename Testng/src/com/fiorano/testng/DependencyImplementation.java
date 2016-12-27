package com.fiorano.testng;

/**
 * Created by Praveen on 12/24/2016.
 */
import org.testng.TestNGException;
import org.testng.annotations.Test;
import org.testng.Assert;
import com.fiorano.GroupUtil;

public class DependencyImplementation
{
    String message="fiorano";
    GroupUtil groupUtil=new GroupUtil();

    @Test(groups={"fiorano"}, enabled = true)
    public void groupCheck()
    {
        groupUtil.setSalutationMessage("Hi!!"+message);
        Assert.assertEquals("Hi!!"+message, groupUtil.getSalutationMessage());
    }

    @Test()
    public void independent()
    {
        groupUtil.setMessage(message);
        Assert.assertEquals(message,groupUtil.getMessage());
    }

//    @Test(dependsOnMethods = {"independent"}, dependsOnGroups = {"fiorano"})
    @Test(dependsOnMethods = {"independent"})
    public void dependent()
    {
        System.out.println(groupUtil.getMessage());
    }
}
