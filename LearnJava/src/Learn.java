/**
 * Created by root on 2/16/17.
 */

import reference.arrays.InfoOnArray;
import reference.controlFlowStatements.DecisionMakingStatements;
import reference.operators.InstanceOfDemo;
import reference.operators.Operators;
import reference.controlFlowStatements.LoopingStatements;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Learn
{
    public static void main(String[] args) throws IOException
    {
        int i, topicNumber=0;
        String[] topic = { "Operators", "arrays", "ControlFlowStatements" };
        for (i=0; i<topic.length; i++)
        {
            System.out.println( (i+1) +"\t"+topic[i]);
        }
        System.out.println("Select the corresponding number for going through the Topic");
        topicNumber=Integer.valueOf(new BufferedReader(new InputStreamReader(System.in)).readLine()).intValue();
        switch (topicNumber)
        {
            case 1:
                System.out.println("\"Operators\"");
                Operators operators=new Operators();
                InstanceOfDemo instanceOfDemo = new InstanceOfDemo();
                operators.ternaryOperator();
                operators.testConditionalOperators();
                operators.infoOnIncrementDecrementOperators();
                System.out.println("\"instanceof\" type comparison operator functionality");
                instanceOfDemo.demo();
                break;

            case 2:
                System.out.println("Arrays");
                InfoOnArray infoOnArray = new InfoOnArray();
                infoOnArray.getErrors();
                break;

            case 3:
                System.out.println("Decision Making Statements");
                DecisionMakingStatements decisionMakingStatements = new DecisionMakingStatements();
                LoopingStatements loopingStatements = new LoopingStatements();
                /*decisionMakingStatements.ifthen();
                System.out.println(decisionMakingStatements.ifelse());*/
                /*loopingStatements.whileExec();
                loopingStatements.doWhileExec();*/
                loopingStatements.forExec();
                break;

            default:
                System.out.println("Select valid topic number");
                /*Didn't keep the break here as it is technically not needed*/
        }
    }
}
