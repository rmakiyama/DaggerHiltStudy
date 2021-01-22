package com.rmakiyama.daggerhiltstudy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var hoge: Hoge
    @Inject lateinit var hogeHoge: HogeHoge
    @Inject lateinit var fuga: Fuga
    @Inject lateinit var fugaFuga: FugaFuga
    @Inject lateinit var piyo: Piyo
    @Inject lateinit var piyoPiyo: PiyoPiyo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
