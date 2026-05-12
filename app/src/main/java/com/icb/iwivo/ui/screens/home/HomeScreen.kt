package com.icb.iwivo.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.firebase.auth.FirebaseAuth
import com.icb.iwivo.R
import com.icb.iwivo.data.model.ShopItemCategory
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoHeader
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.components.WivoStatPill
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    firestoreRepository: FirestoreRepository,
    onStartClick: () -> Unit,
    onContinueLastModeClick: (topic: String, gameType: String) -> Unit,
    onProfileClick: () -> Unit,
    onShopClick: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val lifecycleOwner = LocalLifecycleOwner.current

    var xp by remember { mutableIntStateOf(0) }
    var coins by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }

    var avatarBase64 by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    var equippedAvatarBorderId by remember { mutableStateOf<String?>(null) }
    var equippedBackgroundId by remember { mutableStateOf<String?>(null) }
    var equippedButtonStyleId by remember { mutableStateOf<String?>(null) }
    var equippedThemeId by remember { mutableStateOf<String?>(null) }
    var equippedEffectId by remember { mutableStateOf<String?>(null) }

    var dailyAnsweredQuestions by remember { mutableIntStateOf(0) }
    var dailyGoalQuestions by remember { mutableIntStateOf(10) }
    var dailyMissionClaimed by remember { mutableStateOf(false) }
    var isClaimingDailyMission by remember { mutableStateOf(false) }

    var lastPlayedTopic by remember { mutableStateOf<String?>(null) }
    var lastPlayedGameType by remember { mutableStateOf<String?>(null) }

    fun loadHomeData() {
        firestoreRepository.getCurrentUserData(
            onResult = { remoteXp, remoteCoins, remoteStreak ->
                xp = remoteXp
                coins = remoteCoins
                streak = remoteStreak
            }
        )

        firestoreRepository.getLastPlayedMode(
            onResult = { topic, gameType ->
                lastPlayedTopic = topic
                lastPlayedGameType = gameType
            },
            onError = {
                // No bloqueo el Home si falla el último modo.
            }
        )

        firestoreRepository.getShopStateByCategory(
            onResult = { _, _, equippedItemsByCategory ->
                equippedAvatarBorderId = equippedItemsByCategory[ShopItemCategory.AVATAR_BORDER]
                equippedBackgroundId = equippedItemsByCategory[ShopItemCategory.BACKGROUND]
                equippedButtonStyleId = equippedItemsByCategory[ShopItemCategory.BUTTON_STYLE]
                equippedThemeId = equippedItemsByCategory[ShopItemCategory.THEME]
                equippedEffectId = equippedItemsByCategory[ShopItemCategory.EFFECT]
            },
            onError = {
                // No bloqueo el Home si falla la tienda.
            }
        )

        firestoreRepository.getDailyMissionProgress(
            onResult = { answeredQuestions, goalQuestions, claimed ->
                dailyAnsweredQuestions = answeredQuestions
                dailyGoalQuestions = goalQuestions
                dailyMissionClaimed = claimed
            },
            onError = {
                // No bloqueo el Home si falla la misión diaria.
            }
        )

        val uid = currentUser?.uid

        if (uid != null) {
            firestoreRepository.getUserProfile(
                uid = uid,
                onSuccess = { profile ->
                    username = profile.username
                    avatarBase64 = profile.avatarBase64
                },
                onError = {
                    // No bloqueo el Home si falla el perfil.
                }
            )
        }
    }

    LaunchedEffect(currentUser?.uid) {
        loadHomeData()
    }

    DisposableEffect(lifecycleOwner, currentUser?.uid) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                loadHomeData()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val level = (xp / 500) + 1

    WivoScreen(
        backgroundItemId = equippedBackgroundId,
        themeItemId = equippedThemeId
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            WivoHeader(
                level = level,
                xp = xp,
                username = username,
                avatarBase64 = avatarBase64,
                equippedShopItemId = equippedAvatarBorderId,
                onAvatarClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WivoStatPill("Lv", level.toString())
                WivoStatPill("XP", xp.toString())
                WivoStatPill("💰", coins.toString())
                WivoStatPill("🔥", streak.toString())
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                LastPlayedCard(
                    topic = lastPlayedTopic,
                    gameType = lastPlayedGameType,
                    themeItemId = equippedThemeId,
                    effectItemId = equippedEffectId,
                    onClick = {
                        val topic = lastPlayedTopic
                        val gameType = lastPlayedGameType

                        if (!topic.isNullOrBlank() && !gameType.isNullOrBlank()) {
                            onContinueLastModeClick(topic, gameType)
                        }
                    }
                )

                if (!lastPlayedTopic.isNullOrBlank() && !lastPlayedGameType.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                DailyMissionCard(
                    answeredQuestions = dailyAnsweredQuestions,
                    goalQuestions = dailyGoalQuestions,
                    claimed = dailyMissionClaimed,
                    isClaiming = isClaimingDailyMission,
                    themeItemId = equippedThemeId,
                    effectItemId = equippedEffectId,
                    onClaimClick = {
                        val completed = dailyAnsweredQuestions >= dailyGoalQuestions

                        if (!completed || dailyMissionClaimed || isClaimingDailyMission) {
                            return@DailyMissionCard
                        }

                        isClaimingDailyMission = true

                        firestoreRepository.claimDailyMissionReward(
                            onSuccess = {
                                dailyMissionClaimed = true
                                isClaimingDailyMission = false
                                loadHomeData()
                            },
                            onError = {
                                isClaimingDailyMission = false
                                // No bloqueo el Home si falla la recompensa.
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                MotivationCard(
                    xp = xp,
                    coins = coins,
                    streak = streak,
                    themeItemId = equippedThemeId,
                    effectItemId = equippedEffectId
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            WivoButton(
                text = stringResource(R.string.start_training),
                onClick = onStartClick,
                buttonStyleItemId = equippedButtonStyleId,
                themeItemId = equippedThemeId
            )

            Spacer(modifier = Modifier.height(12.dp))

            WivoButton(
                text = stringResource(R.string.open_shop),
                onClick = onShopClick,
                buttonStyleItemId = equippedButtonStyleId,
                themeItemId = equippedThemeId
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DailyMissionCard(
    answeredQuestions: Int,
    goalQuestions: Int,
    claimed: Boolean,
    isClaiming: Boolean,
    themeItemId: String?,
    effectItemId: String?,
    onClaimClick: () -> Unit
){
    val safeGoal = goalQuestions.coerceAtLeast(1)
    val safeAnswered = answeredQuestions.coerceIn(0, safeGoal)
    val progress = safeAnswered.toFloat() / safeGoal.toFloat()
    val completed = safeAnswered >= safeGoal

    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = stringResource(R.string.daily_mission_real_label),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.daily_mission_answer_questions_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.daily_mission_answer_questions_subtitle),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(14.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = if (completed) GreenAccent else PurplePrimary,
            trackColor = MaterialTheme.colorScheme.surface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(
                R.string.daily_mission_progress,
                safeAnswered,
                safeGoal
            ),
            color = if (completed) GreenAccent else TextSecondary,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.daily_mission_reward),
            color = PurplePrimary,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onClaimClick,
            enabled = completed && !claimed && !isClaiming,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenAccent,
                disabledContainerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = when {
                    claimed -> stringResource(R.string.daily_mission_claimed)
                    isClaiming -> stringResource(R.string.daily_mission_claiming)
                    completed -> stringResource(R.string.daily_mission_claim)
                    else -> stringResource(R.string.daily_mission_incomplete)
                },
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun LastPlayedCard(
    topic: String?,
    gameType: String?,
    themeItemId: String?,
    effectItemId: String?,
    onClick: () -> Unit
) {
    if (topic.isNullOrBlank() || gameType.isNullOrBlank()) {
        return
    }

    val readableTopic = when (topic) {
        "java" -> stringResource(R.string.topic_java)
        "kotlin" -> stringResource(R.string.topic_kotlin)
        "sql" -> stringResource(R.string.topic_sql)
        else -> topic.uppercase()
    }

    val readableGameType = when (gameType) {
        "test" -> stringResource(R.string.game_type_test)
        "true_false" -> stringResource(R.string.game_type_true_false)
        "complete_code" -> stringResource(R.string.game_type_complete_code)
        else -> gameType
    }

    WivoCard(
        modifier = Modifier.clickable {
            onClick()
        },
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = stringResource(R.string.home_last_played_label),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.home_last_played_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(
                R.string.home_last_played_description,
                readableTopic,
                readableGameType
            ),
            color = TextSecondary
        )
    }
}

@Composable
private fun MotivationCard(
    xp: Int,
    coins: Int,
    streak: Int,
    themeItemId: String?,
    effectItemId: String?
) {
    val message = when {
        streak >= 7 -> stringResource(R.string.home_motivation_streak)
        xp >= 500 -> stringResource(R.string.home_motivation_leveling)
        coins >= 100 -> stringResource(R.string.home_motivation_shop)
        else -> stringResource(R.string.home_motivation_default)
    }

    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Text(
            text = stringResource(R.string.home_motivation_label),
            color = PurplePrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.home_motivation_footer),
            color = GreenAccent,
            style = MaterialTheme.typography.bodySmall
        )
    }
}