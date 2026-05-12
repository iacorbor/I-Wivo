package com.icb.iwivo.ui.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Home : Screen("home")
    data object TopicSelection : Screen("topic_selection")

    data object GameType : Screen("game_type/{topic}") {
        fun createRoute(topic: String): String {
            return "game_type/$topic"
        }
    }

    data object Game : Screen("game/{topic}/{gameType}/{difficulty}") {
        fun createRoute(
            topic: String,
            gameType: String,
            difficulty: String
        ): String {
            return "game/$topic/$gameType/$difficulty"
        }
    }

    object Result : Screen(
        "result/{correct}/{total}/{xp}/{coins}/{bestStreak}/{unlockedBadgeIds}"
    ) {
        fun createRoute(
            correct: Int,
            total: Int,
            xp: Int,
            coins: Int,
            bestStreak: Int,
            unlockedBadgeIds: String
        ): String {
            val encodedUnlockedBadgeIds = Uri.encode(unlockedBadgeIds)
            return "result/$correct/$total/$xp/$coins/$bestStreak/$encodedUnlockedBadgeIds"
        }
    }

    data object Achievements : Screen("achievements")
    data object Profile : Screen("profile")
    data object Shop : Screen("shop")
    data object Register : Screen("register")
    data object EditProfile : Screen("edit_profile")
    data object Settings : Screen("settings")
}