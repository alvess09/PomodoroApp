package alves.ariel.pomodoroapp.data

import alves.ariel.pomodoroapp.domain.Navegacao
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

class Usuario(val id:String?,
              private var nome: String,
              private val email:String,
              private var senha:String,
              private var foto:Drawable,
              private val context: Context){

    private val user = Firebase.auth.currentUser


    private fun listaUsuario(): String {
        return " Nome:${nome}, Email: $email "
    }

    private fun setNomeUsuario():String{
        return nome
    }
    private fun trocaNomeUsuario(nomeNovo:String):String{

        return nomeNovo.also { this.nome = it }

    }
    private fun setEmailUsuario():String{
        return email
    }
    private fun trocaEmailUsuario(emailNovo: String) {

        //TODO() implementar verificação de email existente e só alterar se o novo email estiver de acordo

        user!!.updateEmail(emailNovo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    makeText(context, "Email alterado com sucesso!", LENGTH_SHORT).show()
                    Log.d(TAG, "User email address updated.")
                }
            }

    }

    private  fun trocaSenhaUsuario(senhaNova:String) {
        //TODO() implementar validações sobre a nova senha

        user!!.updatePassword(senhaNova)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    makeText(context, "Senha alterada com sucesso!", LENGTH_SHORT).show()
                    Log.d(TAG, "User password updated.")
                }
            }
    }

    private fun setFotoUsuario():Drawable {
        return foto
    }
    private fun mudarFotoUsuario(novaFoto:Uri){
        val profileUpdates = userProfileChangeRequest {
            photoUri = Uri.parse("novaFoto")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //TODO() trocar a imagem no fragment
                    Log.d(TAG, "User profile updated.")
                }
            }
    }

    private fun deletarConta(){
        val user = Firebase.auth.currentUser!!

        user.delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    makeText(context, "Conta excluída com sucesso!", LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        Navegacao(context).goToLoginScreen()
                        Log.d(TAG, "User account deleted.")
                    },1000)

                }
            }
    }



}