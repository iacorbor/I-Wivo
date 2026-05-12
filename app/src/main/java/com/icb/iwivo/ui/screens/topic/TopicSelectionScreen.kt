package com.icb.iwivo.ui.screens.topic

import androidx.compose.foundation.clickable
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
fun TopicSelectionScreen(
    firestoreRepository: FirestoreRepository,
    onTopicClick: (String) -> Unit
) {
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
                // No bloqueo selección de tema si falla la tienda.
            }
        )
    }

    val topics = listOf(
        TopicUiItem(
            id = "java",
            title = stringResource(R.string.topic_java),
            subtitle = stringResource(R.string.topic_java_subtitle),
            icon = "☕",
            color = PurplePrimary
        ),
        TopicUiItem(
            id = "kotlin",
            title = stringResource(R.string.topic_kotlin),
            subtitle = stringResource(R.string.topic_kotlin_subtitle),
            icon = "🤖",
            color = BluePrimary
        ),
        TopicUiItem(
            id = "sql",
            title = stringResource(R.string.topic_sql),
            subtitle = stringResource(R.string.topic_sql_subtitle),
            icon = "🗄️",
            color = GreenAccent
        ),
        TopicUiItem(
            id = "javascript",
            title = stringResource(R.string.topic_javascript),
            subtitle = stringResource(R.string.topic_javascript_subtitle),
            icon = "⚡",
            color = PurplePrimary
        ),
        TopicUiItem(
            id = "html_css",
            title = stringResource(R.string.topic_html_css),
            subtitle = stringResource(R.string.topic_html_css_subtitle),
            icon = "🎨",
            color = BluePrimary
        ),
        TopicUiItem(
            id= "git",
            title = stringResource(R.string.topic_git),
            subtitle = stringResource(R.string.topic_git_subtitle),
            icon = "🐱",
            color = GreenAccent
        ),
        TopicUiItem(
            id = "android",
            title = stringResource(R.string.topic_android),
            subtitle = stringResource(R.string.topic_android_subtitle),
            icon = "📱",
            color = BluePrimary
        ),
        TopicUiItem(
            id = "firebase",
            title = stringResource(R.string.topic_firebase),
            subtitle = stringResource(R.string.topic_firebase_subtitle),
            icon = "🔥",
            color = PurplePrimary
        ),
        TopicUiItem(
            id = "spring",
            title = stringResource(R.string.topic_spring),
            subtitle = stringResource(R.string.topic_spring_subtitle),
            icon = "🌱",
            color = GreenAccent
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
                text = stringResource(R.string.choose_topic),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.choose_topic_subtitle),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            topics.forEach { topic ->
                TopicCard(
                    title = topic.title,
                    subtitle = topic.subtitle,
                    icon = topic.icon,
                    color = topic.color,
                    themeItemId = equippedThemeId,
                    effectItemId = equippedEffectId,
                    onClick = {
                        onTopicClick(topic.id)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

private data class TopicUiItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: String,
    val color: Color
)

@Composable
private fun TopicCard(
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