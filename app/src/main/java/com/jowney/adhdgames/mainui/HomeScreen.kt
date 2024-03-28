package com.jowney.adhdgames.mainui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jowney.adhdgames.R

@Composable
fun Home(navController: NavController) {
  Background {
    ShortAbout(navController = navController)
    HomeGameListBox(navController = navController)
  }
}

@Composable
fun ShortAbout(navController: NavController) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top,
    modifier = Modifier
      .fillMaxSize()
      .padding(15.dp)
  ) {
    Text(text = "ADHD Games by Joanthan Owney", color = MaterialTheme.colorScheme.onBackground)
    Button(
      onClick = { navController.navigate("about") },
      colors = ButtonDefaults.buttonColors(
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        containerColor = MaterialTheme.colorScheme.primaryContainer
      ),
      modifier = Modifier.padding(10.dp)
    ) {
      Text(text = "Click here for more info", color = MaterialTheme.colorScheme.onPrimaryContainer)
    }
  }
}

@Composable
fun HomeGameListBox(navController: NavController) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = Modifier.fillMaxSize()
  ) {
    HomeGameBox(
      text = "Tic Tac Toe",
      imageId = R.drawable.tic_tac_toe_icon,
      imageDescription = "Orange Tic Tac Toe Icon")
    { navController.navigate("tictactoe") }
    
    HomeGameBox(
      text = "Hangman",
      imageId = R.drawable.hangman_icon,
      imageDescription = "Orange Hangman Icon")
    { navController.navigate("hangman") }
    
    HomeGameBox(
      text = "Word Search",
      imageId = R.drawable.word_search_icon,
      imageDescription = "A Box with a eye glass inside"
    )
    { navController.navigate("wordsearch") }
    
    HomeGameBox(
      text = "Rock Paper Scissors",
      imageId = R.drawable.scissors_icon,
      imageDescription = "A orange hand doing the the scissors hand sign"
    ) { navController.navigate("rockpaperscissors") }
    
    HomeGameBox(
      text = "Dots And Boxes",
      imageId = R.drawable.dots_and_boxes_icon,
      imageDescription = "A square with dots on each corner connected to each other with lines."
    ) { navController.navigate("dotsandboxes") }
  }
}

@Composable
fun HomeGameBox(text: String, imageId: Int, imageDescription: String, onClick: () -> Unit) {
  val width = 500.dp
  val height = 100.dp
  
  Box(
    modifier = Modifier
      .size(width = width, height = height)
      .fillMaxSize()
      .padding(5.dp)
  ) {
    // In this box, we will have a button that navigates to the game.
    // It will have the game title
    // Then finally it will have some kind of picture
    
    Button(onClick = onClick,
      shape = Shapes().small,
      colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
      modifier = Modifier
        .align(Alignment.BottomStart)
        .size(width = width, height = height)
        .fillMaxSize()) {}
    
    Text(
      text = text,
      color = MaterialTheme.colorScheme.onSecondaryContainer,
      fontSize = 30.sp,
      modifier = Modifier
        .align(Alignment.CenterStart)
        .padding(start = 10.dp))
    
    Image(bitmap = ImageBitmap.imageResource(
      id = imageId),
      contentDescription = imageDescription,
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .size(height - 10.dp)
        .padding(end = 5.dp)
    )
  }
}
