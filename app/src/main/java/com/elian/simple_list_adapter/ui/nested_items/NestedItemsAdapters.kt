package com.elian.simple_list_adapter.ui.nested_items

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elian.simple_list_adapter.adapters.SimpleListAdapter
import com.elian.simple_list_adapter.databinding.ItemPersonBinding
import com.elian.simple_list_adapter.databinding.ItemSkillBinding
import com.elian.simple_list_adapter.model.Person
import com.elian.simple_list_adapter.model.SkillInfo
import com.elian.simple_list_adapter.setAdapterOrSubmitList

// In this example the items are grouped using a map, but it would also work
// if you have a single data type that contains all the information
// and it would even be more efficient because you would not need to call the ".toList()" method.


//region New way

@Suppress("FunctionName")
fun SkillsByPersonAdapter_New(skillsByPerson: Map<Person, List<SkillInfo>>) = SimpleListAdapter(
	inflate = ItemPersonBinding::inflate,
	areItemsTheSame = { oldItem, newItem -> oldItem.first.uuid == newItem.first.uuid },
) { binding, person: Person, skills: List<SkillInfo>, _ ->

	binding.apply {
		tvName.text = person.name
		tvLastname.text = person.lastname

		rvSkills.setAdapterOrSubmitList(skills) { SkillAdapter_New() }
	}
}.apply { submitList(skillsByPerson.toList()) }

@Suppress("FunctionName")
fun SkillAdapter_New() = SimpleListAdapter(
	inflate = ItemSkillBinding::inflate,
	areItemsTheSame = { oldItem, newItem -> oldItem.uuid == newItem.uuid },
) { binding, skill: SkillInfo, _ ->

	binding.apply {
		tvSkillName.text = skill.name
		tvPercentage.text = "${skill.percentage} %"
	}
}

//endregion

//region Old way

class SkillsByPersonAdapter_Old(skillsByPerson: Map<Person, List<SkillInfo>>) : ListAdapter<Pair<Person, List<SkillInfo>>, SkillsByPersonAdapter_Old.ViewHolder>(
	object : DiffUtil.ItemCallback<Pair<Person, List<SkillInfo>>>() {
		override fun areItemsTheSame(oldItem: Pair<Person, List<SkillInfo>>, newItem: Pair<Person, List<SkillInfo>>) = oldItem.first.uuid == newItem.first.uuid

		override fun areContentsTheSame(oldItem: Pair<Person, List<SkillInfo>>, newItem: Pair<Person, List<SkillInfo>>) = oldItem == newItem
	}
) {
	init {
		submitList(skillsByPerson.toList())
	}

	class ViewHolder(val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)

		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val (person, skills) = getItem(position)

		holder.binding.apply {
			tvName.text = person.name
			tvLastname.text = person.lastname


			// This is for optimization, creating a new adapter every time the view is binded with the object
			// it's really slow
			val listAdapter = (rvSkills.adapter as? ListAdapter<SkillInfo, RecyclerView.ViewHolder>) ?: SkillAdapter_Old()

			if (rvSkills.adapter == null) rvSkills.adapter = listAdapter

			listAdapter.submitList(skills)
		}
	}
}

class SkillAdapter_Old : ListAdapter<SkillInfo, SkillAdapter_Old.ViewHolder>(
	object : DiffUtil.ItemCallback<SkillInfo>() {
		override fun areItemsTheSame(oldItem: SkillInfo, newItem: SkillInfo) = oldItem.uuid == newItem.uuid

		override fun areContentsTheSame(oldItem: SkillInfo, newItem: SkillInfo) = oldItem == newItem
	}
) {
	class ViewHolder(val binding: ItemSkillBinding) : RecyclerView.ViewHolder(binding.root)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val binding = ItemSkillBinding.inflate(LayoutInflater.from(parent.context), parent, false)

		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val skill = getItem(position)

		holder.binding.apply {
			tvSkillName.text = skill.name
			tvPercentage.text = "${skill.percentage} %"
		}
	}
}

//endregion