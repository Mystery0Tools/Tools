package vip.mystery0.tools.factory

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import vip.mystery0.tools.context
import vip.mystery0.tools.model.MimeType
import java.io.File
import java.io.InputStream
import javax.xml.parsers.SAXParserFactory

private val factory by lazy {
	MimeTypeFactory(context().assets.open("tika-mimetypes.xml"))
}

val File.mimeType: String?
	get() = extension.getMimeType()

fun String.getMimeType(): String? = factory.extensionMap[this]?.firstOrNull()
fun String.getMimeTypes(): List<String> = factory.extensionMap[this] ?: emptyList()

fun String.getExtensionFromMimeType(): String? = factory.mimeTypeMap[this]?.firstOrNull()
fun String.getExtensionsFromMimeType(): List<String> = factory.mimeTypeMap[this] ?: emptyList()

class MimeTypeFactory(inputStream: InputStream) {
	private val factory = SAXParserFactory.newInstance()
	private val saxParser = factory.newSAXParser()
	private val handler = Handler()
	val extensionMap: HashMap<String, ArrayList<String>>
		get() = handler.extensionMap
	val mimeTypeMap: HashMap<String, ArrayList<String>>
		get() = handler.mimeTypeMap

	init {
		saxParser.parse(inputStream, handler)
	}

	internal class Handler : DefaultHandler() {
		val extensionMap = HashMap<String, ArrayList<String>>()
		val mimeTypeMap = HashMap<String, ArrayList<String>>()
		private var mimeType: MimeType? = null
		private var tempString: String? = null

		override fun startElement(uri: String?, localName: String?, qName: String, attributes: Attributes) {
			if (qName == "mimeType") {
				mimeType = MimeType()
				mimeType!!.extension = ArrayList()
			}
			super.startElement(uri, localName, qName, attributes)
		}

		override fun endElement(uri: String?, localName: String?, qName: String?) {
			when (qName) {
				"mimeType" -> {
					mimeTypeMap[mimeType!!.value] = mimeType!!.extension
					mimeType!!.extension.forEach {
						val list = extensionMap[it] ?: ArrayList()
						list.add(mimeType!!.value)
						extensionMap[it] = list
					}
					mimeType = null
				}
				"value" -> mimeType!!.value = tempString!!
				"extension" -> mimeType!!.extension.add(tempString!!)
			}
			super.endElement(uri, localName, qName)
		}

		override fun characters(ch: CharArray, start: Int, length: Int) {
			tempString = String(ch, start, length)
			super.characters(ch, start, length)
		}
	}
}