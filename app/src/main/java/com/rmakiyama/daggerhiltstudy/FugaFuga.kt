package com.rmakiyama.daggerhiltstudy

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class FugaFuga @Inject constructor(
    private val fuga: Fuga,
)
