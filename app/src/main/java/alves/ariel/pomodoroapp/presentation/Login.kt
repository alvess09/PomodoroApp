package alves.ariel.pomodoroapp.presentation

import alves.ariel.pomodoroapp.databinding.ActivityLoginBinding
import alves.ariel.pomodoroapp.domain.Navegacao
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
            val password:String  = binding.itSenha.text.toString()

            Log.d(TAG,"$email and $password")

            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(baseContext, "Preencha os campos: Email e Senha", Toast.LENGTH_SHORT)
                    .show()
            }else{
                LogInWithEmailAndPassword(email, password)
            }

        }



        binding.tvResetSenha.setOnClickListener {

            val email =  binding.itEmail.text.toString()
            ResetPassword(email)
        }

        binding.tvCadastro.setOnClickListener {
            //GoToCadastroScreen()
            Navegacao(this).goToCadastroScreen()
        }

//        binding.ivBtnGoogle.setOnClickListener {
//            TODO()
//            //LogInWithGoogle()
//        }
//
//        binding.ivBtnFacebook.setOnClickListener {
//            TODO()
//            //LogInWithFacebook()
//        }
//
//        binding.ivBtnApple.setOnClickListener {
//            TODO()
//            //LogInWithApple()
//        }


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
                    Navegacao(this).goToHomeScreen()
                    finish()

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
    private fun ResetPassword(email: String){
        if (email.isEmpty()){
            Toast.makeText(this, "Preencha o campo email e clique em esqueci minha senha para redefinição de senha", Toast.LENGTH_SHORT).show()
            Log.d(TAG,"Failure to send email message redefinition cause: Email: $email, is Empty.")
        }else {

            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Um email de redefinição de senha foi enviado para $email .", Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Email sent.")
                    }
                }

        }

    }
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



    companion object{
        const val TAG = "LoginWithEmail"
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser

        if (usuarioAtual != null){

            Navegacao(this).goToHomeScreen()
            finish()
        }


    }




}