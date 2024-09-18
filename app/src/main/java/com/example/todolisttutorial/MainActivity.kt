package com.example.todolisttutorial

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolisttutorial.databinding.ActivityMainBinding
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity(), ITaskItem
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        binding.newTaskButton.setOnClickListener {
            NewTask(null).show(supportFragmentManager, "newTaskTag")
        }
        binding.saveButton.setOnClickListener{
            createJsonData()
        }
        binding.loadButton.setOnClickListener {
            readFile()
        }
        setRecyclerView()
    }

    private fun setRecyclerView()
    {
        val mainActivity = this
        taskViewModel.taskItems.observe(this){
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskAdapter(it, mainActivity)
            }
        }
    }

    override fun editTaskItem(taskItem: Task)
    {
        NewTask(taskItem).show(supportFragmentManager,"newTaskTag")
    }

    override fun deleteTaskItem(taskItem: Task)
    {
        taskViewModel.deleteTask(taskItem.id)
    }

    override fun completeTaskItem(taskItem: Task)
    {
        taskViewModel.setStatus(taskItem)
    }

    override fun createJsonData() {
        val writeIntent = Intent().setType("file/json").setAction(Intent.ACTION_CREATE_DOCUMENT)
        writeIntent.putExtra(Intent.EXTRA_TITLE, "ToDo.json")
        startActivityForResult(writeIntent, 13)
    }

    private fun saveJson(jsonString:String, path: Uri){
        val outputStream = OutputStreamWriter(contentResolver.openOutputStream(path))
        outputStream.write(jsonString)
        outputStream.close()
    }

    override fun readFile() {
        val readIntent = Intent().setType("application/json").setAction(Intent.ACTION_GET_CONTENT)
        readIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(readIntent, 10)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK)
            {
                val path = data?.data
                if (path != null)
                {
                    val jsonSelectedFile = contentResolver.openInputStream(path);
                    val inputAsString = jsonSelectedFile!!.bufferedReader().use { it.readText() }
                    taskViewModel.parseFile(inputAsString)
                }
            }
        }
        else if (requestCode == 13){
            if (resultCode == Activity.RESULT_OK){
                val path = data?.data
                if (path != null)
                {
                    val json = taskViewModel.createTaskJson()
                    saveJson(json, path)
                }
            }
        }
    }
}







