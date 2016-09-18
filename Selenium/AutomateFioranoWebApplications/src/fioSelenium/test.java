package fioSelenium;
import fioSelenium.setfioseleniumproperties.SetGeckodriver;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class test 
{
	public static void main(String[] args) throws InterruptedException 
	{
			// TODO Auto-generated method stub
			//System.setProperty("","D:\\Office\\Softwares\\Extracted-DontTouch\\Selenium\\geckodriver-v0.10.0-win64\\geckodriver.exe");
			System.out.println("Hi.. welcome to Selenium Dude");
			SetGeckodriver drive = new SetGeckodriver();
			drive.geckoDriver();
			WebDriver driver=new FirefoxDriver();
			driver.get("https://www.google.co.in/webhp?gws_rd=ssl");
			Thread.sleep(5000);
			driver.quit();

		}

	}

