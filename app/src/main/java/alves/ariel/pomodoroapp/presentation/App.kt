package alves.ariel.pomodoroapp.presentation

import alves.ariel.pomodoroapp.databinding.ActivityAppBinding
import alves.ariel.pomodoroapp.domain.Navegacao
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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

        //definir um tempo para passar automatico
        val btnNextScreen = binding.btnNextScreen

        //delay para passagem de activity
        Handler(Looper.getMainLooper()).postDelayed({

            if (btnNextScreen.isPressed){
                btnNextScreen.setOnClickListener(){
                    nextScreenPost()
                    Log.i("btnProximaTela", "onCreate: Chamando proxima tela")
                }
            }else{
                nextScreenPost()
                Log.i("btnProximaTela", "onCreate: Chamando proxima tela sem pressionar o botão")
            }

        }, 3000)


    }

    private fun nextScreenPost(): View.OnClickListener? {
        Navegacao(this).goToLoginScreen()
        finish()
        return null
    }




}