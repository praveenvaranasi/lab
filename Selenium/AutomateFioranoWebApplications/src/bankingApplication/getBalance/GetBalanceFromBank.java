package bankingApplication.getBalance;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import bankingApplication.SetProperties.SetGeckodriver;


public class GetBalanceFromBank
{
		
	public String getBalance(String bankName) throws InterruptedException
	{
		SetGeckodriver gecDrive = new SetGeckodriver();
		gecDrive.geckoDriver();
		String balance="null";
		String bank=bankName;
		System.out.println(bank);
		if(bank.equals("sbi"))
		{
			WebDriver driver=new FirefoxDriver();
			driver.navigate().to("https://retail.onlinesbi.com/retail/login.htm");
			//driver.manage().window().maximize();
			//driver.get("http://site21.way2sms.com/entry?ec=0080&id=g3mg");
			Thread.sleep(10000);
			driver.findElement(By.xpath(".//*[@id='phishing_banner']/div/a")).click();
			driver.findElement(By.xpath(".//*[@id='username']")).sendKeys("vvssprasad31");
			driver.findElement(By.xpath(".//*[@id='label2']")).sendKeys("vvsimg896.");
			driver.findElement(By.xpath(".//*[@id='Button2']")).click();
			Thread.sleep(10000);
			System.out.println("Logged into the SBI Netbanking");
			driver.findElement(By.xpath(".//*[@id='p_cls_rec']/a/img")).click();
			System.out.println("closed the popup");
			driver.findElement(By.xpath(".//*[@id='accBal00000031891672921']")).click();
			//String value=driver.findElement(By.xpath(".//*[@id='accBal00000031891672921']")).getAttribute();
			Thread.sleep(10000);
			driver.findElement(By.xpath(".//*[@id='tblContainer']/tbody/tr/td/table[2]/tbody/tr/td[9]/span/a")).click();
		}
		return balance;
		
		
	}

}
