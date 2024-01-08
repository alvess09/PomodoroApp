package alves.ariel.pomodoroapp.domain

import alves.ariel.pomodoroapp.databinding.ActivityLoginBinding
import alves.ariel.pomodoroapp.presentation.MainActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private  val auth: FirebaseAuth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root.also {
            setContentView(it)
        }


        binding.btnLogin.setOnClickListener {
            val email:String = binding.itEmail.text.toString()
            val password:String  = binding.textInputSenha.text.toString()

            Log.d(TAG,"$email and $password")

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(baseContext, "Preencha os campos: Email e Senha", Toast.LENGTH_SHORT)
                    .show()
            }else{
                LogInWithEmailAndPassword(email, password)
            }

        }

        binding.tvResetSenha.setOnClickListener {
            TODO()
            //ResetPassword()
        }

        binding.tvCadastro.setOnClickListener {
            GoToCadastroScreen()
        }

        binding.ivBtnGoogle.setOnClickListener {
            TODO()
            //LogInWithGoogle()
        }

        binding.ivBtnFacebook.setOnClickListener {
            TODO()
            //LogInWithFacebook()
        }

        binding.ivBtnApple.setOnClickListener {
            TODO()
            //LogInWithApple()
        }


    }
    private fun LogInWithEmailAndPassword(email:String, password:String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    Toast.makeText(
                        baseContext,
                        "Login realizado com sucesso!",
                        Toast.LENGTH_SHORT,
                    ).show()
                    Log.d(TAG, "signInWithEmail:success")
                    //val user = this.auth.currentUser
                    GoToHomeScreen()

                } else {

                    Toast.makeText(
                        baseContext,
                        "Email ou Senha Inválidos",
                        Toast.LENGTH_SHORT,
                    ).show()

                    Log.w(TAG,"signInWithEmail:failure", task.exception)


                }
            }
    }
    private fun ResetPassword(){}
    private fun LogInWithGoogle(){
        //method logIN
       /* .addOnCompleteListener(this){task ->
            if (task.isSuccessful){
                Toast.makeText(
                    baseContext,
                    "Login realizado com sucesso!",
                    Toast.LENGTH_SHORT,
                ).show()
                Log.d(TAG, "signInWithEmail:success")
                //val user = this.auth.currentUser
                GoToHomeScreen()

            } else {

                Toast.makeText(
                    baseContext,
                    "Email ou Senha Inválidos",
                    Toast.LENGTH_SHORT,
                ).show()

                Log.w(TAG,"signInWithEmail:failure", task.exception)


            }
        } */
    }
    private fun LogInWithFacebook(){
        //method logIN
        /* .addOnCompleteListener(this){task ->
             if (task.isSuccessful){
                 Toast.makeText(
                     baseContext,
                     "Login realizado com sucesso!",
                     Toast.LENGTH_SHORT,
                 ).show()
                 Log.d(TAG, "signInWithEmail:success")
                 //val user = this.auth.currentUser
                 GoToHomeScreen()

             } else {

                 Toast.makeText(
                     baseContext,
                     "Email ou Senha Inválidos",
                     Toast.LENGTH_SHORT,
                 ).show()

                 Log.w(TAG,"signInWithEmail:failure", task.exception)


             }
         } */
    }
    private fun LogInWithApple(){
        //method logIN
        /* .addOnCompleteListener(this){task ->
             if (task.isSuccessful){
                 Toast.makeText(
                     baseContext,
                     "Login realizado com sucesso!",
                     Toast.LENGTH_SHORT,
                 ).show()
                 Log.d(TAG, "signInWithEmail:success")
                 //val user = this.auth.currentUser
                 GoToHomeScreen()

             } else {

                 Toast.makeText(
                     baseContext,
                     "Email ou Senha Inválidos",
                     Toast.LENGTH_SHORT,
                 ).show()

                 Log.w(TAG,"signInWithEmail:failure", task.exception)


             }
         } */
    }
    private fun GoToHomeScreen() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }
    private fun GoToCadastroScreen() {
        val i = Intent(this, Cadastro::class.java)
        startActivity(i)
        finish()
    }


    companion object{
        const val TAG = "LoginWithEmail"
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null){
            GoToHomeScreen()
        }


    }




}