package pl.glownia.maciej.wygrajdzien

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.glownia.maciej.wygrajdzien.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Go to the next activity where is task list and possible to ask a new task
        binding?.btnStart?.setOnClickListener {
            val intent = Intent(this@MainActivity, TaskListActivity::class.java)
            startActivity(intent)
        }
    }
}