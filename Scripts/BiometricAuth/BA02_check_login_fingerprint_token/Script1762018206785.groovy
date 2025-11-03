

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
CustomKeywords.'utils.Startup_app.install'(GlobalVariable.AppName)

// check if fingerprint is needed
Mobile.verifyElementExist(findTestObject('BiometricAuth/Login - PowerFolder Biometric Sign On'),10)
Mobile.verifyElementExist(findTestObject('BiometricAuth/Login - Scan your fingerprint or face'),10)
Mobile.verifyElementExist(findTestObject('BiometricAuth/Login - FingerprintIcon'),10)

// tab on fingerprint to login
Mobile.useFingerprint(1)
Mobile.delay(5)

// open up my account
Mobile.tap(findTestObject('ThreeDotsMenu/MyAccount'), 45)
Mobile.delay(5)

// check username in my account to verify if we are logged in
Mobile.verifyElementExist(findTestObject('ThreeDotsMenu/Username - qapowerfolder.com'),10)

// close app without logout
Mobile.closeApplication()