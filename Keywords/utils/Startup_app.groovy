package utils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import internal.GlobalVariable

class Startup_app {

	private static final String APP_PACKAGE_ID = "de.goddchen.android.powerfolder.A"
	private static final String APK_URL = "https://my.powerfolder.com/dl/fiQ2zfs8zNQovEH8o4o9vy/android/development/PowerFolder.apk"
	private static final String DEFAULT_APK_NAME = "PowerFolder.apk"

	/**
	 * Reads a key:value file from the user's HOME directory
	 * and returns all values as a Map<String, String>.
	 *
	 * @param filename  the name of the file located in HOME
	 * @return Map with key/value pairs
	 */
	@Keyword
	def loadKeyValueFile(String filename) {
		String homeDir = System.getProperty("user.home")
		File file = new File(homeDir, filename)

		if (!file.exists()) {
			KeywordUtil.markFailed("❌ File not found: " + file.absolutePath)
			return [:]
		}

		KeywordUtil.logInfo("🔍 Loading key:value file from: ${file.absolutePath}")

		Map<String, String> result = [:]

		file.eachLine { line ->
			line = line.trim()

			// skip empty lines and comments
			if (line.isEmpty() || line.startsWith("#")) return

				if (!line.contains(":")) {
					KeywordUtil.markWarning("⚠️ Invalid entry (ignored): ${line}")
					return
				}

			def parts = line.split(":", 2)
			String key = parts[0].trim()
			String value = parts[1].trim()

			result[key] = value
		}

		KeywordUtil.logInfo("✅ Loaded ${result.size()} entries.")
		return result
	}
	def loadCredsIntoGlobals(String filename) {
		def creds = loadKeyValueFile(filename)

		if (!creds || creds.isEmpty()) {
			KeywordUtil.markFailed("❌ No credentials found in file: $filename")
			return
		}

		GlobalVariable.userid      = creds["userid"]
		GlobalVariable.password    = creds["password"]
		GlobalVariable.ApiAccount  = creds["ApiAccount"]
		GlobalVariable.ApiPassword = creds["ApiPassword"]

		KeywordUtil.logInfo("✅ Credentials loaded into GlobalVariables")
	}

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
			// get adb path dynamically based on OS-Path
			String adbExecutable

			if (System.properties['os.name'].toLowerCase().contains('windows')) {
				adbExecutable = System.getProperty("user.home") + "\\.katalon\\tools\\android_sdk\\platform-tools\\adb.exe"
			} else {
				adbExecutable = System.getProperty("user.home") + "/.katalon/tools/android_sdk/platform-tools/adb"
			}

			File adbFile = new File(adbExecutable)
			if (!adbFile.exists()) {
				println "⚠️  Attention: Clound find adb unter the following path: ${adbExecutable}"
				return false
			}

			def process = [
				adbExecutable,
				"shell",
				"pm",
				"list",
				"packages",
				packageId
			].execute()

			process.waitFor()

			// trim output to show package id
			def output = process.text?.trim()
			println "ADB output: ${output}"

			return output.contains("package:" + packageId)
		} catch (Exception e) {
			println "❌ ERROR: Failed to check if app is installed: ${e.message}"
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
			startExisting()
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
		
		// make sure account exists for test
		WebDav webdav = new WebDav()
		webdav.createAccount(
				GlobalVariable.ApiURL,
				GlobalVariable.userid,
				GlobalVariable.password,
				GlobalVariable.ApiAccount,
				GlobalVariable.ApiPassword
				)
		// remove old folders
		webdav.deleteAllFolders(
				GlobalVariable.WebdavURL,
				GlobalVariable.userid,
				GlobalVariable.password
				)
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
		// make sure account exists for test
		WebDav webdav = new WebDav()
		webdav.createAccount(
				GlobalVariable.ApiURL,
				GlobalVariable.userid,
				GlobalVariable.password,
				GlobalVariable.ApiAccount,
				GlobalVariable.ApiPassword
				)
		// remove old folders
		webdav.deleteAllFolders(
				GlobalVariable.WebdavURL,
				GlobalVariable.userid,
				GlobalVariable.password
				)
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
