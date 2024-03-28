package com.jowney.adhdgames.tictactoe

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class TicTacToeHiltModule {
  
  @Provides
  fun providesRepository() = TicTacToeRepository()
}