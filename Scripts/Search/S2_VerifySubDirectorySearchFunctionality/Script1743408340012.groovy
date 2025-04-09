import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.builtin.PressBackKeyword
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
	if(!(Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL))) {
		logout()
	}
	Mobile.tap(findTestObject('LoginScreen/ServerURL'),30)
	Mobile.setText(findTestObject('LoginScreen/enterServerURL'), GlobalVariable.ServerURL, 30)
	Mobile.tap(findTestObject('LoginScreen/ServerURL'),30)
}

Mobile.setText(findTestObject('LoginScreen/EnterEmail'), GlobalVariable.userid, 30)
Mobile.setText(findTestObject('LoginScreen/InputPassword'), GlobalVariable.password, 30)
Mobile.hideKeyboard()
Mobile.tap(findTestObject('LoginScreen/LoginButton'), 45)
Mobile.delay(3)
Mobile.tap(findTestObject('Folder_Menu/ClickSecondSubFloder'),30)
Mobile.delay(3)
Mobile.tapAtPosition(639, 1361)
Mobile.delay(3)
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'),30)
Mobile.delay(3)
Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), "Test Folder", 30)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'),30)

//Verify search
Mobile.tap(findTestObject('Search/SearchBtn'), 30)
Mobile.setText(findTestObject('Search/SearchInput'), 'Folder', 30)
Mobile.delay(5)
Mobile.pressKeyCode('ENTER', FailureHandling.CONTINUE_ON_FAILURE)
Mobile.delay(3)
String getFolderName= Mobile.getText(findTestObject('Folder_Menu/VerifyCreatedFolderName'), 30)
Mobile.verifyEqual(getFolderName, 'Test Folder')
Mobile.delay(5)
Mobile.swipe(300, 351, 140, 351)
Mobile.tap(findTestObject('SwipeElements/DeleteIcon'), 30)
Mobile.delay(3)
Mobile.tap(findTestObject('SwipeElements/YesButton'), 30)
Mobile.delay(3)
String alertMsg = Mobile.getText(findTestObject('SwipeElements/DeleteAlertMsg'), 30)
if (alertMsg.contains('Deleted')) {
	println(alertMsg)
}else {
	print('File not deleted')
}
Mobile.pressBack()
logout()

Mobile.closeApplication()


def  logout() {
	Mobile.delay(3)
	Mobile.tap(findTestObject('MainScreen/ThreeDots'), 45)
	Mobile.tap(findTestObject('ThreeDotsMenu/MyAccount'), 45)
	Mobile.delay(5)
	Mobile.tap(findTestObject('Settings/LogoutButton'), 45)
	String confirmationMessage= Mobile.getText(findTestObject('Settings/logoutConfirmationMessage'), 30)
	Mobile.tap(findTestObject('Settings/LogoutConfirmationYes'), 30)
	Mobile.delay(5)
	Mobile.verifyEqual(confirmationMessage, 'Do you really want to log out and remove all user data?')
}