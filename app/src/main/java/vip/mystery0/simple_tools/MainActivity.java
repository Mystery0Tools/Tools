package vip.mystery0.simple_tools;

import android.util.Log;

import vip.mystery0.tools.base.BaseActivity;
import vip.mystery0.tools.utils.CommandTools;

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
		Log.i(TAG, "initData: " + CommandTools.INSTANCE.requestSU());
		String[] cmds = new String[]{"echo test1", "echo test2", "echo test3"};
		CommandTools.CommandResult result1 = CommandTools.INSTANCE.execCommands(cmds);
		Log.i(TAG, "initData: " + result1);
		String[] cmdss = new String[]{"ls", "cd /sdcard/", "ls"};
		CommandTools.CommandResult result2 = CommandTools.INSTANCE.execRootCommands(cmdss);
		Log.i(TAG, "initData: " + result2);
		CommandTools.CommandResult result3 = CommandTools.INSTANCE.execRootCommand("ls /data");
		Log.i(TAG, "initData: " + result3);
		new Thread(new Runnable() {
			@Override
			public void run() {
				CommandTools.CommandResult result4 = CommandTools.INSTANCE.execCommand("ping 127.0.0.1");
				Log.i(TAG, "initData: " + result4);
			}
		}).start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		CommandTools.INSTANCE.killProcess();
		Log.i(TAG, "initData: kill");
	}

	@Override
	public void initView() {
		super.initView();
	}
}
