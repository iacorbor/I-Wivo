package com.icb.iwivo.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.repository.UserRepository
import com.icb.iwivo.ui.theme.CardDark
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary
import com.icb.iwivo.ui.components.WivoScreen

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }

    val xp = userRepository.getXp()
    val level = userRepository.getLevel()
    val streak = userRepository.getStreak()
    val unlockedBadges = userRepository.getUnlockedBadgesCount()
    val badges = userRepository.getBadges()
    val xpCurrentLevel = xp % 500
    val progress = xpCurrentLevel / 500f

    WivoScreen{
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

            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            StatCard(
                title = stringResource(R.string.total_xp),
                value = xp.toString()
            )

            Spacer(modifier = Modifier.height(12.dp))

            StatCard(
                title = stringResource(R.string.badges),
                value = unlockedBadges.toString()
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
        }
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
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
@Composable
private fun BadgeCard(
    name: String,
    description: String,
    unlocked: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = CardDark
        ),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = if (unlocked) "🏆 $name" else "🔒 $name",
                color = if (unlocked) GreenAccent else TextSecondary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                color = TextSecondary
            )
        }
    }
}