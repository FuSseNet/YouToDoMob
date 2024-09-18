package com.example.todolisttutorial

interface ITaskItem
{
    fun editTaskItem(taskItem: Task)
    fun completeTaskItem(taskItem: Task)
    fun deleteTaskItem(taskItem: Task)
    fun createJsonData()
    fun readFile()
}