package pl.glownia.maciej.wygrajdzien.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.glownia.maciej.wygrajdzien.databinding.ActivityTaskListBinding

class TaskListActivity : AppCompatActivity() {
    private var binding: ActivityTaskListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabAddTask?.setOnClickListener {
            val intent = Intent(this@TaskListActivity, AddTaskToDoActivity::class.java)
            startActivity(intent)
        }
    }
}