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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.model.Badge
import com.icb.iwivo.data.model.ShopItemCategory
import com.icb.iwivo.data.repository.BadgeRepository
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen

@Composable
fun BadgesScreen(
    xp: Int,
    coins: Int,
    streak: Int,
    firestoreRepository: FirestoreRepository
) {
    val badgeRepository = remember { BadgeRepository() }

    var equippedBackgroundId by remember { mutableStateOf<String?>(null) }
    var equippedThemeId by remember { mutableStateOf<String?>(null) }
    var equippedEffectId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        firestoreRepository.getShopStateByCategory(
            onResult = { _, _, equippedItemsByCategory ->
                equippedBackgroundId = equippedItemsByCategory[ShopItemCategory.BACKGROUND]
                equippedThemeId = equippedItemsByCategory[ShopItemCategory.THEME]
                equippedEffectId = equippedItemsByCategory[ShopItemCategory.EFFECT]
            },
            onError = {
                // No bloqueo Logros si falla la tienda.
            }
        )
    }

    val badges = remember(xp, coins, streak) {
        badgeRepository.getBadges(
            xp = xp,
            coins = coins,
            streak = streak
        )
    }

    val unlockedBadgeList = badges.filter { it.unlocked }
    val lockedBadgeList = badges.filter { !it.unlocked }

    WivoScreen(
        backgroundItemId = equippedBackgroundId,
        themeItemId = equippedThemeId
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
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
                coins = coins,
                streak = streak,
                unlockedBadges = unlockedBadgeList.size,
                lockedBadges = lockedBadgeList.size,
                themeItemId = equippedThemeId,
                effectItemId = equippedEffectId
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                if (unlockedBadgeList.isNotEmpty()) {
                    item {
                        BadgeSectionTitle(
                            title = stringResource(R.string.badges_section_unlocked),
                            count = unlockedBadgeList.size
                        )
                    }

                    items(
                        items = unlockedBadgeList,
                        key = { badge -> badge.id }
                    ) { badge ->
                        BadgeItem(
                            badge = badge,
                            xp = xp,
                            coins = coins,
                            streak = streak,
                            themeItemId = equippedThemeId,
                            effectItemId = equippedEffectId
                        )
                    }
                }

                if (lockedBadgeList.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))

                        BadgeSectionTitle(
                            title = stringResource(R.string.badges_section_locked),
                            count = lockedBadgeList.size
                        )
                    }

                    items(
                        items = lockedBadgeList,
                        key = { badge -> badge.id }
                    ) { badge ->
                        BadgeItem(
                            badge = badge,
                            xp = xp,
                            coins = coins,
                            streak = streak,
                            themeItemId = equippedThemeId,
                            effectItemId = equippedEffectId
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
private fun BadgeSectionTitle(
    title: String,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = count.toString(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun BadgesSummaryCard(
    xp: Int,
    coins: Int,
    streak: Int,
    unlockedBadges: Int,
    lockedBadges: Int,
    themeItemId: String?,
    effectItemId: String?
) {
    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
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
                value = coins.toString(),
                label = stringResource(R.string.badges_stat_coins)
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
    coins: Int,
    streak: Int,
    themeItemId: String?,
    effectItemId: String?
) {
    val progress = getBadgeProgress(
        badge = badge,
        xp = xp,
        coins = coins,
        streak = streak
    )

    val progressText = getBadgeProgressText(
        badge = badge,
        xp = xp,
        coins = coins,
        streak = streak
    )

    val cardColor = if (badge.unlocked) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f)
    }

    val contentAlpha = if (badge.unlocked) 1f else 0.58f

    WivoCard(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = cardColor
                ),
                modifier = Modifier.size(58.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
                        text = if (badge.unlocked) badge.icon else "🔒",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
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
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun getBadgeProgress(
    badge: Badge,
    xp: Int,
    coins: Int,
    streak: Int
): Float {
    val level = (xp / 500) + 1

    return when {
        badge.unlocked -> 1f

        badge.id.startsWith("xp_") -> {
            val target = badge.id.removePrefix("xp_").toIntOrNull() ?: return 0.15f
            (xp.toFloat() / target.toFloat()).coerceIn(0f, 1f)
        }

        badge.id.startsWith("level_") -> {
            val target = badge.id.removePrefix("level_").toIntOrNull() ?: return 0.15f
            (level.toFloat() / target.toFloat()).coerceIn(0f, 1f)
        }

        badge.id.startsWith("streak_") -> {
            val target = badge.id.removePrefix("streak_").toIntOrNull() ?: return 0.15f
            (streak.toFloat() / target.toFloat()).coerceIn(0f, 1f)
        }

        badge.id.startsWith("coins_") -> {
            val target = badge.id.removePrefix("coins_").toIntOrNull() ?: return 0.15f
            (coins.toFloat() / target.toFloat()).coerceIn(0f, 1f)
        }

        badge.id == "first_steps" -> (xp / 50f).coerceIn(0f, 1f)
        badge.id == "warm_up" -> (xp / 100f).coerceIn(0f, 1f)
        badge.id == "rookie_dev" -> (level / 2f).coerceIn(0f, 1f)
        badge.id == "junior_dev" -> (level / 5f).coerceIn(0f, 1f)
        badge.id == "daily_player" -> (streak / 1f).coerceIn(0f, 1f)
        badge.id == "week_warrior" -> (streak / 7f).coerceIn(0f, 1f)
        badge.id == "month_grinder" -> (streak / 30f).coerceIn(0f, 1f)
        badge.id == "xp_machine" -> (xp / 5000f).coerceIn(0f, 1f)

        badge.id == "serious_student" -> {
            val levelProgress = (level / 10f).coerceIn(0f, 1f)
            val streakProgress = (streak / 7f).coerceIn(0f, 1f)
            minOf(levelProgress, streakProgress)
        }

        badge.id == "discipline_mode" -> {
            val levelProgress = (level / 20f).coerceIn(0f, 1f)
            val streakProgress = (streak / 30f).coerceIn(0f, 1f)
            minOf(levelProgress, streakProgress)
        }

        badge.id == "legend" -> {
            val xpProgress = (xp / 100000f).coerceIn(0f, 1f)
            val streakProgress = (streak / 365f).coerceIn(0f, 1f)
            maxOf(xpProgress, streakProgress)
        }

        else -> 0.15f
    }
}

@Composable
private fun getBadgeProgressText(
    badge: Badge,
    xp: Int,
    coins: Int,
    streak: Int
): String {
    val level = (xp / 500) + 1
    val target = getBadgeTarget(badge)

    return when {
        badge.unlocked -> stringResource(R.string.badges_progress_completed)

        badge.id.startsWith("xp_") && target != null -> {
            stringResource(R.string.badges_progress_xp, xp, target)
        }

        badge.id.startsWith("level_") && target != null -> {
            stringResource(R.string.badges_progress_level, level, target)
        }

        badge.id.startsWith("streak_") && target != null -> {
            stringResource(R.string.badges_progress_days, streak, target)
        }

        badge.id.startsWith("coins_") && target != null -> {
            stringResource(R.string.badges_progress_coins, coins, target)
        }

        badge.id == "first_steps" -> stringResource(R.string.badges_progress_xp, xp, 50)
        badge.id == "warm_up" -> stringResource(R.string.badges_progress_xp, xp, 100)
        badge.id == "rookie_dev" -> stringResource(R.string.badges_progress_level, level, 2)
        badge.id == "junior_dev" -> stringResource(R.string.badges_progress_level, level, 5)
        badge.id == "daily_player" -> stringResource(R.string.badges_progress_days, streak, 1)
        badge.id == "week_warrior" -> stringResource(R.string.badges_progress_days, streak, 7)
        badge.id == "month_grinder" -> stringResource(R.string.badges_progress_days, streak, 30)
        badge.id == "xp_machine" -> stringResource(R.string.badges_progress_xp, xp, 5000)

        badge.id == "serious_student" -> {
            stringResource(R.string.badges_progress_level_streak, level, 10, streak, 7)
        }

        badge.id == "discipline_mode" -> {
            stringResource(R.string.badges_progress_level_streak, level, 20, streak, 30)
        }

        badge.id == "legend" -> {
            stringResource(R.string.badges_progress_legend, xp, 100000, streak, 365)
        }

        else -> stringResource(R.string.badges_progress_pending)
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