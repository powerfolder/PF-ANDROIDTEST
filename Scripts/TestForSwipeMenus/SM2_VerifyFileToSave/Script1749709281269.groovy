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
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import org.openqa.selenium.Keys as Keys
import java.text.SimpleDateFormat
import java.util.Date
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

Mobile.tap(findTestObject('Folder_Menu/ClickOnFolder'), 30)
Mobile.delay(3)

// Create new text file with the help of plus icon coordinates
Mobile.tapAtPosition(GlobalVariable.plusIcontapX , GlobalVariable.plusIcontapY)
Mobile.delay(3)
Mobile.verifyElementExist(findTestObject('PlusIconMenus/NewTextFile'),10)
Mobile.tap(findTestObject('PlusIconMenus/NewTextFile'), 30)
Mobile.delay(3)

// Create new text file and verify .txt extension
Mobile.verifyElementExist(findTestObject('CreateNewFile/CreateNewFilePopUpHeader'), 10)
Mobile.tap(findTestObject('CreateNewFile/CreateNewFileNameField'), 30)
String randomDocName = "TestDoc_ " + System.currentTimeMillis()
Mobile.setText(findTestObject('CreateNewFile/CreateNewFileNameField'), randomDocName, 30)
Mobile.tap(findTestObject('CreateNewFile/ClickOnOkButton'),30)
Mobile.delay(10)
Mobile.tap(findTestObject('VerifyCreatedFileNames/CloseButton'),30)
Mobile.delay(5)

// Create TestObject using dynamic document name
TestObject testFolder = new TestObject()
testFolder.addProperty("xpath", ConditionType.EQUALS,
	"//*[@class = 'android.widget.TextView' and (@text = '${randomDocName}.txt'  or . = '${randomDocName}.txt')]")

// Get element position and size
int startX = Mobile.getElementLeftPosition(testFolder, 10)
int startY = Mobile.getElementTopPosition(testFolder, 10)
int elementWidth = Mobile.getElementWidth(testFolder, 10)
int elementHeight = Mobile.getElementHeight(testFolder, 10)

// Calculate common swipe center
int centerY = startY + (elementHeight / 2)

// ================== Swipe Left to Right (for Save action) ==================
int leftToRight_FromX = startX + 10
int leftToRight_ToX = startX + elementWidth - 10

Mobile.swipe(leftToRight_FromX, centerY, leftToRight_ToX, centerY)
Mobile.delay(1)

// Verify and tap Save icon
Mobile.verifyElementExist(findTestObject('SwipeElements/SaveIcon'), 10)
Mobile.tap(findTestObject('SwipeElements/SaveIcon'), 30)

// Verify alert message for Save
Mobile.delay(5)
/*String alertTextForSaveFile = Mobile.getText(findTestObject('SwipeElements/VerifySaveFileAlertMsg'), 30)
Mobile.verifyEqual(alertTextForSaveFile, 'File successfully saved to "Downloads"')*/

Mobile.closeNotifications()
Mobile.delay(5)
// ================== Swipe Right to Left (for Delete action) ==================
int rightToLeft_FromX = startX + elementWidth - 10
int rightToLeft_ToX = startX + 10

Mobile.swipe(rightToLeft_FromX, centerY, rightToLeft_ToX, centerY)
Mobile.delay(2)

// Tap Delete icon and confirm
Mobile.swipe(rightToLeft_FromX, centerY, rightToLeft_ToX, centerY)
Mobile.tap(findTestObject('SwipeElements/DeleteIcon'), 30)
Mobile.tap(findTestObject('SwipeElements/YesButton'), 30)
Mobile.delay(3)

// Verify Delete alert message
String alertMsg = Mobile.getText(findTestObject('SwipeElements/DeleteAlertMsg'), 30)
if (alertMsg.contains('Deleted')) {
	println(alertMsg)
} else {
	println('File not deleted')
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