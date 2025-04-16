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
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import com.kms.katalon.core.configuration.RunConfiguration

// Generate dynamic file name
String randomName = "A" + System.currentTimeMillis()

// Start application logic
if (GlobalVariable.isExistingApp) {
    Mobile.startExistingApplication('de.goddchen.android.powerfolder.A', FailureHandling.STOP_ON_FAILURE)
} else {
    String applocation = RunConfiguration.getProjectDir() + '/apks/' + GlobalVariable.AppName
    Mobile.startApplication(applocation, false, FailureHandling.CONTINUE_ON_FAILURE)
    Mobile.delay(5)

    if (Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL)) {
        login()
    }

    Mobile.tap(findTestObject('LoginScreen/HomeIcon'), 30)
    Mobile.delay(3)
}

// Rename flow
Mobile.tap(findTestObject('Folder_Menu/ClickOnFolder'), 30)
Mobile.delay(5)
Mobile.swipe(568, 351, 140, 351)

Mobile.tap(findTestObject('SwipeElements/RenameIcon'), 30)
Mobile.delay(3)
Mobile.tap(findTestObject('SwipeElements/CrossIconRenameTab'), 30)
Mobile.delay(3)
Mobile.setText(findTestObject('SwipeElements/EnterNewNameField'), randomName, 30)
Mobile.tap(findTestObject('SwipeElements/SaveButton'), 30)
Mobile.delay(5)

/*// Create dynamic XPath
String dynamicXpath = "//*[@class = 'android.widget.TextView' and (@text = '${randomName}' or . = '${randomName}')]"

// Create dynamic TestObject
TestObject dynamicFileObject = new TestObject("dynamicFile")
dynamicFileObject.addProperty("xpath", ConditionType.EQUALS, dynamicXpath)

// Get text and assert
String getFolderName = Mobile.getText(dynamicFileObject, 30)
Mobile.verifyEqual(getFolderName, randomName)

Mobile.delay(5)

// Delete the file to clean up
Mobile.tap(findTestObject('SwipeElements/RenameFileDropdown'), 30)
Mobile.tap(findTestObject('SwipeElements/DeleteIcon'), 30)
Mobile.tap(findTestObject('SwipeElements/YesButton'), 30)*/

Mobile.closeApplication()

// Login method
def login() {
    Mobile.tap(findTestObject('LoginScreen/ServerURL'), 30)
    Mobile.setText(findTestObject('LoginScreen/enterServerURL'), GlobalVariable.ServerURL, 30)
    Mobile.tap(findTestObject('LoginScreen/ServerURL'), 30)
    Mobile.setText(findTestObject('LoginScreen/EnterEmail'), GlobalVariable.userid, 30)
    Mobile.setText(findTestObject('LoginScreen/InputPassword'), GlobalVariable.password, 30)
    Mobile.hideKeyboard()
    Mobile.tap(findTestObject('LoginScreen/LoginButton'), 45)
    Mobile.delay(3)
}
