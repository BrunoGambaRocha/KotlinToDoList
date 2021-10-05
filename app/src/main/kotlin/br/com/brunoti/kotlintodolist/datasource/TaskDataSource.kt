package br.com.brunoti.kotlintodolist.datasource

import br.com.brunoti.kotlintodolist.model.Task

object TaskDataSource  {
	private var list = mutableListOf<Task>()

	fun getList() = list

	fun insertTask(task: Task) {
		if (task.id == 0) {
			list.add(task.copy(id = list.size + 1))
		} else {
			list.remove(task)
			list.add(task)
		}
	}

	fun findById(taskId: Int) = list.find { it.id == taskId }

	fun deleteTask(task: Task): Boolean {
		if (list.remove(task)) return true
		return false
	}
}