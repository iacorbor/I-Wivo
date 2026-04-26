package com.icb.iwivo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.icb.iwivo.ui.screens.home.HomeScreen
import com.icb.iwivo.ui.screens.login.LoginScreen
import com.icb.iwivo.ui.screens.topic.TopicSelectionScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.icb.iwivo.ui.screens.game.GameTypeScreen
import com.icb.iwivo.ui.screens.game.GameScreen
import com.icb.iwivo.ui.screens.result.ResultScreen
import com.icb.iwivo.data.repository.UserRepository
import androidx.compose.ui.platform.LocalContext
import com.icb.iwivo.data.repository.AuthRepository
import com.icb.iwivo.ui.screens.profile.ProfileScreen
import com.icb.iwivo.ui.screens.shop.ShopScreen
import com.icb.iwivo.ui.screens.register.RegisterScreen
import com.icb.iwivo.data.repository.FirestoreRepository

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(Screen.Home.route)
                },
                onGoToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onStartClick = {
                    navController.navigate(Screen.TopicSelection.route)
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
                onGameTypeClick = { gameType ->
                    navController.navigate(Screen.Game.createRoute(topic, gameType))
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
                }
            )
        ) { backStackEntry ->

            val context = LocalContext.current
            val userRepository = androidx.compose.runtime.remember { UserRepository(context) }

            val topic = backStackEntry.arguments?.getString("topic").orEmpty()
            val gameType = backStackEntry.arguments?.getString("gameType").orEmpty()

            GameScreen(
                topic = topic,
                gameType = gameType,
                onFinishGame = { correct, total ->
                    val xpEarned = correct * 50
                    val coinsEarned = correct * 10
                    val firestoreRepository = FirestoreRepository()

                    userRepository.addXp(xpEarned)
                    userRepository.addCoins(coinsEarned)
                    userRepository.updateStreak()

                    firestoreRepository.addXp(xpEarned)
                    firestoreRepository.addCoins(coinsEarned)
                    firestoreRepository.updateStreakSimple()


                    navController.navigate(
                        Screen.Result.createRoute(correct, total)
                    )
                }
            )
        }
        composable(
            route = Screen.Result.route,
            arguments = listOf(
                navArgument("correct") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 0

            ResultScreen(
                correct = correct,
                total = total,
                onBackHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogoutClick = {
                    AuthRepository().logout()

                    navController.navigate(Screen.Login.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(Screen.Shop.route) {
            ShopScreen()
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route)
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
    }

}
