package utils
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import internal.GlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.model.FailureHandling as FailureHandling


class Startup_app {
	/**
	 * Check if app is installed
	 * if yes: start it
	 * if no: install it from path (applocation)
	 * call via: CustomKeywords.'utils.Startup_app.start'()
	 */
	@Keyword
	def start(){
		if (GlobalVariable.isExistingApp) {
			Mobile.startExistingApplication('de.goddchen.android.powerfolder.A', FailureHandling.STOP_ON_FAILURE)
		} else {
			String applocation = (RunConfiguration.getProjectDir() + '/apks/') + GlobalVariable.AppName
			Mobile.startApplication(applocation, false, FailureHandling.CONTINUE_ON_FAILURE)
			Mobile.delay(5)
		}
	}
}