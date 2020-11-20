package com.my.projects.quizapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.my.projects.quizapp.databinding.ActivityMainBinding
import com.my.projects.quizapp.presentation.ui.widgets.ThemeModeDialog
import com.my.projects.quizapp.util.Util.Companion.makeToast
import com.my.projects.quizapp.util.Util.Companion.setNightMode


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title = ""
        setNavController()
        navigationListener()
    }

    private fun setNavController() {
        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    private fun navigationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.categories) {
                binding.appLogo.root.visibility = VISIBLE
            } else {
                binding.appLogo.root.visibility = GONE
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true // must return true to consume it here
            }
            R.id.history -> {
                makeToast(this, "Your Quiz History")
                navController.navigate(R.id.action_global_history)
            }
            R.id.mode_switch -> {
                ThemeModeDialog().show(supportFragmentManager, "ThemeModeDialog")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.findItem(R.id.mode_switch)


        val switch = menuItem.actionView.findViewById<SwitchCompat>(R.id.mode_switch_control)
        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> switch.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> switch.isChecked = false
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.app_setting, menu)
        val menuItem = menu.findItem(R.id.mode_switch)
        val switch = menuItem.actionView.findViewById<SwitchCompat>(R.id.mode_switch_control)
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        return true
    }

}