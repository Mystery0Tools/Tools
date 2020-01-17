package vip.mystery0.simple_tools

import android.util.Log
import vip.mystery0.tools.ToolsClient
import vip.mystery0.tools.base.BaseActivity
import vip.mystery0.tools.factory.getExtensionsFromMimeType
import vip.mystery0.tools.factory.toJson

class MainActivity : BaseActivity(R.layout.activity_main) {
	private val TAG = "MainActivity"

	override fun requestData() {
		super.requestData()
		ToolsClient.initWithContext(this)
		Log.i(TAG, "text/plain".getExtensionsFromMimeType().toJson())
	}
}
