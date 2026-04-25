package com.icb.iwivo.data.model

data class Question(
    val id: String,
    val topic: String,
    val gameType: String,
    val questionText: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String
)