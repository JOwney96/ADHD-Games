package com.jowney.adhdgames.mainui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Background(content: @Composable () -> Unit) {
  // Single background component as to make a background across the app super simple
  
  Surface(
    color = MaterialTheme.colorScheme.background,
    content = content,
    modifier = Modifier
      .fillMaxSize()
  )
}