package com.jowney.adhdgames.mainui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jowney.adhdgames.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ADHDTopBar(navController: NavController) {
  val mColors = MaterialTheme.colorScheme
  CenterAlignedTopAppBar(
    title = { ADHDTitle() },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = mColors.secondaryContainer,
      navigationIconContentColor = mColors.onSecondaryContainer,
      titleContentColor = mColors.primary,
    ),
    navigationIcon = { ADHDBackIcon(navController) },
  )
}

@Composable
private fun ADHDTitle() {
  Text(
    text = "ADHD Games",
    color = MaterialTheme.colorScheme.primary,
    fontSize = 25.sp
  )
}

@Composable
private fun ADHDBackIcon(navController: NavController) {
  val imageId =
    if (!isSystemInDarkTheme()) R.drawable.light_back_icon
    else R.drawable.dark_back_icon
  IconButton(onClick = { navController.navigateUp() })
  {
    Icon(
      painter = painterResource(id = imageId),
      contentDescription = "Dark theme back arrow",
    )
  }
}