package alves.ariel.pomodoroapp.presentation

import alves.ariel.pomodoroapp.R
import alves.ariel.pomodoroapp.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root.also {
            setContentView(it)
        }

    }
}