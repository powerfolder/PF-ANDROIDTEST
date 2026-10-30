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

// get info about qa-system
CustomKeywords.'utils.Startup_app.loadCredsIntoGlobals'("katalon.txt")

// start up app
CustomKeywords.'utils.Startup_app.install'(GlobalVariable.AppName)

// proceed login not logged in
if (Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL)) {
	CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL,GlobalVariable.userid, GlobalVariable.password)
}

def createFolder(String prefix) {

	// tap on + (fab) button
	Mobile.delay(3)
	Mobile.tapAtPosition(
		GlobalVariable.EMU_P8_plusIconTabX,
		GlobalVariable.EMU_P8_plusIconTabY
	)
	Mobile.delay(2)

	// tap on New Directory
	Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)

	// create folder name using timestamp
	String timestamp = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
	String folderName = prefix + timestamp

	// enter folder name and confirm
	Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), folderName, 30)
	Mobile.delay(1)
	Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)

	// wait for folder to appear
	Mobile.delay(3)

	// verify folder exists
	TestObject folderObj = new TestObject()
	folderObj.addProperty(
		"xpath",
		ConditionType.EQUALS,
		"//*[@text='" + folderName + "']"
	)
	Mobile.verifyElementExist(folderObj, 5)

	return folderName
}

String topFolder = createFolder("top_folder")
String sharedFolder = createFolder("shared_")
// tab on toplvl folder
Mobile.delay(2)
def tapOnFolder(String folderName) {
	
		// scroll in case folder is not visible
		Mobile.scrollToText(folderName)
	
		TestObject folderObj = new TestObject()
		folderObj.addProperty(
			"xpath",
			ConditionType.EQUALS,
			"//*[@text='" + folderName + "']"
		)
	
		Mobile.waitForElementPresent(folderObj, 10)
		Mobile.tap(folderObj, 10)
	}

// Create on plus icon and verify upload here
tapOnFolder(topFolder)
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(6)
Mobile.verifyElementExist(findTestObject('PlusIconMenus/UploadHere'), 10)
Mobile.tap(findTestObject('PlusIconMenus/UploadHere'), 30)
Mobile.delay(3)

// Selecting file from the device
Mobile.tap(findTestObject('CreateNewFile/SelectUploadFileFromDevice'), 30)
Mobile.delay(5)

// verifying file upload (filename needs to be screenshot.*)
TestObject file_obj = new TestObject()
file_obj.addProperty("xpath", ConditionType.EQUALS, "//*[contains(@text, 'Screenshot')]")
Mobile.verifyElementExist(file_obj, 5)

// sharing file after upload
Mobile.tap(findTestObject('ShareFile/ShareIcon'), 30)
Mobile.tap(findTestObject('ShareFile/SelectPowerFolderApp'), 30)

// go to home - toplvl
Mobile.tap(findTestObject('LoginScreen/HomeIcon'),30)

// tap on save-button
Mobile.tap(findTestObject('ShareFile/SaveButton'),30)

// verifying error popup msg
TestObject error_msg = new TestObject()
error_msg.addProperty("xpath", ConditionType.EQUALS, "//*[contains(@text, 'Select destination folder for upload')]")
Mobile.verifyElementExist(error_msg, 5)

// click on cancel button
Mobile.tap(findTestObject('ShareFile/CancelButton'),30)
Mobile.delay(2)

// startup app after close
Mobile.startExistingApplication('de.goddchen.android.powerfolder.A', FailureHandling.STOP_ON_FAILURE)
Mobile.delay(15)

// Recreate TestObject for top folder
TestObject top_folder_obj = new TestObject()
top_folder_obj.addProperty(
	"xpath",
	ConditionType.EQUALS,
	"//*[@text='" + topFolder + "']"
)

// Delete top folder
CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)

// Recreate TestObject for shared folder
TestObject shared_folder_obj = new TestObject()
shared_folder_obj.addProperty(
	"xpath",
	ConditionType.EQUALS,
	"//*[@text='" + sharedFolder + "']"
)

// Delete shared folder
CustomKeywords.'utils.Delete_object.swipeAndDelete'(shared_folder_obj)

//logout and close app
WebUI.callTestCase(findTestCase('Android 16/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)