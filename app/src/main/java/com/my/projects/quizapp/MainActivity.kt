package com.my.projects.quizapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.my.projects.quizapp.databinding.ActivityMainBinding
import com.my.projects.quizapp.util.extensions.hide
import com.my.projects.quizapp.util.extensions.setupWithNavController
import com.my.projects.quizapp.util.extensions.show
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = ""
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bottomNav
        val navGraphIds =
            listOf(R.navigation.nav_home, R.navigation.nav_history, R.navigation.nav_setting)

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )


        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, { navController ->
            setupActionBarWithNavController(this, navController)
            setNavigationListener(navController)
        })
        currentNavController = controller
    }

    private fun setNavigationListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _,
                                                        destination,
                                                        _ ->
            binding.toolbar.subtitle = ""
            if (destination.id == R.id.quiz) {
                binding.bottomNav.hide()
            } else {
                binding.bottomNav.show()
                if (destination.id == R.id.categories) {
                    binding.logo.root.show()
                } else {
                    binding.logo.root.hide()
                    binding.toolbar.title = destination.label
                }
            }

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


}