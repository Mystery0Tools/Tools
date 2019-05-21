package vip.mystery0.simple_tools;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import vip.mystery0.tools.base.BaseActivity;
import vip.mystery0.tools.utils.CommandTools;
import vip.mystery0.tools.utils.FileTools;
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
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("title");
		progressDialog.setMessage("message");
		progressDialog.show();

		new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while (i <= 10) {
					final int finalI = i;
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							progressDialog.setProgress(finalI);
						}
					});
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					i++;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progressDialog.cancel();
					}
				});
			}
		}).start();
	}

	@Override
	public void initView() {
		super.initView();
	}

	@Override
	public void requestData() {
		super.requestData();
		Log.i(TAG, "requestData: " + CommandTools.INSTANCE.requestSU());
	}
}
