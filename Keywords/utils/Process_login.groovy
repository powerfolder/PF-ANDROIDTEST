package utils
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import internal.GlobalVariable


class Process_login {
	/**
	 * Procceed login on PFM
	 * call via: CustomKeywords.'utils.Process_login.login'(serverurl,username, password)
	 */
	@Keyword
	def login(String serverurl, String username, String password){
		Mobile.delay(2)
		Mobile.tap(findTestObject('LoginScreen/ServerURLTMP'), 30)
		Mobile.setText(findTestObject('LoginScreen/enterServerURL'), serverurl, 30)
		Mobile.delay(2)
		Mobile.tap(findTestObject('LoginScreen/ServerURLTMP'), 30)
		Mobile.delay(2)
		Mobile.setText(findTestObject('LoginScreen/EnterEmail'), username, 30)
		Mobile.delay(2)
		Mobile.setText(findTestObject('LoginScreen/InputPassword'), password, 30)
		Mobile.tap(findTestObject('LoginScreen/LoginButton'), 45)
		Mobile.delay(3)
		Mobile.tap(findTestObject('LoginScreen/HomeIcon'), 30)
	}

	/**
	 * Logout of the current user, app stays open on the login screen
	 * call via: CustomKeywords.'utils.Process_login.logout'()
	 */
	@Keyword
	def logout(){
		Mobile.tap(findTestObject('MainScreen/ThreeDots'), 45)
		Mobile.tap(findTestObject('ThreeDotsMenu/MyAccount'), 45)
		Mobile.tap(findTestObject('Settings/LogoutButton'), 45)
		Mobile.tap(findTestObject('Settings/LogoutConfirmationYes'), 30)
		Mobile.delay(3)
		Mobile.verifyElementExist(findTestObject('LoginScreen/LoginButton'), 30)
	}
}