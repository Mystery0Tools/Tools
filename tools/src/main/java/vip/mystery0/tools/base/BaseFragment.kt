package vip.mystery0.tools.base

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

abstract class BaseFragment(@LayoutRes private val layoutId: Int) : Fragment() {
	private var rootView: View? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		if (rootView == null)
			rootView = inflateView(layoutId, inflater, container)
		return rootView
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		initView()
		monitor()
	}


	abstract fun initView()
	open fun inflateView(layoutId: Int, inflater: LayoutInflater, container: ViewGroup?): View {
		return inflater.inflate(layoutId, container, false)
	}

	open fun monitor() {}

	fun <T : View> findViewById(@IdRes id: Int): T {
		return rootView!!.findViewById(id)
	}
}