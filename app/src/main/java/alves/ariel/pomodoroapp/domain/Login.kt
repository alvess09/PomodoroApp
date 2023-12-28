package alves.ariel.pomodoroapp.domain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import alves.ariel.pomodoroapp.R
import alves.ariel.pomodoroapp.databinding.ActivityLoginBinding
import android.widget.ImageView

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root.also {
            setContentView(it)
        }



    }
}