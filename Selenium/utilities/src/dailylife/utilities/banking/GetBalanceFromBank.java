package dailylife.utilities.banking;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import dailylife.utilities.properties.SetGeckoDriver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;


public class GetBalanceFromBank
{
	String sbiurl="https://retail.onlinesbi.com/retail/login.htm";
	String hdfcurl="https://netbanking.hdfcbank.com/netbanking/";
	String username, password;
	
	public void getBalance(String bankName) throws InterruptedException, IOException
	{
		SetGeckoDriver gecDrive = new SetGeckoDriver();
		gecDrive.geckoDriver();
		String balance="null";
		String bank=bankName;
		if(bank.equals("sbi"))
		{
			System.out.println("Enter the Username of your "+bank+" account");
			BufferedReader sbiusernameBufferedReader = new BufferedReader(new InputStreamReader(System.in));
			username = sbiusernameBufferedReader.readLine() ;
			System.out.println("Enter the password of your "+bank+" account");
			BufferedReader sbipasswordBufferedReader = new BufferedReader(new InputStreamReader(System.in));
			password = sbipasswordBufferedReader.readLine() ;
			WebDriver driver=new FirefoxDriver();
			driver.navigate().to(sbiurl);
			driver.manage().window().maximize();
			Thread.sleep(10000);
			driver.findElement(By.xpath(".//*[@id='phishing_banner']/div/a")).click();
			driver.findElement(By.xpath(".//*[@id='username']")).sendKeys(username);
			driver.findElement(By.xpath(".//*[@id='label2']")).sendKeys(password);
			driver.findElement(By.xpath(".//*[@id='Button2']")).click();
			Thread.sleep(10000);
			System.out.println("Logged into the SBI Netbanking");
			driver.findElement(By.xpath(".//*[@id='p_cls_rec']/a/img")).click();
			System.out.println("closed the popup");
			driver.findElement(By.xpath(".//*[@id='accBal00000031891672921']")).click();
			Thread.sleep(10000);
			balance=driver.findElement(By.xpath(".//*[@id='accBalRes00000031891672921']")).getText();
			System.out.println("Balance in your "+bank+" account is "+balance);
			driver.findElement(By.xpath(".//*[@id='tblContainer']/tbody/tr/td/table[2]/tbody/tr/td[9]/span/a")).click();
		}
		else
		{
			System.out.println("I cant process your request. Right now, I can give balance only");
		}
	}
}
