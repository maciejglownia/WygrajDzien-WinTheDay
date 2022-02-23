package pl.glownia.maciej.wygrajdzien.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.glownia.maciej.wygrajdzien.R
import pl.glownia.maciej.wygrajdzien.database.TaskApp
import pl.glownia.maciej.wygrajdzien.database.TaskDao
import pl.glownia.maciej.wygrajdzien.database.TaskEntity
import pl.glownia.maciej.wygrajdzien.databinding.ActivityAddTasksToDoBinding

// View.OnClickListener -> make whole class -> also need to implement members (onClick())
class AddTaskToDoActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityAddTasksToDoBinding? = null
    private var mTaskDetails: TaskEntity? = null
    private var mChosenCategoryImage: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTasksToDoBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarAddTaskToDo)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        // Back button
        binding?.toolbarAddTaskToDo?.setNavigationOnClickListener {
            onBackPressed()
        }
        // Set up save button and category items to click
        binding?.btnSave?.setOnClickListener(this)
        binding?.cvFirstCategoryField?.setOnClickListener(this)
        binding?.cvSecondCategoryField?.setOnClickListener(this)
        binding?.cvThirdCategoryField?.setOnClickListener(this)

        // Create a new entry
        if (intent.hasExtra(TaskListActivity.EXTRA_TASK_DETAILS)) {
            mTaskDetails =
                intent.getSerializableExtra(TaskListActivity.EXTRA_TASK_DETAILS) as TaskEntity
        }

        // If mTaskDetails is not null, then we know that we are editing because if it is not,
        // then we know  that we are just creating a new entry
        // After swip it to edit it send you to the AddTaskToDoActivity, but it will change
        // toolbar to name: edit task and put data to rows, so you can edit it
        if (mTaskDetails != null) {
            supportActionBar?.title = "Edytuj zadanie" // Edit Task
            val update = "Zaktualizuj" // Update
            binding?.btnSave?.text = update
        }
    }

    override fun onClick(view: View?) {
        val taskDao = (application as TaskApp).db.taskDao()
        when (view!!.id) {
            R.id.cvFirstCategoryField -> {
                binding?.ivTaskChosenCategory?.setImageResource(R.drawable.sport)
                binding?.ivTaskChosenCategory?.visibility = View.VISIBLE
                mChosenCategoryImage = 1
            }
            R.id.cvSecondCategoryField -> {
                binding?.ivTaskChosenCategory?.setImageResource(R.drawable.bulb_brain)
                binding?.ivTaskChosenCategory?.visibility = View.VISIBLE
                mChosenCategoryImage = 2
            }
            R.id.cvThirdCategoryField -> {
                binding?.ivTaskChosenCategory?.setImageResource(R.drawable.money)
                binding?.ivTaskChosenCategory?.visibility = View.VISIBLE
                mChosenCategoryImage = 3
            }
            R.id.btnSave -> {
                when {
                    binding?.etTaskTitle?.text.isNullOrEmpty() -> {
                        Toast.makeText(
                            this,
                            "Wpisz nazwę zadania.", // Add task name
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding?.ivTaskChosenCategory?.isVisible == false -> {
                        Toast.makeText(
                            this,
                            "Wybierz kategorię.", // Choose category
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        // If a task in database is null add, if exists update it
                        //Toast.makeText( this, "It works", Toast.LENGTH_SHORT).show()
                        if (mTaskDetails == null) {
                            addTask(taskDao)
                        } else {
                            updateTask(mTaskDetails!!.id, taskDao)
                        }
                        // Clear the entry for next adding
                        binding?.etTaskTitle?.text?.clear()
                        binding?.ivTaskChosenCategory?.visibility = View.GONE
                        // After click save button go to the main screen where is list of places
                        val intent = Intent(this@AddTaskToDoActivity, TaskListActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    // Add all details, change them to the String and save it in the database
    private fun addTask(taskDao: TaskDao) {
        val taskTitle = binding?.etTaskTitle?.text.toString()
        val categoryImageNumber = giveChosenCategoryNumberToPass()
        // Insert data (need to do in the background) -> lifecycleScope
        lifecycleScope.launch {
            taskDao.insert(
                TaskEntity(
                    title = taskTitle, image = categoryImageNumber
                )
            )
            // Let user know what happened
            Toast.makeText(
                applicationContext,
                "Zadanie utworzone.", // Task has been created
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Update all details, change them to the String and save it in the database
    private fun updateTask(id: Int, taskDao: TaskDao) {
        val taskTitle = binding?.etTaskTitle?.text.toString()
        val categoryImageNumber = giveChosenCategoryNumberToPass()

        lifecycleScope.launch {
            taskDao.update(
                TaskEntity(
                    id = id, title = taskTitle, image = categoryImageNumber
                )
            )
            // Let user know what happened
            Toast.makeText(
                applicationContext,
                "Zadanie zaktualizowane.", // Task has been updating
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Depends on which category(image) user choose it has unique number and this number
    // is passing to database as String
    private fun giveChosenCategoryNumberToPass(): String {
        val chosenCategoryNumberToPass: String = when (mChosenCategoryImage) {
            1 -> {
                "1"
            }
            2 -> {
                "2"
            }
            else -> {
                "3"
            }
        }
        return chosenCategoryNumberToPass
    }
}
