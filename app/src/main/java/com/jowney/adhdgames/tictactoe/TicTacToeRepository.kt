package com.jowney.adhdgames.tictactoe

import javax.inject.Inject
import kotlin.random.Random

class TicTacToeRepository @Inject constructor() {
  var boardList = List(3) { List(3) { "" } }
    private set
  val winnerPair
    get() = checkForWinner()
  val drawFound
    get() = checkForDraw()
  
  fun resetButton() {
    boardList = List(3) { List(3) { "" } }
  }
  
  fun buttonOnClick(row: Int, col: Int) {
    if (winnerPair.first) return
    
    val boardCopy = boardList.map { it.toMutableList() }.toMutableList()
    if (boardCopy[row][col] == "") boardCopy[row][col] = "X"
    else return
    
    val freeSpot: Boolean = boardCopy.flatten().contains("")
    
    if (freeSpot) {
      val (comRow, comCol) = computer(convertToImmutable(boardCopy))
      if (boardCopy[comRow][comCol] != "") throw Error("Computer Picked a square that was already taken")
      boardCopy[comRow][comCol] = "O"
    }
    
    boardList = boardCopy
  }
  
  private fun checkForWinner(): Pair<Boolean, String> {
    
    val allCombinations = mutableListOf<String>()
    
    //Get rows
    for (i in 0..2) {
      allCombinations.add(boardList[i][0] + boardList[i][1] + boardList[i][2])
    }
    
    //Get cols
    for (i in 0..2) {
      allCombinations.add(boardList[0][i] + boardList[1][i] + boardList[2][i])
    }
    
    //Get diagonals
    allCombinations.add(boardList[0][0] + boardList[1][1] + boardList[2][2])
    allCombinations.add(boardList[0][2] + boardList[1][1] + boardList[2][0])
    
    allCombinations.forEach { if (it == "XXX") return Pair(true, "Player") }
    allCombinations.forEach { if (it == "OOO") return Pair(true, "Computer") }
    return Pair(false, "")
  }
  
  private fun countDiagonalChars(d: String): Int {
    
    var counter = 0
    for (char in d) {
      when (char) {
        'X' -> counter++
        'O' -> counter--
      }
    }
    return counter
  }
  
  private fun computer(boardList: List<List<String>>): Pair<Int, Int> {
    
    // Take the middle if you can
    if (boardList[1][1] == "") return Pair(1, 1)
    
    // Smart Offensive move is to finish a two in a row
    // Check Rows
    for (row in 0..2) {
      var counter = 0
      
      // Go through the row and count X's and O's
      for (col in 0..2) {
        when (boardList[row][col]) {
          "X" -> counter--
          "O" -> counter++
        }
      }
      
      // If counter == 2 then there are 2 O's and no X's
      if (counter == 2) {
        // Find the empty slot and return the id of the location
        for (col in 0..2) {
          if (boardList[row][col] == "") return Pair(row, col)
        }
      }
    }
    
    // Check Cols
    for (col in 0..2) {
      var counter = 0
      
      // Go through the row and count X's and O's
      for (row in 0..2) {
        when (boardList[row][col]) {
          "X" -> counter--
          "O" -> counter++
        }
      }
      
      // If counter == 2 then there are 2 O's and no X's
      if (counter == 2) {
        // Find the empty slot and return the id of the location
        for (row in 0..2) {
          if (boardList[row][col] == "") return Pair(row, col)
        }
      }
    }
    
    // Smart defensive move is to find where the player will win and block them i.e., X_X Put O in between the X's
    // Check Rows
    for (row in 0..2) {
      var counter = 0
      
      // Go through the row and count X's and O's
      for (col in 0..2) {
        when (boardList[row][col]) {
          "X" -> counter++
          "O" -> counter--
        }
      }
      
      // If counter == 2 then there are 2 X's and no O's
      if (counter == 2) {
        // Find the empty slot and return the id of the location
        for (col in 0..2) {
          if (boardList[row][col] == "") return Pair(row, col)
        }
      }
    }
    
    // Check Cols
    for (col in 0..2) {
      var counter = 0
      
      // Go through the row and count X's and O's
      for (row in 0..2) {
        when (boardList[row][col]) {
          "X" -> counter++
          "O" -> counter--
        }
      }
      
      // If counter == 2 then there are 2 X's and no O's
      if (counter == 2) {
        // Find the empty slot and return the id of the location
        for (row in 0..2) {
          if (boardList[row][col] == "") return Pair(row, col)
        }
      }
    }
    
    // Check Diagonals
    val d1String = boardList[0][0] + boardList[1][1] + boardList[2][2]
    val d1Counter = countDiagonalChars(d1String)
    val d2String = boardList[0][2] + boardList[1][1] + boardList[2][0]
    val d2Counter = countDiagonalChars(d2String)
    
    if (d1Counter == 2) {
      when {
        boardList[0][0] == "" -> return Pair(0, 0)
        boardList[1][1] == "" -> return Pair(1, 1)
        boardList[2][2] == "" -> return Pair(2, 2)
      }
    }
    
    if (d2Counter == 2) {
      when {
        boardList[0][0] == "" -> return Pair(0, 0)
        boardList[1][1] == "" -> return Pair(1, 1)
        boardList[2][2] == "" -> return Pair(2, 2)
      }
    }
    
    // Foolish move is to just find the first open spot and pick it.
    // After we can't do a smart move, we do the foolish move
    val openSpots = mutableMapOf<Int, Pair<Int, Int>>()
    for (row in 0..2) {
      for (col in 0..2) {
        if (boardList[row][col] == "") openSpots[openSpots.count()] = Pair(row, col)
      }
    }
    
    for (col in 0..2) {
      for (row in 0..2) {
        if (boardList[row][col] == "") openSpots[openSpots.count()] = Pair(row, col)
      }
    }
    
    return openSpots[Random.nextInt(0, openSpots.count())]!!
  }
  
  private fun checkForDraw(): Boolean = !boardList.flatten().contains("")
  
  private fun convertToImmutable(list: MutableList<MutableList<String>>) = list.map { it.toList() }.toList()
}