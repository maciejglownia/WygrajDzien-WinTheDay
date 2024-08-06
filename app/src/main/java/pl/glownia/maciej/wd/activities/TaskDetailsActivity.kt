package pl.glownia.maciej.wd.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.glownia.maciej.wd.R
import pl.glownia.maciej.wd.database.TaskEntity
import pl.glownia.maciej.wd.databinding.ActivityTaskDetailsBinding

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
            intent.getSerializableExtra(TaskListActivity.EXTRA_TASK_DETAILS) as TaskEntity

        // Need to set here, so depends on which task we click, in TaskDetailsActivity
        // toolbar title will be same as title of our task
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = taskDetails.title
        }
        // Set up view based on our data
        // So, depends what number is store in database for specific record this image will appear
        when (taskDetails.image) {
            "1" -> {
                binding?.ivTaskCategoryImage?.setImageResource(R.drawable.sport)
            }
            "2" -> {
                binding?.ivTaskCategoryImage?.setImageResource(R.drawable.bulb_brain)
            }
            else -> {
                binding?.ivTaskCategoryImage?.setImageResource(R.drawable.money)
            }
        }

        // Below an image of category user can see extra information or sentence to motivate them
        val introduction =
            "Postawione przed tobą zadania pozwalą Ci określić, " +
                    "jakie czynności należy wykonać w tej chwili, " +
                    "aby osiągnąć zamierzony cel." +
                    "\nDzięki ich skutecznej realizacji \n >>> WYGRYWANIU <<<  \nna pewno tego dokonasz."
        binding?.tvShortIntroduction?.text = introduction

        // Below an image of category user can see extra information or sentence to motivate them
        val quote =
            "''Gdy nie wiesz, do którego portu płyniesz, \nżaden wiatr nie jest dobry.''"
        binding?.tvQuote?.text = quote

        // Below an image of category user can see extra information or sentence to motivate them
        val author = "\n\t\t\t\t\t\t\t\t\t\t\tSeneka"
        binding?.tvAuthor?.text = author

    }


}