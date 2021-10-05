package br.com.brunoti.kotlintodolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.brunoti.kotlintodolist.databinding.ActivityMainBinding
import br.com.brunoti.kotlintodolist.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {
	var taskList = TaskDataSource.getList()
	private lateinit var binding: ActivityMainBinding
	private val adapter by lazy { TaskListAdapter(taskList) }

	/**
	 * Nova maneira de iniciar uma activity.
	 * Já que `startActivityForResult` foi depreciado.
	 */
	private val register =
		registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
			if (it.resultCode == Activity.RESULT_OK)
				updateList()
		}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.rvTasks.layoutManager = LinearLayoutManager(this)
		binding.rvTasks.adapter = adapter

		updateList()

		insertListeners()
	}

	private fun insertListeners() {
		binding.fab.setOnClickListener {
			register.launch(Intent(this, SaveTaskActivity::class.java))
		}

		adapter.listenerEdit = {
			val intent = Intent(this, SaveTaskActivity::class.java)

			intent.putExtra(SaveTaskActivity.TASK_ID, it.id)
			register.launch(intent)
		}

		adapter.listenerDelete = {
			if (TaskDataSource.deleteTask(it))
				updateList()
		}
	}

	private fun updateList() {
		taskList = TaskDataSource.getList()

		binding.includeEmpty.emptyState.visibility =
			if (taskList.isEmpty()) View.VISIBLE
			else View.GONE

		adapter.submitList(null) // TODO: remove this hack when implements room
		adapter.submitList(taskList.toList())
	}
}