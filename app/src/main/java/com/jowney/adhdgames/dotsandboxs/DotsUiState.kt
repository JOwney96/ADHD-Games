package com.jowney.adhdgames.dotsandboxs

data class DotsUiState(
    val edgeMap: Map<DotsEdge, DotsOwnerEnum>,
    val squares: List<DotsSquare>,
    val player1Score: Int,
    val player2Score: Int,
    val gameOver: Boolean,
    val xSize: Int,
    val ySize: Int,
    val selectedPoints: List<DotsCoordinate>,
    val turn: DotsOwnerEnum,
)