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

if(GlobalVariable.isExistingApp) {
Mobile.startExistingApplication('de.goddchen.android.powerfolder.A', FailureHandling.STOP_ON_FAILURE)
} else {
	String applocation = RunConfiguration.getProjectDir()+'/apks/'+GlobalVariable.AppName;
	System.out.println("Applocation"+ applocation)
	Mobile.startApplication(applocation, false, FailureHandling.CONTINUE_ON_FAILURE)
	Mobile.delay(5)
	if((Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL))) {
		login()
	}
	// click on home icon button
	Mobile.tap(findTestObject('LoginScreen/HomeIcon'),30)
	Mobile.delay(4)}
Mobile.tap(findTestObject('Folder_Menu/ClickOnFolder'), 30)

Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.plusIcontapX,GlobalVariable.plusIcontapY)
Mobile.verifyElementExist(findTestObject('PlusIconMenus/NewDirectory'),10)
Mobile.delay(3)
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'),30)
Mobile.verifyElementExist(findTestObject('Folder_Menu/CreateFolderPopUpHeader'),5)
Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), "Test Folder", 30)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'),30)
Mobile.delay(5)

// Verifying folder name 
String getFolderName= Mobile.getText(findTestObject('Folder_Menu/VerifyCreatedFolderName'), 30)
Mobile.verifyEqual(getFolderName, 'Test Folder')
Mobile.delay(5)
Mobile.tap(findTestObject('Folder_Menu/Button_Dropdown'), 30)
Mobile.tap(findTestObject('SwipeElements/RenameIcon'), 30)
Mobile.delay(3)
Mobile.tap(findTestObject('SwipeElements/CrossIconRenameTab'), 30)
Mobile.delay(3)
Mobile.setText(findTestObject('SwipeElements/EnterNewNameField'), "Rename Folder", 30)
Mobile.tap(findTestObject('SwipeElements/SaveButton'), 30)
Mobile.delay(5)

// Verify folder name as expected after renamed 
String getRenameFolder= Mobile.getText(findTestObject('Folder_Menu/VerifyRenamedFolder'), 30)
Mobile.verifyEqual(getRenameFolder, 'Rename Folder')

// delete created file by dropdown menu
Mobile.delay(3)
Mobile.tap(findTestObject('Folder_Menu/Button_Dropdown'), 30)
Mobile.tap(findTestObject('SwipeElements/DeleteIcon'), 30)
Mobile.tap(findTestObject('SwipeElements/YesButton'), 30)
Mobile.delay(3)

// verifying delete alert message
String alertMsg = Mobile.getText(findTestObject('SwipeElements/DeleteAlertMsg'), 30)
if (alertMsg.contains('Deleted')) {
	println(alertMsg)
}else {
	print('File not deleted')
}

//logout and close app
WebUI.callTestCase(findTestCase('Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)

def login() {
	Mobile.tap(findTestObject('LoginScreen/ServerURL'),30)
	Mobile.setText(findTestObject('LoginScreen/enterServerURL'), GlobalVariable.ServerURL, 30)
	Mobile.tap(findTestObject('LoginScreen/ServerURL'),30)
	Mobile.setText(findTestObject('LoginScreen/EnterEmail'), GlobalVariable.userid, 30)
	Mobile.setText(findTestObject('LoginScreen/InputPassword'), GlobalVariable.password, 30)
	Mobile.hideKeyboard()
	Mobile.tap(findTestObject('LoginScreen/LoginButton'), 45)
	Mobile.delay(3)
}