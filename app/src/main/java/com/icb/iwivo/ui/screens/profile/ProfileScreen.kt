package com.icb.iwivo.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.icb.iwivo.R
import com.icb.iwivo.data.repository.BadgeRepository
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.BadgeCard
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit
) {
    val firestoreRepository = remember { FirestoreRepository() }
    val auth = remember { FirebaseAuth.getInstance() }

    var xp by remember { mutableIntStateOf(0) }
    var coins by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }

    val email = auth.currentUser?.email ?: stringResource(R.string.no_email)
    val badgeRepository = remember { BadgeRepository() }
    val badges = badgeRepository.getBadges(xp, streak)

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

    WivoScreen {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.profile_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(R.string.profile_subtitle),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            WivoCard {
                Text(
                    text = email,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.level, level),
                    style = MaterialTheme.typography.headlineSmall,
                    color = PurplePrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = GreenAccent,
                    trackColor = MaterialTheme.colorScheme.surface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.xp, xpCurrentLevel, 500),
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            StatCard(
                title = stringResource(R.string.total_xp),
                value = xp.toString()
            )

            Spacer(modifier = Modifier.height(12.dp))

            StatCard(
                title = stringResource(R.string.wivo_coins),
                value = coins.toString()
            )

            Spacer(modifier = Modifier.height(12.dp))

            StatCard(
                title = stringResource(R.string.streak),
                value = stringResource(R.string.streak_days, streak)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.badges_section),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            badges.forEach { badge ->
                BadgeCard(
                    name = badge.name,
                    description = badge.description,
                    unlocked = badge.unlocked
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
            WivoButton(
                text = stringResource(R.string.logout),
                onClick = onLogoutClick
            )
        }
    }
}



@Composable
private fun StatCard(
    title: String,
    value: String
) {
    WivoCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = TextSecondary
            )

            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}