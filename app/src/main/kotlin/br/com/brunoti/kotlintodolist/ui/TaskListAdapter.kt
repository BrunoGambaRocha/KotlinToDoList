package br.com.brunoti.kotlintodolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.brunoti.kotlintodolist.R
import br.com.brunoti.kotlintodolist.databinding.ItemTaskBinding
import br.com.brunoti.kotlintodolist.model.Task

class TaskListAdapter(val taskList: MutableList<Task>) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(Task.DIFF_CALLBACK) {
	var listenerEdit: (Task) -> Unit = {}
	var listenerDelete: (Task) -> Unit = {}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = ItemTaskBinding.inflate(inflater, parent, false)

		return TaskViewHolder(binding)
	}

	override fun submitList(list: List<Task?>?) {
		super.submitList(list?.let { ArrayList(it) })
	}

	override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
		holder.bind(taskList[position])
	}

	override fun getItemCount(): Int {
		return taskList.size
	}

	inner class TaskViewHolder(
		private val binding: ItemTaskBinding
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(item: Task) {
			binding.tvTitle.text = item.title
			binding.tvDate.text = "${item.date} ${item.hour}"
			binding.ivMore.setOnClickListener {
				showPopup(item)
			}
		}

		private fun showPopup(item: Task) {
			val ivMore = binding.ivMore
			val popupMenu = PopupMenu(ivMore.context, ivMore)

			popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
			popupMenu.setOnMenuItemClickListener {
				when (it.itemId) {
					R.id.action_edit -> listenerEdit(item)
					R.id.action_delete -> listenerDelete(item)
				}

				return@setOnMenuItemClickListener true
			}
			popupMenu.show()
		}
	}
}