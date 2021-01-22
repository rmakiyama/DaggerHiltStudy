package com.rmakiyama.daggerhiltstudy

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object HiltStudyAppModule {

    @Provides
    fun providePiyo(): Piyo {
        return PiyoImpl()
    }
}
