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
import org.openqa.selenium.Keys as Keys
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

// tab on fab_button - plus-button
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(3)

// tab on menu-entry New-Directory to start Toplvl-folder-creation dialog
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

// delete created toplvl-folder with presentation inside
CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)

// verifying delete alert message
String alertMsg = Mobile.getText(findTestObject('SwipeElements/DeleteAlertMsg'), 30)
if (alertMsg.contains('Deleted')) {
	println(alertMsg)
}else {
	print('File not deleted')
}

//logout and close app
WebUI.callTestCase(findTestCase('Android 16/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)