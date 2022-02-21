package pl.glownia.maciej.wygrajdzien.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.glownia.maciej.wygrajdzien.database.TaskEntity
import pl.glownia.maciej.wygrajdzien.databinding.ActivityTaskDetailsBinding

class TaskDetailsActivity : AppCompatActivity() {
    private var binding: ActivityTaskDetailsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarTaskDetails)

        // Back button
        binding?.toolbarTaskDetails?.setNavigationOnClickListener {
            onBackPressed()
        }

        // Here you retrieve data
        val taskDetails =
            intent.getSerializableExtra(TaskListActivity.EXTRA_PLACE_DETAILS) as TaskEntity

        // Need to set here, so depends on which place we click, in HappyPlaceDetail Activity
        // toolbar title will be same as title of our place
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = taskDetails.title
        }
        // Set up view based on our data
        // TODO ("set up image")

        val introduction =
            "Postawione przed sobą zadanie pozwala Ci określić, co należy " +
                    "teraz zrobić, aby osiągnąć zamierzony cel. " +
                    "\nDzięki skutecznej realizacji zadań \n >>> WYGRYWANIU <<<  \nna pewno tego dokonasz. \n\n Powodzenia!"
        binding?.tvShortIntroduction?.text = introduction
    }
}