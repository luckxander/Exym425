package automation.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.HttpSessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import org.springframework.boot.test.context.SpringBootTest;

import com.google.common.collect.Ordering;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


@SpringBootTest
public class StepsCode {
	
	public WebDriver dr = null;
	static String URL_login = "https://exym-vnext-teamtest2.azurewebsites.net/";
	static HttpSessionId stored = null;

	
	 //Browser SetUp and Login
	 ////////////////////////////////////////////////////////////////////////////////////////////////////
	 
    @Before
    public void setUp() throws Throwable { 
    	   System.out.println("Running setUp ...");
		   System.setProperty("webdriver.chrome.driver","C:/Users/luckx/OneDrive/Documents/workspace-spring-tool-suite-4-4.13.1.RELEASE/exym425/chromedriver.exe");
	       
		   dr = new ChromeDriver();
	       dr.manage().window().maximize();
	       dr.get(URL_login); 
		   
	       if(stored == null) {
			   HttpSessionId session = new HttpSessionId();
			   TimeUnit.SECONDS.sleep(6);
			   WebDriverWait wait = new WebDriverWait(dr, 10);
			   wait.until(ExpectedConditions.textToBePresentInElement(dr.findElement(By.xpath("//body[@id='kt_body']/app-root/ng-component/div/div/div/div/div/tenant-change/span")),"Current tenant"));

			   String currentText = dr.findElement(By.cssSelector("#kt_body > app-root > ng-component > div > div > div.d-flex.flex-center.flex-column.flex-column-fluid.p-10.pb-lg-20 > div > div > tenant-change > span")).getText();
			   //System.out.println("Current text => "+currentText);
			   if(currentText.intern() == "Current tenant: Not selected ( Change )") {
				  
				  //switch to default 
				  dr.findElement(By.linkText("Change")).click();
				  TimeUnit.SECONDS.sleep(3);
				  dr.findElement(By.xpath("//*[@id=\"kt_body\"]/app-root/ng-component/div/div/div[1]/div/div/tenant-change/span/tenantchangemodal/div/div/div/form/div[2]/div[1]/div/label/input")).click();
				  TimeUnit.SECONDS.sleep(3);
				  dr.findElement(By.cssSelector("#tenancyNameInput")).sendKeys("default");
				  TimeUnit.SECONDS.sleep(3);
				  dr.findElement(By.xpath("//*[@id=\"kt_body\"]/app-root/ng-component/div/div/div[1]/div/div/tenant-change/span/tenantchangemodal/div/div/div/form/div[3]/button[2]")).click();
			   
				  //click openIDconnect
				  TimeUnit.SECONDS.sleep(6);
				  dr.findElement(By.linkText("OpenIdConnect")).click();
				  
				  //user credentials 
				  TimeUnit.SECONDS.sleep(3);
				  dr.findElement(By.cssSelector("#signInName")).sendKeys("BettyWhite@mailinator.com");
				  dr.findElement(By.cssSelector("#password")).sendKeys("Password22!");
				  dr.findElement(By.cssSelector("#next")).click();
				  				  
			   }
			   
			   stored = session;
			   TimeUnit.SECONDS.sleep(3);
			}
    }
    
    /////////////////////////////////////////////////////////////////////////////
	//End Browser SetUp and Login

	
	@Given("I am a clinician user")
	public void i_am_a_clinician_user() throws Throwable {
		assertNotEquals(stored,null);
	}

	@When ("I go to the main page Exym vNext portal")
	public void i_go_to_the_main_page_Exym_vNext_portal() throws Throwable {
		TimeUnit.SECONDS.sleep(6);
		String expected_notes = dr.findElement(By.xpath("//*[@id=\"kt_wrapper\"]/div[2]/app-landing-dashboard/div/div/sub-header/div/div/div[1]/h5")).getText();
	    String current_notes ="Dashboard";
	    assertEquals(current_notes,expected_notes);

	}

	@Then ("I should see in the notes table a column with a header of 'Title'")
	public void column_table_with_header_title() throws Throwable {
		String current_title = dr.findElement(By.xpath("//*[@id=\"pr_id_6\"]/div/table/thead/tr/th[2]")).getText();
		String expected_title = "TITLE";
		assertEquals(current_title,expected_title);
		System.out.println("TITLE validated on code => "+current_title);
	}
	
