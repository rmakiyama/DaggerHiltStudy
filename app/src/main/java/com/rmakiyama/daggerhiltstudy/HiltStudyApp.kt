package com.rmakiyama.daggerhiltstudy

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HiltStudyApp : Application() {
    @Inject lateinit var hoge: Hoge
    @Inject lateinit var fuga: Fuga
    @Inject lateinit var piyo: Piyo
}
