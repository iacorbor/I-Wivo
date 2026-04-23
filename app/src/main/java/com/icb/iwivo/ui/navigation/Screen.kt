package com.icb.iwivo.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object TopicSelection : Screen("topic_selection")
    data object GameType : Screen("game_type/{topic}") {
        fun createRoute(topic: String) = "game_type/$topic"
    }
}