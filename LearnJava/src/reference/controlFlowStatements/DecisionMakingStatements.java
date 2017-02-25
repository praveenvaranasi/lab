package reference.controlFlowStatements;

/**
 * Created by root on 2/26/17.
 */
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class DecisionMakingStatements
{
    public void ifthen() throws IOException
    {
        int currentSpeed=10;
        String isMoving=null;
        System.out.println("Enter whether the vehicle is moving or not (true/false)");
        isMoving = new BufferedReader(new InputStreamReader(System.in)).readLine();
//        boolean isMoving=true;
        if(isMoving.equalsIgnoreCase(String.valueOf(true)))
        {
            System.out.println("applying brakes at "+currentSpeed+"km/hr to decrease the speed");
            currentSpeed--;
            System.out.println("speed decreased to "+currentSpeed+"Km/hr");
        }
        else
        {
            System.out.println("No need to apply brakes as its not moving");
        }
    }
    public char ifelse() throws IOException
    {
        int marks;
        char grade;
        System.out.println("enter marks");
        marks=Integer.valueOf(new BufferedReader(new InputStreamReader(System.in)).readLine()).intValue();
        System.out.println(marks);
        if (marks >= 90)
        {
            return 'A';
        }
        else if (marks>80 && marks < 90)
        {
            return 'B';
        }
        else if (marks > 70)
        {
            return 'C';
        }
        else if (marks > 60)
        {
            return 'D';
        }
        else
        {
            return 'F';
        }
    }
}
