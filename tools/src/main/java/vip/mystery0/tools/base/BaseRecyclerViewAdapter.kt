package vip.mystery0.tools.base

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseRecyclerViewAdapter<T : RecyclerView.ViewHolder, in C : Any>(private val context: Context?,
																				@LayoutRes private val itemLayoutId: Int,
																				private val list: ArrayList<C>) : RecyclerView.Adapter<T>() {
	override fun getItemCount(): Int = list.size

	override fun onBindViewHolder(holder: T, position: Int) {
		setItemView(holder, position, list[position])
	}

	abstract fun setItemView(holder: T, position: Int, data: C)

	fun createView(parent: ViewGroup): View = LayoutInflater.from(context).inflate(itemLayoutId, parent, false)
}