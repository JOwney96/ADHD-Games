package com.jowney.adhdgames.dotsandboxs

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object DotsHiltModule {
  
  @Provides
  fun dataSource() = DotsSettingsDataSource()
  
  @Provides
  fun repository(dataSource: DotsSettingsDataSource) = DotBoxRepository(dataSource)
}