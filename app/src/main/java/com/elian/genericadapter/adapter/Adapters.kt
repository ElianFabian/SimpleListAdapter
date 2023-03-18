package com.elian.genericadapter.adapter

import com.elian.genericadapter.databinding.ItemOperationBinding
import com.elian.genericadapter.databinding.ItemPersonBinding
import com.elian.genericadapter.model.MultiItem
import com.elian.genericadapter.model.OperationInfo
import com.elian.genericadapter.model.Person

// In this file we define all the adapters, in here were using the simple version of both GenericAdapters.


// We can define extension functions to reuse the binding logic if needed.

fun ItemOperationBinding.bind(item: OperationInfo)
{
	item.apply()
	{
		tvFirstNumber.text = "$firstNumber"
		tvSecondNumber.text = "$secondNumber"
		tvOperationSymbol.text = operationSymbol
		tvExpectedResult.text = "$result"
	}
}

fun ItemPersonBinding.bind(item: Person)
{
	item.apply()
	{
		tvName.text = name
		tvLastname.text = lastname
	}
}


// The GenericAdapter inherits from a ListAdapter, so you should always give a value
// to the areItemsTheSame parameter...
// Unless the list is going to be static (never modified) then it won't be necessary or if the content if going
// to be different for all the items.
// And the areContentsTheame in general I don't think you should define it unless you want a different behaviour.

@Suppress("FunctionName")
fun OperationAdapter(items: List<OperationInfo>) = GenericAdapter(
	inflate = ItemOperationBinding::inflate,
	areItemsTheSame = { oldItem, newItem -> oldItem.uuid == newItem.uuid },
) { item: OperationInfo, binding ->

	binding.bind(item)

}.apply { submitList(items) }


@Suppress("FunctionName")
fun PersonAdapter(items: List<Person>) = GenericAdapter(
	inflate = ItemPersonBinding::inflate,
	areItemsTheSame = { oldItem, newItem -> oldItem.uuid == newItem.uuid },
) { item: Person, binding ->

	binding.bind(item)

}.apply { submitList(items) }


// For a MultiItem adapter it's pretty useful to make use of a sealed class/interface
// it gives you better type safety and also a cleaner way to define the value for the areItemsTheSame parameter.

// If you do not use a sealed class you should do something like this for the areItemsTheSame parameter:
//areItemsTheSame = { oldItem, newItem ->
//	when
//	{
//		oldItem is OperationInfo && newItem is OperationInfo -> oldItem.uuid == newItem.uuid
//		oldItem is Person && newItem is Person               -> oldItem.uuid == newItem.uuid
//		else                                                 -> error("Unexpected type '${oldItem::class}' in adapter.")
//	}
//}

@Suppress("FunctionName")
fun OperationAndPersonAdapter(
	items: List<MultiItem>,
	onOperationClick: ((OperationInfo) -> Unit)? = null,
	onPersonClick: ((Person) -> Unit)? = null,
) = GenericAdapter(
	areItemsTheSame = { oldItem, newItem -> oldItem.uuid == newItem.uuid },
	itemBindings = listOf(
		Binding(ItemOperationBinding::inflate) { item: OperationInfo, binding ->

			binding.bind(item)

			binding.root.setOnClickListener { onOperationClick?.invoke(item) }
		},
		Binding(ItemPersonBinding::inflate) { item: Person, binding ->

			binding.bind(item)

			binding.root.setOnClickListener { onPersonClick?.invoke(item) }
		},
	),
).apply { submitList(items) }