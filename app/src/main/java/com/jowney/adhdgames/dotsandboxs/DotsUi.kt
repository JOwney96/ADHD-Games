package com.jowney.adhdgames.dotsandboxs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.jowney.adhdgames.mainui.Background

@Composable
fun DotsMain(viewModel: DotsViewModel = hiltViewModel()) {
  Background {
    if (viewModel.settingsUiState.start) DotsGame(viewModel)
    else DotsSettingsMain(viewModel)
  }
}

@Composable
fun DotsGame(viewModel: DotsViewModel) {
  Scores(
    player1Score = viewModel.uiState.player1Score,
    player2Score = viewModel.uiState.player2Score
  )
  
  GameBoard(viewModel)
  
  if (viewModel.uiState.gameOver) {
    val gameOverText = when {
      viewModel.uiState.player1Score > viewModel.uiState.player2Score -> "Player 1 won with ${viewModel.uiState.player1Score} points"
      viewModel.uiState.player1Score < viewModel.uiState.player2Score -> "Player 2 won with ${viewModel.uiState.player2Score} points"
      else -> "Draw!"
    }
    
    GameOver(gameOverText) { viewModel.resetGame() }
  }
}

@Composable
private fun SingleScore(playerName: String, score: Int) {
  val mColors = MaterialTheme.colorScheme
  Box(
    modifier = Modifier.sizeIn(maxHeight = 100.dp, maxWidth = 200.dp)
  ) {
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxSize()
    ) {
      Row {
        Text(
          text = score.toString(),
          color = mColors.onBackground,
          fontSize = 35.sp
        )
      }
      Row {
        Text(
          text = playerName,
          color = mColors.onBackground,
          fontSize = 20.sp
        )
      }
    }
  }
}

@Composable
private fun Scores(player1Score: Int, player2Score: Int) {
  Box {
    Row {
      SingleScore(playerName = "Player 1", score = player1Score)
      SingleScore(playerName = "Player 2", score = player2Score)
    }
  }
}

@Composable
private fun GameButton(
  viewModel: DotsViewModel,
  x: Int,
  y: Int,
  onClick: () -> Unit
) {
  val edgeMap = viewModel.uiState.edgeMap
  val isSelected = viewModel.uiState.selectedPoints.contains(DotsCoordinate(x, y))
  val outlineColor = MaterialTheme.colorScheme.onPrimary
  
  return Button(
    onClick = onClick,
    border =
    if (isSelected)
      BorderStroke(width = 2.dp, color = outlineColor)
    else null,
    
    modifier = Modifier
      .padding(20.dp)
      .size(25.dp)
      .drawWithContent {
        val verticalEdge = DotsEdge(
          DotsCoordinate(x, y),
          DotsCoordinate(x, y + 1)
        )
        
        val horizontalEdge = DotsEdge(
          DotsCoordinate(x, y),
          DotsCoordinate(x + 1, y)
        )
        
        if (edgeMap.containsKey(verticalEdge) || edgeMap.containsKey(verticalEdge.reverse())
        ) {
          drawLine(
            color =
            if (edgeMap[verticalEdge] == DotsOwnerEnum.PLAYER1 || edgeMap[verticalEdge.reverse()] == DotsOwnerEnum.PLAYER1) viewModel.settingsUiState.player1Color
            else viewModel.settingsUiState.player2Color,
            
            start = center,
            end = center + Offset(x = center.x + 150.dp.value, y = 0f),
            strokeWidth = 25.0F
          )
        }
        
        if (edgeMap.containsKey(horizontalEdge) || edgeMap.containsKey(horizontalEdge.reverse())
        ) {
          drawLine(
            color =
            if (edgeMap[horizontalEdge] == DotsOwnerEnum.PLAYER1 || edgeMap[horizontalEdge.reverse()] == DotsOwnerEnum.PLAYER1) viewModel.settingsUiState.player1Color
            else viewModel.settingsUiState.player2Color,
            
            start = center,
            end = center + Offset(x = 0F, y = center.y + 150.dp.value),
            strokeWidth = 25.0F
          )
        }
        
        drawContent()
      }
  ) {}
}

@Composable
private fun GameBoard(viewModel: DotsViewModel) {
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .fillMaxSize()
  ) {
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxHeight()
        .padding(5.dp)
    ) {
      for (x in 0..<viewModel.uiState.ySize) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center,
          modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
        ) {
          for (y in 0..<viewModel.uiState.xSize) {
            GameButton(
              viewModel = viewModel,
              x = x,
              y = y,
              onClick = { viewModel.onClick(DotsCoordinate(x, y), viewModel.uiState.turn) }
            )
          }
        }
      }
    }
  }
}

@Composable
private fun GameOver(gameOverText: String, resetOnClick: () -> Unit) {
  Box(
    modifier = Modifier
      .zIndex(2.0F)
      .background(MaterialTheme.colorScheme.primaryContainer)
      .fillMaxSize()
  ) {
    Text(
      text = gameOverText,
      color = MaterialTheme.colorScheme.onPrimaryContainer,
      modifier = Modifier.align(Alignment.Center)
    )
    
    Button(
      onClick = resetOnClick,
      colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
      ),
      modifier = Modifier
        .align(Alignment.Center)
        .offset(y = 50.dp)
    ) {
      Text(
        text = "Restart?",
        color = MaterialTheme.colorScheme.onPrimary
      )
    }
  }
}