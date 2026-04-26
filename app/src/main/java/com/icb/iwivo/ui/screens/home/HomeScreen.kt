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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.ui.theme.BluePrimary
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import com.icb.iwivo.data.repository.FirestoreRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen

@Composable
fun HomeScreen(
    onStartClick: () -> Unit,
    onProfileClick: () -> Unit,
    onShopClick: () -> Unit
) {
    WivoScreen {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.greeting),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(R.string.subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            ProgressCard()

            Spacer(modifier = Modifier.height(16.dp))

            DailyMissionCard()

            Spacer(modifier = Modifier.height(16.dp))

            WivoButton(
                text = stringResource(R.string.start_training),
                onClick = onStartClick
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onProfileClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.view_profile))
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onShopClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.open_shop))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.learning_paths),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            LearningPathCard(
                title = stringResource(R.string.topic_java),
                subtitle = stringResource(R.string.topic_java_subtitle),
                color = PurplePrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            LearningPathCard(
                title = stringResource(R.string.topic_kotlin),
                subtitle = stringResource(R.string.topic_kotlin_subtitle),
                color = BluePrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            LearningPathCard(
                title = stringResource(R.string.topic_sql),
                subtitle = stringResource(R.string.topic_sql_subtitle),
                color = GreenAccent
            )
        }
    }
}

@Composable
private fun ProgressCard() {
    val firestoreRepository = remember { FirestoreRepository() }

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