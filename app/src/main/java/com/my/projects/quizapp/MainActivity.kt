package com.my.projects.quizapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.my.projects.quizapp.data.db.entity.relations.QuizWithQuestionsAndAnswers
import com.my.projects.quizapp.databinding.ActivityMainBinding
import com.my.projects.quizapp.presentation.ui.widgets.ThemeModeDialog
import com.my.projects.quizapp.util.Const
import com.my.projects.quizapp.util.Util.Companion.makeToast
import com.my.projects.quizapp.util.converters.Converters


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
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHost.findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
    }

    private fun navigationListener() {
        navController.addOnDestinationChangedListener { nav, destination,bundle ->
            if (destination.id == R.id.quizDetail) {
                val data=bundle?.getSerializable(Const.KEY_QUIZ) as QuizWithQuestionsAndAnswers
                supportActionBar?.title = data.quiz.title
                supportActionBar?.subtitle = Converters.dateToString(data.quiz.date.time)
            }else if (destination.id == R.id.categories) {
                binding.appLogo.root.visibility = VISIBLE
            } else {
                binding.appLogo.root.visibility = GONE
                supportActionBar?.title = destination.label
                supportActionBar?.subtitle = ""
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
                navController.navigate(R.id.action_global_history)
                return true
            }
            R.id.mode_switch -> {
                ThemeModeDialog().show(supportFragmentManager, "ThemeModeDialog")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

}