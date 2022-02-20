package pl.glownia.maciej.wygrajdzien.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.glownia.maciej.wygrajdzien.R
import pl.glownia.maciej.wygrajdzien.database.TaskApp
import pl.glownia.maciej.wygrajdzien.database.TaskDao
import pl.glownia.maciej.wygrajdzien.database.TaskEntity
import pl.glownia.maciej.wygrajdzien.databinding.ActivityAddTasksToDoBinding

class AddTaskToDoActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityAddTasksToDoBinding? = null
    private var mTaskDetails: TaskEntity? = null

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
        // Set up save button
        binding?.btnSave?.setOnClickListener(this)
        binding?.cvFirstCategoryField?.setOnClickListener(this)
        binding?.cvSecondCategoryField?.setOnClickListener(this)
        binding?.cvThirdCategoryField?.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        val taskDao = (application as TaskApp).db.taskDao()
        when (view!!.id) {
            R.id.cvFirstCategoryField -> {
                binding?.ivTaskChosenCategory?.setImageResource(R.drawable.sport)
                binding?.ivTaskChosenCategory?.visibility = View.VISIBLE
            }
            R.id.cvSecondCategoryField -> {
                binding?.ivTaskChosenCategory?.setImageResource(R.drawable.bulb_brain)
                binding?.ivTaskChosenCategory?.visibility = View.VISIBLE
            }
            R.id.cvThirdCategoryField -> {
                binding?.ivTaskChosenCategory?.setImageResource(R.drawable.money)
                binding?.ivTaskChosenCategory?.visibility = View.VISIBLE
            }
            R.id.btnSave -> {
                when {
                    binding?.etTaskTitle?.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Wpisz nazwę zadania.", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        // If a task in database is null add, if exists update it
                        Toast.makeText(
                            this,
                            "It is working now! - checking completed",
                            Toast.LENGTH_SHORT
                        ).show()
                        if (mTaskDetails == null) {
                            // addTask(taskDao)
                        } else {
                            // updateTask(mTaskDetails!!.id, taskDao)
                        }
                        // Clear the entry for next adding
                        binding?.etTaskTitle?.text?.clear()
                    }
                }
                // After click save button go to the main screen where is list of places
                val intent = Intent(this@AddTaskToDoActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}