package alves.ariel.pomodoroapp.data

import alves.ariel.pomodoroapp.domain.Navegacao
import android.content.ContentValues.TAG
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

class Usuario(private val context: Context){
    private val user = Firebase.auth.currentUser
    private var nome: String? = "Usuário"
     fun listaUsuario(): ArrayList<String?> {

        val id: String? = user?.uid
        val nome: String? = nome
        val email: String? = user?.email
        val foto: Uri? = user?.photoUrl
       return arrayListOf( id, nome, email, foto.toString() )
     }


     fun trocaNomeUsuario(nomeNovo:String):String{

        return nomeNovo.also { this.nome = it }
    }

     fun trocaEmailUsuario(emailNovo: String) {

        //TODO() implementar verificação de email existente e só alterar se o novo email estiver de acordo

        user!!.updateEmail(emailNovo)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    makeText(context, "Email alterado com sucesso!", LENGTH_SHORT).show()
                    Log.d(TAG, "User email address updated.")
                }
            }

    }

     fun trocaSenhaUsuario(senhaNova:String) {
        //TODO() implementar validações sobre a nova senha

        user!!.updatePassword(senhaNova)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    makeText(context, "Senha alterada com sucesso!", LENGTH_SHORT).show()
                    Log.d(TAG, "User password updated.")
                }
            }
    }

    private fun setFotoUsuario(): Uri? {
        val fotoPadrao:Uri? = user?.photoUrl
        var fotoAtual: Uri? = null

        if (fotoPadrao == null){
            // não altera a foto atual
        }else{
            //recupera a foto do banco de dados
            fotoAtual = user!!.photoUrl!!
        }
        return fotoAtual
    }

     fun trocaFotoUsuario(novaFoto:Uri){
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

     fun deletarConta(){
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