package com.jowney.adhdgames.wordsearch

data class WordSearchUiState(
  val words: List<String>,
  val foundWords: List<String>,
  val reset: Boolean = true,
  val wordBoard: List<List<Pair<String, Boolean>>>
)