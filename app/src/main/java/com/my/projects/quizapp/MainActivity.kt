package com.my.projects.quizapp

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.my.projects.quizapp.databinding.ActivityMainBinding
import com.my.projects.quizapp.util.extensions.hide
import com.my.projects.quizapp.util.extensions.show
import timber.log.Timber
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        setTopAppBar()
        setNavigationViewWithDrawer()
        setNavigationListener()
        binding.fab.setOnClickListener {
            navController.navigate(R.id.graph_quiz)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return when (navController.currentDestination?.id) {
            R.id.score -> {
                // custom behavior here
                Timber.d("Custom Nav")
                navController.navigate(R.id.action_score_to_categories)
                true
            }
            else -> navController.navigateUp()
        }
    }

    private fun setTopAppBar() {
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(this, navController)
        binding.toolbar.title = navController.currentDestination?.label.toString()
    }

    private fun setNavigationViewWithDrawer() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawer,
            binding.bottomAppBar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close,
        )
        binding.drawer.addDrawerListener(toggle)
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.history -> {
                    navController.navigate(R.id.graph_history)
                }
                R.id.setting -> {
                    navController.navigate(R.id.graph_setting)
                }
            }
            binding.drawer.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setNavigationListener() {
        val hideBottomBar = arrayOf(R.id.quiz, R.id.categories, R.id.quizSetting, R.id.score)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbar.subtitle = ""
            binding.toolbar.title = destination.label.toString().toUpperCase(Locale.ROOT)
            when (destination.id) {
                in hideBottomBar -> {
                    hideBottomAppBar()
                }
                else -> {
                    showBottomAppBar()
                }
            }
        }
    }


    private fun hideBottomAppBar() {
        binding.navHostFragment.setPadding(0, 0, 0, 0)
        binding.bottomAppBar.hide()
        binding.fab.hide()
    }

    private fun showBottomAppBar() {
        val scale = resources.displayMetrics.density
        val dpAsPixels = (100 * scale + 0.5f).toInt()
        binding.navHostFragment.setPadding(0, 0, 0, dpAsPixels)
        binding.bottomAppBar.show()
        binding.fab.show()
    }

}