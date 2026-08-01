import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import io.appium.java_client.AppiumDriver
import io.appium.java_client.remote.SupportsContextSwitching;
import io.appium.java_client.android.AndroidDriver
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

// get info about qa-system
CustomKeywords.'utils.Startup_app.loadCredsIntoGlobals'("katalon.txt")

// start up app
CustomKeywords.'utils.Startup_app.install'(GlobalVariable.AppName)

// proceed login not logged in
if (Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL)) {
	CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL, GlobalVariable.userid, GlobalVariable.password)
}

// tap on fab_button - plus-button
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(3)

// tap on menu-entry New-Directory to start Toplvl-folder-creation dialog
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)

// create foldername based on timestamp
String timestamp_folder = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
String folderName = 'Folder_' + timestamp_folder

Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), folderName, 30)
Mobile.delay(2)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)

// wait some seconds after setting up new toplvl folder
Mobile.delay(3)

// verifying folder is existing
TestObject top_folder_obj = new TestObject()
top_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + folderName + "']")
Mobile.verifyElementExist(top_folder_obj, 5)

// tap on fab_button - plus-button
Mobile.delay(5)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(3)

// tap on menu-entry New-Directory to start Toplvl-folder-creation dialog
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)

// create second folder name based on timestamp
String secFolderName = 'SecFolder_' + timestamp_folder

Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), secFolderName, 30)
Mobile.delay(2)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)

// wait some seconds after setting up second toplvl folder
Mobile.delay(3)

// verifying second folder is existing
TestObject second_top_folder_obj = new TestObject()
second_top_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + secFolderName + "']")
Mobile.verifyElementExist(second_top_folder_obj, 5)

// tap on shorting icon button 
Mobile.tap(findTestObject('Shorting/ShortingButtonIcon'), 30)

// ==========================================
// Tap on sorting icon
// ==========================================
Mobile.tap(findTestObject('Shorting/ShortingButtonIcon'), 30)

// Select A-Z sorting
Mobile.tap(findTestObject('Object Repository/Shorting/ShortByAToZ'), 30)

// Wait for sorting to complete
Mobile.delay(2)

// ==========================================
// Verify A-Z Sorting
// ==========================================

// Get Android driver
AndroidDriver driver = MobileDriverFactory.getDriver()

// Get all TextView elements displayed on screen
List<WebElement> elements = driver.findElements(By.className("android.widget.TextView"))

List<String> folderNames = []

// Collect only the folders created in this test
for (WebElement element : elements) {

	String text = element.getText()

	if (text != null && (text.equals(folderName) || text.equals(secFolderName))) {
		folderNames.add(text)
	}
}

// Print folders found
println("Folders Found : " + folderNames)

// Verify both folders are found
assert folderNames.size() == 2 : "Both created folders were not found on screen."

assert folderNames.get(0).equals(secFolderName) :
"Z-A sorting failed. Expected first folder: ${secFolderName}, Actual: ${folderNames.get(0)}"

assert folderNames.get(1).equals(folderName) :
"Z-A sorting failed. Expected second folder: ${folderName}, Actual: ${folderNames.get(1)}"

println("Z-A Sorting verified successfully.")

// Delete first folder
CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)

// Delete second folder
CustomKeywords.'utils.Delete_object.swipeAndDelete'(second_top_folder_obj)

// Logout
WebUI.callTestCase(findTestCase('Android 14/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)