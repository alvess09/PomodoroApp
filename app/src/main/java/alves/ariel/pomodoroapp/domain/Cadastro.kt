package alves.ariel.pomodoroapp.domain

import alves.ariel.pomodoroapp.databinding.ActivityCadastroBinding
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Cadastro : AppCompatActivity() {
    lateinit var binding: ActivityCadastroBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCadastroBinding.inflate(layoutInflater)
        val view = binding.root.also {
            setContentView(it)
        }
        val auth = Firebase.auth

    }
    fun CreateUserwithEmailAndPassword(email:String, password:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    //val user = auth.currentUser

                    Toast.makeText(
                        baseContext,
                        "Cadastro realizado com sucesso!",
                        Toast.LENGTH_SHORT,
                    ).show()

                    val i =  Intent(this, Login::class.java)
                    startActivity(i)

                } else {
                    Log.w(ContentValues.TAG,"createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }


    }

}