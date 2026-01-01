package utils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil
import internal.GlobalVariable



import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.ConditionType
import groovy.json.JsonSlurper

class PowerFolderAPI {

	/**
	 * Calls the PowerFolder API to get all folders and extracts name + folderID.
	 * Equivalent to: curl -v -u username 'https://.../api/folders?action=getAll'
	 *
	 * @param baseUrl e.g. "https://mimas.powerfolder.net/api/folders"
	 * @param username PowerFolder username (Basic Auth)
	 * @param password PowerFolder password (Basic Auth)
	 * @return a List of Maps [ [name: 'tmp', folderID: '2AzCh...'], ... ]
	 */
	@Keyword
	def getFolderIdByName(String apiUrl, String username, String password, String folderName) {
		// build request
		RequestObject request = new RequestObject("getAllFolders")
		request.setRestUrl("${apiUrl}?action=getAll")
		request.setRestRequestMethod("GET")

		// use basic auth
		String basicAuth = "Basic " + ("${username}:${password}".getBytes("UTF-8").encodeBase64().toString())
		List<TestObjectProperty> headers = [
			new TestObjectProperty("Authorization", ConditionType.EQUALS, basicAuth)
		]
		request.setHttpHeaderProperties(headers)

		// do request
		def response = WS.sendRequest(request)
		int code = response.getStatusCode()

		if (code != 200) {
			KeywordUtil.markWarning("⚠️ API returned HTTP ${code}")
			return null
		}

		// parse json
		def json = new JsonSlurper().parseText(response.getResponseBodyContent())
		def result = json?.ResultSet?.Result

		if (!result) {
			KeywordUtil.markWarning("⚠️ Keine Folder-Daten empfangen.")
			return null
		}

		// search for folder
		def folder = result.find { it.name == folderName }

		if (folder) {
			KeywordUtil.logInfo("✅ Folder '${folderName}' found with ID = ${folder.folderID}")
			return folder.folderID
		} else {
			KeywordUtil.markWarning("⚠️ Folder '${folderName}' not found.")
			return null
		}
	}
	/**
	 * check if user is listed in list of invitations to folder
	 */
	@Keyword
	def isUserInvitedToFolder(String apiBaseUrl, String folderId, String apiUser, String apiPass, String usernameToFind) {
		try {
			// build api url
			String url = "${apiBaseUrl}?action=getByFolder&FolderID=${folderId}&json=1"

			// set request
			RequestObject req = new RequestObject("getInvitationsByFolder")
			req.setRestUrl(url)
			req.setRestRequestMethod("GET")

			// use basic auth header
			String basicAuth = "Basic " + ("${apiUser}:${apiPass}".getBytes("UTF-8").encodeBase64().toString())
			req.setHttpHeaderProperties([
				new TestObjectProperty("Authorization", ConditionType.EQUALS, basicAuth)
			])

			// do request
			def resp = WS.sendRequest(req)
			int code = resp.getStatusCode()

			// 204: meaning no content - no invitations to folder
			if (code == 204) {
				KeywordUtil.markError("ℹ️ HTTP 204 - no invitations to FolderID ${folderId}")
				return false
			}

			// make sure only 200 is a success
			if (code != 200) {
				KeywordUtil.markWarning("⚠️ API returned HTTP ${code}")
				return false
			}

			// check json return from api
			def json = new JsonSlurper().parseText(resp.getResponseBodyContent())
			def results = json?.ResultSet?.Result

			if (!results) {
				KeywordUtil.logInfo("ℹ️ no entrys for FolderID ${folderId}")
				return false
			}

			// check for username
			boolean found = results.any { it.username?.equalsIgnoreCase(usernameToFind) }

			if (found) {
				KeywordUtil.logInfo("✅ Found user in invitationslist to folder")
				return true
			} else {
				KeywordUtil.markFailed("❌ Did find '${usernameToFind}' as invited to folder ${folderId}")
			}

			return found
		} catch (Exception e) {
			KeywordUtil.markError("❌ Fehler bei getInvitationsByFolder: ${e.message}")
			return false
		}
	}
}