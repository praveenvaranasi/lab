package reference.arrays;

import java.util.Arrays;

/**
 * Created by Praveen on 2/3/17.
 * <p>
 * related to InfoOnArray, all the Information what I got will be coded here which helps to refer in future
 */

public class InfoOnArray
{
    char globalVariable;

    public static void main(String[] args)
    {
        InfoOnArray infoOnArray = new InfoOnArray();
        infoOnArray.getErrors();
        //Declaration
        //int[] array;

        //Allocating Memory for the array
        //array = new int[10];

        //Declaration and Initialization at once
        int[] srcArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        int[] destArray;
        destArray = new int[10];

        System.arraycopy(srcArray, 2, destArray, 0, 4);
        int i;
        char example='\u0108';
        System.out.println(example);

        for (i = 0; i < 7; i++)
        {
            System.out.println(destArray[i]);
        }
        //Initialization
        //array[1]=10;

        /*Initialization for all at once
        int[] arrayCombo={10,20,30};*/

        //Prints the array length (allocated Memory)
        System.out.println(srcArray.length + "+" + destArray.length);

    }

    public void getErrors() {
        //local variables must be initialized. Global variables will get initialized to default value;
        int localVariable = 2;
        System.out.println("Value of global variable:" + globalVariable);
        System.out.println("value of local variable:" + localVariable);
    }
}
