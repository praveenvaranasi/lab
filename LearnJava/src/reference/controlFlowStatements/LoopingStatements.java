package reference.controlFlowStatements;

/**
 * Created by root on 2/26/17.
 */
public class LoopingStatements
{
    public void whileExec()
    {
        int i=0;
        while(i<10)
        {
            System.out.println(i);
            i+=1;
        }
    }

    public void doWhileExec()
    {
        int j=0;
        do
        {
            System.out.println(j);
            j+=1;
        } while (j<10);
    }

    public void forExec()
    {
        int[] array={1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for ( int k : array)
        {
            System.out.println(k);
        }
    }
}
