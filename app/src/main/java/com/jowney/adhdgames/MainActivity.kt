package com.jowney.adhdgames

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.jowney.adhdgames.mainui.ADHDTopBar
import com.jowney.adhdgames.mainui.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ADHDGames : Application()

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  init {
    instance = this
  }
  
  companion object {
    private var instance: MainActivity? = null
    fun ctx(): Context = instance!!.applicationContext
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    supportActionBar?.hide()
    
    // Start the Compose app
    setContent {
      
      // The controller that handles all navigations. Must be created at root level
      val navController = rememberNavController()
      AppTheme {
        
        // Scaffold to set the apps topBar to a custom-built bar
        Scaffold(
          topBar = { ADHDTopBar(navController) }
        ) { contentPadding ->
          Box(modifier = Modifier.padding(contentPadding)) {
            
            // All page navigations happen here, including loading the home screen
            ADHDNavHost(navController)
            /*DotsSettingsMain(viewModel = DotsSettingsViewModel(DotsSettingsDataSource()))*/
          }
        }
      }
    }
  }
}