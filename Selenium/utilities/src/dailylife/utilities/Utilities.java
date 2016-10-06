package dailylife.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import dailylife.utilities.banking.GetBalanceFromBank;
import java.io.IOException;


public class Utilities 
{
	public static void main(String args[]) throws IOException, InterruptedException
	{
		int option;
		System.out.println("Please select an option from the below utilities: \n1.Banking \n2.to be developed Dude");
		BufferedReader bufferedReaderOption = new BufferedReader(new InputStreamReader(System.in));
		option=Integer.valueOf(bufferedReaderOption.readLine()).intValue();
		switch(option)
		{
			case 1: System.out.println("Please select the bank");
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
					String bank=bufferedReader.readLine();
					GetBalanceFromBank getbalance = new GetBalanceFromBank();
					getbalance.getBalance(bank);
					break;
			
			case 2: System.out.println("u have selected two");
					break;
			
			default:System.out.println("Please select a valid option");
					break;
		}
	}
}
