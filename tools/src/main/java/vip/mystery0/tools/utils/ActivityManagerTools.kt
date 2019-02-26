package vip.mystery0.tools.utils

import android.app.Activity
import java.util.*

/**
 * Created by kun on 2016/7/12.
 * Activity管理类
 */
object ActivityManagerTools {
	private var activityStack: Stack<Activity> = Stack()
	/**
	 * 添加Activity到堆栈
	 */
	fun addActivity(activity: Activity) {
		activityStack.add(activity)
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	fun currentActivity(): Activity? {
		if (!activityStack.empty())
			return activityStack.lastElement()
		return null
	}

	/**
	 * 获取倒数第二个Activity
	 */
	fun lastLastActivity(): Activity? {
		return if (activityStack.size <= 2)
			null
		else
			activityStack[activityStack.size - 2]
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	fun finishActivity() {
		activityStack.lastElement()?.finish()
	}

	/**
	 * 结束指定的Activity
	 */
	fun finishActivity(activity: Activity) {
		activityStack.remove(activity)
		activity.finish()
	}

	/**
	 * 结束指定类名的Activity
	 */
	fun finishActivity(cls: Class<*>) {
		activityStack
				.filter { it.javaClass == cls }
				.forEach { finishActivity(it) }
	}

	/**
	 * 结束所有Activity
	 */
	fun finishAllActivity() {
		var i = 0
		val size = activityStack.size
		while (i < size) {
			if (null != activityStack[i]) {
				activityStack[i].finish()
			}
			i++
		}
		activityStack.clear()
	}
}