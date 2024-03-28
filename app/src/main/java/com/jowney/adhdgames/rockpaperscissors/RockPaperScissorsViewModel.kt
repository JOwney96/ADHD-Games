package com.jowney.adhdgames.rockpaperscissors

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class RockPaperScissorsViewModel
@Inject constructor() : ViewModel() {
  var uiState by mutableStateOf(RockPaperScissorsUiState())
  
  fun onClick(userChoice: Int) {
    // If we had a previous winner, then reset the uiState and pick a new value for computer
    if (uiState.winner != "") {
      uiState = uiState.copy(
        computerChoice = Random.nextInt(RPSValues.ROCK.ordinal, RPSValues.SCISSORS.ordinal + 1),
        winner = ""
      )
    }
    
    // Set the winner
    // Default will be computer
    val winner = when {
      // Draw check
      userChoice == uiState.computerChoice -> "Nobody"
      
      // Computer win checks
      userChoice == RPSValues.ROCK.ordinal && uiState.computerChoice == RPSValues.SCISSORS.ordinal -> "Player"
      userChoice == RPSValues.PAPER.ordinal && uiState.computerChoice == RPSValues.ROCK.ordinal -> "Player"
      userChoice == RPSValues.SCISSORS.ordinal && uiState.computerChoice == RPSValues.PAPER.ordinal -> "Player"
      
      else -> "Computer"
    }
    
    uiState = uiState.copy(
      winner = winner
    )
  }
}