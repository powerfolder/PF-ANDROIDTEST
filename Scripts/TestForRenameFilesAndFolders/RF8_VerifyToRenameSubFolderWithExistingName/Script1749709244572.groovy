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
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.configuration.RunConfiguration

// get info about qa-system
CustomKeywords.'utils.Startup_app.loadCredsIntoGlobals'("katalon.txt")

// Start up the app
CustomKeywords.'utils.Startup_app.install'(GlobalVariable.AppName)

// Proceed with login if not already logged in
if (Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL)) {
	CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL, GlobalVariable.userid, GlobalVariable.password)
}

// Tap on the FAB button - plus-button
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(3)

// Tap on menu-entry "New Directory" to start Toplvl-folder-creation dialog
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)

// Create folder name based on timestamp
String timestamp_folder = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
String folderName = 'Folder_' + timestamp_folder

Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), folderName, 30)
Mobile.delay(2)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)

// Wait a few seconds after setting up the new Toplvl folder
Mobile.delay(3)

// Verifying the folder exists
TestObject top_folder_obj = new TestObject()
top_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + folderName + "']")
Mobile.verifyElementExist(top_folder_obj, 5, FailureHandling.OPTIONAL)

// Tap on top-level folder to open
Mobile.delay(2)
Mobile.tap(top_folder_obj, 5)

// Tap on the FAB button - plus-button
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(3)

// Tap on menu-entry "New Directory" to start Toplvl-folder-creation dialog
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)

// Create folder name based on timestamp
String timestamp_subfolder = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
String subfolderName = 'subFolder_' + timestamp_subfolder

Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), subfolderName, 30)
Mobile.delay(2)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)

// Wait a few seconds after setting up the new Toplvl folder
Mobile.delay(3)

// Verifying the folder exists
TestObject sub_folder_obj = new TestObject()
sub_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + subfolderName + "']")
Mobile.verifyElementExist(sub_folder_obj, 5, FailureHandling.OPTIONAL)

// Create 2nd directory based on timestamp
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(3)

// Tap on menu-entry "New Directory" to start Toplvl-folder-creation dialog
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)
String timestamp_2subfolder = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
String secsubfolderName = 'secsubFolder_' + timestamp_2subfolder

Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), secsubfolderName, 30)
Mobile.delay(2)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)

// Verifying the secfolder exists
TestObject secsub_folder_obj = new TestObject()
secsub_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + secsubfolderName + "']")
Mobile.verifyElementExist(secsub_folder_obj, 5, FailureHandling.OPTIONAL)

Mobile.delay(5)

// Rename flow
// Swipe file to open rename icon
CustomKeywords.'utils.Swipe_object.swipe'(secsub_folder_obj)
Mobile.tap(findTestObject('SwipeElements/RenameIcon'), 30)
Mobile.delay(3)
Mobile.tap(findTestObject('SwipeElements/CrossIconRenameTab'), 30)
Mobile.delay(3)

// renaming file based on existing folder
Mobile.setText(findTestObject('SwipeElements/EnterNewNameField'), subfolderName, 30)
Mobile.tap(findTestObject('SwipeElements/SaveButton'), 30)

// Verifying alert message as Already exists
String getErrorALertMessage= Mobile.getText(findTestObject('SwipeElements/AlreadyExistAlertMsg'), 30)
Mobile.verifyEqual(getErrorALertMessage, 'Already exists')

Mobile.delay(5)
Mobile.pressBack()

// go to home - toplvl
Mobile.tap(findTestObject('LoginScreen/HomeIcon'),30)
Mobile.delay(2)

// delete created toplvl-folder with presentation inside
CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)

// Logout and close the app
WebUI.callTestCase(findTestCase('Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)