package alves.ariel.pomodoroapp.presentation

import alves.ariel.pomodoroapp.databinding.ActivityCadastroBinding
import alves.ariel.pomodoroapp.domain.Navegacao
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Cadastro : AppCompatActivity() {
    lateinit var binding: ActivityCadastroBinding
    private  var auth: FirebaseAuth = Firebase.auth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCadastroBinding.inflate(layoutInflater)
        val view = binding.root.also {
            setContentView(it)
        }



        binding.btnCadastro.setOnClickListener {

           val nome = binding.itCadastroNome.text.toString()
           val email = binding.itCadastroEmail.text.toString()
           val password = binding.itCadastroSenha.text.toString()
           ValidForm(nome,email,password)


        }

    }
    private fun CreateUserwithEmailAndPassword(email:String, password:String){
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
    private fun ValidForm(nome: String, email: String, password: String) {
        if (nome.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Preencha todos os campos corretamente",Toast.LENGTH_SHORT).show()
            Log.d(TAG,"Cadastro Falhou! Nome: $nome, Email: $email, Senha: $password")
        }else{
            CreateUserwithEmailAndPassword(email, password)
            Toast.makeText(this, "Cadastro Efetuado com Sucesso!", Toast.LENGTH_SHORT).show()
            Navegacao(this).goToLoginScreen()
            finish()
        }

    }

    companion object{
        const val TAG = "FormularioCadastro"

    }
}