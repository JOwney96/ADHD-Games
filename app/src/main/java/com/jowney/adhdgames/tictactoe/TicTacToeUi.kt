package com.jowney.adhdgames.tictactoe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jowney.adhdgames.mainui.Background

@Composable
fun TicTacToe(viewModel: TicTacToeViewModel = hiltViewModel()) {
  Background {
    
    when {
      viewModel.uiState.winnerFound -> WinnerCard("${viewModel.uiState.whoWon} Won!") { viewModel.resetButtonClick() }
      viewModel.uiState.draw -> WinnerCard("Draw!") { viewModel.resetButtonClick() }
    }
    Board(viewModel)
  }
}

@Composable
private fun WinnerCard(text: String, onClick: () -> Unit) = Box(modifier = Modifier.fillMaxSize()) {
  val buttonPrimaryColor = ButtonDefaults.buttonColors(
    containerColor = MaterialTheme.colorScheme.primaryContainer,
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
  )
  
  Column(
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxSize()
      .padding(50.dp)
  ) {
    Text(
      text = text,
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.primary,
    )
    Button(
      onClick,
      colors = buttonPrimaryColor,
      modifier = Modifier
        .offset(y = 25.dp)
    ) { Text("Restart?") }
  }
}


@Composable
private fun Board(viewModel: TicTacToeViewModel) {
  
  @Composable
  fun square(text: String, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    
    val buttonColor = ButtonDefaults.buttonColors(
      contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
      containerColor = colors.primaryContainer,
    )
    
    Button(
      onClick,
      colors = buttonColor,
      border = BorderStroke(2.dp, color = colors.secondaryContainer),
      modifier = Modifier
        .size(100.dp)
        .padding(paddingValues = PaddingValues(
          end = 5.dp,
          bottom = 5.dp
        ))
    ) {
      Text(
        fontSize = 50.sp,
        color = colors.onPrimaryContainer,
        text = text)
    }
  }
  
  @Composable
  fun row(rowID: Int) {
    
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .clip(MaterialTheme.shapes.small)
    ) {
      square(viewModel.uiState.boardList[rowID][0]) { viewModel.buttonClick(rowID, 0) }
      square(viewModel.uiState.boardList[rowID][1]) { viewModel.buttonClick(rowID, 1) }
      square(viewModel.uiState.boardList[rowID][2]) { viewModel.buttonClick(rowID, 2) }
    }
  }
  
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .fillMaxHeight()
  ) {
    row(0)
    row(1)
    row(2)
  }
}