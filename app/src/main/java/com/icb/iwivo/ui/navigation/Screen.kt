package com.icb.iwivo.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object TopicSelection : Screen("topic_selection")

    data object GameType : Screen("game_type/{topic}") {
        fun createRoute(topic: String) = "game_type/$topic"
    }

    data object Game : Screen("game/{topic}/{gameType}") {
        fun createRoute(topic: String, gameType: String) = "game/$topic/$gameType"
    }
    data object Result : Screen("result/{correct}/{total}") {
        fun createRoute(correct: Int, total: Int) = "result/$correct/$total"
    }
    data object Profile : Screen("profile")

    data object Shop : Screen("shop")

    data object Register : Screen("register")
}