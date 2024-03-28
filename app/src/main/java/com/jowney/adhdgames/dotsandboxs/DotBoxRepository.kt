package com.jowney.adhdgames.dotsandboxs

import androidx.compose.ui.graphics.Color
import javax.inject.Inject
import kotlin.math.abs

// TODO: Bug - Went out of bounds on charles' tablet bottom rightish

class DotBoxRepository
@Inject constructor(private val settingsDataSource: DotsSettingsDataSource) {
  val player1Color
    get() = settingsDataSource.player1Color
  val player2Color
    get() = settingsDataSource.player2Color
  private var gameHasStarted = settingsDataSource.gameHasStarted
    private set
  val computerIsPlaying
    get() = settingsDataSource.computerPlaying
  
  val xSize = 5
  val ySize = 4
  
  // Make list 2d to help create the squares' corners. Then convert the list to a flat map
  private val emptySquares =
    List(xSize - 1) { y -> List(ySize - 1) { x -> DotsSquare(points = generateSquarePoints(x, y)) } }.flatten()
  
  var squares = emptySquares
    private set
  
  // This map is mutable and should only be accessed by the class
  private var _edgeMap = mutableMapOf<DotsEdge, DotsOwnerEnum>()
  
  // This map is immutable
  val edgeMap
    get() = _edgeMap.toMap()
  
  var player1Score = 0
    private set
  
  var player2Score = 0
    private set
  
  val gameOverFlag
    get() = player1Score + player2Score == squares.size
  
  /**
   * Returns true if the edge is a valid choice.
   */
  fun validateEdge(edge: DotsEdge): Boolean {
    val point1 = edge.coordinate1
    val point2 = edge.coordinate2
    
    if (point1 == point2) return false
    
    // Check if the edge is already assigned
    if (edgeMap.containsKey(edge) || edgeMap.containsKey(edge.reverse())) return false
    
    // Check if the edge is diagonal
    if (point2 == DotsCoordinate(point1.x + 1, point1.y + 1) ||
      point1 == DotsCoordinate(point2.x + 1, point2.y + 1)
    ) return false
    
    if (point1 == DotsCoordinate(point2.x - 1, point2.y + 1) ||
      point2 == DotsCoordinate(point1.x - 1, point1.y + 1)
    ) return false
    
    if (point2 == DotsCoordinate(point1.y, point1.x)) return false
    
    // Check if the edge is abnormally long
    val xDis = abs(point1.x - point2.x)
    val yDis = abs(point1.y - point2.y)
    
    return !(xDis > 1 || yDis > 1)
  }
  
  /**
   * Adds the edge to squares and to the edge map.
   * This will also check for a box and update scores.
   *
   * Returns true if a box was created because of the points provided
   *
   * This function calls the validateEdge function
   */
  fun addEdge(point1: DotsCoordinate, point2: DotsCoordinate, ownerEnum: DotsOwnerEnum): Boolean {
    val edge = DotsEdge(point1, point2)
    
    if (!validateEdge(edge)) return false
    
    // Add edge info to all squares that needs it - Max is 2???
    squares = squares.map { square ->
      if (square.points.containsAll(listOf(point1, point2))) {
        val newEdges = square.edges.toMutableList()
        newEdges[newEdges.indexOf(DotsOwnerEnum.NONE)] = ownerEnum
        square.copy(edges = newEdges)
        
      } else square
    }
    
    // Add the edge to the map
    _edgeMap[DotsEdge(point1, point2)] = ownerEnum
    
    // Call check and add the number of boxes to the player score.
    val boxCount = checkForBox(point1, point2)
    if (ownerEnum == DotsOwnerEnum.PLAYER1) {
      player1Score += boxCount
    } else {
      player2Score += boxCount
    }
    
    return boxCount >= 1
  }
  
  /**
   * Returns the number of boxes found
   */
  private fun checkForBox(point1: DotsCoordinate, point2: DotsCoordinate): Int {
    val potentialSquares = squares.filter { square -> square.points.containsAll(listOf(point1, point2)) }
    
    // Count the number of squares where each square has four edges that are owned by a player
    return potentialSquares.count { square -> (square.edges.count { it != DotsOwnerEnum.NONE } == 4) }
  }
  
  /**
   * Creates the 4 [DotsCoordinate]s need for each square
   */
  private fun generateSquarePoints(x: Int, y: Int): List<DotsCoordinate> {
    val pointsList = mutableListOf<DotsCoordinate>()
    
    pointsList.add(DotsCoordinate(x, y))
    pointsList.add(DotsCoordinate(x + 1, y))
    pointsList.add(DotsCoordinate(x, y + 1))
    pointsList.add(DotsCoordinate(x + 1, y + 1))
    
    return pointsList
  }
  
  /**
   * Resets the repo to init settings
   */
  fun resetRepo() {
    squares = emptySquares
    _edgeMap = mutableMapOf()
    player1Score = 0
    player2Score = 0
  }
  
  /**
   * Sets the start game flag to true
   */
  fun startGame() {
    settingsDataSource.startGame()
    gameHasStarted = settingsDataSource.gameHasStarted
  }
  
  /**
   * Passes the color to the settings data source
   */
  fun setPlayer1Color(color: Color) {
    settingsDataSource.setPlayer1Color(color)
  }
  
  /**
   * Passes the color to the settings data source
   */
  fun setPlayer2Color(color: Color) {
    settingsDataSource.setPlayer2Color(color)
  }
  
  /**
   * Passes the computer is playing boolean to the settings data source
   */
  fun setComputerPlaying(setting: Boolean) {
    settingsDataSource.setComputerPlaying(setting)
  }
}