package com.my.projects.quizapp

import android.graphics.Color
import android.os.Build
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
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = Color.BLACK
            }
        }
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.navController

        setTopAppBar()
        setNavigationViewWithDrawer()
        setNavigationListener()
        binding.fabMain.setOnClickListener {
            navController.navigate(R.id.graph_quiz)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return when (navController.currentDestination?.id) {
            R.id.score -> {
                navController.navigate(R.id.action_score_to_categories)
                true
            }
            else -> navController.navigateUp()
        }
    }

    private fun setTopAppBar() {
        setSupportActionBar(binding.toolbarMain)
        setupActionBarWithNavController(this, navController)
        binding.toolbarMain.title = navController.currentDestination?.label.toString()
    }

    private fun setNavigationViewWithDrawer() {
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayoutMain,
            binding.bottomAppBarMain,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close,
        )
        binding.drawerLayoutMain.addDrawerListener(toggle)
        binding.navigationviewMain.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.history -> {
                    navController.navigate(R.id.graph_history)
                }
                R.id.setting -> {
                    navController.navigate(R.id.graph_setting)
                }
            }
            binding.drawerLayoutMain.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun setNavigationListener() {
        val hideBottomBar = arrayOf(R.id.quiz, R.id.categories, R.id.quizSetting, R.id.score)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbarMain.subtitle = ""
            binding.toolbarMain.title = destination.label.toString().toUpperCase(Locale.ROOT)
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
        binding.bottomAppBarMain.hide()
        binding.fabMain.hide()
    }

    private fun showBottomAppBar() {
        val scale = resources.displayMetrics.density
        val dpAsPixels = (100 * scale + 0.5f).toInt()
        binding.navHostFragment.setPadding(0, 0, 0, dpAsPixels)
        binding.bottomAppBarMain.show()
        binding.fabMain.show()
    }

    //Expose
    val mainBinding: ActivityMainBinding get() = binding
}