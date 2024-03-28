package com.jowney.adhdgames.dotsandboxs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

private val colors = listOf(
  Color.Blue,
  Color.Red,
  Color.Green,
  Color.Black,
  Color(60, 35, 125), // Purple for my slut
  Color(163, 98, 0), // Orange
  Color(162, 228, 184), // Its fucking mint bro
  Color(222, 51, 128), // Hoooooot pink
  Color(135, 206, 235) // High like the sky blue
)

@Composable
fun DotsSettingsMain(viewModel: DotsViewModel = hiltViewModel()) {
  AiPlayingCheckbox(computerPlaying = viewModel.settingsUiState.computerPlaying) { viewModel.computerPlayingHandler(!viewModel.settingsUiState.computerPlaying) }
  ColorSelector("Player 1", 0, viewModel)
  ColorSelector("Player 2", 150, viewModel)
  StartButton { viewModel.startGame() }
}

@Composable
fun AiPlayingCheckbox(computerPlaying: Boolean, onClick: (Boolean) -> Unit) {
  Column(
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .fillMaxWidth()
        .offset(y = (100).dp)
  ) {
    Text(
      text = "Single Player?",
      color = MaterialTheme.colorScheme.onBackground,
      fontSize = 25.sp
    )
    Checkbox(
      checked = computerPlaying,
      onCheckedChange = onClick,
      modifier = Modifier.scale(1.5F)
    )
  }
}

@Composable
fun ColorSquare(color: Color, onClick: () -> Unit) {
  Canvas(
    modifier = Modifier
        .size(30.dp)
        .clickable(onClick = onClick)
  ) {
    drawRect(color = color)
  }
}

@Composable
fun ColorSelector(player: String, startingYOffset: Int, viewModel: DotsViewModel) {
  Box {
    Text(
      text = player,
      color = MaterialTheme.colorScheme.onBackground,
      modifier = Modifier
          .align(Alignment.Center)
          .offset(y = (-100).dp + startingYOffset.dp)
          .drawBehind {
              val strokeWidthPx = 2.dp.toPx()
              val verticalOffset = size.height - 2.sp.toPx()
              
              drawLine(
                  color =
                  if (player == "Player 1")
                      viewModel.settingsUiState.player1Color
                  else
                      viewModel.settingsUiState.player2Color,
                  
                  strokeWidth = strokeWidthPx,
                  start = Offset(0F, verticalOffset),
                  end = Offset(size.width, verticalOffset)
              )
          }
    )
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
      modifier = Modifier
          .fillMaxWidth()
          .offset(y = (-50).dp + startingYOffset.dp)
    ) {
      for (color in colors) {
        Column(
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
              .padding(5.dp)
              .fillMaxHeight()
        ) {
          ColorSquare(color = color) {
            if (player == "Player 1")
              viewModel.player1ColorHandler(color)
            else
              viewModel.player2ColorHandler(color)
          }
        }
      }
    }
  }
}

@Composable
fun StartButton(onClick: () -> Unit) {
  Column(
    verticalArrangement = Arrangement.Bottom,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
        .fillMaxWidth()
        .offset(y = (-50).dp)
  ) {
    Row(
      verticalAlignment = Alignment.Bottom,
      horizontalArrangement = Arrangement.Center,
      modifier = Modifier.fillMaxHeight()
    ) {
      Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
      ) {
        Text(
          text = "Start",
          color = MaterialTheme.colorScheme.onPrimary
        )
      }
    }
  }
}