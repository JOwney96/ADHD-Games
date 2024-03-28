package com.jowney.adhdgames.dotsandboxs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DotsViewModel
@Inject constructor(private val repository: DotBoxRepository) : ViewModel() {
  var uiState by mutableStateOf(
    DotsUiState(
      edgeMap = repository.edgeMap,
      gameOver = repository.gameOverFlag,
      player1Score = repository.player1Score,
      player2Score = repository.player2Score,
      squares = repository.squares,
      xSize = repository.xSize,
      ySize = repository.ySize,
      selectedPoints = listOf(),
      turn = DotsOwnerEnum.PLAYER1,
    )
  )
  
  var settingsUiState by mutableStateOf(
    DotsSettingsUiState(
      computerPlaying = repository.computerIsPlaying,
      player1Color = repository.player1Color,
      player2Color = repository.player2Color
    )
  )
  
  /**
   * Passes the color to the repository
   */
  fun player1ColorHandler(color: Color) {
    repository.setPlayer1Color(color)
    updateSettingsUiState()
  }
  
  /**
   * Passes the color to the repository
   */
  fun player2ColorHandler(color: Color) {
    repository.setPlayer2Color(color)
    updateSettingsUiState()
  }
  
  fun computerPlayingHandler(setting: Boolean) {
    repository.setComputerPlaying(setting)
    updateSettingsUiState()
  }
  
  private fun updateSettingsUiState() {
    settingsUiState = DotsSettingsUiState(
      computerPlaying = repository.computerIsPlaying,
      player1Color = repository.player1Color,
      player2Color = repository.player2Color
    )
  }
  
  fun startGame() {
    repository.startGame()
    settingsUiState = settingsUiState.copy(start = true)
  }
  
  fun onClick(point: DotsCoordinate, owner: DotsOwnerEnum) {
    // Add to temp coordinates if the point selected is valid.
    // Valid means two things.
    // 1. The point must be next to the other point if it exists.
    // 2. The point combined with the other point must not be an edge that is contained by the edge map
    
    // If the list is empty, then we will add this point and return as to allow the user to pick another point
    if (uiState.selectedPoints.isEmpty()) {
      val newTempCoordinates = uiState.selectedPoints.toMutableList()
      newTempCoordinates.add(point)
      uiState = uiState.copy(selectedPoints = newTempCoordinates)
      return
    }
    
    if (uiState.selectedPoints.size == 1) {
      if (uiState.selectedPoints[0] == point) {
        uiState = uiState.copy(selectedPoints = listOf())
        return
      }
      
      val newEdge = DotsEdge(uiState.selectedPoints[0], point)
      
      // Guard clause against malformed edges
      if (!repository.validateEdge(newEdge)) return
      
      val newTempCoordinates = uiState.selectedPoints.toMutableList()
      newTempCoordinates.add(point)
      uiState = uiState.copy(selectedPoints = newTempCoordinates)
    }
    
    // This section is for calling and doing repo things
    if (uiState.selectedPoints.size == 2) {
      val completedBox = repository.addEdge(uiState.selectedPoints[0], uiState.selectedPoints[1], owner)
      uiState = uiState.copy(selectedPoints = listOf())
      
      if (!completedBox && !settingsUiState.computerPlaying) swapTurns()
      
      var aiTurn = settingsUiState.computerPlaying && owner != DotsOwnerEnum.PLAYER2 && !completedBox
      aiTurn = aiTurn || (settingsUiState.computerPlaying && completedBox && owner == DotsOwnerEnum.PLAYER2)
      
      if (aiTurn) computerTurn()
      
      updateUiState()
    }
  }
  
  private fun computerTurn() {
    // Smart move is find and complete all the boxes we can
    var possibleSquares =
      repository.squares.filter { square ->
        square.edges.count { edge -> edge != DotsOwnerEnum.NONE } == 3
      }
    if (possibleSquares.isNotEmpty()) {
      computerFindAndPickEdge(possibleSquares)
      return
    }
    possibleSquares = repository.squares.filter { square ->
      square.edges.count { edge -> edge == DotsOwnerEnum.NONE } >= 2
    }
    
    if (possibleSquares.isNotEmpty()) {
      computerFindAndPickEdge(possibleSquares)
      return
    }
  }
  
  private fun computerFindAndPickEdge(possibleSquares: List<DotsSquare>) {
    val squareChoice = possibleSquares.random()
    val allPossibleEdges =
      cartesianProduct(squareChoice.points, squareChoice.points).filter { edge -> repository.validateEdge(edge) }
    
    val edgeChoice = allPossibleEdges.first { coordinate ->
      !repository.edgeMap.containsKey(coordinate) && !repository.edgeMap.containsKey(coordinate.reverse())
    }
    
    onClick(edgeChoice.coordinate1, DotsOwnerEnum.PLAYER2)
    onClick(edgeChoice.coordinate2, DotsOwnerEnum.PLAYER2)
    
  }
  
  private fun cartesianProduct(
    c1: List<DotsCoordinate>,
    c2: List<DotsCoordinate>
  ): List<DotsEdge> {
    return c1.flatMap { c1Element -> c2.map { c2Element -> DotsEdge(c1Element, c2Element) } }
  }
  
  private fun updateUiState() {
    uiState = uiState.copy(
      edgeMap = repository.edgeMap,
      gameOver = repository.gameOverFlag,
      player1Score = repository.player1Score,
      player2Score = repository.player2Score,
      squares = repository.squares,
    )
  }
  
  private fun swapTurns() {
    uiState = uiState.copy(
      turn =
      if (uiState.turn == DotsOwnerEnum.PLAYER1)
        DotsOwnerEnum.PLAYER2
      else
        DotsOwnerEnum.PLAYER1
    )
  }
  
  fun resetGame() {
    repository.resetRepo()
    
    uiState = DotsUiState(
      edgeMap = repository.edgeMap,
      gameOver = repository.gameOverFlag,
      player1Score = repository.player1Score,
      player2Score = repository.player2Score,
      squares = repository.squares,
      xSize = repository.xSize,
      ySize = repository.ySize,
      selectedPoints = listOf(),
      turn = DotsOwnerEnum.PLAYER1,
    )
  }
}