package com.icb.iwivo.ui.screens.badges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.model.Badge
import com.icb.iwivo.data.repository.BadgeRepository
import com.icb.iwivo.ui.components.WivoScreen

@Composable
fun BadgesScreen(
    xp: Int,
    streak: Int
) {
    val badgeRepository = remember { BadgeRepository() }

    val badges = remember(xp, streak) {
        badgeRepository.getBadges(
            xp = xp,
            streak = streak
        )
    }

    val unlockedBadges = badges.count { it.unlocked }
    val lockedBadges = badges.size - unlockedBadges

    WivoScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.badges_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.badges_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            BadgesSummaryCard(
                xp = xp,
                streak = streak,
                unlockedBadges = unlockedBadges,
                lockedBadges = lockedBadges
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(badges) { badge ->
                    BadgeItem(
                        badge = badge,
                        xp = xp,
                        streak = streak
                    )
                }
            }
        }
    }
}

@Composable
private fun BadgesSummaryCard(
    xp: Int,
    streak: Int,
    unlockedBadges: Int,
    lockedBadges: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        )
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = stringResource(R.string.badges_summary_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BadgeStatItem(
                    value = xp.toString(),
                    label = stringResource(R.string.badges_stat_xp)
                )

                BadgeStatItem(
                    value = streak.toString(),
                    label = stringResource(R.string.badges_stat_streak)
                )

                BadgeStatItem(
                    value = unlockedBadges.toString(),
                    label = stringResource(R.string.badges_stat_unlocked)
                )

                BadgeStatItem(
                    value = lockedBadges.toString(),
                    label = stringResource(R.string.badges_stat_locked)
                )
            }
        }
    }
}

@Composable
private fun BadgeStatItem(
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f)
        )
    }
}

@Composable
private fun BadgeItem(
    badge: Badge,
    xp: Int,
    streak: Int
) {
    val icon = getBadgeIcon(badge)
    val progress = getBadgeProgress(
        badge = badge,
        xp = xp,
        streak = streak
    )
    val progressText = getBadgeProgressText(
        badge = badge,
        xp = xp,
        streak = streak
    )

    val cardColor = if (badge.unlocked) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f)
    }

    val contentAlpha = if (badge.unlocked) 1f else 0.58f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(
                        if (badge.unlocked) {
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (badge.unlocked) icon else "🔒",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = badge.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = contentAlpha),
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = if (badge.unlocked) {
                            stringResource(R.string.badges_status_unlocked)
                        } else {
                            stringResource(R.string.badges_status_locked)
                        },
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (badge.unlocked) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f * contentAlpha)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = progressText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f * contentAlpha)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = {
                        progress
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun getBadgeIcon(badge: Badge): String {
    return when {
        badge.id.startsWith("xp_") -> "⚡"
        badge.id.startsWith("level_") -> "🎓"
        badge.id.startsWith("streak_") -> "🔥"
        badge.id.startsWith("coins_") -> "🪙"
        badge.id == "first_steps" -> "🚀"
        badge.id == "warm_up" -> "🔥"
        badge.id == "rookie_dev" -> "💻"
        badge.id == "junior_dev" -> "🧠"
        badge.id == "daily_player" -> "📅"
        badge.id == "week_warrior" -> "🛡️"
        badge.id == "month_grinder" -> "🏋️"
        badge.id == "xp_machine" -> "⚙️"
        badge.id == "serious_student" -> "📚"
        badge.id == "discipline_mode" -> "🎯"
        badge.id == "legend" -> "👑"
        else -> "🏅"
    }
}

private fun getBadgeProgress(
    badge: Badge,
    xp: Int,
    streak: Int
): Float {
    val level = (xp / 500) + 1
    val estimatedCoins = xp / 5

    val target = getBadgeTarget(badge) ?: return if (badge.unlocked) 1f else 0.15f

    val current = when {
        badge.id.startsWith("xp_") -> xp
        badge.id.startsWith("level_") -> level
        badge.id.startsWith("streak_") -> streak
        badge.id.startsWith("coins_") -> estimatedCoins
        badge.id == "first_steps" -> xp
        badge.id == "warm_up" -> xp
        badge.id == "rookie_dev" -> level
        badge.id == "junior_dev" -> level
        badge.id == "daily_player" -> streak
        badge.id == "week_warrior" -> streak
        badge.id == "month_grinder" -> streak
        badge.id == "xp_machine" -> xp
        badge.id == "serious_student" -> minOf(level, streak)
        badge.id == "discipline_mode" -> minOf(level, streak)
        badge.id == "legend" -> maxOf(xp, streak)
        else -> 0
    }

    return (current.toFloat() / target.toFloat()).coerceIn(0f, 1f)
}

private fun getBadgeProgressText(
    badge: Badge,
    xp: Int,
    streak: Int
): String {
    val level = (xp / 500) + 1
    val estimatedCoins = xp / 5
    val target = getBadgeTarget(badge)

    return when {
        badge.id.startsWith("xp_") && target != null -> "$xp / $target XP"
        badge.id.startsWith("level_") && target != null -> "Nivel $level / $target"
        badge.id.startsWith("streak_") && target != null -> "$streak / $target días"
        badge.id.startsWith("coins_") && target != null -> "$estimatedCoins / $target monedas aprox."

        badge.id == "first_steps" -> "$xp / 50 XP"
        badge.id == "warm_up" -> "$xp / 100 XP"
        badge.id == "rookie_dev" -> "Nivel $level / 2"
        badge.id == "junior_dev" -> "Nivel $level / 5"
        badge.id == "daily_player" -> "$streak / 1 días"
        badge.id == "week_warrior" -> "$streak / 7 días"
        badge.id == "month_grinder" -> "$streak / 30 días"
        badge.id == "xp_machine" -> "$xp / 5000 XP"
        badge.id == "serious_student" -> "Nivel $level / 10 · Racha $streak / 7"
        badge.id == "discipline_mode" -> "Nivel $level / 20 · Racha $streak / 30"
        badge.id == "legend" -> "$xp / 100000 XP o $streak / 365 días"

        else -> if (badge.unlocked) "Completado" else "Pendiente"
    }
}

private fun getBadgeTarget(badge: Badge): Int? {
    return when {
        badge.id.startsWith("xp_") -> badge.id.removePrefix("xp_").toIntOrNull()
        badge.id.startsWith("level_") -> badge.id.removePrefix("level_").toIntOrNull()
        badge.id.startsWith("streak_") -> badge.id.removePrefix("streak_").toIntOrNull()
        badge.id.startsWith("coins_") -> badge.id.removePrefix("coins_").toIntOrNull()

        badge.id == "first_steps" -> 50
        badge.id == "warm_up" -> 100
        badge.id == "rookie_dev" -> 2
        badge.id == "junior_dev" -> 5
        badge.id == "daily_player" -> 1
        badge.id == "week_warrior" -> 7
        badge.id == "month_grinder" -> 30
        badge.id == "xp_machine" -> 5000
        badge.id == "serious_student" -> 10
        badge.id == "discipline_mode" -> 20
        badge.id == "legend" -> 100000

        else -> null
    }
}