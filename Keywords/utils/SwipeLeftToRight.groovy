package utils

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

public class SwipeLeftToRight {
	@Keyword
	def swipe(TestObject obj) {
		// Verify element exists
		Mobile.verifyElementExist(obj, 5)
		Mobile.delay(1)

		// Calculate swipe coordinates
		int startX = Mobile.getElementLeftPosition(obj, 5) - 100   // Start from left side
		int endX   = Mobile.getElementLeftPosition(obj, 5) + 200   // Swipe towards right
		int y      = Mobile.getElementTopPosition(obj, 5)

		// Perform swipe left → right
		Mobile.swipe(startX, y, endX, y)
		Mobile.delay(1)
	}
}