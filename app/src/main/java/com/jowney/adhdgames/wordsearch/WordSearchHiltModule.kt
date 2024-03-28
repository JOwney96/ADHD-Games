package com.jowney.adhdgames.wordsearch

import com.jowney.adhdgames.RandomWordDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object WordSearchHiltModule {
  
  @Provides
  fun dataSource() = RandomWordDataSource()
  
  @Provides
  fun repository(dataSource: RandomWordDataSource) = WordSearchRepository(dataSource)
}