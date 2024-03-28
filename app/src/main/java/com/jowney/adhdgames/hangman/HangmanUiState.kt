package com.jowney.adhdgames.hangman

data class HangmanUiState(
    val lives: Int,
    val word: String,
    val guesses: List<Char>,
    val reset: Boolean = false
)