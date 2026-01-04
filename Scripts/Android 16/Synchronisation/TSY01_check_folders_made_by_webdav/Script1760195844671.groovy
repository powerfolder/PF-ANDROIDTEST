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
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

// get info about qa-system
CustomKeywords.'utils.Startup_app.loadCredsIntoGlobals'('katalon.txt')

// start or install package
CustomKeywords.'utils.Startup_app.install'(GlobalVariable.AppName)

// proceed login when not logged in
if (Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 5, FailureHandling.OPTIONAL)) {
    CustomKeywords.'utils.Process_login.login'(GlobalVariable.ServerURL, GlobalVariable.userid, GlobalVariable.password)
}

Mobile.delay(3)

// start webdav connection
String base = GlobalVariable.WebdavURL

String user = GlobalVariable.userid

String pass = GlobalVariable.password

// create topvlv folder via webdav
String folderName_webdav = getRandomFolderName()

CustomKeywords.'utils.WebDav.createFolder'(base, folderName_webdav, user, pass)
Mobile.delay(3)

// sync via home button
Mobile.tap(findTestObject('LoginScreen/HomeIcon'), 30)
Mobile.delay(10)

// verify toplvl folder is shown
TestObject top_folder_obj = new TestObject()
top_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + folderName_webdav + "']")
Mobile.verifyElementExist(top_folder_obj, 5)

// tab on toplvl folder
Mobile.delay(2)
Mobile.tap(top_folder_obj, 5)

// tab on fab_button - plus-button
Mobile.delay(3)
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(3)

// tab on menu-entry New-Directory to start Toplvl-folder-creation dialog
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)

// create foldername based on timestamp
String timestamp_folder = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
String folderName = 'Folder_' + timestamp_folder
Mobile.delay(3)

Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), folderName, 30)
Mobile.delay(2)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)
Mobile.delay(3)

// sync via swipe down
Mobile.swipe(200, 400, 200, 1100)

// prepair for renaming-sync test
String folderName_base = "TSY01_renamed_"
String folderName_webdav_org = folderName_base + "1"
String folderName_webdav_renamed = ""
println("Initial folder: " + folderName_webdav_org)

// rename folder 10x and check each time if renamed folder is shown
for (int i = 1; i <= 7; i++) {

    folderName_webdav_renamed = folderName_base + i
    println("Loop #" + i + " → rename " + folderName_webdav_org + " → " + folderName_webdav_renamed)

    if (i == 1) {
        CustomKeywords.'utils.WebDav.renameOrMove'(
            base,
            folderName_webdav + '/' + folderName,
            folderName_webdav + '/' + folderName_webdav_renamed,
            user,
            pass,
            true
        )
    } else {
        CustomKeywords.'utils.WebDav.renameOrMove'(
            base,
            folderName_webdav + '/' + folderName_webdav_org,
            folderName_webdav + '/' + folderName_webdav_renamed,
            user,
            pass,
            true
        )
    }
	Mobile.delay(10)
    // sync via pull down
    Mobile.swipe(200, 400, 200, 1100)
    Mobile.delay(2)
	Mobile.swipe(200, 400, 200, 1100)
	
    // verify if folder is shown
    TestObject top_folder_renamed_obj = new TestObject()
    top_folder_renamed_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + folderName_webdav_renamed + "']")
    Mobile.verifyElementExist(top_folder_renamed_obj, 5)
	folderName_webdav_org = folderName_webdav_renamed
}

// return to root
Mobile.tap(findTestObject('LoginScreen/HomeIcon'), 30)

// delete toplvl folder
CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)

//logout and close app
WebUI.callTestCase(findTestCase('Android 16/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)

String getRandomFolderName() {
    String folderName = 'TSY01_' + getTimestamp()

    return folderName
}

String getTimestamp() {
    Date todaysDate = new Date()

    String formattedDate = todaysDate.format('ddMMMyyyyhhmmss')

    return formattedDate
}

