package com.example.todolisttutorial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalTime

class TaskViewModel: ViewModel()
{
    var taskItems = MutableLiveData<MutableList<Task>>()

    init {
        taskItems.value = mutableListOf()
    }

    fun addTask(newTask: Task)
    {
        val list = taskItems.value
        list!!.add(newTask)
        taskItems.postValue(list)
    }

    fun updateTask(id: String, name: String, desc: String, dueTime: LocalTime?)
    {
        val list = taskItems.value
        val task = list!!.find { it.id == id }!!
        task.name = name
        task.desc = desc
        task.dueTime = dueTime
        taskItems.postValue(list)
    }

    fun setStatus(taskItem: Task)
    {
        val list = taskItems.value
        val task = list!!.find { it.id == taskItem.id }!!
        if (task.completedDate == "")
            task.completedDate = LocalDate.now().toString()
        else task.completedDate = ""
        taskItems.postValue(list)
    }

    fun deleteTask(id: String) {
        val list = taskItems.value
        list!!.remove(list.find { it.id == id }!!)
        taskItems.postValue(list)
    }
    fun createTaskJson():String{
        val json = JSONObject()
        val list = taskItems.value
        if (list != null) {
            for (item in list){
                json.put(item.id.toString(),addTaskJson(item))
            }
        }
        return json.toString()
    }
    private fun addTaskJson(taskItem: Task):JSONObject{
        return JSONObject()
            .put("task", taskItem.name)
            .put("description", taskItem.desc)
            .put("time", taskItem.dueTime)
            .put("status", taskItem.completedDate)
            .put("id", taskItem.id)
    }

    fun parseFile(json: String) {
        val list: MutableList<Task> = mutableListOf()
        val data = JSONObject(json)

        for (key in data.keys()) {
            val task: Task = Task("", "", null, "")
            val obj = data.getJSONObject(key)
            task.desc = obj.getString("description")
            task.name = obj.getString("task")
            task.id = obj.getString("id")
            if (obj.has("time")){
                task.dueTime = LocalTime.parse(obj.getString("time"))
            }
            if(obj.has("status")){
                task.completedDate = obj.getString("status")
            }
            list.add(task)
        }
        taskItems.postValue(list)
    }
}