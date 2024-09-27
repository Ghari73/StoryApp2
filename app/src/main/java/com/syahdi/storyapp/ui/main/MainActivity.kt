package com.syahdi.storyapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.syahdi.storyapp.R
import com.syahdi.storyapp.ui.adapter.LoadingStateAdapter
import com.syahdi.storyapp.ui.adapter.StoryListAdapter
import com.syahdi.storyapp.databinding.ActivityMainBinding
import com.syahdi.storyapp.data.local.preferences.UserPreferences
import com.syahdi.storyapp.ui.login.LoginActivity
import com.syahdi.storyapp.ui.maps.MapsActivity
import com.syahdi.storyapp.ui.createStory.AddActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvStory: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAuthentication()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rvStory = findViewById(R.id.rvStory)
        rvStory.setHasFixedSize(true)
        progressBar = findViewById(R.id.progressBarMain)
        progressBar.visibility = View.GONE

        setup()

        binding.addActivityButton.setOnClickListener() {
            val intent = Intent(this, AddActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setup() {
        val token = UserPreferences.getString("token", "")
        val viewModelFactory = MainViewModelFactory(this, token)
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        val adapter = StoryListAdapter()

        rvStory.layoutManager = LinearLayoutManager(this)
        rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        mainViewModel.storiesPaging.observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }

            R.id.logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Logout")
                    setMessage("Are you sure you want to logout?")
                    setNegativeButton("No") { _, _ ->
                        // back to the app
                    }
                    setPositiveButton("Yes") { _, _ ->
                        // remove token and got to login page
                        UserPreferences.removeString("token")
                        checkAuthentication()
                    }
                    create()
                    show()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun checkAuthentication() {
        // Check if the token is stored in SharedPreferences
        val token = UserPreferences.getString("token", "")
        if (token == "") {
            // Token is not stored, start LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // Finish MainActivity to prevent going back to it without logging in
            finish()
        }
    }
}