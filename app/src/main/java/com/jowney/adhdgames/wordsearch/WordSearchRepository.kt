package com.jowney.adhdgames.wordsearch

import com.jowney.adhdgames.RandomWordDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WordSearchRepository
@Inject constructor(private val randomWordDataSource: RandomWordDataSource) {
  var words: List<String> = listOf()
    private set
  var foundWords = listOf<String>()
    private set
  
  
  suspend fun generateWords(dispatcher: CoroutineDispatcher): List<String> {
    val newWords = mutableListOf<String>()
    
    withContext(dispatcher) {
      for (i in 1..5) {
        launch(dispatcher) {
          newWords.add(randomWordDataSource.getRandomWord())
        }
      }
    }
    
    words = newWords
    foundWords = listOf()
    return words
  }
  
  fun foundAWord(word: String) {
    val newFoundWords = foundWords.toMutableList()
    newFoundWords.add(word.lowercase())
    foundWords = newFoundWords
  }
}