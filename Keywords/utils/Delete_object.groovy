package utils

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

class ElementSwipeHandler {

	@Keyword
	def swipeAndDelete(TestObject obj) {
		// verify element exists
		Mobile.verifyElementExist(obj, 5)
		Mobile.delay(1)

		// calc swipe coordinates
		int startX = Mobile.getElementLeftPosition(obj, 5) + 200
		int endX = Mobile.getElementLeftPosition(obj, 5) - 100
		int y = Mobile.getElementTopPosition(obj, 5)

		// swipe
		Mobile.swipe(startX, y, endX, y)
		Mobile.delay(1)

		// press delete button
		Mobile.tap(ObjectRepository.findTestObject('SwipeElements/DeleteIcon'), 30)

		// ack deletion
		Mobile.tap(ObjectRepository.findTestObject('SwipeElements/YesButton'), 30)
		Mobile.delay(2)

		// check if deletion-popup occurs
		String alertMsg = Mobile.getText(ObjectRepository.findTestObject('SwipeElements/DeleteAlertMsg'), 30)

		if (alertMsg.contains('Deleted')) {
			println("✅ SUCESS: " + alertMsg)
		} else {
			println("❌ NO SUCESS")
		}
	}

	/**
	 * Leave a toplvl folder shared by another user (swipe -> delete -> confirm "remove access")
	 * call via: CustomKeywords.'utils.Delete_object.swipeAndLeave'(folder_obj, folderName)
	 */
	@Keyword
	def swipeAndLeave(TestObject obj, String folderName) {
		Mobile.verifyElementExist(obj, 5)
		Mobile.delay(1)

		int startX = Mobile.getElementLeftPosition(obj, 5) + 200
		int endX = Mobile.getElementLeftPosition(obj, 5) - 100
		int y = Mobile.getElementTopPosition(obj, 5)

		Mobile.swipe(startX, y, endX, y)
		Mobile.delay(1)

		Mobile.tap(ObjectRepository.findTestObject('SwipeElements/DeleteIcon'), 30)

		// not owned toplvl folder: dialog must offer to remove access instead of deleting
		String leaveText = Mobile.getText(ObjectRepository.findTestObject('SwipeElements/DeleteDialogText'), 30)
		Mobile.verifyEqual(leaveText, 'Do you really want to remove access from ' + folderName + '?')

		Mobile.tap(ObjectRepository.findTestObject('SwipeElements/YesButton'), 30)
		Mobile.delay(3)
	}
}
