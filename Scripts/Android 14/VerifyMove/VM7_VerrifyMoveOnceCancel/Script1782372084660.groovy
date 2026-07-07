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

// tap on first toplvl folder
Mobile.delay(2)
Mobile.tap(top_folder_obj, 5)

// Click on plus icon and verify upload here
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(6)

Mobile.verifyElementExist(findTestObject('PlusIconMenus/UploadHere'), 10)
Mobile.tap(findTestObject('PlusIconMenus/UploadHere'), 30)

Mobile.delay(3)

// Selecting file from the device
Mobile.tap(findTestObject('CreateNewFile/SelectUploadPDFFileFromDevice'), 30)
Mobile.delay(10)

// verifying file upload
TestObject file_obj = new TestObject()
file_obj.addProperty("xpath", ConditionType.EQUALS, "//*[contains(@text,'PDFViewer')]")
Mobile.verifyElementExist(file_obj, 5)

// Swap left to right for move
CustomKeywords.'utils.SwipeLeftToRight.swipe'(file_obj)

// Tap on move icon
Mobile.tap(findTestObject('SwipeMove/MoveIcon'), 30)

// click to cancel move button
Mobile.tap(findTestObject('SwipeMove/MoveCancelButton'), 30)
Mobile.delay(2)

// Swap left to right to move 
CustomKeywords.'utils.SwipeLeftToRight.swipe'(file_obj)
Mobile.tap(findTestObject('SwipeMove/MoveIcon'), 30)

// press back
Mobile.pressBack()

// Tap to open second top folder
Mobile.tap(second_top_folder_obj, 5)

// Tap on move button
Mobile.tap(findTestObject('SwipeMove/MoveButton'), 30)
Mobile.delay(5)

// Verify file after move
Mobile.verifyElementExist(file_obj, 5)

// go to home - toplvl
Mobile.tap(findTestObject('LoginScreen/HomeIcon'), 30)
Mobile.delay(2)

// delete created toplvl-folder with presentation inside
CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)

// delete created secont toplvl-folder with presentation inside
CustomKeywords.'utils.Delete_object.swipeAndDelete'(second_top_folder_obj)

// logout and close app
WebUI.callTestCase(findTestCase('Android 14/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)