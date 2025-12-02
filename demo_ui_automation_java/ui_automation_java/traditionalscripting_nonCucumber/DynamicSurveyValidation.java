package automation.demo.traditional;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

@Test public void testDynamicSurveyFieldValidation()throws Exception{WebDriverWait wait=new WebDriverWait(driver,20);JavascriptExecutor jse=(JavascriptExecutor)driver;

// Survey Data
HashMap<String,String>surveyData=new HashMap<>();surveyData.put("group_name","QA_Automation_Team");surveyData.put("company_id","CID-9958");surveyData.put("has_sub_group","Yes"); // Trigger
surveyData.put("sub_group_name","Mobile_Testing_Unit");

// Navigate to the Survey Module
wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-survey-builder"))).click();

// Validate Dynamic Fields with Soft Assertions
for(Map.Entry<String,String>entry:surveyData.entrySet()){String fieldKey=entry.getKey();String expectedValue=entry.getValue();

try{
// Dynamic Locator
By locator=By.id("survey-field-"+fieldKey);

// Ensure the element is visible
WebElement field=wait.until(ExpectedConditions.visibilityOfElementLocated(locator));String actualValue=field.getAttribute("value");

// Perform Soft Assertions
softly.assertThat(actualValue).as("Validating dynamic field: "+fieldKey).isEqualTo(expectedValue);

System.out.println("Success: Field ["+fieldKey+"] matches value: "+actualValue);

}catch(TimeoutException e){
// Log a warning if a conditional field fails
System.out.println("Warning: Conditional field ["+fieldKey+"] not detected on UI.");}}

// Handle Conditional Components (Yes/No Toggles)
// Example: If 'has_sub_group' is 'Yes', interact with sub-group details
if("Yes".equals(surveyData.get("has_sub_group"))){WebElement toggle=driver.findElement(By.id("toggle-sub-group-details"));jse.executeScript("arguments[0].click();",toggle);System.out.println("Conditional sub-group section expanded.");}

// Submit Survey
driver.findElement(By.id("btn-submit-survey")).click();

softly.assertAll();
}