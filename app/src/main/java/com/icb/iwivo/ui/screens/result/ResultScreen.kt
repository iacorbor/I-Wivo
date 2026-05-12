package com.icb.iwivo.ui.screens.result

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.model.Badge
import com.icb.iwivo.data.model.ShopItemCategory
import com.icb.iwivo.data.repository.BadgeRepository
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary

@Composable
fun ResultScreen(
    correct: Int,
    total: Int,
    xpEarned: Int,
    coinsEarned: Int,
    bestStreak: Int,
    unlockedBadgeIds: List<String>,
    firestoreRepository: FirestoreRepository,
    onBackHome: () -> Unit
) {
    var equippedBackgroundId by remember { mutableStateOf<String?>(null) }
    var equippedButtonStyleId by remember { mutableStateOf<String?>(null) }
    var equippedThemeId by remember { mutableStateOf<String?>(null) }
    var equippedEffectId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        firestoreRepository.getShopStateByCategory(
            onResult = { _, _, equippedItemsByCategory ->
                equippedBackgroundId = equippedItemsByCategory[ShopItemCategory.BACKGROUND]
                equippedButtonStyleId = equippedItemsByCategory[ShopItemCategory.BUTTON_STYLE]
                equippedThemeId = equippedItemsByCategory[ShopItemCategory.THEME]
                equippedEffectId = equippedItemsByCategory[ShopItemCategory.EFFECT]
            },
            onError = {
                // No bloqueo Resultado si falla la tienda.
            }
        )
    }

    val percentage = if (total > 0) (correct * 100) / total else 0

    val badgeRepository = remember { BadgeRepository() }

    val defaultBadgeName = stringResource(R.string.result_default_badge_name)
    val defaultBadgeDescription = stringResource(R.string.result_default_badge_description)

    val unlockedBadges = remember(
        unlockedBadgeIds,
        defaultBadgeName,
        defaultBadgeDescription
    ) {
        unlockedBadgeIds.map { badgeId ->
            badgeRepository.getBadgeById(badgeId) ?: Badge(
                id = badgeId,
                name = defaultBadgeName,
                description = defaultBadgeDescription
            )
        }
    }

    val resultMessage = when {
        percentage == 100 -> stringResource(R.string.result_perfect)
        percentage >= 90 -> stringResource(R.string.result_excellent)
        percentage >= 70 -> stringResource(R.string.result_good)
        percentage >= 50 -> stringResource(R.string.result_medium)
        else -> stringResource(R.string.result_keep_practicing)
    }

    WivoScreen(
        backgroundItemId = equippedBackgroundId,
        themeItemId = equippedThemeId
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(56.dp))

                Text(
                    text = stringResource(R.string.result_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(24.dp))

                WivoCard(
                    themeItemId = equippedThemeId,
                    effectItemId = equippedEffectId
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$percentage%",
                            style = MaterialTheme.typography.displayMedium,
                            color = if (percentage >= 60) GreenAccent else PurplePrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = resultMessage,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = stringResource(R.string.result_score, correct, total),
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = stringResource(R.string.result_best_streak, bestStreak),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = GreenAccent
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                WivoCard(
                    themeItemId = equippedThemeId,
                    effectItemId = equippedEffectId
                ) {
                    Text(
                        text = stringResource(R.string.rewards_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.result_xp, xpEarned),
                        color = GreenAccent
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.result_coins, coinsEarned),
                        color = PurplePrimary
                    )
                }

                if (unlockedBadges.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    WivoCard(
                        themeItemId = equippedThemeId,
                        effectItemId = equippedEffectId
                    ) {
                        Text(
                            text = stringResource(R.string.result_badge_unlocked_title),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        unlockedBadges.forEachIndexed { index, badge ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = badge.icon,
                                    style = MaterialTheme.typography.titleLarge
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = badge.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Text(
                                        text = badge.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }
                            }

                            if (index != unlockedBadges.lastIndex) {
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            WivoButton(
                text = stringResource(R.string.back_home),
                onClick = onBackHome,
                buttonStyleItemId = equippedButtonStyleId,
                themeItemId = equippedThemeId
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}