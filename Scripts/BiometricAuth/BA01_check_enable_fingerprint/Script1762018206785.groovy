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

// Requirement: Setup finderprint as shown in docu
// https://docs.katalon.com/katalon-studio/manage-projects/set-up-projects/mobile-testing/android/configure-fingerprint-setting-in-an-android-emulator

// start up app
CustomKeywords.'utils.Startup_app.install'()

// proceed login not logged in
if (Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL)) {
	CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL,GlobalVariable.userid, GlobalVariable.password)
}

// open up my account
Mobile.tap(findTestObject('MainScreen/ThreeDots'), 20)
Mobile.tap(findTestObject('ThreeDotsMenu/MyAccount'), 20)

Mobile.delay(5)

// check if settings for biometric options are available
Mobile.verifyElementExist(findTestObject('ThreeDotsMenu/Headline - Use BiometricFace ID protection'),5)
Mobile.verifyElementExist(findTestObject('ThreeDotsMenu/Description - BiometricFace'),5)

// enable fingerprint
Mobile.tap(findTestObject('ThreeDotsMenu/enable - BiometricFace'), 45)
Mobile.delay(5)
Mobile.useFingerprint(1)


// check if fingerprint is checked
TestObject msg = new TestObject()
msg.addProperty("xpath", ConditionType.EQUALS, "//*[contains(@text, 'protection enabled')]")
Mobile.verifyElementExist(msg, 10)

Mobile.delay(5)

// close app without logout
Mobile.closeApplication()