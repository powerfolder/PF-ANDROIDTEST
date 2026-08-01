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

// tab on toplvl folder
Mobile.delay(2)
Mobile.tap(top_folder_obj, 5)

// Upload Screenshot1
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(6)
Mobile.verifyElementExist(findTestObject('PlusIconMenus/UploadHere'), 10)
Mobile.tap(findTestObject('PlusIconMenus/UploadHere'), 30)
Mobile.delay(3)

Mobile.tap(findTestObject('CreateNewFile/SelectUploadFileFromDevice'), 30)
Mobile.delay(20)

// Wait so the first file becomes older
Mobile.delay(65)

// Upload Screenshot2
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(6)
Mobile.tap(findTestObject('PlusIconMenus/UploadHere'), 30)
Mobile.delay(10)

Mobile.tap(findTestObject('CreateNewFile/SelectUploadSecFileFromDevice'), 30)
Mobile.delay(10)

// verifying file upload (filename needs to be screenshot.*)
TestObject file_obj = new TestObject()
file_obj.addProperty("xpath", ConditionType.EQUALS, "//*[contains(@text, 'Screenshot')]")
Mobile.verifyElementExist(file_obj, 5)

/*
// ==========================================
// Tap on sorting icon
// ==========================================
Mobile.tap(findTestObject('Shorting/ShortingButtonIcon'), 30)

Mobile.tap(findTestObject('Object Repository/Shorting/ShortByDateOld'), 30)

// Wait for sorting to complete
Mobile.delay(2)

// ==========================================
// Verify Date - Old Sorting
// ==========================================

AndroidDriver driver = MobileDriverFactory.getDriver()

List<WebElement> timeElements = driver.findElements(
	By.xpath("//android.widget.TextView[contains(@text,'changed')]")
)

assert timeElements.size() == 2 : "Expected 2 timestamp labels."

String firstTime = timeElements.get(0).getAttribute("text").toLowerCase()
String secondTime = timeElements.get(1).getAttribute("text").toLowerCase()

println("First Time  : " + firstTime)
println("Second Time : " + secondTime)

int firstValue
int secondValue

// First timestamp
if (firstTime.contains("just now")) {
	firstValue = 0
} else {
	def matcher = firstTime =~ /changed\s+(\d+)\s+minute/
	assert matcher.find() : "Unable to extract minutes from: ${firstTime}"
	firstValue = matcher.group(1).toInteger()
}

// Second timestamp
if (secondTime.contains("just now")) {
	secondValue = 0
} else {
	def matcher = secondTime =~ /changed\s+(\d+)\s+minute/
	assert matcher.find() : "Unable to extract minutes from: ${secondTime}"
	secondValue = matcher.group(1).toInteger()
}

println("First Value  : " + firstValue)
println("Second Value : " + secondValue)

// Date Old -> older file should appear first
assert firstValue > secondValue :
	   "Date Old sorting failed.\nFirst: ${firstTime}\nSecond: ${secondTime}"

println("Date Old Sorting verified successfully.")*/

// go to home - toplvl
Mobile.tap(findTestObject('LoginScreen/HomeIcon'),30)
Mobile.delay(2)

// Delete first folder
CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)

// Logout
WebUI.callTestCase(findTestCase('Android 14/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)