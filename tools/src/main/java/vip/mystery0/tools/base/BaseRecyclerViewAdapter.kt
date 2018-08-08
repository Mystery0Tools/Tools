package vip.mystery0.tools.base

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseRecyclerViewAdapter<T : RecyclerView.ViewHolder, M : Any>(@LayoutRes private val itemLayoutId: Int) : RecyclerView.Adapter<T>() {
	val list = ArrayList<M>()

	override fun getItemCount(): Int = list.size

	override fun onBindViewHolder(holder: T, position: Int) =setItemView(holder, position, list[position])

	abstract fun setItemView(holder: T, position: Int, data: M)

	fun createView(parent: ViewGroup): View = LayoutInflater.from(parent.context).inflate(itemLayoutId, parent, false)
}