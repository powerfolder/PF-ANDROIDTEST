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

// get info about qa-system
CustomKeywords.'utils.Startup_app.loadCredsIntoGlobals'("katalon.txt")

// start up app
//CustomKeywords.'utils.Startup_app.install'()
CustomKeywords.'utils.Startup_app.install'(GlobalVariable.AppName)

// proceed login not logged in
if (Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL)) {
	CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL,GlobalVariable.userid, GlobalVariable.password)
}

//Verify search with invalid input
Mobile.tap(findTestObject('Search/SearchBtn'), 30)
Mobile.setText(findTestObject('Search/SearchInput'), 'InvalidSearch', 30)
Mobile.delay(5)
Mobile.pressKeyCode('ENTER', FailureHandling.CONTINUE_ON_FAILURE)
Mobile.delay(3)
// verifying no result found text
Mobile.verifyElementExist(findTestObject('Search/VerifyNoResultFoundText'), 10)
Mobile.delay(3)

Mobile.tap(findTestObject('Search/BackButtonSearch'), 30)
Mobile.delay(2)

//logout and close app
WebUI.callTestCase(findTestCase('Android 14/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)