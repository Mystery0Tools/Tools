package vip.mystery0.tools.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import java.util.*

fun currentActivity() = ActivityManagerTools.currentActivity()

/**
 * Created by kun on 2016/7/12.
 * Activity管理类
 */
object ActivityManagerTools {
	private val activityStack by lazy { Stack<Activity>() }

	/**
	 * 添加Activity到堆栈
	 */
	fun addActivity(activity: Activity?) = activityStack.add(activity)

	/**
	 * 移除Activity到堆栈
	 */
	fun removeActivity(activity: Activity?) = activityStack.remove(activity)

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
	fun finishActivity() = activityStack.removeAt(activityStack.lastIndex)?.finish()

	/**
	 * 结束指定的Activity
	 */
	fun finishActivity(activity: Activity?) {
		activityStack.remove(activity)
		activity?.finish()
	}

	/**
	 * 结束指定类名的Activity
	 */
	fun finishActivity(cls: Class<*>) {
		activityStack.filter { it.javaClass == cls }.forEach { finishActivity(it) }
	}

	/**
	 * 结束所有Activity
	 */
	fun finishAllActivity() {
		activityStack.forEach {
			it?.finish()
		}
		activityStack.clear()
	}
}

fun Application.registerActivityLifecycle() {
	registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
		override fun onActivityPaused(activity: Activity?) = Unit
		override fun onActivityResumed(activity: Activity?) = Unit
		override fun onActivityStarted(activity: Activity?) = Unit
		override fun onActivityDestroyed(activity: Activity?) {
			ActivityManagerTools.removeActivity(activity)
		}

		override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) = Unit
		override fun onActivityStopped(activity: Activity?) = Unit
		override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
			ActivityManagerTools.addActivity(activity)
		}
	})
}