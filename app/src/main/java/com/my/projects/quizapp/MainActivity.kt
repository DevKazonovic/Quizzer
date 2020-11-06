package com.my.projects.quizapp

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.my.projects.quizapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var appBarConfiguration:AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)



        setSupportActionBar(binding.toolbar)
        val navHost= supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true // must return true to consume it here
            }
           /* R.id.mode_switch -> {
                if (item.isChecked) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                return true
            }*/
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
                Toast.makeText(this, "This is on", Toast.LENGTH_SHORT).show()
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                Toast.makeText(this, "This is off", Toast.LENGTH_SHORT).show()
               setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        return true
    }

    private fun setNightMode(@AppCompatDelegate.NightMode nightMode: Int) {
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }
}