package com.elian.simple_list_adapter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding

// Set of functions for common use cases for the creation of list adapters
// using the SingleItemListAdapter and MultiItemListAdapter classes.

@Suppress("FunctionName")
fun <VB : ViewBinding, ItemT : Any> SimpleListAdapter(
	inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	diffCallback: DiffUtil.ItemCallback<ItemT>,
	onBind: SingleItemListAdapter<VB, ItemT>.(
		binding: VB,
		item: ItemT,
		position: Int,
	) -> Unit,
): ListAdapter<ItemT, out ViewHolder> {
	return object : SingleItemListAdapter<VB, ItemT>(
		inflate = inflate,
		diffCallback = diffCallback,
	) {
		override fun onBind(binding: VB, position: Int, holder: ViewHolder) {
			onBind(binding, getItem(position), position)
		}
	}
}

@Suppress("FunctionName")
fun <ItemT : Any, VB : ViewBinding> SimpleListAdapter(
	inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	areItemsTheSame: (oldItem: ItemT, newItem: ItemT) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	areContentsTheSame: (oldItem: ItemT, newItem: ItemT) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	onBind: SingleItemListAdapter<VB, ItemT>.(
		binding: VB,
		item: ItemT,
		position: Int,
	) -> Unit,
): ListAdapter<ItemT, out ViewHolder> {

	val diffCallback = object : DiffUtil.ItemCallback<ItemT>() {
		override fun areItemsTheSame(oldItem: ItemT, newItem: ItemT) = areItemsTheSame(oldItem, newItem)
		override fun areContentsTheSame(oldItem: ItemT, newItem: ItemT) = areContentsTheSame(oldItem, newItem)
	}

	return object : SingleItemListAdapter<VB, ItemT>(
		inflate = inflate,
		diffCallback = diffCallback,
	) {
		override fun onBind(binding: VB, position: Int, holder: ViewHolder) {
			onBind(binding, getItem(position), position)
		}
	}
}

@Suppress("FunctionName")
fun <
	A : Any,
	B : Any,
	ItemT : Pair<A, B>,
	VB : ViewBinding,
	> SimpleListAdapter(
	inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	diffCallback: DiffUtil.ItemCallback<ItemT>,
	onBind: SingleItemListAdapter<VB, ItemT>.(
		binding: VB,
		A, B,
		position: Int,
	) -> Unit,
) = SimpleListAdapter(
	inflate = inflate,
	diffCallback = diffCallback,
) { binding, item, position ->

	onBind(binding, item.first, item.second, position)
}

@Suppress("FunctionName")
fun <
	A : Any,
	B : Any,
	ItemT : Pair<A, B>,
	VB : ViewBinding,
	> SimpleListAdapter(
	inflate: (LayoutInflater, ViewGroup, Boolean) -> VB,
	areItemsTheSame: (oldItem: ItemT, newItem: ItemT) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	areContentsTheSame: (oldItem: ItemT, newItem: ItemT) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	onBind: SingleItemListAdapter<VB, ItemT>.(
		binding: VB,
		A, B,
		position: Int,
	) -> Unit,
): ListAdapter<ItemT, out ViewHolder> = SimpleListAdapter(
	inflate = inflate,
	areItemsTheSame = areItemsTheSame,
	areContentsTheSame = areContentsTheSame,
) { binding, item, position ->

	onBind(binding, item.first, item.second, position)
}


inline fun <reified VB : ViewBinding, reified ItemT : Any> bindingOf(
	crossinline onBind: MultiItemListAdapter<ItemT>.(
		binding: VB,
		item: ItemT,
	) -> Unit
): BindingData<ItemT> {
	return bindingOf<VB, ItemT>(
		onBind = { binding, position, _ ->
			onBind(binding, currentList[position])
		}
	)
}

@Suppress("FunctionName")
fun <ItemT : Any> SimpleListAdapter(
	bindings: List<BindingData<ItemT>>,
	diffCallback: DiffUtil.ItemCallback<ItemT>
): ListAdapter<ItemT, out ViewHolder> {
	return object : MultiItemListAdapter<ItemT>(diffCallback) {
		override val bindings = bindings
	}
}

@Suppress("FunctionName")
fun <ItemT : Any> SimpleListAdapter(
	bindings: List<BindingData<ItemT>>,
	areItemsTheSame: (oldItem: ItemT, newItem: ItemT) -> Boolean = { oldItem, newItem -> oldItem == newItem },
	areContentsTheSame: (oldItem: ItemT, newItem: ItemT) -> Boolean = { oldItem, newItem -> oldItem == newItem },
): ListAdapter<ItemT, out ViewHolder> {

	val diffCallback = object : DiffUtil.ItemCallback<ItemT>() {
		override fun areItemsTheSame(oldItem: ItemT, newItem: ItemT) = areItemsTheSame(oldItem, newItem)
		override fun areContentsTheSame(oldItem: ItemT, newItem: ItemT) = areContentsTheSame(oldItem, newItem)
	}

	return object : MultiItemListAdapter<ItemT>(diffCallback) {
		override val bindings = bindings
	}
}
