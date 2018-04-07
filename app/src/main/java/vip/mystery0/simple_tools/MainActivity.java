package vip.mystery0.simple_tools;

import android.os.Bundle;
import android.util.Log;

import vip.mystery0.tools.base.BaseActivity;
import vip.mystery0.tools.utils.CmdTools;

public class MainActivity extends BaseActivity {
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.i(TAG, "onCreate: " + CmdTools.isRoot());
		CmdTools.CommandResult result1 = CmdTools.execRootCmd("echo test");
		Log.i(TAG, "result: " + result1.getResult() + " s: " + result1.getSuccessMessage() + " e: " + result1.getErrorMessage());
	}
}
