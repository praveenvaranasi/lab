package bankingApplication;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import org.openqa.selenium.firefox.FirefoxDriver;
import bankingApplication.SetProperties.SetGeckodriver;
import bankingApplication.getBalance.GetBalanceFromBank;

public class test 
{
	public static void main(String[] args) throws IOException, InterruptedException  
	{
			// TODO Auto-generated method stub
			String username, password, bankName;
			
			//Giving Bank Name
			System.out.println("Please enter the Bank Name");
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			bankName = bufferedReader.readLine();
			SetGeckodriver drive = new SetGeckodriver();
			drive.geckoDriver();
			GetBalanceFromBank getBalanceFromBank = new GetBalanceFromBank();
			getBalanceFromBank.getBalance(bankName);
			FirefoxDriver driver = new FirefoxDriver();
			driver.quit();
	}

}