	@And ("I should see all notes to complete under the 'Title' column")
	public void all_notes_to_complete() throws Throwable {
		String current_notes = dr.findElement(By.cssSelector("#kt_wrapper > div:nth-child(2) > app-landing-dashboard > div > div > div > note-list > div > div.card-header.align-items-center.border-0 > h2 > span")).getText();
		String expected_notes = "My Notes";
		assertEquals(current_notes,expected_notes);
		String first_note = dr.findElement(By.cssSelector("#pr_id_6 > div > table > tbody > tr:nth-child(1) > td:nth-child(2)")).getText();
		//System.out.println("First Note => "+first_note);
		Boolean fn = first_note.isEmpty();
		assertFalse("Notes column not filled",fn);
		System.out.println("Notes under TITLE column validated on code");
	}
	
	@And ("I should see notes as their 'activity name'")
	public void notes_as_their_activity_name() throws Throwable {
		String column = dr.findElement(By.cssSelector("#pr_id_6 > div > table > tbody > tr:nth-child(1) > td:nth-child(2)")).getText();
		Boolean st1 = column.isEmpty();
		assertFalse("Activity name field not filled",st1);
		System.out.println("Activity name validated on code");
	}
	
	@And ("I should not see any notes that have a status of 'completed'")
	public void status_not_complete()  throws Throwable {
		
		//Count the number of rows
		List<WebElement> rows = dr.findElements(By.xpath("//*[@id=\"pr_id_6\"]/div/table/tbody/tr"));
		int rows_number = rows.size();
		//System.out.println("Total rows : "+rows_number);
		
		//Count the number os columns
		//List<WebElement> cols = dr.findElements(By.xpath("//*[@id=\"pr_id_6\"]/div/table/tbody/tr[1]/td"));
		//int cols_number = cols.size();
		//System.out.println("Total cols : "+cols_number);
		
		for (int i=1; i<=rows_number; i++){
			String status = dr.findElement(By.xpath("//*[@id=\"pr_id_6\"]/div/table/tbody/tr["+i+"]/td[5]")).getText();
			try {
				switch (status) {
				  case "In Progress":
				    break;
				    
				  case "Not Started":
				    break;
				    
				  case "Returned":
				    break;
				    
				  default:
						throw new Exception("Invalid status => "+status);
				} 
			}
			catch (Exception e) {
					throw new IOException(e.getMessage());
			}

		}
	}
	
	@And ("if I click on the arrow I should see that the notes are sorted by first letter of first word of activity name")
	public void sort_alphabetical_order()  throws Throwable {
		//Count the number of rows
		List<WebElement> rows_activities = dr.findElements(By.xpath("//*[@id=\"pr_id_6\"]/div/table/tbody/tr"));
		int rows__activities_number = rows_activities.size();
		TimeUnit.SECONDS.sleep(2);
		
		//Click on the arrow to sort
		dr.findElement(By.xpath("//*[@id=\"pr_id_6\"]/div/table/thead/tr/th[2]/p-sorticon")).click();

		//Double click on the arrow to sort in descendant order
		//Actions action = new Actions(dr);
		//WebElement btnElement = dr.findElement(By.xpath("//*[@id=\"pr_id_6\"]/div/table/thead/tr/th[2]/p-sorticon"));
		//action.doubleClick(btnElement).build().perform();
		
		char charArray[] = new char[rows__activities_number];
		

		//Start from one because there is no line number zero (see xpath below)
		for (int i=1; i<=charArray.length; i++){
			String activites_name = dr.findElement(By.xpath("//*[@id=\"pr_id_6\"]/div/table/tbody/tr["+i+"]/td[2]")).getText();
			//Fill array from zero position
			charArray[i-1] = activites_name.charAt(0);

	    }
		
		for (int i = 0; i < charArray.length-1; i++) {
		    if (charArray[i] > charArray[i + 1]) {
		    	throw new Exception("It isn't sorted alphabetical order");
		    }
		    //System.out.println("First letter : "+charArray[i]);
		}
		
		
		//To validate the code above, double click on sort arrow and it will display descendant and throw expection
		/**************************************** How to double click ************************************************
		Actions action = new Actions(dr);
		WebElement btnElement = dr.findElement(By.xpath("//*[@id=\"pr_id_6\"]/div/table/thead/tr/th[2]/p-sorticon"));
		action.doubleClick(btnElement).build().perform();
		**************************************************************************************************************/
	}
		
    @After //tearDown() (close browser)
    public void tearDown() throws Exception {
    	 TimeUnit.SECONDS.sleep(3);
        System.out.println("Running tearDown ...");
        dr.close();
    }
}
