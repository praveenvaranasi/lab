package dailylife.utilities.banking;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.server.handler.FindElement;

import dailylife.utilities.properties.SetGeckoDriver;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
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
		if(bank.equalsIgnoreCase("sbi"))
		{
			System.out.println("Enter the Username of your "+bank+" account");
			BufferedReader sbiUsernameBufferedReader = new BufferedReader(new InputStreamReader(System.in));
			username = sbiUsernameBufferedReader.readLine() ;
			System.out.println("Enter the password of your "+bank+" account");
			BufferedReader sbiPasswordBufferedReader = new BufferedReader(new InputStreamReader(System.in));
			password = sbiPasswordBufferedReader.readLine() ;
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
		else if (bank.equalsIgnoreCase("hdfc"))
		{
			System.out.println("Enter the UserID/CustomerID of "+bank+" bank");
			String userID;
			BufferedReader bufferedReaderUserID = new BufferedReader(new InputStreamReader(System.in));
			userID=bufferedReaderUserID.readLine();
			System.out.println("Enter the Password of UserID of "+bank+" bank");
			BufferedReader bufferedReaderPassword=new BufferedReader(new InputStreamReader(System.in));
			password=bufferedReaderPassword.readLine();
			WebDriver driver = new FirefoxDriver();
			driver.navigate().to(hdfcurl);
			driver.manage().window().maximize();
			Thread.sleep(5000);
			driver.switchTo().frame("login_page");
			driver.findElement(By.className("pwd_field")).sendKeys(userID);
			Thread.sleep(3000);
			driver.findElement(By.xpath("html/body/form/table[2]/tbody/tr/td[2]/table/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[2]/table/tbody/tr[6]/td[2]/a/img")).click();
			Thread.sleep(5000);
			driver.findElement(By.className("pwd_field")).sendKeys(password);
			driver.findElement(By.name("chkrsastu")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("html/body/form/table[2]/tbody/tr/td[2]/table/tbody/tr[1]/td[1]/table/tbody/tr[3]/td[2]/table/tbody/tr[6]/td/table/tbody/tr[9]/td/a/img")).click();
			Thread.sleep(4000);
			driver.findElement(By.id(".//*[@id='savingAcctList']")).click();
			Thread.sleep(4000);
			balance=driver.findElement(By.id(".//*[@id='SavingTotalSummary']")).getText();
			System.out.println("Balance in your "+bank+" account is "+balance);
		}
		else
		{
			System.out.println("Select among the hdfc and sbi banks");
		}
	}
}
