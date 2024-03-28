package com.jowney.adhdgames.tictactoe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TicTacToeViewModel
@Inject constructor(private val repository: TicTacToeRepository) : ViewModel() {
  var uiState by mutableStateOf(TicTacToeUiState(
    boardList = repository.boardList,
    winnerFound = repository.winnerPair.first,
    whoWon = repository.winnerPair.second,
    draw = repository.drawFound))
    private set
  
  fun buttonClick(row: Int, col: Int) {
    repository.buttonOnClick(row, col)
    uiState = uiState.copy(
      boardList = repository.boardList,
      winnerFound = repository.winnerPair.first,
      whoWon = repository.winnerPair.second,
      draw = repository.drawFound
    )
  }
  
  fun resetButtonClick() {
    repository.resetButton()
    uiState = uiState.copy(
      boardList = repository.boardList,
      winnerFound = repository.winnerPair.first,
      whoWon = repository.winnerPair.second,
      draw = repository.drawFound
    )
  }
}