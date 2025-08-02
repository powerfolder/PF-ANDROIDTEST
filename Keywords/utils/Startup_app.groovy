package utils
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import internal.GlobalVariable
import com.kms.katalon.core.configuration.RunConfiguration as RunConfiguration
import com.kms.katalon.core.model.FailureHandling as FailureHandling

import java.nio.file.Files
import java.nio.file.Paths

class Startup_app {
	/**
	 * Check if app is installed
	 * if yes: remove it and install newest
	 * if no: install it from web dl
	 * optional: specify local app with full apk-name to install custom app version
	 * call via: CustomKeywords.'utils.Startup_app.start'() or CustomKeywords.'utils.Startup_app.start'('my_app.apk')
	 */
	@Keyword
	def start(String localAppVersion = null) {
		String apksDir = RunConfiguration.getProjectDir() + "/apks/"
		// use specified local app-version
		if (localAppVersion != null && !localAppVersion.trim().isEmpty()) {
			String apkFilePath = apksDir + localAppVersion
			println "INFO: Using local APK: $apkFilePath"
	
			Mobile.startApplication(apkFilePath, true, FailureHandling.STOP_ON_FAILURE)
	
		} else {
			// set dl-link and storage path to store apk temporary
		    String apkUrl = "https://my.powerfolder.com/dl/fiQ2zfs8zNQovEH8o4o9vy/android/development/PowerFolder.apk"
		    String apkFilePath = apksDir + "PowerFolder.apk"

		    // download apk
		    println "INFO: Downloading APK: $apkUrl"
		    new URL(apkUrl).withInputStream { input ->
			    new File(apkFilePath).withOutputStream { out ->
				   out << input
			    }
		    }
		    println "INFO: Saved APK under: $apkFilePath"

		    // reinstall app if needed
		    Mobile.startApplication(apkFilePath, true, FailureHandling.STOP_ON_FAILURE)

		    // remove downloaded apk
		    File apkFile = new File(apkFilePath)
		    if (apkFile.exists()) {
			    boolean deleted = apkFile.delete()
			    if (deleted) {
				   println "INFO: deleted apk"
			    } else {
				   println "WARN: cloud not del apk"
			    }
		    }
		Mobile.delay(5)
	    }
	}
}