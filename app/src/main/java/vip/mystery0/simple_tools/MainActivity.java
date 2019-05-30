package vip.mystery0.simple_tools;

import android.util.Log;

import java.io.File;

import vip.mystery0.tools.base.BaseActivity;
import vip.mystery0.tools.utils.ActivityManagerTools;
import vip.mystery0.tools.utils.ArchiveTools;
import vip.mystery0.tools.utils.CommandTools;
import vip.mystery0.view.ProgressDialog;

public class MainActivity extends BaseActivity {
	private static final String TAG = "MainActivity";

	public MainActivity() {
		super(R.layout.activity_main);
	}

	@Override
	public void bindView() {
		super.bindView();
	}

	@Override
	public void initData() {
		super.initData();
//		final ProgressDialog progressDialog = new ProgressDialog(this);
//		progressDialog.setTitle("title");
//		progressDialog.setMessage("message");
//		progressDialog.show();

//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				ArchiveTools.Companion.tarGz()
//						.setCompressDir(new File("/sdcard/JanYo Share"))
//						.setArchiveFileName("test")
//						.setSavePath(new File("/sdcard/"))
//						.setSuffix("tar.gz")
//						.compress();

//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						progressDialog.cancel();
//					}
//				});
//			}
//		}).start();
	}

	@Override
	public void initView() {
		super.initView();
	}

	@Override
	public void requestData() {
		super.requestData();
	}
}
