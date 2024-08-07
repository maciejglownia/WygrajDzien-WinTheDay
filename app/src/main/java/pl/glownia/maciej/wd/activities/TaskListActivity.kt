package pl.glownia.maciej.wd.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.glownia.maciej.wd.R
import pl.glownia.maciej.wd.adapter.TaskAdapter
import pl.glownia.maciej.wd.database.TaskApp
import pl.glownia.maciej.wd.database.TaskDao
import pl.glownia.maciej.wd.database.TaskEntity
import pl.glownia.maciej.wd.databinding.ActivityTaskListBinding
import pl.glownia.maciej.wd.databinding.DialogCustomBackButtonForExitBinding
import pl.glownia.maciej.wd.utils.SwipeToDeleteCallback
import pl.glownia.maciej.wd.utils.SwipeToEditCallback
import java.text.SimpleDateFormat
import java.util.*

class TaskListActivity : AppCompatActivity(), TaskAdapter.OnItemClickListener {
    private var binding: ActivityTaskListBinding? = null

    // Here set up calendar
    private var calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarAddTaskToDo)

        if (supportActionBar != null) {
            supportActionBar?.title = "Wygraj dzień"
        }
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
            // Check fetchAllTasks method in TaskDao interface
            /**
             * Which is a part of the coroutine class, just like to suspend keyword, it runs
             * the operation on a different thread, but with an additional feature of omitting
             * updates automatically when they occur
             *
             * What this allows us to do, is we don't have to tell the RecycleView to update itself
             * It will do that automatically because we're using flow here
             */
            // All tasks -> we get them in form of a list so we can see our List<TaskEntity>
            taskDao.fetchAllTasks().collect {
                val list = ArrayList(it) // IMPORTANT cause here is a list and we need an ArrayList
                setUpListOfDataIntoRecyclerView(list, taskDao)
            }
        }
        // Here a date will be updating automatically
        updateDateInView()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        customDialogForBackButton()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    // After click on setting icon it will take user to the settings activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settingsBtn -> {
                // Toast.makeText(applicationContext, "Click on settings", Toast.LENGTH_LONG).show()
                val intent = Intent(this@TaskListActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Set up custom dialog when user click back button
    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        // We need to create DialogCustomBack...because normal binding which we use here
        // is not connected with our dialog!
        // So here dialog custom confirmation is the name of the XML file, and we just have this
        // binding keyword and we inflate with the layout inflater
        val dialogBinding = DialogCustomBackButtonForExitBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        // Only want to be able to cancel it when click either no or yes - not around it
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            customDialog.dismiss() // Dialog will be dismissed
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    // This will take care of put date to row with date
    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding?.tvDate?.text = sdf.format(calendar.time).toString() // Setting text as String
    }

    private fun setUpListOfDataIntoRecyclerView(
        taskList: ArrayList<TaskEntity>,
        taskDao: TaskDao
    ) {
        // Set up the data
        if (taskList.isNotEmpty()) {
            // Create an item adapter object -> our item adapter class that we just had created
            // and set it up

            // Get an integer, which is the update ID so Update ID, I'm going to use this
            // number again, use my update record dialog percent to update ID
            // as well as the taskDao
            val taskAdapter = TaskAdapter(
                this,
                taskList,
                this
            ) { deleteId ->
                delete(deleteId, taskDao)
            }
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
            // Set up text with info for user how to edit and delete task
            val swipe = "Swipe >>>>> to edit \n <<<<< to delete"
            binding?.tvSwipeHint?.text = swipe
            // If task list becomes bigger (longer) - more than 6 tasks, swipe hint will disappear
            if (taskList.size > 5) {
                binding?.tvSwipeHint?.visibility = View.GONE
            } else {
                // If there is less than 6 tasks user can see hint
                binding?.tvSwipeHint?.visibility = View.VISIBLE
            }
        } else {
            // In other way opposite happens
            binding?.rvTaskList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
            binding?.tvSwipeHint?.visibility = View.GONE
        }

        val editSwipeHandler = object : SwipeToEditCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding?.rvTaskList?.adapter as TaskAdapter
                // Send us to the task list activity screen and which can add it to activity
                adapter.notifyEditItem(
                    this@TaskListActivity,
                    viewHolder.adapterPosition,
                    ADD_TASK_ACTIVITY_REQUEST_CODE
                )
            }
        }
        // Use editSwipeHandler
        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding?.rvTaskList)

        // Now, the activity that will get this information, needs to know about what it
        // should change -> check notify method in adapter

        val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding?.rvTaskList?.adapter as TaskAdapter
                // Use adapter to delete record
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        // Use deleteSwipeHandler
        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(binding?.rvTaskList)
    }

    private fun delete(id: Int, taskDao: TaskDao) {
        lifecycleScope.launch {
            taskDao.delete(TaskEntity(id))
            Toast.makeText(
                applicationContext,
                "Zadanie zostało usunięte.", // Task has been deleted
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onItemClick(position: Int, entity: TaskEntity) {
        //Toast.makeText(this, "Click -> onItemClick", Toast.LENGTH_SHORT).show() // <- way to confirm it works
        val intent = Intent(this@TaskListActivity, TaskDetailsActivity::class.java)
        // That is a little different of a topic, but just to know:
        // If you want to store an object of a class, then you can store it in a serializable way as well
        // But basically that is how we can pass the information
        intent.putExtra(
            EXTRA_TASK_DETAILS,
            entity
        )  // It was needed to add Serializable in TaskEntity -> check it
        startActivity(intent)
    }

    // In TaskDetailsActivity we called TaskListActivity to take task detail
    // Companion object variable, this static variable that we have here that we now
    // can use and can see if it has this specific name, then do something with it
    companion object {
        private const val ADD_TASK_ACTIVITY_REQUEST_CODE = 1
        internal const val EXTRA_TASK_DETAILS = "extra_task_details"
    }
//        }
}