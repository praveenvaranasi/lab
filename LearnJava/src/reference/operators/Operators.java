package reference.operators;

/**
 * Created by root on 2/16/17.
 */
public class Operators
{
    public void testConditionalOperators()
    {
        int a=1;
        int b=1;
        if ((a==1) && (b==2))
        {
            System.out.println("&& condition is fine");
        }
        if ((a==1) || (b==2))
        {
            System.out.println("|| condition is good");
        }
    }

    public void infoOnIncrementDecrementOperators()
    {
        int i=1;
        System.out.println("value of i: "+i);
        System.out.println("value of ++i: "+ ++i);
        i-=1;
        System.out.println("value of i++: "+ i++);
    }

    public void ternaryOperator()
    {
        boolean success=true;
        String evaluate=(success)? "Ok" : "notOk";
        System.out.println(evaluate);
    }
}
