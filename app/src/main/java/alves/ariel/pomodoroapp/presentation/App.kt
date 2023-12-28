package alves.ariel.pomodoroapp.presentation

import alves.ariel.pomodoroapp.databinding.ActivityAppBinding
import alves.ariel.pomodoroapp.domain.Login
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class App : AppCompatActivity() {
     private lateinit var binding : ActivityAppBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        val view = binding.root.also {
            setContentView(it)

        }


        nextScreen()
        val btnNextScreen = binding.btnNextScreen.setOnClickListener(){
            nextScreenPost()
        }

    }

    private fun nextScreenPost(): View.OnClickListener? {
        val nextScreen = Intent(this,Login::class.java)
        startActivity(nextScreen)
        return null
    }

    private fun nextScreen(){
        val nextScreen = Intent(this,Login::class.java)
        //delay para passagem de activity
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(nextScreen)}, 3000)
    }


}