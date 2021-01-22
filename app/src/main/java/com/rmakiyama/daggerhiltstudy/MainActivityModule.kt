package com.rmakiyama.daggerhiltstudy

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides
    fun providePiyoPiyo(
        piyo: Piyo,
    ): PiyoPiyo {
        return PiyoPiyoImpl(piyo)
    }
}
