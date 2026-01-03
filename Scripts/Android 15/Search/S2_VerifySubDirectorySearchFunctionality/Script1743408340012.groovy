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
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.util.KeywordUtil

// get info about qa-system
CustomKeywords.'utils.Startup_app.loadCredsIntoGlobals'("katalon.txt")

// start up app
CustomKeywords.'utils.Startup_app.install'(GlobalVariable.AppName)

// proceed login not logged in
if (Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL)) {
	CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL,GlobalVariable.userid, GlobalVariable.password)
}

// tab on fab_button - plus-button
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(3)

// tab on menu-entry New-Directory to start Toplvl-folder-creation dialog
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)

// create foldername based on timestamp
String timestamp_folder = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
String folderName = 'Folder_' + timestamp_folder

Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), folderName, 30)
Mobile.delay(2)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)

// wait some seconds after setting up new toplvl folder
Mobile.delay(3)

// verifying folder is existing
TestObject top_folder_obj = new TestObject()
top_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + folderName + "']")
Mobile.verifyElementExist(top_folder_obj, 5)

// Tap on top-level folder to open
Mobile.delay(2)
Mobile.tap(top_folder_obj, 5)

// create 3 subfolders
List<String> createdFolders = []

for (int i = 1; i <= 3; i++) {
	Mobile.delay(3)

    String timestamp = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
    String subfolderName = "subFolder_${timestamp}_${i}"
    
    createdFolders.add(subfolderName)

    Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
    Mobile.delay(2)
    Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)
	Mobile.delay(3)
    Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), subfolderName, 30)
    Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)
}

for (String sfolderName : createdFolders) {
	TestObject folderObj = new TestObject()
	folderObj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='${sfolderName}']")
	Mobile.verifyElementExist(folderObj, 10)
	Mobile.delay(3)
}

//Verify search
Mobile.tap(findTestObject('Search/SearchBtn'), 30)
Mobile.setText(findTestObject('Search/SearchInput'), createdFolders[1], 30)
Mobile.delay(5)
Mobile.pressKeyCode('ENTER', FailureHandling.CONTINUE_ON_FAILURE)
Mobile.delay(3)
String FilterName = Mobile.getText(findTestObject('Search/SearchInput'), 30)
Mobile.delay(3)
String ListName = createdFolders[1]

if(FilterName != ListName) {
	KeywordUtil.markWarning("searched:" + FilterName + " and got " + ListName)
}

// leave searchbar
Mobile.pressBack()
Mobile.delay(2)
Mobile.pressBack()


//logout and close app
WebUI.callTestCase(findTestCase('Android 15/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)