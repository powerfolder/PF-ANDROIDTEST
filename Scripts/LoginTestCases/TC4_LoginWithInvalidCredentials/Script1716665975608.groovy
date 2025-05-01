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
	if(!(Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL))) {
		logout()
	}
	
	// enter server URL
	Mobile.tap(findTestObject('LoginScreen/ServerURL'),30)
	Mobile.setText(findTestObject('LoginScreen/enterServerURL'), GlobalVariable.ServerURL, 30)
	Mobile.tap(findTestObject('LoginScreen/ServerURL'),30)
}

// enter inavlid or wrong password
Mobile.setText(findTestObject('LoginScreen/EnterEmail'), GlobalVariable.userid, 30)
Mobile.setText(findTestObject('LoginScreen/InputPassword'), 'WrongPassword', 30)
Mobile.tap(findTestObject('LoginScreen/LoginButton'), 45)

// verify error message as Incorrect username or password
String toastMessage= Mobile.getText(findTestObject('LoginScreen/incorrectToastMessage'), 45)
Mobile.verifyEqual(toastMessage, 'Incorrect username or password')

// closing application
Mobile.closeApplication() 


def  logout() {
	Mobile.tap(findTestObject('MainScreen/ThreeDots'), 45)
	Mobile.tap(findTestObject('ThreeDotsMenu/MyAccount'), 45)
	Mobile.delay(5)
	Mobile.tap(findTestObject('Settings/LogoutButton'), 45)
	String confirmationMessage= Mobile.getText(findTestObject('Settings/logoutConfirmationMessage'), 30)
	Mobile.tap(findTestObject('Settings/LogoutConfirmationYes'), 30)
	Mobile.delay(5)
	Mobile.verifyEqual(confirmationMessage, 'Do you really want to log out and remove all user data?')
}
