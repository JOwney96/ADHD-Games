package com.jowney.adhdgames.hangman

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jowney.adhdgames.R
import com.jowney.adhdgames.mainui.Background

private val QWERTY_KEYS = listOf(
  'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A',
  'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M'
)

@Composable
fun HangmanMain(viewModel: HangmanViewModel = hiltViewModel()) {
  Background {
    HangmanScreen(viewModel = viewModel)
  }
}

@Composable
private fun HangmanScreen(viewModel: HangmanViewModel) {
  
  // If the repo has not been initialized, do so now
  if (viewModel.uiState.word == "") viewModel.GenWord()
  
  // If a user has won or loss, we set a reset flag and here we use GenWord to reset the repo
  if (viewModel.uiState.reset) viewModel.GenWord()
  
  // The Restart Button is displayed under the image
  HangmanImageDisplay(
    lives = viewModel.uiState.lives,
    userWon = checkForWin(viewModel.uiState.word, viewModel.uiState.guesses)
  )
  { viewModel.setReset() }
  
  WordBlanks(
    word = viewModel.uiState.word,
    guessedChars = viewModel.uiState.guesses,
    lives = viewModel.uiState.lives
  )
  
  // Nested the keyboard function so that the viewModel.GuessChar event function is in scope
  @Composable
  fun Keyboard() {
    val keyboardRowModifier = Modifier
    
    Column(
      verticalArrangement = Arrangement.Bottom,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxSize()
        .offset(y = (-50).dp)
    ) {
      
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = keyboardRowModifier
      ) {
        for (i in 0..9) CharButton(
          text = QWERTY_KEYS[i].toString(),
          enabled = !viewModel.uiState.guesses.contains(QWERTY_KEYS[i])
        )
        { viewModel.onGuess(QWERTY_KEYS[i]) }
      }
      
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = keyboardRowModifier
      ) {
        for (i in 10..18) CharButton(
          text = QWERTY_KEYS[i].toString(),
          enabled = !viewModel.uiState.guesses.contains(QWERTY_KEYS[i])
        )
        { viewModel.onGuess(QWERTY_KEYS[i]) }
      }
      
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = keyboardRowModifier
      ) {
        for (i in 19..25) CharButton(
          text = QWERTY_KEYS[i].toString(),
          enabled = !viewModel.uiState.guesses.contains(QWERTY_KEYS[i])
        )
        { viewModel.onGuess(QWERTY_KEYS[i]) }
      }
    }
  }
  Keyboard()
}

@Composable
private fun CharButton(text: String, enabled: Boolean, onClick: () -> Unit) {
  val mColors = MaterialTheme.colorScheme
  val buttonColor = ButtonDefaults.buttonColors(
    containerColor = mColors.primaryContainer,
    contentColor = mColors.onPrimaryContainer,
  )
  
  Button(
    onClick = onClick,
    shape = Shapes().extraSmall,
    contentPadding = PaddingValues(0.dp),
    enabled = enabled,
    colors = buttonColor,
    modifier = Modifier
      .size(
        height = 50.dp,
        width = 39.dp
      )
      .padding(2.dp),
  ) {
    Text(
      text = text,
      fontSize = 10.sp,
      color = mColors.onPrimaryContainer,
      textAlign = TextAlign.Center,
    )
  }
}

private fun checkForWin(word: String, guessedChars: List<Char>): Boolean {
  val wordSet = word.uppercase().toSet()
  var numberOfCorrectChars = 0
  guessedChars.forEach {
    if (wordSet.contains(it.uppercaseChar())) numberOfCorrectChars++
  }
  
  return numberOfCorrectChars == wordSet.count()
}

@Composable
private fun HangmanImageDisplay(lives: Int, userWon: Boolean, resetOnClick: () -> Unit) {
  // Using a Column to help center the image
  
  Column(
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier.fillMaxSize()
  ) {
    val lightMode = !isSystemInDarkTheme()
    when (lives) {
      6 -> HangmanImage(
        id =
        if (lightMode) R.drawable.light_full_life
        else R.drawable.dark_full_life,
        contentDescription = "Hanging Stand Only"
      )
      
      5 -> HangmanImage(
        id =
        if (lightMode) R.drawable.light_five_lives
        else R.drawable.dark_five_lives,
        contentDescription = "Stand With Head"
      )
      
      4 -> HangmanImage(
        id =
        if (lightMode) R.drawable.light_four_lives
        else R.drawable.dark_four_lives,
        contentDescription = "Stand With Head and Torso"
      )
      
      3 -> HangmanImage(
        id =
        if (lightMode) R.drawable.light_three_lives
        else R.drawable.dark_three_lives,
        contentDescription = "Stand With Head, Torso, and Left Arm"
      )
      
      2 -> HangmanImage(
        id =
        if (lightMode) R.drawable.light_two_lives
        else R.drawable.dark_two_lives,
        contentDescription = "Stand With Head, Torso, Left Arm, and Right Arm"
      )
      
      1 -> HangmanImage(
        id =
        if (lightMode) R.drawable.light_one_life
        else R.drawable.dark_one_life,
        contentDescription = "Stand With Head, Torso, Left Arm, Right Arm, and Left Leg"
      )
      
      0 -> {
        HangmanImage(
          id =
          if (lightMode) R.drawable.light_dead
          else R.drawable.dark_dead,
          contentDescription = "Stand With Full Body"
        )
        ResetButton(resetOnClick)
      }
    }
    if (userWon) ResetButton(resetOnClick)
  }
}

@Composable
private fun HangmanImage(id: Int, contentDescription: String) = Image(
  bitmap = ImageBitmap.imageResource(id),
  contentDescription = contentDescription,
  modifier = Modifier.size(300.dp)
)

@Composable
private fun ResetButton(onClick: () -> Unit) {
  val mColors = MaterialTheme.colorScheme
  val buttonColor = ButtonDefaults.buttonColors(
    containerColor = mColors.primaryContainer,
    contentColor = mColors.onPrimaryContainer,
  )
  
  Button(onClick = onClick, colors = buttonColor) {
    Text(text = "Restart?", color = mColors.onPrimaryContainer)
  }
}

@Composable
private fun WordBlanks(word: String, guessedChars: List<Char>, lives: Int) {
  // Go through each char in word and if we have guessed it print the char else print a "_"
  
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
      .offset(y = 100.dp)
  ) {
    if (lives == 0)
      Row {
        Text(
          text = word.replaceFirstChar { it.uppercaseChar() },
          color = MaterialTheme.colorScheme.secondary
        )
      }
    Row {
      for (char in word.uppercase()) {
        if (guessedChars.contains(char)) WordBlankText(text = char.toString())
        else WordBlankText(text = "_")
      }
    }
  }
}

@Composable
private fun WordBlankText(text: String) = Text(
  text = text,
  fontSize = 30.sp,
  modifier = Modifier.padding(5.dp),
  color = MaterialTheme.colorScheme.secondary
)
