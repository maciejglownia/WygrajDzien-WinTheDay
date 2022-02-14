package pl.glownia.maciej.wygrajdzien

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.glownia.maciej.wygrajdzien.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabAddTask?.setOnClickListener {
            val intent = Intent(this@MainActivity, AddTasksToDo::class.java)
            startActivity(intent)
        }
    }
}