package com.fiorano.testng;

/**
 * Created by Praveen on 12/24/2016.
 */
import com.fiorano.GroupUtil;
import org.testng.annotations.Test;
import org.testng.Assert;

public class GroupUtilImplementation
{
    GroupUtil groupUtil=new GroupUtil();
    String msg="praveen";

    @Test(groups = {"original"})
    public void message()
    {
        groupUtil.setMessage(msg);
        Assert.assertEquals(msg, groupUtil.getMessage());
    }


    @Test(groups = {"modification"})
    public void modifiedMessage()
    {
        String modMessage="Hi!!";
        groupUtil.setSalutationMessage(modMessage+msg);
        Assert.assertEquals(modMessage+msg, groupUtil.getSalutationMessage());
    }

    @Test(groups = {"original","modification"})
    public void modifiedOriginalMessage()
    {
        String goodByeMessage="GoodBye";
        groupUtil.setGoodByeMessage(goodByeMessage+msg);
        Assert.assertEquals(goodByeMessage+msg, groupUtil.getGoodByeMessage());
    }


}
