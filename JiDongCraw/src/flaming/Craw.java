package flaming;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jeasy.analysis.MMAnalyzer;

public class Craw 
{
	private static Connection conn;
	private static Map<String, Integer> allWords = new HashMap<String, Integer>();
	private static Set<String> allNoWords = new HashSet<String>();
	static 
	{
		try 
		{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;DatabaseName=jidong", "xxx",
					"xxx");
		} 
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		allNoWords.add("小米");
		allNoWords.add("手机");
		allNoWords.add("还是");
		allNoWords.add("感觉");
		allNoWords.add("没有");
		allNoWords.add("京东");
		allNoWords.add("就是");
		allNoWords.add("一个");
		allNoWords.add("非常");
		allNoWords.add("不过");
		allNoWords.add("但是");
		allNoWords.add("这个");
		allNoWords.add("真的");
		allNoWords.add("不是");
		
	}
	
	public static void main(String[] args) throws Exception
	{
		System.setProperty("webdriver.chrome.driver", "F:\\JAVA\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.manage().window().maximize();
		driver.get("https://item.jd.com/4099139.html");
		wait.until(ExpectedConditions.presenceOfElementLocated(
				By.cssSelector("div.detail>div.ETab>div.tab-main.large>ul>li:nth-child(5)")));
		WebElement message = driver.findElement(
				By.cssSelector("div.detail>div.ETab>div.tab-main.large>ul>li:nth-child(5)"));
		System.out.println(message.getText());
		jsDriver.executeScript("window.scrollTo(0,document.body.scrollHeight * 0.05);");
		message.click();
		int i = 0;
		while(true) 
		{
			i++;
			if (i > 20)
			{
				break;
			}
			wait.until(ExpectedConditions.presenceOfElementLocated(
					By.cssSelector("div.comment-column.J-comment-column>p")));
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e2) 
			{
				e2.printStackTrace();
			}
			List<WebElement> list = driver.findElements(
					By.cssSelector("div.comment-column.J-comment-column>p"));
			MMAnalyzer analyzer = new MMAnalyzer();
			MMAnalyzer.addWord("骁龙");
			String[] words = null;
			for (WebElement e : list){
				try 
				{
					words = analyzer.segment(e.getText(), "|").split("\\|");
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				for(String s : words)
				{
					if(s.length() > 1&&!allNoWords.contains(s)) 
					{
						if(!allWords.containsKey(s)) 
						{
							allWords.put(s, 1);
						} else 
						{
							allWords.put(s, allWords.get(s) + 1);
						}
					}
				}
			}
			WebElement nextPage = driver.findElement(By.cssSelector("a.ui-pager-next"));
			System.out.println("-------------" + nextPage.getText() + i + "------------");
			jsDriver.executeScript("arguments[0].scrollIntoViewIfNeeded(true);", nextPage);
			nextPage.click();
		}
		
		String sql = "INSERT INTO jidong_data VALUES (?,?)";
		Set<String> keySet = allWords.keySet();
		for (String key : keySet) 
		{
			int value = allWords.get(key);
			try 
			{
				PreparedStatement pst = conn.prepareStatement(sql);
				pst.setString(2, key);
				pst.setInt(1, value);
				pst.executeUpdate();
				pst.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		printMySqlData();
		conn.close();
	}
	
	private static void printMySqlData() throws Exception{
		String sql = "SELECT keywords,counts FROM jidong_data where counts >= 1 and counts <=20 ORDER BY counts DESC";
		PreparedStatement pst = conn.prepareStatement(sql);
		ResultSet rs = pst.executeQuery();
		while(rs.next()) {
			System.out.print(rs.getString(1) + ":");
			System.out.println(rs.getInt(2));
		}
		rs.close();
		pst.close();
	}
}