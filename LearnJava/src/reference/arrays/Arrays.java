package reference.arrays;

/**
 * Created by Praveen on 2/3/17.
 *
 * related to Arrays, all the Information what I got will be coded here which helps to refer in future
 */

public class Arrays {
    public static void main(String args[])
    {
        //Declaration
        int[] array;

        //Allocating Memory for the array
        array = new int[2];

        /*Declaration and Initialization at once
        int[] array1=new int[3];*/

        //Initialization
        array[1]=10;

        /*Initialization for all at once
        int[] arrayCombo={10,20,30};*/

        //Prints the array length (allocated Memory)
        System.out.println(array.length);
    }
}
