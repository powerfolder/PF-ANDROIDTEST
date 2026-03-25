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
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// get info about qa-system
CustomKeywords.'utils.Startup_app.loadCredsIntoGlobals'("katalon.txt")

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

// create 100kb file in toplvl folder
String filename = 'TSY03.txt'
CustomKeywords.'utils.WebDav.createAndUploadTextFile'(base,folderName_webdav + '/' + filename, user, pass,100)

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

// check if file is shown
TestObject file = new TestObject()
file.addProperty("xpath", ConditionType.EQUALS, "//*[@text='" + filename + "']")
Mobile.verifyElementExist(top_folder_obj, 5)

// check sync via file size
DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US)
DecimalFormat df = new DecimalFormat("0.00", symbols)

int size = 150
String search_size = df.format(size) + " KB"

for (int i = 1; i <= 7; i++) {
	CustomKeywords.'utils.WebDav.createAndUploadTextFile'(base, folderName_webdav + '/' + 'TSY03.txt', user, pass, size)
	Mobile.delay(10)
	
	// sync via pull down
	Mobile.swipe(200, 400, 200, 1100)
	Mobile.delay(2)
	Mobile.swipe(200, 400, 200, 1100)
	
	println("🔎 Suche nach: ${search_size}")

	TestObject file_change = new TestObject()
	file_change.addProperty("xpath", ConditionType.EQUALS,
		"//*[@class='android.widget.TextView' and contains(@text, '" + search_size + "')]")

	Mobile.verifyElementExist(file_change, 5)
	
	size += 50
	search_size = df.format(size) + " KB"
}

// return to root
Mobile.tap(findTestObject('LoginScreen/HomeIcon'), 30)

// delete toplvl folder
CustomKeywords.'utils.Delete_object.swipeAndDelete'(top_folder_obj)

//logout and close app
WebUI.callTestCase(findTestCase('Android 14/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)

String getRandomFolderName() {
	String folderName = 'TSY03_' + getTimestamp()

	return folderName
}

String getTimestamp() {
	Date todaysDate = new Date()

	String formattedDate = todaysDate.format('ddMMMyyyyhhmmss')

	return formattedDate
}