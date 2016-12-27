package com.fiorano.testng;

/**
 * Created by Praveen on 12/25/2016.
 */
import java.lang.Object;
import org.testng.annotations.*;
import org.testng.annotations.DataProvider;
import com.fiorano.GroupUtil;

public class ParameterisedClass
{

    String parameter;
    GroupUtil groupUtil=new GroupUtil();
    @BeforeMethod
    public void testInitialise()
    {
        System.out.println("Starting the Test");
    }
    @AfterMethod
    public void testCompletion()
    {
        System.out.println("Done with the Test");
    }

    @Test
    @Parameters("parameter")
    public void testParameterisedTests(String parameter)
    {
        System.out.println(parameter);
    }

    @DataProvider(name = "provide")
    public static Object[][] testInputs()
    {
        return new Object[][]{{2,true},{3,false},{4,false}};
    }
    @Test(dataProvider = "provide")
    public void testParameters(int a, boolean decision)
    {
        System.out.println(a+","+decision);
    }
}
