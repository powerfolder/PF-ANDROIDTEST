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

// create toplvl folder
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)
Mobile.delay(3)

// tab on menu-entry New-Directory to start Toplvl-folder-creation dialog
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)

// create foldername 
String timestamp_folder = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
String folderName_app = getRandomFolderName()
Mobile.delay(3)

Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), folderName_app, 30)
Mobile.delay(2)
Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)
Mobile.delay(3)

// start webdav connection
String base = GlobalVariable.WebdavURL
String user = GlobalVariable.userid
String pass = GlobalVariable.password

// check present of toplvl folder made in app
boolean toplvl_present = CustomKeywords.'utils.WebDav.exists'(base, folderName_app, user, pass)
assert toplvl_present

// verify if folder is shown in app
TestObject top_folder_obj = new TestObject()
top_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + folderName_app + "']")
Mobile.verifyElementExist(top_folder_obj, 5)

// tab on toplvl folder
Mobile.delay(2)
Mobile.tap(top_folder_obj, 5)

// create subfolder via webdav
String timestamp_folder_1 = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
String folderName_webdav = 'Folder_' + timestamp_folder_1
CustomKeywords.'utils.WebDav.createFolder'(base, folderName_app + '/' + folderName_webdav, user, pass)
Mobile.delay(5)

// sync
Mobile.swipe(200, 400, 200, 1000)

// verify if folder is shown
TestObject sub_folder_obj = new TestObject()
sub_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + folderName_webdav + "']")
Mobile.verifyElementExist(top_folder_obj, 5)

// === Konfiguration ===
String folderName_base = "TSY02_renamed_"
String folderName_app_org = folderName_base + "1"    // Startordner
String folderName_app_renamed = ""

// === Rename-Schleife ===
for (int i = 1; i <= 7; i++) {

    int startX
    int endX
    int y

    // --- SWIPE nach links auf das aktuelle Element ---
	if (i == 1) {
		startX = Mobile.getElementLeftPosition(sub_folder_obj, 5) + 200
		endX   = Mobile.getElementLeftPosition(sub_folder_obj, 5) - 100
		y      = Mobile.getElementTopPosition(sub_folder_obj, 5)
	} else {
		TestObject r_sub_folder_obj = new TestObject()
		r_sub_folder_obj.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + folderName_app_renamed + "']")
		Mobile.verifyElementExist(r_sub_folder_obj, 5)
		startX = Mobile.getElementLeftPosition(r_sub_folder_obj, 5) + 200
		endX   = Mobile.getElementLeftPosition(r_sub_folder_obj, 5) - 100
		y      = Mobile.getElementTopPosition(r_sub_folder_obj, 5)
	}

    Mobile.swipe(startX, y, endX, y)
    Mobile.delay(1)

    // --- Neuen Ordnernamen festlegen ---
    folderName_app_renamed = folderName_base + i

    // --- Rename-Dialog öffnen und Text setzen ---
    Mobile.tap(findTestObject('SwipeElements/RenameIcon'), 30)
    Mobile.tap(findTestObject('SwipeElements/CrossIconRenameTab'), 30)
    Mobile.delay(2)
    Mobile.setText(findTestObject('SwipeElements/EnterNewNameField'), folderName_app_renamed, 30)
    Mobile.tap(findTestObject('SwipeElements/SaveButton'), 30)
    Mobile.delay(10)

    println("Renamed folder: ${folderName_app_org} → ${folderName_app_renamed}")

    // --- Lokale Variable aktualisieren ---
    folderName_app_org = folderName_app_renamed

    // --- Sync (Pull-to-Refresh) ---
    Mobile.swipe(200, 400, 200, 1100)
    Mobile.delay(5)
	Mobile.swipe(200, 400, 200, 1100)
    // --- WebDAV-Überprüfung: existiert der Ordner auf dem Server? ---
    boolean web_toplvl_present = CustomKeywords.'utils.WebDav.exists'(
        base,
        folderName_app + '/' + folderName_app_renamed,
        user,
        pass
    )

    assert web_toplvl_present : "Ordner '${folderName_app_renamed}' wurde auf WebDAV nicht gefunden!"
}

// return to root
Mobile.tap(findTestObject('LoginScreen/HomeIcon'), 30)

// delete toplvl folder
CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)

//logout and close app
WebUI.callTestCase(findTestCase('Android 15/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)

String getRandomFolderName() {
    String folderName = 'TSY02_' + getTimestamp()

    return folderName
}

String getTimestamp() {
    Date todaysDate = new Date()

    String formattedDate = todaysDate.format('ddMMMyyyyhhmmss')

    return formattedDate
}

