package com.jowney.adhdgames.tictactoe

data class TicTacToeUiState(
  val boardList: List<List<String>>,
  val winnerFound: Boolean,
  val whoWon: String,
  val draw: Boolean,
)