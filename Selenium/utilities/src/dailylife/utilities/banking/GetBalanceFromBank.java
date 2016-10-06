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
		
	public void getBalance(String bankName) throws InterruptedException, IOException
	{
		SetGeckoDriver gecDrive = new SetGeckoDriver();
		gecDrive.geckoDriver();
		String balance="null";
		String bank=bankName;
		if(bank.equals("sbi"))
		{
			String username, password;
			System.out.println("Enter the Username of your "+bank+" account");
			BufferedReader usernameBufferedReader = new BufferedReader(new InputStreamReader(System.in));
			username = usernameBufferedReader.readLine() ;
			System.out.println("Enter the password of your "+bank+" account");
			BufferedReader passwordBufferedReader = new BufferedReader(new InputStreamReader(System.in));
			password = passwordBufferedReader.readLine() ;
			WebDriver driver=new FirefoxDriver();
			driver.navigate().to("https://retail.onlinesbi.com/retail/login.htm");
			driver.manage().window().maximize();
			//driver.get("http://site21.way2sms.com/entry?ec=0080&id=g3mg");
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
	}
}
