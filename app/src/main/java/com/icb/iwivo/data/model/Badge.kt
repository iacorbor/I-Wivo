package com.icb.iwivo.data.model

data class Badge(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val icon: String = "🏅",
    val requirement: String = "",
    val unlocked: Boolean = false
)