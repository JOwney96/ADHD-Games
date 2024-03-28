package com.jowney.adhdgames

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.jowney.adhdgames.dotsandboxs.DotsMain
import com.jowney.adhdgames.hangman.HangmanMain
import com.jowney.adhdgames.mainui.AboutMain
import com.jowney.adhdgames.mainui.Home
import com.jowney.adhdgames.rockpaperscissors.RockPaperScissorsMain
import com.jowney.adhdgames.tictactoe.TicTacToe
import com.jowney.adhdgames.wordsearch.WordSearchMain

@Composable
fun ADHDNavHost(navController: NavHostController) {
  NavHost(navController = navController, startDestination = "home") {
    composable("home") { Home(navController) }
    
    composable("tictactoe") { TicTacToe() }
    
    composable("hangman") { HangmanMain() }
    
    composable("about") { AboutMain() }
    
    composable("wordsearch") { WordSearchMain() }
    
    composable("rockpaperscissors") { RockPaperScissorsMain() }
    
    composable("dotsandboxes") { DotsMain() }
  }
}