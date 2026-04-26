package com.icb.iwivo.ui.utils

import android.content.Context
import android.media.MediaPlayer
import com.icb.iwivo.R

object SoundUtils {

    fun playCorrect(context: Context) {
        play(context, R.raw.correct)
    }

    fun playWrong(context: Context) {
        play(context, R.raw.wrong)
    }

    private fun play(context: Context, resId: Int) {
        val mp = MediaPlayer.create(context, resId)
        mp.setOnCompletionListener { it.release() }
        mp.start()
    }
}