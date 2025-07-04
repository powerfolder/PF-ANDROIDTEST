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
	Mobile.delay(3)}

//clicking on 2nd folder
Mobile.tap(findTestObject('ListContent/Second_folder'), 30)

// Click on plus icon button and select new presentation 
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.plusIcontapX , GlobalVariable.plusIcontapY)
Mobile.delay(3)
Mobile.verifyElementExist(findTestObject('PlusIconMenus/NewPresentation'), 10)
Mobile.tap(findTestObject('PlusIconMenus/NewPresentation'), 30)
Mobile.delay(3)

//Create and verify new presentation with .pptx extension
Mobile.verifyElementExist(findTestObject('CreateNewFile/CreateNewFilePopUpHeader'), 10)
Mobile.tap(findTestObject('CreateNewFile/CreateNewFileNameField'), 30)
Mobile.setText(findTestObject('CreateNewFile/CreateNewFileNameField'), "Test Document", 30)
Mobile.tap(findTestObject('CreateNewFile/ClickOnOkButton'),30)
Mobile.delay(10)
Mobile.tap(findTestObject('VerifyCreatedFileNames/CloseButton'),30)
Mobile.delay(5)
String getFolderName= Mobile.getText(findTestObject('VerifyCreatedFileNames/VerifyCreatedNewPresentationName'), 30)
Mobile.verifyEqual(getFolderName, 'Test Document.pptx')
Mobile.tap(findTestObject('VerifyCreatedFileNames/VerifyCreatedNewPresentationName'),30)

//click on edit button with help of coordinates
Mobile.delay(20)
Mobile.tapAtPosition(GlobalVariable.PresentationEditButtontapX,GlobalVariable.PresentationEditButtontapY)
Mobile.delay(3)
Mobile.verifyElementVisible(findTestObject('VerifyCreatedFileNames/SlideDropDown'), 10)

// Verifying style page
Mobile.tap(findTestObject('EditPresentationFile/Style'),30)
Mobile.delay(3)
Mobile.verifyElementExist(findTestObject('EditPresentationFile/HeaderOfStylePopup'), 10)
Mobile.tap(findTestObject('VerifyCreatedFileNames/CloseButton'),30)
Mobile.delay(5)

//Swipe to delete created docx.
//Mobile.swipe(402, 351, 140, 351)
Mobile.tap(findTestObject('Folder_Menu/Button_Dropdown'), 30)
Mobile.delay(1)
Mobile.tap(findTestObject('SwipeElements/DeleteIcon'), 30)
Mobile.tap(findTestObject('SwipeElements/YesButton'), 30)
Mobile.delay(1)

// Verifying delete alert message
String alertMsg = Mobile.getText(findTestObject('SwipeElements/DeleteAlertMsg'), 30)
if (alertMsg.contains('Deleted Test Document.pptx')) {
	println(alertMsg)
}else {
	print('text File not deleted')
}

// Closing application
Mobile.closeApplication()

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
