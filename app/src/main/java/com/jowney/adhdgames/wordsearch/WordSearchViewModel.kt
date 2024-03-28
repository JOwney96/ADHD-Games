package com.jowney.adhdgames.wordsearch

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class WordSearchViewModel
@Inject constructor(private val wordSearchRepository: WordSearchRepository) : ViewModel() {
  var uiState by mutableStateOf(WordSearchUiState(
    words = wordSearchRepository.words,
    foundWords = wordSearchRepository.foundWords,
    wordBoard = List(1) { List(1) { Pair("", false) } }
  ))
  private val defaultDispatcher = Dispatchers.Default
  private var userWord = String()
  private val emptyList = viewModelScope.async(defaultDispatcher) { MutableList(20) { MutableList(20) { Pair("", false) } } }
  
  fun flagReset() {
    uiState = uiState.copy(reset = true)
  }
  
  fun generateWords() = runBlocking {
    userWord = ""
    val words = async(defaultDispatcher) { wordSearchRepository.generateWords(defaultDispatcher) }
    val wordBoard = buildWordBoard(words.await())
    
    uiState = uiState.copy(
      words = wordSearchRepository.words,
      foundWords = wordSearchRepository.foundWords,
      wordBoard = wordBoard,
      reset = false
    )
  }
  
  private fun checkAroundChar(row: Int, col: Int): Boolean {
    return when {
      uiState.wordBoard.getOrNull(row - 1)?.getOrNull(col)?.second ?: false -> true
      uiState.wordBoard.getOrNull(row + 1)?.getOrNull(col)?.second ?: false -> true
      uiState.wordBoard.getOrNull(row)?.getOrNull(col - 1)?.second ?: false -> true
      uiState.wordBoard.getOrNull(row)?.getOrNull(col + 1)?.second ?: false -> true
      uiState.wordBoard.getOrNull(row - 1)?.getOrNull(col - 1)?.second ?: false -> true
      uiState.wordBoard.getOrNull(row + 1)?.getOrNull(col + 1)?.second ?: false -> true
      else -> false
    }
  }
  
  private fun checkVertical(row: Int, col: Int): Boolean {
    return when {
      uiState.wordBoard.getOrNull(row)?.getOrNull(col - 1)?.second ?: false -> {
        if (checkAroundChar(row, col - 1)) {
          uiState.wordBoard.getOrNull(row)?.getOrNull(col - 2)?.second ?: false
        } else
          true
      }
      
      uiState.wordBoard.getOrNull(row)?.getOrNull(col + 1)?.second ?: false -> {
        if (checkAroundChar(row, col + 1)) {
          uiState.wordBoard.getOrNull(row)?.getOrNull(col + 2)?.second ?: false
        } else
          true
      }
      
      else -> false
    }
  }
  
  private fun checkHorizontal(row: Int, col: Int): Boolean {
    return when {
      uiState.wordBoard.getOrNull(row - 1)?.getOrNull(col)?.second ?: false -> {
        if (checkAroundChar(row - 1, col)) {
          uiState.wordBoard.getOrNull(row - 2)?.getOrNull(col)?.second ?: false
        } else
          true
      }
      
      uiState.wordBoard.getOrNull(row + 1)?.getOrNull(col)?.second ?: false -> {
        if (checkAroundChar(row + 1, col)) {
          uiState.wordBoard.getOrNull(row + 2)?.getOrNull(col)?.second ?: false
        } else
          true
      }
      
      else -> false
    }
  }
  
  private fun checkDiagonal(row: Int, col: Int): Boolean {
    return when {
      uiState.wordBoard.getOrNull(row - 1)?.getOrNull(col - 1)?.second ?: false -> {
        if (checkAroundChar(row - 1, col - 1)) {
          uiState.wordBoard.getOrNull(row - 2)?.getOrNull(col - 2)?.second ?: false
        } else
          true
      }
      
      uiState.wordBoard.getOrNull(row + 1)?.getOrNull(col + 1)?.second ?: false -> {
        if (checkAroundChar(row + 1, col + 1)) {
          uiState.wordBoard.getOrNull(row + 2)?.getOrNull(col + 2)?.second ?: false
        } else
          true
      }
      
      else -> false
    }
  }
  
  private fun checkIfLastChar(row: Int, col: Int): Boolean {
    var foundChar = false
    
    if (uiState.wordBoard.getOrNull(row - 1)?.getOrNull(col)?.second == true) {
      foundChar = true
    }
    
    if (uiState.wordBoard.getOrNull(row + 1)?.getOrNull(col)?.second == true) {
      if (foundChar) return false
      else foundChar = true
    }
    
    if (uiState.wordBoard.getOrNull(row)?.getOrNull(col - 1)?.second == true) {
      if (foundChar) return false
      else foundChar = true
    }
    
    if (uiState.wordBoard.getOrNull(row)?.getOrNull(col + 1)?.second == true) {
      if (foundChar) return false
      else foundChar = true
    }
    
    if (uiState.wordBoard.getOrNull(row - 1)?.getOrNull(col - 1)?.second == true) {
      if (foundChar) return false
      else foundChar = true
    }
    
    if (uiState.wordBoard.getOrNull(row + 1)?.getOrNull(col + 1)?.second == true) {
      if (foundChar) return false
    }
    
    return true
  }
  
  private fun foundAWord() {
    var mutableWordBoard: List<List<Pair<String, Boolean>>> = listOf()
    val buildBoardJob = viewModelScope.launch(defaultDispatcher) {
      mutableWordBoard = uiState.wordBoard.map { innerList ->
        innerList.map { pair ->
          if (pair.second) Pair("", false)
          else pair
        }
      }
    }
    
    wordSearchRepository.foundAWord(word = userWord)
    userWord = ""
    
    buildBoardJob.invokeOnCompletion {
      uiState = uiState.copy(
        foundWords = wordSearchRepository.foundWords,
        wordBoard = mutableWordBoard
      )
    }
  }
  
  private fun checkIfFirstChar(row: Int, col: Int): Boolean {
    return when {
      uiState.wordBoard.getOrNull(row - 1)?.getOrNull(col)?.second ?: false -> false
      
      uiState.wordBoard.getOrNull(row)?.getOrNull(col - 1)?.second ?: false -> false
      
      uiState.wordBoard.getOrNull(row - 1)?.getOrNull(col - 1)?.second ?: false -> false
      
      else -> true
    }
  }
  
  fun selectCharEvent(row: Int, col: Int) {
    viewModelScope.launch(defaultDispatcher) {
      
      if (uiState.wordBoard[row][col].second && checkIfLastChar(row, col)) {
        userWord =
          when {
            userWord.length == 1 -> ""
            userWord[0].uppercase() == uiState.wordBoard[row][col].first.uppercase() -> userWord.substring(1)
            else -> userWord.substring(0, userWord.length - 1)
          }
        
        val mutableWordBoard = uiState.wordBoard.map { it.toMutableList() }.toMutableList()
        mutableWordBoard[row][col] = mutableWordBoard[row][col].copy(second = false)
        uiState = uiState.copy(
          wordBoard = mutableWordBoard
        )
        return@launch
      }
      
      var validSelection: Boolean
      var nothingSelected = true
      
      uiState.wordBoard.forEach { innerList ->
        innerList.forEach {
          if (it.second) {
            validSelection = false
            nothingSelected = false
          }
        }
      }
      
      
      val vertical = async(defaultDispatcher) { checkVertical(row, col) }
      val horizontal = async(defaultDispatcher) { checkHorizontal(row, col) }
      val diagonal = async(defaultDispatcher) { checkDiagonal(row, col) }
      
      validSelection = vertical.await().xor(horizontal.await().xor(diagonal.await()))
      
      if (!nothingSelected && !validSelection) return@launch
      
      if (!checkIfFirstChar(row, col))
        userWord += uiState.wordBoard[row][col].first
      else
        userWord = uiState.wordBoard[row][col].first + userWord
      
      val mutableWordBoard = uiState.wordBoard.map { it.toMutableList() }.toMutableList()
      mutableWordBoard[row][col] = mutableWordBoard[row][col].copy(second = true)
      uiState = uiState.copy(
        wordBoard = mutableWordBoard
      )
      
      if (uiState.words.contains(userWord.lowercase()) || uiState.words.contains(userWord.lowercase().reversed()))
        foundAWord()
    }
  }
  
  private fun checkVerticalLine(boardList: List<List<Pair<String, Boolean>>>, rowStart: Int, colStart: Int, wordLength: Int): Boolean {
    for (i in rowStart..(rowStart + wordLength))
      if (boardList[i][colStart].first != "") return false
    
    return true
  }
  
  private fun checkHorizontalLine(boardList: List<List<Pair<String, Boolean>>>, rowStart: Int, colStart: Int, wordLength: Int): Boolean {
    for (i in colStart..(colStart + wordLength))
      if (boardList[rowStart][i].first != "") return false
    
    return true
  }
  
  private fun checkDiagonalLine(boardList: List<List<Pair<String, Boolean>>>, rowStart: Int, colStart: Int, wordLength: Int): Boolean {
    var colCounter = 0
    for (i in rowStart..(rowStart + wordLength))
      if (boardList[i][colCounter++ + colStart].first != "") return false
    
    return true
  }
  
  private fun findVerticalPair(boardList: List<List<Pair<String, Boolean>>>, word: String): Pair<Int, Int> {
    var rowStart: Int
    var columnStart: Int
    var attempts = 0
    
    do {
      rowStart = Random.nextInt(0, (20 - word.length - 1))
      columnStart = Random.nextInt(0, 20)
      attempts++
      
    } while (!checkVerticalLine(boardList, rowStart = rowStart, colStart = columnStart, wordLength = word.length - 1) && attempts <= 50)
    
    if (attempts > 49) throw Error("Couldn't find a vertical pair before the attempt limit")
    
    return Pair(rowStart, columnStart)
  }
  
  private fun findHorizontalPair(boardList: List<List<Pair<String, Boolean>>>, word: String): Pair<Int, Int> {
    var rowStart: Int
    var columnStart: Int
    var attempts = 0
    
    do {
      rowStart = Random.nextInt(0, 20)
      columnStart = Random.nextInt(0, (20 - word.length - 1))
      attempts++
      
    } while (!checkHorizontalLine(boardList, rowStart, columnStart, word.length - 1) && attempts <= 50)
    
    if (attempts > 49) throw Error("Couldn't find a horizontal pair before the attempt limit")
    
    return Pair(rowStart, columnStart)
  }
  
  private fun findDiagonalPair(boardList: List<List<Pair<String, Boolean>>>, word: String): Pair<Int, Int> {
    var rowStart: Int
    var colStart: Int
    
    do {
      rowStart = Random.nextInt(0, (20 - word.length - 1))
      colStart = Random.nextInt(0, (20 - word.length - 1))
    } while (!checkDiagonalLine(boardList, rowStart, colStart, word.length - 1))
    
    return Pair(rowStart, colStart)
  }
  
  private fun addVerticalWord(boardList: MutableList<MutableList<Pair<String, Boolean>>>, word: String) {
    val (rowStart, colStart) = findVerticalPair(boardList, word)
    
    for (i in rowStart until rowStart + word.length)
      boardList[i][colStart] = boardList[i][colStart].copy(first = word[i - rowStart].uppercase())
  }
  
  private fun addHorizontalWord(boardList: MutableList<MutableList<Pair<String, Boolean>>>, word: String) {
    val (rowStart, colStart) = findHorizontalPair(boardList, word)
    
    for (i in colStart until colStart + word.length)
      boardList[rowStart][i] = boardList[rowStart][i].copy(first = word[i - colStart].uppercase())
  }
  
  private fun buildWordBoard(words: List<String>) = runBlocking {
    val boardList = emptyList.await()
    val failedList = mutableListOf<String>()
    
    words.forEach { word ->
      // 1 = Vertical, 2 = Horizontal, 3 = Diagonal
      when (Random.nextInt(1, 4)) {
        1 -> {
          addVerticalWord(boardList, word)
        }
        
        2 -> {
          addHorizontalWord(boardList, word)
        }
        
        3 -> {
          val rowStart: Int
          val colStart: Int
          try {
            val diagonalPair = findDiagonalPair(boardList, word)
            rowStart = diagonalPair.first
            colStart = diagonalPair.second
          } catch (e: Error) {
            failedList.add(word)
            return@forEach
          }
          var colCounter = 0
          
          for (i in rowStart until rowStart + word.length)
            boardList[i][colCounter++ + colStart] = boardList[i][colCounter + colStart].copy(first = word[i - rowStart].uppercase())
          
        }
      }
    }
    
    failedList.forEach { word ->
      when (Random.nextInt(1, 3)) {
        1 -> {
          addVerticalWord(boardList, word)
        }
        
        2 -> {
          addHorizontalWord(boardList, word)
        }
      }
    }
    
    
    val randomLetters = words.joinToString("").uppercase().toSet()
    return@runBlocking boardList.map { innerList ->
      innerList.map {
        if (it.first == "") Pair(randomLetters.random().toString(), false)
        else it
      }
    }
  }
}