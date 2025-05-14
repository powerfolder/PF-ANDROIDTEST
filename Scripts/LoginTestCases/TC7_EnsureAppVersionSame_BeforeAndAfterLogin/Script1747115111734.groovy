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
import com.kms.katalon.entity.global.GlobalVariableEntity
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.configuration.RunConfiguration

String loginScreenVersion = ''
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
	
	//Get app version on Login screen
	Mobile.delay(3)
	loginScreenVersion = Mobile.getText(findTestObject('LoginScreen/GetAppVersion'),30)

	// click on enter server URL
	Mobile.tap(findTestObject('LoginScreen/ServerURL'),30)
	Mobile.setText(findTestObject('LoginScreen/enterServerURL'), GlobalVariable.ServerURL, 30)
	Mobile.tap(findTestObject('LoginScreen/ServerURL'),30)
}

// enter login credentials
Mobile.setText(findTestObject('LoginScreen/EnterEmail'), GlobalVariable.userid, 30)
Mobile.setText(findTestObject('LoginScreen/InputPassword'), GlobalVariable.password, 30)
Mobile.hideKeyboard()
Mobile.tap(findTestObject('LoginScreen/LoginButton'), 45)
Mobile.delay(3)

//Get app version in my account menu
Mobile.tap(findTestObject('MainScreen/ThreeDots'), 45)
Mobile.tap(findTestObject('ThreeDotsMenu/MyAccount'), 45)
Mobile.delay(1)

//Get app version from my account menu
String myAccountVersion = Mobile.getText(findTestObject('ThreeDotsMenu/GetAppVersion'),30)

// Clean versions to ensure consistent format
loginScreenVersion = loginScreenVersion.replace("v", "").trim()
myAccountVersion = myAccountVersion.replace("v", "").trim()

// Log both versions
println("Cleaned Login Version: " + loginScreenVersion)
println("Cleaned MyAccount Version: " + myAccountVersion)

// verify version match
Mobile.verifyMatch(loginScreenVersion, myAccountVersion, false)

// clossing application
Mobile.closeApplication()

def  logout() {
	Mobile.tap(findTestObject('MainScreen/ThreeDots'), 45)
	Mobile.tap(findTestObject('ThreeDotsMenu/MyAccount'), 45)
	Mobile.delay(5)
	Mobile.tap(findTestObject('Settings/LogoutButton'), 45)
	String confirmationMessage= Mobile.getText(findTestObject('Settings/logoutConfirmationMessage'), 30)
	Mobile.tap(findTestObject('Settings/LogoutConfirmationYes'), 30)
	Mobile.delay(3)
	Mobile.verifyEqual(confirmationMessage, 'Do you really want to log out and remove all user data?')
}
