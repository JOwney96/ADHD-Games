package com.jowney.adhdgames.dotsandboxs

import androidx.compose.ui.graphics.Color

data class DotsSettingsUiState(
    val computerPlaying: Boolean,
    val player1Color: Color,
    val player2Color: Color,
    val start: Boolean = false
)