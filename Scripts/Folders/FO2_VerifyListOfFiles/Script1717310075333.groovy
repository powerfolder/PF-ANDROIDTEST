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

// tab on toplvl folder
Mobile.delay(2)
Mobile.tap(top_folder_obj, 5)

// Get the current timestamp for unique naming
String timestamp_file = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()

// Generate names for different files
String presentationName = 'Presentation_' + timestamp_file
String docxName = 'Document_' + timestamp_file
String textFileName = 'TextFile_' + timestamp_file
String spreadsheetName = 'Spreadsheet_' + timestamp_file

// Open Plus Icon
Mobile.delay(4)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(6)

// Create Presentation File
Mobile.tap(findTestObject('PlusIconMenus/NewPresentation'), 30)
Mobile.verifyElementExist(findTestObject('CreateNewFile/CreateNewFilePopUpHeader'), 10)
Mobile.delay(2)
Mobile.tap(findTestObject('CreateNewFile/CreateNewFileNameField'), 30)
Mobile.setText(findTestObject('CreateNewFile/CreateNewFileNameField'), presentationName, 30)
Mobile.tap(findTestObject('CreateNewFile/ClickOnOkButton'), 30)
Mobile.delay(10)
Mobile.tap(findTestObject('VerifyCreatedFileNames/CloseButton'), 30)
Mobile.delay(5)

// Verify Presentation file exists
TestObject presentation_obj = new TestObject()
presentation_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + presentationName + ".pptx']")
Mobile.verifyElementExist(presentation_obj, 5)

// Reopen Plus Icon for the next file type
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(6)

// Create Word Document (.docx) File
Mobile.tap(findTestObject('PlusIconMenus/NewDocument'), 30)
Mobile.verifyElementExist(findTestObject('CreateNewFile/CreateNewFilePopUpHeader'), 10)
Mobile.delay(2)
Mobile.tap(findTestObject('CreateNewFile/CreateNewFileNameField'), 30)
Mobile.setText(findTestObject('CreateNewFile/CreateNewFileNameField'), docxName, 30)
Mobile.tap(findTestObject('CreateNewFile/ClickOnOkButton'), 30)
Mobile.delay(10)
Mobile.tap(findTestObject('VerifyCreatedFileNames/CloseButton'), 30)
Mobile.delay(5)

// Verify Document file exists
TestObject docx_obj = new TestObject()
docx_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + docxName + ".docx']")
Mobile.verifyElementExist(docx_obj, 5)

// Reopen Plus Icon for the next file type
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(6)

// Create Text File (.txt)
Mobile.tap(findTestObject('PlusIconMenus/NewTextFile'), 30)
Mobile.verifyElementExist(findTestObject('CreateNewFile/CreateNewFilePopUpHeader'), 10)
Mobile.delay(2)
Mobile.tap(findTestObject('CreateNewFile/CreateNewFileNameField'), 30)
Mobile.setText(findTestObject('CreateNewFile/CreateNewFileNameField'), textFileName, 30)
Mobile.tap(findTestObject('CreateNewFile/ClickOnOkButton'), 30)
Mobile.delay(10)
Mobile.tap(findTestObject('VerifyCreatedFileNames/CloseButton'), 30)
Mobile.delay(5)

// Verify Text file exists
TestObject textFile_obj = new TestObject()
textFile_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + textFileName + ".txt']")
Mobile.verifyElementExist(textFile_obj, 5)

// Reopen Plus Icon for the next file type
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(4)

// Create Spreadsheet (.xlsx) File
Mobile.tap(findTestObject('PlusIconMenus/NewSpreadsSheet'), 30)
Mobile.verifyElementExist(findTestObject('CreateNewFile/CreateNewFilePopUpHeader'), 10)
Mobile.delay(2)
Mobile.tap(findTestObject('CreateNewFile/CreateNewFileNameField'), 30)
Mobile.setText(findTestObject('CreateNewFile/CreateNewFileNameField'), spreadsheetName, 30)
Mobile.tap(findTestObject('CreateNewFile/ClickOnOkButton'), 30)
Mobile.delay(10)
Mobile.tap(findTestObject('VerifyCreatedFileNames/CloseButton'), 30)
Mobile.delay(5)

// Verify Spreadsheet file exists
TestObject spreadsheet_obj = new TestObject()
spreadsheet_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + spreadsheetName + ".xlsx']")
Mobile.verifyElementExist(spreadsheet_obj, 5)

// go to home - toplvl
Mobile.tap(findTestObject('LoginScreen/HomeIcon'),30)
Mobile.delay(2)

// delete created toplvl-folder with presentation inside
CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)

//logout and close app
WebUI.callTestCase(findTestCase('Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)
