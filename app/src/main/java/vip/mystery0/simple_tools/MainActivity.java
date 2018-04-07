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
		Log.i(TAG, "onCreate: " + CmdTools.requestSU());
		String[] cmds = new String[]{"echo test1", "echo test2", "echo test3"};
		CmdTools.CommandResult result1 = CmdTools.execCommands(cmds);
		Log.i(TAG, "onCreate: " + result1);
		String[] cmdss = new String[]{"ls", "cd /sdcard/", "ls"};
		CmdTools.CommandResult result2 = CmdTools.execRootCommands(cmdss);
		Log.i(TAG, "onCreate: " + result2);
		CmdTools.CommandResult result3 = CmdTools.execRootCommand("ls /data");
		Log.i(TAG, "onCreate: " + result3);
	}
}
