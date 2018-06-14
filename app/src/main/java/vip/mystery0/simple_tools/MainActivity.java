package vip.mystery0.simple_tools;

import android.util.Log;

import vip.mystery0.tools.base.BaseActivity;
import vip.mystery0.tools.utils.CmdTools;

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
		Log.i(TAG, "onCreate: " + CmdTools.INSTANCE.isRoot());
		Log.i(TAG, "onCreate: " + CmdTools.INSTANCE.requestSU());
		String[] cmds = new String[]{"echo test1", "echo test2", "echo test3"};
		CmdTools.CommandResult result1 = CmdTools.INSTANCE.execCommands(cmds);
		Log.i(TAG, "onCreate: " + result1);
		String[] cmdss = new String[]{"ls", "cd /sdcard/", "ls"};
		CmdTools.CommandResult result2 = CmdTools.INSTANCE.execRootCommands(cmdss);
		Log.i(TAG, "onCreate: " + result2);
		CmdTools.CommandResult result3 = CmdTools.INSTANCE.execRootCommand("ls /data");
		Log.i(TAG, "onCreate: " + result3);
	}

	@Override
	public void initView() {
		super.initView();
	}
}
