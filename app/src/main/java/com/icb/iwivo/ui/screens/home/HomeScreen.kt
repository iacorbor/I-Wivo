package com.icb.iwivo.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.icb.iwivo.R
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoHeader
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.components.WivoStatPill
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary

@Composable
fun HomeScreen(
    firestoreRepository: FirestoreRepository,
    onStartClick: () -> Unit,
    onProfileClick: () -> Unit,
    onShopClick: () -> Unit
) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    var xp by remember { mutableIntStateOf(0) }
    var coins by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }

    var avatarBase64 by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }

    LaunchedEffect(currentUser?.uid) {
        firestoreRepository.getCurrentUserData(
            onResult = { remoteXp, remoteCoins, remoteStreak ->
                xp = remoteXp
                coins = remoteCoins
                streak = remoteStreak
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
                    // No bloqueamos el Home si falla la foto.
                }
            )
        }
    }

    val level = (xp / 500) + 1

    WivoScreen {
        Column(modifier = Modifier.fillMaxSize()) {

            Spacer(modifier = Modifier.height(32.dp))

            WivoHeader(
                level = level,
                xp = xp,
                username = username,
                avatarBase64 = avatarBase64,
                onAvatarClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                WivoStatPill("Lv", level.toString())
                WivoStatPill("XP", xp.toString())
                WivoStatPill("💰", coins.toString())
                WivoStatPill("🔥", streak.toString())
            }

            Spacer(modifier = Modifier.height(24.dp))

            ProgressCard(
                xp = xp,
                coins = coins,
                streak = streak
            )

            Spacer(modifier = Modifier.height(16.dp))

            DailyMissionCard()

            Spacer(modifier = Modifier.height(24.dp))

            WivoButton(
                text = stringResource(R.string.start_training),
                onClick = onStartClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            WivoButton(
                text = stringResource(R.string.view_profile),
                onClick = onProfileClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            WivoButton(
                text = stringResource(R.string.open_shop),
                onClick = onShopClick
            )
        }
    }
}

@Composable
private fun ProgressCard(
    xp: Int,
    coins: Int,
    streak: Int
) {
    val level = (xp / 500) + 1
    val xpCurrentLevel = xp % 500
    val progress = xpCurrentLevel / 500f

    WivoCard {
        Text(
            text = stringResource(R.string.progress_title),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.level, level),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = PurplePrimary,
            trackColor = MaterialTheme.colorScheme.surface
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.xp, xpCurrentLevel, 500),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(R.string.home_coins_streak, coins, streak),
            color = TextSecondary
        )
    }
}

@Composable
private fun DailyMissionCard() {
    WivoCard {
        Text(
            text = stringResource(R.string.next_mission),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.daily_mission_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = stringResource(R.string.daily_mission_subtitle),
            color = TextSecondary
        )
    }
}

@Composable
private fun LearningPathCard(
    title: String,
    subtitle: String,
    color: Color
) {
    WivoCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = subtitle,
                    color = TextSecondary
                )
            }

            Text(
                text = stringResource(R.string.arrow),
                style = MaterialTheme.typography.headlineSmall,
                color = color
            )
        }
    }
}