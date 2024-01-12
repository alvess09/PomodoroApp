package alves.ariel.pomodoroapp.presentation

import alves.ariel.pomodoroapp.databinding.ActivityMainBinding
import alves.ariel.pomodoroapp.domain.Login
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val auth: FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root.also {
            setContentView(it)
        }

        binding.btnLogout.setOnClickListener {
            Logout()
            GoToLoginScreen()

        }

    }
    private fun Logout(){
        Firebase.run { auth.signOut() }
        Log.d(TAG,"Logout realizado")
    }
    private fun GoToLoginScreen(){
        val i = Intent(this, Login::class.java)
        startActivity(i)
    }
    companion object{
        const val TAG = "LogOut"
    }
}