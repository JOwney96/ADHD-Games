package com.jowney.adhdgames.rockpaperscissors

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jowney.adhdgames.mainui.Background

@Composable
fun RockPaperScissorsMain(viewModel: RockPaperScissorsViewModel = hiltViewModel()) {
  val computerString = when (viewModel.uiState.computerChoice) {
    RPSValues.ROCK.ordinal -> "Rock"
    RPSValues.PAPER.ordinal -> "Paper"
    RPSValues.SCISSORS.ordinal -> "Scissors"
    else -> throw Error("Computer Choice was not in range of 0-2.\n" +
      "Choice was ${viewModel.uiState.computerChoice}")
  }
  
  Background {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)
    ) {
      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
      ) {
        if (viewModel.uiState.winner != "") {
          Winner(winnerString = viewModel.uiState.winner)
          ComputerChoice(computerString = computerString)
        }
        
        UserChoices(viewModel)
      }
    }
  }
}

@Composable
private fun Winner(winnerString: String) {
  Text(
    text = "$winnerString won!",
    color = MaterialTheme.colorScheme.onBackground,
    fontSize = 25.sp,
    modifier = Modifier.offset(y = (-150).dp)
  )
}

@Composable
private fun ComputerChoice(computerString: String) {
  Text(
    text = "Computer picks: $computerString",
    color = MaterialTheme.colorScheme.onBackground,
    fontSize = 25.sp,
    modifier = Modifier.offset(y = (-100).dp)
  )
}

@Composable
private fun UserChoices(viewModel: RockPaperScissorsViewModel) {
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxWidth()
      .absoluteOffset(y = 100.dp)
  ) {
    UserButton(text = "Rock") { viewModel.onClick(RPSValues.ROCK.ordinal) }
    UserButton(text = "Paper") { viewModel.onClick(RPSValues.PAPER.ordinal) }
    UserButton(text = "Scissors") { viewModel.onClick(RPSValues.SCISSORS.ordinal) }
  }
}

@Composable
private fun UserButton(text: String, onClick: () -> Unit) {
  ExtendedFloatingActionButton(
    onClick = onClick,
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    modifier = Modifier
      .padding(10.dp)
      .size(width = 300.dp, height = 50.dp)
  ) {
    Text(
      text = text,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      fontSize = 25.sp
    )
  }
}