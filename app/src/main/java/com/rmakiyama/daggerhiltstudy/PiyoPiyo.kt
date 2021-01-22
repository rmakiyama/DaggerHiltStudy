package com.rmakiyama.daggerhiltstudy

import javax.inject.Inject

interface PiyoPiyo

class PiyoPiyoImpl @Inject constructor(
    private val piyo: Piyo,
) : PiyoPiyo
