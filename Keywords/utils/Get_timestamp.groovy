package utils
import com.kms.katalon.core.annotation.Keyword
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Random

class Get_timestamp {
	/*
	 * Create timestamps with format yyyymmddHHMMSS
	 * Can be used for setting dynamic folder/filenames
	 * Call via:
	 * String folderName = CustomKeywords.'utils.Get_timestamp.generateTimestamp'()
	 */
	@Keyword
	def String generateTimestamp() {
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
		return timestamp
	}
}