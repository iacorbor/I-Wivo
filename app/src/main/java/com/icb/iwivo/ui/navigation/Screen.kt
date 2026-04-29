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
    data object Result : Screen("result/{correct}/{total}/{xp}/{coins}") {
        fun createRoute(correct: Int, total: Int, xp: Int, coins: Int) =
            "result/$correct/$total/$xp/$coins"
    }
    data object Achievements : Screen("achievements")
    data object Profile : Screen("profile")

    data object Shop : Screen("shop")

    data object Register : Screen("register")
}