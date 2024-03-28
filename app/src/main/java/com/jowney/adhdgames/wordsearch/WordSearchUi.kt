package com.jowney.adhdgames.wordsearch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jowney.adhdgames.mainui.Background
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun WordSearchMain(viewModel: WordSearchViewModel = hiltViewModel()) {
  val uiScope = rememberCoroutineScope()
  if (viewModel.uiState.reset)
    viewModel.generateWords()
  
  Background {
    Column(
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.fillMaxSize()
    ) {
      if (viewModel.uiState.foundWords.size == viewModel.uiState.words.size)
        Restart(uiScope) { viewModel.flagReset() }
      else
        Words(viewModel.uiState.words, viewModel.uiState.foundWords)
      
      WordBox(uiScope, viewModel.uiState.wordBoard, viewModel)
    }
  }
}

@Composable
private fun Restart(scope: CoroutineScope, onClick: () -> Unit) {
  Button(
    onClick = { scope.launch(Dispatchers.Default) { onClick() } },
    modifier = Modifier.offset(y = (-25).dp),
    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
  ) {
    Text(
      text = "Restart",
      color = MaterialTheme.colorScheme.onPrimary
    )
  }
}

@Composable
private fun Words(words: List<String>, foundWords: List<String>) {
  Box(modifier = Modifier.offset(y = (-15).dp)) {
    Column {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
          .fillMaxWidth()
      ) {
        SingleWord(word = "${words[0]}, ", found = foundWords.contains(words[0]))
        SingleWord(word = "${words[1]}, ", found = foundWords.contains(words[1]))
        SingleWord(word = words[2], found = foundWords.contains(words[2]))
      }
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
          .fillMaxWidth()
      ) {
        SingleWord(word = "${words[3]}, ", found = foundWords.contains(words[3]))
        SingleWord(word = words[4], found = foundWords.contains(words[4]))
      }
    }
  }
}

@Composable
private fun SingleWord(word: String, found: Boolean) {
  Text(
    text = word,
    textDecoration =
    if (found) TextDecoration.LineThrough
    else null,
    color = MaterialTheme.colorScheme.onBackground
  )
}

@Composable
private fun WordBox(
  scope: CoroutineScope,
  wordBoard: List<List<Pair<String, Boolean>>>,
  viewModel: WordSearchViewModel
) {
  
  Box(
    contentAlignment = Alignment.Center,
    modifier = Modifier
      .requiredSize(width = 350.dp, height = 600.dp)
      .fillMaxSize()
      .border(width = 3.dp, color = MaterialTheme.colorScheme.outline)
      .background(color = MaterialTheme.colorScheme.tertiaryContainer)
  ) {
    Column(
      verticalArrangement = Arrangement.SpaceEvenly,
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .fillMaxHeight()
    ) {
      for (col in 0..19) {
        Row(
          horizontalArrangement = Arrangement.spacedBy(1.dp),
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .padding(PaddingValues(horizontal = 5.dp))
            .fillMaxWidth()
        ) {
          for (row in 0..19) {
            Text(
              text = wordBoard[col][row].first,
              textAlign = TextAlign.Center,
              color = MaterialTheme.colorScheme.onTertiaryContainer,
              style =
              if (viewModel.uiState.wordBoard[col][row].second) TextStyle(background = MaterialTheme.colorScheme.inversePrimary)
              else TextStyle(),
              
              modifier = Modifier
                .weight(1f)
                .clickable { scope.launch(Dispatchers.Default) { viewModel.selectCharEvent(col, row) } }
            )
          }
        }
      }
    }
  }
}