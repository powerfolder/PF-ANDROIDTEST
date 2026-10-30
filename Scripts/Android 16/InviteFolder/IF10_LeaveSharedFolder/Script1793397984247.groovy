import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import utils.PowerFolderAPI

/*
 * PFM-788: leave a shared folder
 * 1. create a second user account via API
 * 2. login as standard user
 * 3. create a toplvl folder and share it (r/w) with the second user
 * 4. logout standard user
 * 5. login second user
 * 6. accept share
 * 7. create file in shared folder
 * 8. leave share - shared folder is no longer shown in folder list
 * 9. logout and relogin as standard user
 * 10. check that the file created by the second user is still present
 */

// get info about qa-system
CustomKeywords.'utils.Startup_app.loadCredsIntoGlobals'("katalon.txt")

String timestamp = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
String secondUser = 'leave' + System.currentTimeMillis() + '@qa-automated-mobiletest-A16.com'
String secondPassword = 'QaLeave#' + timestamp

// 1. create second user account via API
boolean accountCreated = CustomKeywords.'utils.WebDav.createAccount'(GlobalVariable.ApiURL, secondUser, secondPassword,
	GlobalVariable.ApiAccount, GlobalVariable.ApiPassword)
assert accountCreated : "Second account '${secondUser}' could not be created"

try {
	// start up app
	CustomKeywords.'utils.Startup_app.install'(GlobalVariable.AppName)

	// 2. login standard user if not logged in
	if (Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL)) {
		CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL, GlobalVariable.userid,
			GlobalVariable.password)
	}

	// 3. create toplvl folder
	Mobile.delay(3)
	Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
	Mobile.delay(3)
	Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)

	String folderName = 'Leave_' + timestamp
	Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), folderName, 30)
	Mobile.delay(2)
	Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)
	Mobile.delay(3)

	TestObject top_folder_obj = new TestObject()
	top_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + folderName + "']")
	Mobile.verifyElementExist(top_folder_obj, 5)

	// 3. share toplvl folder with second user, read/write is the preselected permission
	Mobile.tap(findTestObject('Folder_Menu/ShareIcon'), 45)
	Mobile.verifyElementExist(findTestObject('InviteFolder/InvitePopUpHeader'), 5)
	Mobile.tap(findTestObject('InviteFolder/Email_InputField'), 30)
	Mobile.setText(findTestObject('InviteFolder/Email_InputField'), secondUser, 30)
	Mobile.tap(findTestObject('InviteFolder/VerifyOkButton'), 30)
	String invitationText = Mobile.getText(findTestObject('InviteFolder/VerifyInvitationSentText'), 30)
	Mobile.verifyEqual(invitationText, 'Invitation sent')
	Mobile.delay(5)

	// check invitation on server
	PowerFolderAPI api = new PowerFolderAPI()
	def folderID = api.getFolderIdByName(GlobalVariable.ApiURL + '/folders', GlobalVariable.userid,
		GlobalVariable.password, folderName)
	assert folderID : "Folder '${folderName}' not found on server"
	assert api.isUserInvitedToFolder("${GlobalVariable.ApiURL}/invitations", folderID, GlobalVariable.userid,
		GlobalVariable.password, secondUser)

	// 4. logout standard user
	CustomKeywords.'utils.Process_login.logout'()

	// 5. login second user
	CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL, secondUser, secondPassword)
	Mobile.delay(3)

	// 6. accept share: tapping the invited folder opens the invitation dialog
	Mobile.verifyElementExist(top_folder_obj, 30)
	Mobile.tap(top_folder_obj, 30)
	Mobile.verifyElementExist(findTestObject('InviteFolder/AcceptInvitationButton'), 30)
	Mobile.tap(findTestObject('InviteFolder/AcceptInvitationButton'), 30)
	Mobile.delay(5)

	// 7. create text file inside the shared folder
	Mobile.verifyElementExist(top_folder_obj, 30)
	Mobile.tap(top_folder_obj, 30)
	Mobile.delay(4)

	String textFileName = 'text_' + CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
	Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
	Mobile.delay(6)
	Mobile.verifyElementExist(findTestObject('PlusIconMenus/NewTextFile'), 10)
	Mobile.tap(findTestObject('PlusIconMenus/NewTextFile'), 30)
	Mobile.verifyElementExist(findTestObject('CreateNewFile/CreateNewFilePopUpHeader'), 10)
	Mobile.delay(2)
	Mobile.tap(findTestObject('CreateNewFile/CreateNewFileNameField'), 30)
	Mobile.setText(findTestObject('CreateNewFile/CreateNewFileNameField'), textFileName, 30)
	Mobile.tap(findTestObject('CreateNewFile/ClickOnOkButton'), 30)
	Mobile.delay(10)
	Mobile.tap(findTestObject('VerifyCreatedFileNames/CloseButton'), 30)
	Mobile.delay(5)

	TestObject text_file_obj = new TestObject()
	text_file_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + textFileName + ".txt']")
	Mobile.verifyElementExist(text_file_obj, 10)

	// file must be stored in the folder of the owner
	String filePath = folderName + '/' + textFileName + '.txt'
	assert CustomKeywords.'utils.WebDav.exists'(GlobalVariable.WebdavURL, filePath, GlobalVariable.userid,
		GlobalVariable.password) : "File '${filePath}' not found on server"

	// 8. leave share
	Mobile.tap(findTestObject('LoginScreen/HomeIcon'), 30)
	Mobile.delay(2)
	CustomKeywords.'utils.Delete_object.swipeAndLeave'(top_folder_obj, folderName)

	// shared folder is no longer shown, also not after a refresh from server
	Mobile.verifyElementNotExist(top_folder_obj, 10)
	Mobile.swipe(200, 400, 200, 1100)
	Mobile.delay(5)
	Mobile.verifyElementNotExist(top_folder_obj, 10)

	// second user has no access on the server anymore
	assert !api.getFolderIdByName(GlobalVariable.ApiURL + '/folders', secondUser, secondPassword, folderName) :
		"Second user still has access to '${folderName}'"

	// 9. logout second user and relogin as standard user
	CustomKeywords.'utils.Process_login.logout'()
	CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL, GlobalVariable.userid, GlobalVariable.password)
	Mobile.delay(3)

	// 10. file created by the second user is still present
	Mobile.verifyElementExist(top_folder_obj, 30)
	Mobile.tap(top_folder_obj, 30)
	Mobile.delay(4)
	Mobile.verifyElementExist(text_file_obj, 30)
	assert CustomKeywords.'utils.WebDav.exists'(GlobalVariable.WebdavURL, filePath, GlobalVariable.userid,
		GlobalVariable.password) : "File '${filePath}' was removed from the owner's folder"

	// cleanup: delete toplvl folder
	Mobile.tap(findTestObject('LoginScreen/HomeIcon'), 30)
	Mobile.delay(2)
	CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)
} finally {
	// cleanup: remove second user account
	CustomKeywords.'utils.WebDav.deleteAccount'(GlobalVariable.ApiURL, secondUser, GlobalVariable.ApiAccount,
		GlobalVariable.ApiPassword)
}

// logout and close app
WebUI.callTestCase(findTestCase('Android 16/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)
