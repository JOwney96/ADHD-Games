package com.jowney.adhdgames.rockpaperscissors

import kotlin.random.Random

data class RockPaperScissorsUiState(
  val computerChoice: Int = Random.nextInt(RPSValues.ROCK.ordinal, RPSValues.SCISSORS.ordinal + 1),
  val winner: String = ""
)