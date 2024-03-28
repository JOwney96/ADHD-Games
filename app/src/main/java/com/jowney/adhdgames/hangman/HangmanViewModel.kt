package com.jowney.adhdgames.hangman

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HangmanViewModel
@Inject constructor(private val repository: HangmanRepository) : ViewModel() {
  var uiState by mutableStateOf(HangmanUiState(
    guesses = repository.guessedLetters,
    lives = repository.lives,
    word = repository.computerWord,
  ))
  
  fun onGuess(char: Char) {
    repository.letterGuess(char)
    uiState = uiState.copy(
      guesses = repository.guessedLetters,
      lives = repository.lives,
      word = repository.computerWord,
    )
  }
  
  @Composable
  fun GenWord() {
    repository.resetRepository()
    uiState = uiState.copy(
      guesses = repository.guessedLetters,
      lives = repository.lives,
      word = repository.computerWord,
      reset = false
    )
  }
  
  fun setReset() {
    uiState = uiState.copy(reset = true)
  }
}