package alves.ariel.pomodoroapp.presentation

import alves.ariel.pomodoroapp.R
import alves.ariel.pomodoroapp.databinding.ActivityMainBinding
import alves.ariel.pomodoroapp.presentation.fragments.EditTasksFragment
import alves.ariel.pomodoroapp.presentation.fragments.HomeFragment
import alves.ariel.pomodoroapp.presentation.fragments.ProfileFragment
import alves.ariel.pomodoroapp.presentation.fragments.SettingsFragment
import alves.ariel.pomodoroapp.presentation.fragments.TasksFragment
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
    private val fragmentStack = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Inicialização dos fragments
        val homeFragment = HomeFragment()
        val tasksFragment = TasksFragment()
        val editTasksFragment = EditTasksFragment()
        val settingsFragment = SettingsFragment()
        val profileFragment = ProfileFragment()

        //Configuração do FragmentManager
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer,homeFragment)
            commit()
        }
        fragmentStack.add(homeFragment)



        //capturar o clique dos botões de navegação da barra e direcionar para os fragments corretos
        bottom_nav = binding.bottomNavigation
        // Configuração do escutador de seleção de tab
        bottom_nav.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                // Troca o fragment de acordo com o indice da tab selecionada
                when (newIndex) {
                    0 -> showFragment(homeFragment)
                    1 -> showFragment(tasksFragment)
                    2 -> showFragment(editTasksFragment)
                    3 -> showFragment(settingsFragment)
                    4 -> showFragment(profileFragment)
                }

                // Realiza a troca do fragment
//                selectedFragment?.let { goToFragment(it) }

                Log.d("bottom_bar", "Selected index: $newIndex, title: ${newTab.title}")
            }

            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                // Código para lidar com a reseleção de um item
                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }


        })


    }
    private fun showFragment(fragment: Fragment) {
        fragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null) // Adiciona o fragment à pilha
            commit()
        }
        fragmentStack.add(fragment)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (fragmentStack.size > 1) {
            // Remove o fragment atual da pilha
            fragmentStack.removeAt(fragmentStack.size - 1)
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
//    private fun goToFragment(fragment: Fragment) {
//        //faz o gerenciamento dos fragments
//        this.fragmentManager = supportFragmentManager
//        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
//
//
//    }


}


