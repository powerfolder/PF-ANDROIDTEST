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
// start up app
//CustomKeywords.'utils.Startup_app.install'()
CustomKeywords.'utils.Startup_app.install'(GlobalVariable.AppName)

// proceed login not logged in
if (Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL)) {
	loginScreenVersion = Mobile.getText(findTestObject('LoginScreen/GetAppVersion'),30)
	CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL,GlobalVariable.userid, GlobalVariable.password)
}

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
Mobile.pressBack()

//logout and close app
WebUI.callTestCase(findTestCase('Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)