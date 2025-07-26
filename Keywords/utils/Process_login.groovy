package utils
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import internal.GlobalVariable


class Process_login {
	/**
	 * Procceed login on PFM
	 * call via: CustomKeywords.'utils.Process_login.login'()
	 */
	@Keyword
	def login(){
		Mobile.tap(findTestObject('LoginScreen/ServerURL'), 30)
		Mobile.setText(findTestObject('LoginScreen/enterServerURL'), GlobalVariable.ServerURL, 30)
		Mobile.tap(findTestObject('LoginScreen/ServerURL'), 30)
		Mobile.setText(findTestObject('LoginScreen/EnterEmail'), GlobalVariable.userid, 30)
		Mobile.setText(findTestObject('LoginScreen/InputPassword'), GlobalVariable.password, 30)
		Mobile.tap(findTestObject('LoginScreen/LoginButton'), 45)
		Mobile.delay(3)
		Mobile.tap(findTestObject('LoginScreen/HomeIcon'), 30)
	}
}