package utils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling

class Startup_app {

	private static final String APP_PACKAGE_ID = "de.goddchen.android.powerfolder.A"
	private static final String APK_URL = "https://my.powerfolder.com/dl/fiQ2zfs8zNQovEH8o4o9vy/android/development/PowerFolder.apk"
	private static final String DEFAULT_APK_NAME = "PowerFolder.apk"

	/**
	 * Usage examples:
	 * - Install latest APK only if not already installed:
	 *     CustomKeywords.'utils.Startup_app.install'()
	 *
	 * - Install a specific local APK only if not already installed:
	 *     CustomKeywords.'utils.Startup_app.install'('PowerFolder_v23.1.101.apk')
	 *
	 * - Force reinstall the latest APK regardless of install state:
	 *     CustomKeywords.'utils.Startup_app.install'(null, true)
	 *
	 * - Force reinstall a specific local APK regardless of install state:
	 *     CustomKeywords.'utils.Startup_app.install'('PowerFolder_v23.1.101.apk', true)
	 */

	/**
	 * Checks if the app is installed on the device using ADB.
	 */
	private boolean isAppInstalled(String packageId) {
		try {
			def process = [
				"adb",
				"shell",
				"pm",
				"list",
				"packages",
				packageId
			].execute()
			process.waitFor()
			def output = process.text.trim()
			return output.contains("package:" + packageId)
		} catch (Exception e) {
			println "ERROR: Failed to check if app is installed: ${e.message}"
			return false
		}
	}

	/**
	 * Installs the app.
	 * - Skips installation if app is already installed (unless forceReinstall is true)
	 * - If a filename is provided, installs from apks/<name>.apk
	 * - If not, downloads the latest dev build
	 */
	@Keyword
	def install(String localAppVersion = null, boolean forceReinstall = false) {
		String apksDir = RunConfiguration.getProjectDir() + "/apks/"
		String apkFilePath

		//  Check if app is already installed
		if (!forceReinstall && isAppInstalled(APP_PACKAGE_ID)) {
			println "INFO: App is already installed. Skipping installation."
			return
		}

		if (localAppVersion != null && !localAppVersion.trim().isEmpty()) {
			// Path for local APK
			apkFilePath = apksDir + localAppVersion
			println "INFO: Installing local APK: $apkFilePath"
		} else {
			// Download latest dev-build
			apkFilePath = apksDir + DEFAULT_APK_NAME
			println "INFO: Downloading APK: $APK_URL"
			new URL(APK_URL).withInputStream { input ->
				new File(apkFilePath).withOutputStream { out ->
					out << input
				}
			}
			println "INFO: Saved APK under: $apkFilePath"
		}

		// Install APK
		Mobile.startApplication(apkFilePath, true, FailureHandling.STOP_ON_FAILURE)

		// Clean up if it was a downloaded file
		if (localAppVersion == null) {
			File apkFile = new File(apkFilePath)
			if (apkFile.exists() && apkFile.delete()) {
				println "INFO: Deleted downloaded APK."
			} else {
				println "WARN: Could not delete APK."
			}
		}

		Mobile.delay(10)
	}

	/**
	 * Starts the already installed app on device.
	 */
	@Keyword
	def startExisting() {
		try {
			Mobile.startExistingApplication(APP_PACKAGE_ID, FailureHandling.STOP_ON_FAILURE)
			println "INFO: Successfully started existing app: $APP_PACKAGE_ID"
			Mobile.delay(20)
		} catch (Exception e) {
			println "ERROR: App not installed or failed to start: $APP_PACKAGE_ID"
			throw e
		}
	}
}
