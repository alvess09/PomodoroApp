package alves.ariel.pomodoroapp.domain

import alves.ariel.pomodoroapp.presentation.Cadastro
import alves.ariel.pomodoroapp.presentation.Login
import alves.ariel.pomodoroapp.presentation.MainActivity
import android.content.Context
import android.content.Intent

class Navegacao(private val context: Context ){

    fun goToHomeScreen() {
        val intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }
    fun goToLoginScreen() {
        val intent = Intent(context, Login::class.java)
        context.startActivity(intent)
    }
    fun goToCadastroScreen() {
        val intent = Intent(context, Cadastro::class.java)
        context.startActivity(intent)
    }


}