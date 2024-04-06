package alves.ariel.pomodoroapp.presentation.fragments

import alves.ariel.pomodoroapp.R
import alves.ariel.pomodoroapp.databinding.FragmentProfileBinding
import alves.ariel.pomodoroapp.domain.Navegacao
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class ProfileFragment : Fragment(R.layout.fragment_profile) {


    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var listProfile : ListView
    private  var itens:ArrayList<String> = arrayListOf(
        "Histórico",
        "Alterar Senha",
        "Deletar Conta",
        "Pague-me um Café",
        "Privacidade"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//  TODO() RECEBER DADOS DO USUARIO JÁ PASSADOS NO LOGIN
//        val usuario = Usuario()
//        binding.tvNameUser.text = usuario.showNomedeUsuario()

        binding.btnLogout.setOnClickListener {
            Logout()
        }

        listProfile = binding.listProfile

        //definir um adapter
        val adaptador : ArrayAdapter<String> = ArrayAdapter(
            requireContext(),
            R.layout.lista_layout,
            R.id.tv_item,
            itens
        )

        //passa lista para o adapter
        listProfile.adapter = adaptador

        listProfile.setOnItemClickListener { parent, _, position, _ ->
            val itemClicado = parent.getItemAtPosition(position) as String
            // TODO( Implementar o comportamento das opções )
            when (itemClicado) {
                "Histórico" -> {
                    // Implemente a funcionalidade para quando "Histórico" for clicado
                    Toast.makeText(requireContext(), "Historico clicado", Toast.LENGTH_SHORT).show()
                }
                "Alterar Senha" -> {
                    // Implemente a funcionalidade para quando "Alterar Senha" for clicado
                    Toast.makeText(requireContext(), "Alterar Senha clicado", Toast.LENGTH_SHORT).show()
                }
                "Deletar Conta" -> {
                    // Implemente a funcionalidade para quando "Deletar Conta" for clicado
                    Toast.makeText(requireContext(), "Deletar Conta clicado", Toast.LENGTH_SHORT).show()
                }
                "Pague-me um Café" -> {
                    // Implemente a funcionalidade para quando "Pague-me um Café" for clicado
                    Toast.makeText(requireContext(), "Pague-me um Café clicado", Toast.LENGTH_SHORT).show()
                }
                "Privacidade" -> {
                    // Implemente a funcionalidade para quando "Privacidade" for clicado
                    Toast.makeText(requireContext(), "Privacidade clicado", Toast.LENGTH_SHORT).show()
                }
            }
        }



    }
    private fun Logout() {
        Firebase.run { auth.signOut() }
        Navegacao(requireContext()).goToLoginScreen()

        Log.d("logout", "Logout realizado")
        Toast.makeText( requireContext(),"Logout Realizado", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
