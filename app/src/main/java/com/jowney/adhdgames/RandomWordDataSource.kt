package com.jowney.adhdgames

import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.random.Random

class RandomWordDataSource @Inject constructor() {
  fun getRandomWord(): String {
    
    val resources = MainActivity.ctx().resources
    
    val lineIndex: Int
    val fileStream: InputStreamReader
    
    when (Random.nextInt(1, 4)) {
      1 -> {
        fileStream = resources.assets.open("wordlist_filtered_1.txt").reader()
        lineIndex = Random.nextInt(1, 6483)
      }
      
      2 -> {
        fileStream = resources.assets.open("wordlist_filtered_2.txt").reader()
        lineIndex = Random.nextInt(1, 6483)
      }
      
      3 -> {
        fileStream = resources.assets.open("wordlist_filtered_3.txt").reader()
        lineIndex = Random.nextInt(1, 6483)
      }
      
      4 -> {
        fileStream = resources.assets.open("wordlist_filtered_4.txt").reader()
        lineIndex = Random.nextInt(1, 6486)
      }
      
      else -> throw Error("File Index wasn't in range of 1-4")
    }
    
    var counter = 1
    var word: String? = null
    
    fileStream.use { stream ->
      stream.forEachLine {
        if (lineIndex == counter) {
          word = it
        }
        counter++
      }
    }
    
    return word ?: throw Error("HangmanDataSource - word was null meaning couldn't " +
      "find a word")
  }
}