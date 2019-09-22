package vip.mystery0.simple_tools

import android.os.Environment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import vip.mystery0.tools.base.BaseActivity
import vip.mystery0.tools.utils.*
import java.io.File

class MainActivity : BaseActivity(R.layout.activity_main) {

	override fun bindView() {
		super.bindView()
	}

	override fun initData() {
		super.initData()
	}

	override fun initView() {
		super.initView()
	}

	override fun requestData() {
		super.requestData()
//		GlobalScope.launch(Dispatchers.Main) {
//			val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
//			val file = File(dir, "test.txt")
//			"this is test\nthis is second line".writeToFile(file)
//			val outFile = File(dir, "out.txt")
//			file.copyToFile(outFile)
//			val content = outFile.readToString()
//			content.toast()
//			text_view.text = content
//			val outDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//			dir!!.copyToDir(outDir!!)
//			hasSuInBackground().toast()
//		}
	}
}
