package alves.ariel.pomodoroapp.presentation

import alves.ariel.pomodoroapp.R
import alves.ariel.pomodoroapp.databinding.ActivityMainBinding
import alves.ariel.pomodoroapp.domain.Login
import alves.ariel.pomodoroapp.presentation.ui.EditTasksFragment
import alves.ariel.pomodoroapp.presentation.ui.HomeFragment
import alves.ariel.pomodoroapp.presentation.ui.ProfileFragment
import alves.ariel.pomodoroapp.presentation.ui.SettingsFragment
import alves.ariel.pomodoroapp.presentation.ui.TasksFragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import nl.joery.animatedbottombar.AnimatedBottomBar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var fragmentManager: FragmentManager
    private lateinit var bottom_nav: AnimatedBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root.also {
            setContentView(it)
        }

        /* binding.btnLogout.setOnClickListener {
             Logout()
             GoToLoginScreen()

         }*/


        //capturar o clique dos botões de navegação da barra e direcionar para os fragments corretos


        // Criação dos fragments
        val homeFragment = HomeFragment()
        val tasksFragment = TasksFragment()
        val editTasksFragment = EditTasksFragment()
        val settingsFragment = SettingsFragment()
        val profileFragment = ProfileFragment()

        bottom_nav = binding.bottomNavigation
        // Configuração do escutador de seleção de tab
         bottom_nav.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
                    override fun onTabSelected(
                        lastIndex: Int,
                        lastTab: AnimatedBottomBar.Tab?,
                        newIndex: Int,
                        newTab: AnimatedBottomBar.Tab
                    ) {
                        // Troca o fragment de acordo com a tab selecionada
                        val selectedFragment = when (newIndex) {
                          0 -> homeFragment
                          1 -> tasksFragment
                          2 -> editTasksFragment
                          3 -> settingsFragment
                          4 -> profileFragment
                          else -> null
                        }

                        // Realiza a troca do fragment
                        selectedFragment?.let { goToFragment(it) }

                        Log.d("bottom_bar", "Selected index: $newIndex, title: ${newTab.title}")
                    }

                    override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                        // Código para lidar com a reseleção de um item
                        Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
                    }


         })



    }

    private fun goToFragment(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()


    }

    private fun Logout() {
        Firebase.run { auth.signOut() }
        Log.d(TAG, "Logout realizado")
    }

    private fun GoToLoginScreen() {
        val i = Intent(this, Login::class.java)
        this.startActivity(i)
    }

    private companion object {
        const val TAG = "LogOut"
    }

}



