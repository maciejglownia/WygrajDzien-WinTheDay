package pl.glownia.maciej.wygrajdzien

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.glownia.maciej.wygrajdzien.databinding.ActivityAddTasksToDoBinding

class AddTasksToDoActivity : AppCompatActivity() {
    private var binding: ActivityAddTasksToDoBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTasksToDoBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarAddTasksToDo)

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        // Back button
        binding?.toolbarAddTasksToDo?.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}