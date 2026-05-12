package com.icb.iwivo.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.icb.iwivo.data.repository.AuthRepository
import com.icb.iwivo.data.repository.BadgeRepository
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.screens.badges.BadgesScreen
import com.icb.iwivo.ui.screens.game.GameScreen
import com.icb.iwivo.ui.screens.game.GameTypeScreen
import com.icb.iwivo.ui.screens.home.HomeScreen
import com.icb.iwivo.ui.screens.login.LoginScreen
import com.icb.iwivo.ui.screens.profile.EditProfileScreen
import com.icb.iwivo.ui.screens.profile.ProfileScreen
import com.icb.iwivo.ui.screens.register.RegisterScreen
import com.icb.iwivo.ui.screens.result.ResultScreen
import com.icb.iwivo.ui.screens.settings.SettingsScreen
import com.icb.iwivo.ui.screens.shop.ShopScreen
import com.icb.iwivo.ui.screens.topic.TopicSelectionScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    val authRepository = remember { AuthRepository() }
    val firestoreRepository = remember { FirestoreRepository() }

    val auth = FirebaseAuth.getInstance()

    val startDestination = if (auth.currentUser != null) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onGoToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                firestoreRepository = firestoreRepository,
                onStartClick = {
                    navController.navigate(Screen.TopicSelection.route)
                },
                onContinueLastModeClick = { topic, gameType ->
                    navController.navigate(
                        Screen.Game.createRoute(
                            topic = topic,
                            gameType = gameType,
                            difficulty = "easy"
                        )
                    )
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                },
                onShopClick = {
                    navController.navigate(Screen.Shop.route)
                }
            )
        }

        composable(Screen.TopicSelection.route) {
            TopicSelectionScreen(
                firestoreRepository = firestoreRepository,
                onTopicClick = { topic ->
                    navController.navigate(Screen.GameType.createRoute(topic))
                }
            )
        }

        composable(
            route = Screen.GameType.route,
            arguments = listOf(
                navArgument("topic") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic").orEmpty()

            GameTypeScreen(
                topic = topic,
                firestoreRepository = firestoreRepository,
                onGameTypeClick = { gameType, difficulty ->
                    navController.navigate(
                        Screen.Game.createRoute(
                            topic = topic,
                            gameType = gameType,
                            difficulty = difficulty
                        )
                    )
                }
            )
        }

        composable(
            route = Screen.Game.route,
            arguments = listOf(
                navArgument("topic") {
                    type = NavType.StringType
                },
                navArgument("gameType") {
                    type = NavType.StringType
                },
                navArgument("difficulty") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val topic = backStackEntry.arguments?.getString("topic").orEmpty()
            val gameType = backStackEntry.arguments?.getString("gameType").orEmpty()
            val difficulty = backStackEntry.arguments?.getString("difficulty").orEmpty()
                .ifBlank { "easy" }

            GameScreen(
                topic = topic,
                gameType = gameType,
                difficulty = difficulty,
                firestoreRepository = firestoreRepository,
                onFinishGame = { correct, total, bestStreak ->
                    val badgeRepository = BadgeRepository()

                    val xpEarned = correct * 50
                    val coinsEarned = correct * 10

                    firestoreRepository.saveLastPlayedMode(
                        topic = topic,
                        gameType = gameType
                    )

                    firestoreRepository.getCurrentUserProgress(
                        onResult = { currentXp, currentCoins, currentStreak, lastActivityDate ->

                            val badgesBefore = badgeRepository.getBadges(
                                xp = currentXp,
                                coins = currentCoins,
                                streak = currentStreak
                            )

                            val xpAfter = currentXp + xpEarned
                            val coinsAfter = currentCoins + coinsEarned

                            val streakAfter = firestoreRepository.calculateStreakAfterActivity(
                                currentStreak = currentStreak,
                                lastActivityDate = lastActivityDate
                            )

                            val badgesAfter = badgeRepository.getBadges(
                                xp = xpAfter,
                                coins = coinsAfter,
                                streak = streakAfter
                            )

                            val unlockedBeforeIds = badgesBefore
                                .filter { it.unlocked }
                                .map { it.id }

                            val newlyUnlockedBadges = badgesAfter.filter { badgeAfter ->
                                badgeAfter.unlocked && badgeAfter.id !in unlockedBeforeIds
                            }

                            val unlockedBadgeIds = newlyUnlockedBadges
                                .joinToString("|") { it.id }
                                .ifBlank { "none" }

                            navController.navigate(
                                Screen.Result.createRoute(
                                    correct = correct,
                                    total = total,
                                    xp = xpEarned,
                                    coins = coinsEarned,
                                    bestStreak = bestStreak,
                                    unlockedBadgeIds = unlockedBadgeIds
                                )
                            )

                            firestoreRepository.addXp(xpEarned)
                            firestoreRepository.addCoins(coinsEarned)
                            firestoreRepository.updateStreak()
                            firestoreRepository.updateDailyMissionAfterGame(
                                questionsAnswered = total
                            )
                        }
                    )
                }
            )
        }

        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("correct") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType },
                navArgument("xp") { type = NavType.IntType },
                navArgument("coins") { type = NavType.IntType },
                navArgument("bestStreak") { type = NavType.IntType },
                navArgument("unlockedBadgeIds") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0
            val xp = backStackEntry.arguments?.getInt("xp") ?: 0
            val coins = backStackEntry.arguments?.getInt("coins") ?: 0
            val bestStreak = backStackEntry.arguments?.getInt("bestStreak") ?: 0

            val unlockedBadgeIdsRaw =
                backStackEntry.arguments?.getString("unlockedBadgeIds") ?: "none"

            val decodedUnlockedBadgeIds = Uri.decode(unlockedBadgeIdsRaw)

            val unlockedBadgeIds = if (decodedUnlockedBadgeIds == "none") {
                emptyList()
            } else {
                decodedUnlockedBadgeIds
                    .split("|")
                    .filter { it.isNotBlank() }
            }

            ResultScreen(
                correct = correct,
                total = total,
                xpEarned = xp,
                coinsEarned = coins,
                bestStreak = bestStreak,
                unlockedBadgeIds = unlockedBadgeIds,
                firestoreRepository = firestoreRepository,
                onBackHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Achievements.route) {
            var xp by remember { mutableIntStateOf(0) }
            var coins by remember { mutableIntStateOf(0) }
            var streak by remember { mutableIntStateOf(0) }

            LaunchedEffect(Unit) {
                firestoreRepository.getCurrentUserData(
                    onResult = { remoteXp, remoteCoins, remoteStreak ->
                        xp = remoteXp
                        coins = remoteCoins
                        streak = remoteStreak
                    }
                )
            }

            BadgesScreen(
                xp = xp,
                coins = coins,
                streak = streak,
                firestoreRepository = firestoreRepository
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                authRepository = authRepository,
                firestoreRepository = firestoreRepository,
                onEditProfileClick = {
                    navController.navigate(Screen.EditProfile.route)
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onAchievementsClick = {
                    navController.navigate(Screen.Achievements.route)
                },
                onLogout = {
                    authRepository.logout()

                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                firestoreRepository = firestoreRepository,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                firestoreRepository = firestoreRepository,
                onBackClick = {
                    navController.popBackStack()
                },
                onProfileUpdated = {
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Profile.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.Shop.route) {
            ShopScreen(
                firestoreRepository = firestoreRepository
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }
}