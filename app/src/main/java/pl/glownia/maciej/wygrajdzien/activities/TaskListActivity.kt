package pl.glownia.maciej.wygrajdzien.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.glownia.maciej.wygrajdzien.adapter.TaskAdapter
import pl.glownia.maciej.wygrajdzien.database.TaskApp
import pl.glownia.maciej.wygrajdzien.database.TaskEntity
import pl.glownia.maciej.wygrajdzien.databinding.ActivityTaskListBinding

class TaskListActivity : AppCompatActivity() {
    private var binding: ActivityTaskListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Set up taskDao using TaskApp
        // This one here db gets database object and use the taskDao, which is
        // an abstract function that we created inside of our room database -> TaskDatabase.kt
        val taskDao = (application as TaskApp).db.taskDao()

        binding?.fabAddTask?.setOnClickListener {
            val intent = Intent(this@TaskListActivity, AddTaskToDoActivity::class.java)
            startActivity(intent)
        }

        // setUpListOfDataIntoRecyclerView() needs to go in the background
        lifecycleScope.launch {
            // With collect method, we now get the list of task entity items that are
            // inside of our database
            // It's going to give us all the entries that we have in our tasks table
            // Check fetchAllPlaces method in TaskDao interface
            /**
             * Which is a part of the coroutine class, just like to suspend keyword, it runs
             * the operation on a different thread, but with an additional feature of omitting
             * updates automatically when they occur
             *
             * What this allows us to do, is we don't have to tell the RecycleView to update itself
             * It will do that automatically because we're using flow here
             */
            // All tasks -> we get them in form of a list so we can see our List<TaskEntity>
            taskDao.fetchAllPlaces().collect {
                val list = ArrayList(it) // IMPORTANT cause here is a list and we need an ArrayList
                setUpListOfDataIntoRecyclerView(list)
            }
        }
    }

    private fun setUpListOfDataIntoRecyclerView(
        taskList: ArrayList<TaskEntity>,
    ) {
        // Set up the data
        if (taskList.isNotEmpty()) {
            // Create an item adapter object -> our item adapter class that we just had created
            // and set it up

            // Get an integer, which is the update ID so Update ID, I'm going to use this
            // number again, use my update record dialog percent to update ID
            // as well as the taskDao
            val taskAdapter = TaskAdapter(
                taskList
            )
            // To display our content from database -> need LinearLayoutManager
            // Set up the RecyclerView -> items are going to be on top of each other
            binding?.rvTaskList?.layoutManager = LinearLayoutManager(this)
            // Assigning these itemAdapter that was just created above as the adapter for my
            // RecyclerView (where we will find our items)
            binding?.rvTaskList?.adapter = taskAdapter
            // Make it visible
            binding?.rvTaskList?.visibility = View.VISIBLE
            // Then if there is data, sentence no records are available should be set to gone
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        } else {
            // In other way opposite happens
            binding?.rvTaskList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }
}