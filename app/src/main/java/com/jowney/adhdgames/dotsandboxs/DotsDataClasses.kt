package com.jowney.adhdgames.dotsandboxs

data class DotsCoordinate(val x: Int, val y: Int)

data class DotsEdge(val coordinate1: DotsCoordinate, val coordinate2: DotsCoordinate) {
    fun reverse() = DotsEdge(coordinate2, coordinate1)
}

data class DotsSquare(
    val edges: List<DotsOwnerEnum> = List(4) { DotsOwnerEnum.NONE },
    val points: List<DotsCoordinate> = listOf()
)