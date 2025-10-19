package utils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import com.kms.katalon.core.testobject.impl.HttpTextBodyContent
import com.kms.katalon.core.testobject.ConditionType
import groovy.json.JsonSlurper
import java.nio.file.Files
import java.nio.file.Paths
import java.net.HttpURLConnection

class WebDav {

	// ========= Helper =========

	private String baseUrlJoin(String baseUrl, String path) {
		if (!baseUrl.endsWith("/")) baseUrl += "/"
		if (path.startsWith("/")) path = path.substring(1)
		return baseUrl + path
	}

	private String basicAuth(String user, String pass) {
		return "Basic " + ("${user}:${pass}".getBytes("UTF-8").encodeBase64().toString())
	}

	private getToken(String username, String password) {
        // create API-URL
        String url = "https://mimas.powerfolder.net/login?Username=${username}&Password=${password}&json=1"

        // Request-Objekt erstellen
        RequestObject request = new RequestObject("loginRequest")
        request.setRestUrl(url)
        request.setRestRequestMethod("GET")

        // Request ausführen
        def response = WS.sendRequest(request)

        // JSON parsen
        def json = new JsonSlurper().parseText(response.getResponseBodyContent())

        // Token extrahieren
        String token = json.account.token

        println "INFO: erhaltenes Token = ${token}"

        return token
    }

	
	private RequestObject req(String name, String method, String url, Map<String,String> headers = [:], String body = null) {
		RequestObject r = new RequestObject(name)
		r.setRestUrl(url)
		r.setRestRequestMethod(method)

		List<TestObjectProperty> hdrs = []
		headers.each { k,v ->
			hdrs.add(new TestObjectProperty(k, ConditionType.EQUALS, v))
		}
		if (!hdrs.isEmpty()) r.setHttpHeaderProperties(hdrs)

		if (body != null) {
			r.setBodyContent(new HttpTextBodyContent(body, "UTF-8", "text/xml"))
		}
		return r
	}

	// ========= Keywords =========

	/**
	 * Create Folder (MKCOL). Success 201 (Created) or 405 (Already exists).
	 */
	@Keyword
	def createFolder(String baseUrl, String folderPath, String username, String password) {
		String url = baseUrlJoin(baseUrl, folderPath)
		def r = req("MKCOL", "MKCOL", url, ["Authorization": basicAuth(username, password)])
		def resp = WS.sendRequest(r)
		int code = resp.getStatusCode()
		if (code == 201 || code == 405) {
			KeywordUtil.logInfo("MKCOL ${url} -> ${code}")
			return true
		}
		KeywordUtil.markWarning("MKCOL ${url} -> HTTP ${code}")
		return false
	}

	@Keyword
	def uploadFile(String baseUrl, String remotePath, String username, String password, byte[] data, String contentType = "application/octet-stream") {
		String urlStr = baseUrlJoin(baseUrl, remotePath)
		HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection()
		conn.setRequestMethod("PUT")
		conn.setDoOutput(true)
		conn.setRequestProperty("Authorization", basicAuth(username, password))
		conn.setRequestProperty("Content-Type", contentType)
		conn.setFixedLengthStreamingMode(data.length)

		conn.outputStream.withCloseable { it.write(data) }

		int code = conn.getResponseCode()
		conn.disconnect()

		if (code == 200 || code == 201 || code == 204) {
			KeywordUtil.logInfo("✅ PUT ${urlStr} -> ${code}, uploaded ${data.length} bytes")
			return true
		}
		KeywordUtil.markWarning("⚠️ PUT ${urlStr} -> HTTP ${code}")
		return false
	}

	/**
	 * File/Folder DELETE.
	 */
	@Keyword
	def deletePath(String baseUrl, String path, String username, String password) {
		String url = baseUrlJoin(baseUrl, path)
		def r = req("DELETE", "DELETE", url, ["Authorization": basicAuth(username, password)])
		def resp = WS.sendRequest(r)
		int code = resp.getStatusCode()
		if (code == 200 || code == 204) {
			KeywordUtil.logInfo("DELETE ${url} -> ${code}")
			return true
		}
		KeywordUtil.markWarning("DELETE ${url} -> HTTP ${code}")
		return false
	}

	/**
	 * Rename=move (MOVE). destPath is relative to baseUrl.
	 */
	@Keyword
	def renameOrMove(String baseUrl, String srcPath, String destPath, String username, String password, boolean overwrite = true) {
		String srcUrl = baseUrlJoin(baseUrl, srcPath)
		String dstUrl = baseUrlJoin(baseUrl, destPath)
		def r = req("MOVE", "MOVE", srcUrl, [
			"Authorization": basicAuth(username, password),
			"Destination"  : dstUrl,
			"Overwrite"    : overwrite ? "T" : "F"
		])
		def resp = WS.sendRequest(r)
		int code = resp.getStatusCode()
		if (code == 201 || code == 204) {
			KeywordUtil.logInfo("MOVE ${srcUrl} -> ${dstUrl} -> ${code}")
			return true
		}
		KeywordUtil.markWarning("MOVE ${srcUrl} -> ${dstUrl} -> HTTP ${code}")
		return false
	}

	/**
	 * check if file is created (PROPFIND depth 0). returns 200/207.
	 */
	@Keyword
	def exists(String baseUrl, String path, String username, String password) {
		String url = baseUrlJoin(baseUrl, path)
		def r = req("PROPFIND", "PROPFIND", url,
				["Authorization": basicAuth(username, password),
					"Depth"        : "0"],
				// minimaler PROPFIND-Body
				"""<?xml version="1.0" encoding="utf-8" ?>
                   <d:propfind xmlns:d="DAV:">
                     <d:prop><d:resourcetype/></d:prop>
                   </d:propfind>"""
				)
		def resp = WS.sendRequest(r)
		int code = resp.getStatusCode()
		KeywordUtil.logInfo("PROPFIND ${url} -> ${code}")
		return (code == 200 || code == 207)
	}
}