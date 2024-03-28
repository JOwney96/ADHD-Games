package com.jowney.adhdgames.dotsandboxs

import androidx.compose.ui.graphics.Color
import javax.inject.Inject

class DotsSettingsDataSource @Inject constructor() {
  var player1Color = Color.Blue
    private set
  var player2Color = Color.Red
    private set
  var computerPlaying = true
    private set
  var gameHasStarted = false
    private set
  
  fun setPlayer1Color(color: Color) {
    if (color == player2Color) return
    
    player1Color = color
  }
  
  fun setPlayer2Color(color: Color) {
    if (color == player1Color) return
    
    player2Color = color
  }
  
  fun setComputerPlaying(boolean: Boolean) {
    computerPlaying = boolean
  }
  
  fun startGame() {
    gameHasStarted = true
  }
}