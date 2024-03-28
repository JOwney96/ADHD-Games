package com.jowney.adhdgames.hangman

import com.jowney.adhdgames.RandomWordDataSource
import javax.inject.Inject

private const val MAX_LIVES = 6

class HangmanRepository
@Inject constructor(private val dataSource: RandomWordDataSource) {
  var computerWord = ""
    private set
  var lives = MAX_LIVES
    private set
  var guessedLetters = listOf<Char>()
    private set
  
  private fun generateWord() {
    computerWord = dataSource.getRandomWord()
  }
  
  fun resetRepository() {
    lives = MAX_LIVES
    guessedLetters = listOf()
    generateWord()
  }
  
  fun letterGuess(char: Char) {
    if (lives == 0) return
    if (!computerWord.contains(char, ignoreCase = true)) lives--
    
    val guessedLettersCopy = guessedLetters.toMutableList()
    guessedLettersCopy.add(char)
    guessedLetters = guessedLettersCopy.toList()
  }
}
