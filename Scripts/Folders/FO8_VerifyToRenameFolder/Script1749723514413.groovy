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
	if((Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL))) {
		login()
	}
	// click on home icon button
	Mobile.tap(findTestObject('LoginScreen/HomeIcon'),30)
	Mobile.delay(4)}
Mobile.tap(findTestObject('Folder_Menu/ClickOnFolder'), 30)
Mobile.pressBack()
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.plusIcontapX,GlobalVariable.plusIcontapY)
Mobile.delay(3)
Mobile.verifyElementExist(findTestObject('PlusIconMenus/NewDirectory'),10)
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'),30)
Mobile.verifyElementExist(findTestObject('Folder_Menu/CreateFolderPopUpHeader'),5)
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

// Create new directory with the help of plus icon coordinates
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.plusIcontapX,GlobalVariable.plusIcontapY)
Mobile.delay(3)
Mobile.verifyElementExist(findTestObject('PlusIconMenus/NewDirectory'),10)
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'),30)
Mobile.verifyElementExist(findTestObject('Folder_Menu/CreateFolderPopUpHeader'),5)
Mobile.delay(1)
String folderName = "Test Folder " + System.currentTimeMillis()
Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), folderName, 30)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'),30)
Mobile.delay(5)

//Verifying new directory name
String expectedFolderName =   folderName
// Create dynamic TestObject for that folder name
TestObject dynamicFolder = new TestObject()
dynamicFolder.addProperty("xpath", ConditionType.EQUALS, "//*[@class = 'android.widget.TextView' and (@text = '${expectedFolderName}' or . = '${expectedFolderName}')]")
// Get the actual Folder name
String getFolderName = Mobile.getText(dynamicFolder, 30)
// Verify
Mobile.verifyEqual(getFolderName, expectedFolderName)

TestObject threeDot = new TestObject()
threeDot.addProperty("xpath", ConditionType.EQUALS,
	"//*[@class = 'android.widget.TextView' and (@text = '${folderName}'  or . = '${folderName}')]/following::android.widget.Image[@text='Context'][1]")

// Rename flow
Mobile.delay(1)
Mobile.tap(threeDot, 30)
Mobile.tap(findTestObject('SwipeElements/RenameIcon'), 30)
Mobile.delay(3)
Mobile.tap(findTestObject('SwipeElements/CrossIconRenameTab'), 30)
Mobile.delay(3)
// Rename the file
Mobile.setText(findTestObject('SwipeElements/EnterNewNameField'), "rename${folderName}", 30)
Mobile.tap(findTestObject('SwipeElements/SaveButton'), 30)
Mobile.delay(5)

// Define expected Folder name
String expectedRenamedFolderName = "rename${folderName}"

// Create a dynamic TestObject to locate the renamed file
TestObject renamedFolder = new TestObject()
renamedFolder.addProperty("xpath", ConditionType.EQUALS,
	"//*[@class = 'android.widget.TextView' and (@text = '${expectedRenamedFolderName}' or . = '${expectedRenamedFolderName}')]")

// Get the actual Folder name
String actualRenamedFolderName = Mobile.getText(renamedFolder, 30)

// Verify that the actual file name matches the expected one
assert actualRenamedFolderName == expectedRenamedFolderName : "Expected: ${expectedRenamedFolderName}, but found: ${actualRenamedFolderName}"

// Tap on the three-dot menu associated with the renamed file
TestObject renameFolderThreeDot = new TestObject()
renameFolderThreeDot.addProperty("xpath", ConditionType.EQUALS,
	"//*[@class = 'android.widget.TextView' and (@text = '${expectedRenamedFolderName}' or . = '${expectedRenamedFolderName}')]/following::android.widget.Image[@text='Context'][1]")
Mobile.tap(renameFolderThreeDot, 30)
Mobile.delay(1)

// Delete the created file
Mobile.tap(findTestObject('SwipeElements/DeleteIcon'), 30)
Mobile.tap(findTestObject('SwipeElements/YesButton'), 30)
Mobile.delay(1)

// verifying delete alert message
String alertMsg = Mobile.getText(findTestObject('SwipeElements/DeleteAlertMsg'), 30)
if (alertMsg.contains('Deleted')) {
	println(alertMsg)
}else {
	print('File not deleted')
}

// Logout and close app
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