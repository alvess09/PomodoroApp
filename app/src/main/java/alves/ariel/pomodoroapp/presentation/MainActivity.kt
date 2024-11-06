package alves.ariel.pomodoroapp.presentation

import alves.ariel.pomodoroapp.R
import alves.ariel.pomodoroapp.databinding.ActivityMainBinding
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var bottom_nav: AnimatedBottomBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração do NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        // Configurar Bottom Navigation
        bottom_nav = binding.bottomBarNavigation

        // Listener para seleção das abas
        bottom_nav.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                // Navegar para o novo destino selecionado
                when (newTab.id) {
                    R.id.nav_home -> navController.navigate(R.id.fragment_home)
                    R.id.nav_tasks -> navController.navigate(R.id.fragment_tasks)
                    R.id.nav_edtasks -> navController.navigate(R.id.fragment_edit_tasks)
                    R.id.nav_settings -> navController.navigate(R.id.fragment_settings)
                    R.id.nav_profile -> navController.navigate(R.id.fragment_profile)
                }
            }
        })

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (!navController.popBackStack()) {
                    finish()
                }
            else {
                // Atualizar o bottom navigation com o destino atual
                when (navController.currentDestination?.id) {
                    R.id.fragment_home -> bottom_nav.selectTabById(R.id.nav_home, true)
                    R.id.fragment_tasks -> bottom_nav.selectTabById(R.id.nav_tasks, true)
                    R.id.fragment_edit_tasks -> bottom_nav.selectTabById(R.id.nav_edtasks, true)
                    R.id.fragment_settings -> bottom_nav.selectTabById(R.id.nav_settings, true)
                    R.id.fragment_profile -> bottom_nav.selectTabById(R.id.nav_profile, true)
                }
            }
        }
    })

}
}





