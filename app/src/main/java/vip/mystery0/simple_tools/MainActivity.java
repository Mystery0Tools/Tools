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
		startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE), 1);
	}

	@Override
	public void initView() {
		super.initView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == RESULT_OK) {
			Uri treeUri = data.getData();
			Log.i(TAG, "onActivityResult: " + treeUri);
			Uri child = DocumentsContract.buildChildDocumentsUriUsingTree(treeUri, "test");
//			SAFFileTools.INSTANCE.mkdirs(this, child);
		}
	}
}
