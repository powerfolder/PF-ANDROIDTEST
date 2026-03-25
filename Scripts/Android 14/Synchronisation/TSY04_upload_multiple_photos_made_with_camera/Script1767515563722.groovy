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
import com.kms.katalon.core.testobject.ConditionType as ConditionType
import com.kms.katalon.core.annotation.Keyword as Keyword
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory as MobileDriverFactory
import com.kms.katalon.core.util.KeywordUtil as KeywordUtil
import org.openqa.selenium.By as By
import org.openqa.selenium.WebElement as WebElement

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

// create toplvl folder
Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)

Mobile.delay(3)

// tab on menu-entry New-Directory to start Toplvl-folder-creation dialog
Mobile.tap(findTestObject('PlusIconMenus/NewDirectory'), 30)

// create foldername
String timestamp_folder = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()

String toplvl_folder = getRandomFolderName()

Mobile.delay(3)

Mobile.setText(findTestObject('Folder_Menu/EnterNewFolderName'), toplvl_folder, 30)

Mobile.delay(2)

Mobile.tap(findTestObject('Folder_Menu/ClickOnOkButton'), 30)

Mobile.delay(3)

// verify if folder is shown in app
TestObject top_folder_obj = new TestObject()

top_folder_obj.addProperty('xpath', ConditionType.EQUALS, ('//*[@text=\'' + toplvl_folder) + '\']')

Mobile.verifyElementExist(top_folder_obj, 5)

// tab on toplvl folder
Mobile.delay(2)

Mobile.tap(top_folder_obj, 5)

Mobile.delay(3)

// do a loop
for (int i = 0; i < 10; i++) {
    // open up camera
    Mobile.tapAtPosition(GlobalVariable.EMU_P8_plusIconTabX, GlobalVariable.EMU_P8_plusIconTabY)

    Mobile.delay(2)

    Mobile.tap(findTestObject('PlusIconMenus/TakePicture'), 30)

    Mobile.delay(6)

    // take picture
    Mobile.tapAtPosition(545, 2245)

    Mobile.delay(5)

    Mobile.tapAtPosition(545, 2245)

    Mobile.delay(10)

    // get current timestamp
    long nowSec = System.currentTimeMillis() / 1000

    // create a timewindow in which the photo will be created +-12
    int half = (25 - 1) / 2

    // build a list of possible file names
    List<String> prefixes = []

    for (long s = nowSec - half; s <= (nowSec + half); s++) {
        prefixes << s.toString()
    }
    
    // build xpath with possible filenames
    // //*[ (starts-with(@text,'sec1') or starts-with(@text,'sec2') ...) and contains(@text,'.jpg') ]
    StringBuilder xpathSb = new StringBuilder('//*[(')

    for (int x = 0; x < prefixes.size(); x++) {
        if (x > 0) {
            xpathSb.append(' or ')
        }
        
        xpathSb.append('starts-with(@text,\'').append(prefixes[x]).append('\')')
    }
    
    xpathSb.append(') and contains(@text, \'.jpg\')]')

    String xpath = xpathSb.toString()

    println("DEBUG($i): Generated xpath for recent-file-check: $xpath")

    // create test object and check if file is shown in app
    TestObject fileObj = new TestObject('recentFile_' + i)

    fileObj.addProperty('xpath', ConditionType.EQUALS, xpath)

    Mobile.verifyElementExist(fileObj, 10)

    // check if file is on server
    String fileName = Mobile.getAttribute(fileObj, 'text', 5)

    String serverPath = (toplvl_folder + '/') + fileName

    boolean exists = CustomKeywords.'utils.WebDav.exists'(base, serverPath, user, pass)

    Mobile.verifyEqual(exists, true)
}

//logout and close app
WebUI.callTestCase(findTestCase('Android 14/Logout/Logout'), [:], FailureHandling.CONTINUE_ON_FAILURE)

String getRandomFolderName() {
    String folderName = 'TSY04_' + getTimestamp()

    return folderName
}

String getTimestamp() {
    Date todaysDate = new Date()

    String formattedDate = todaysDate.format('ddMMMyyyyhhmmss')

    return formattedDate
}

