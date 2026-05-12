package com.icb.iwivo.data.model

import androidx.annotation.StringRes

data class Question(
    val id: String,
    val topic: String,
    val gameType: String,
    @StringRes val questionTextRes: Int,
    val optionsRes: List<Int>,
    val correctOptionIndex: Int,
    @StringRes val explanationRes: Int,
    val difficulty: String = "easy"
)