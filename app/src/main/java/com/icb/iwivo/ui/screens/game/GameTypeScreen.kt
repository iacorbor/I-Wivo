package com.icb.iwivo.ui.screens.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.model.ShopItemCategory
import com.icb.iwivo.data.repository.FirestoreRepository
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.theme.BluePrimary
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary

@Composable
fun GameTypeScreen(
    topic: String,
    firestoreRepository: FirestoreRepository,
    onGameTypeClick: (gameType: String, difficulty: String) -> Unit
) {
    var equippedBackgroundId by remember { mutableStateOf<String?>(null) }
    var equippedThemeId by remember { mutableStateOf<String?>(null) }
    var equippedEffectId by remember { mutableStateOf<String?>(null) }

    var selectedDifficulty by remember { mutableStateOf("easy") }

    val topicPersonality = getTopicPersonality(topic)

    LaunchedEffect(Unit) {
        firestoreRepository.getShopStateByCategory(
            onResult = { _, _, equippedItemsByCategory ->
                equippedBackgroundId = equippedItemsByCategory[ShopItemCategory.BACKGROUND]
                equippedThemeId = equippedItemsByCategory[ShopItemCategory.THEME]
                equippedEffectId = equippedItemsByCategory[ShopItemCategory.EFFECT]
            },
            onError = {
                // No bloqueo esta pantalla si falla la tienda.
            }
        )
    }

    val gameModes = listOf(
        GameModeUiItem(
            gameType = "test",
            title = stringResource(R.string.game_type_test),
            subtitle = stringResource(R.string.game_type_test_subtitle),
            icon = "✅"
        ),
        GameModeUiItem(
            gameType = "true_false",
            title = stringResource(R.string.game_type_true_false),
            subtitle = stringResource(R.string.game_type_true_false_subtitle),
            icon = "⚖️"
        ),
        GameModeUiItem(
            gameType = "complete_code",
            title = stringResource(R.string.game_type_complete_code),
            subtitle = stringResource(R.string.game_type_complete_code_subtitle),
            icon = "🧩"
        ),
        GameModeUiItem(
            gameType = "match_concept",
            title = stringResource(R.string.game_type_match_concept),
            subtitle = stringResource(R.string.game_type_match_concept_subtitle),
            icon = "🔗"
        ),
        GameModeUiItem(
            gameType = "order_code",
            title = stringResource(R.string.game_type_order_code),
            subtitle = stringResource(R.string.game_type_order_code_subtitle),
            icon = "📚"
        ),
        GameModeUiItem(
            gameType = "detect_error",
            title = stringResource(R.string.game_type_detect_error),
            subtitle = stringResource(R.string.game_type_detect_error_subtitle),
            icon = "🐞"
        ),
        GameModeUiItem(
            gameType = "console_output",
            title = stringResource(R.string.game_type_console_output),
            subtitle = stringResource(R.string.game_type_console_output_subtitle),
            icon = "🖥️"
        ),
        GameModeUiItem(
            gameType = "technical_wordle",
            title = stringResource(R.string.game_type_technical_wordle),
            subtitle = stringResource(R.string.game_type_technical_wordle_subtitle),
            icon = "🟩"
        ),
        GameModeUiItem(
            gameType = "crossword",
            title = stringResource(R.string.game_type_crossword),
            subtitle = stringResource(R.string.game_type_crossword_subtitle),
            icon = "🧠"
        )
    )

    WivoScreen(
        backgroundItemId = equippedBackgroundId,
        themeItemId = equippedThemeId
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "${topicPersonality.icon} ${stringResource(R.string.choose_game_type)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = topicPersonality.color
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.selected_topic, topicPersonality.title),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = topicPersonality.subtitle,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.74f),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(18.dp))

            DifficultySelector(
                selectedDifficulty = selectedDifficulty,
                accentColor = topicPersonality.color,
                onDifficultySelected = { difficulty ->
                    selectedDifficulty = difficulty
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            gameModes.forEach { gameMode ->
                GameTypeCard(
                    title = gameMode.title,
                    subtitle = gameMode.subtitle,
                    icon = gameMode.icon,
                    color = topicPersonality.color,
                    themeItemId = equippedThemeId,
                    effectItemId = equippedEffectId,
                    onClick = {
                        onGameTypeClick(gameMode.gameType, selectedDifficulty)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DifficultySelector(
    selectedDifficulty: String,
    accentColor: Color,
    onDifficultySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DifficultyChip(
            text = stringResource(R.string.difficulty_easy),
            selected = selectedDifficulty == "easy",
            accentColor = accentColor,
            onClick = { onDifficultySelected("easy") }
        )

        DifficultyChip(
            text = stringResource(R.string.difficulty_medium),
            selected = selectedDifficulty == "medium",
            accentColor = accentColor,
            onClick = { onDifficultySelected("medium") }
        )

        DifficultyChip(
            text = stringResource(R.string.difficulty_hard),
            selected = selectedDifficulty == "hard",
            accentColor = accentColor,
            onClick = { onDifficultySelected("hard") }
        )
    }
}

@Composable
private fun DifficultyChip(
    text: String,
    selected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = accentColor.copy(alpha = 0.28f),
            selectedLabelColor = MaterialTheme.colorScheme.onSurface,
            labelColor = TextSecondary
        )
    )
}

@Composable
private fun GameTypeCard(
    title: String,
    subtitle: String,
    icon: String,
    color: Color,
    themeItemId: String?,
    effectItemId: String?,
    onClick: () -> Unit
) {
    WivoCard(
        modifier = Modifier.clickable {
            onClick()
        },
        themeItemId = themeItemId,
        effectItemId = effectItemId
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = subtitle,
                        color = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = stringResource(R.string.arrow),
                style = MaterialTheme.typography.headlineSmall,
                color = color
            )
        }
    }
}


private data class GameModeUiItem(
    val gameType: String,
    val title: String,
    val subtitle: String,
    val icon: String
)