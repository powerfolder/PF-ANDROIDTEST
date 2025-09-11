package utils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling

class Startup_app {

	private static boolean forceReinstall = false
	private static final String APP_PACKAGE_ID = "de.goddchen.android.powerfolder.A"

	@Keyword
	def resetMarker() {
		forceReinstall = true
		println "INFO: Reset marker set. Next start() will reinstall the app."
	}

	/**
	 * Attempts to start the app. Returns true if started successfully, false otherwise.
	 */
	private boolean tryStartExistingApp(String packageName) {
		try {
			Mobile.startExistingApplication(packageName, FailureHandling.STOP_ON_FAILURE)
			println "INFO: Successfully started existing app: $packageName"
			return true
		} catch (Exception e) {
			println "WARN: App not installed or failed to start: $packageName"
			return false
		}
	}

	@Keyword
	def start(String localAppVersion = null) {
		String apksDir = RunConfiguration.getProjectDir() + "/apks/"
		String apkFilePath

		if (localAppVersion != null && !localAppVersion.trim().isEmpty()) {
			apkFilePath = apksDir + localAppVersion
			println "INFO: Using local APK: $apkFilePath"
		} else {
			apkFilePath = apksDir + "PowerFolder.apk"
			String apkUrl = "https://my.powerfolder.com/dl/fiQ2zfs8zNQovEH8o4o9vy/android/development/PowerFolder.apk"

			println "INFO: Downloading APK: $apkUrl"
			new URL(apkUrl).withInputStream { input ->
				new File(apkFilePath).withOutputStream { out ->
					out << input
				}
			}
			println "INFO: Saved APK under: $apkFilePath"
		}

		boolean reinstall = forceReinstall
		forceReinstall = false

		if (!reinstall) {
			// Try starting existing app first
			boolean started = tryStartExistingApp(APP_PACKAGE_ID)
			if (started) {
				Mobile.delay(10)
				return
			} else {
				reinstall = true
			}
		}

		// App not installed or reinstall required
		println "INFO: Installing and starting app from APK: $apkFilePath"
		Mobile.startApplication(apkFilePath, true, FailureHandling.STOP_ON_FAILURE)

		// Clean up if we downloaded the APK
		if (localAppVersion == null) {
			File apkFile = new File(apkFilePath)
			if (apkFile.exists()) {
				boolean deleted = apkFile.delete()
				println deleted ? "INFO: Deleted downloaded APK." : "WARN: Could not delete APK."
			}
		}

		Mobile.delay(20)
	}
}
