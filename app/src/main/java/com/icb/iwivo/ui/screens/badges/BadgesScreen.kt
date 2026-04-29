package com.icb.iwivo.ui.screens.badges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.model.Badge
import com.icb.iwivo.ui.components.WivoScreen

@Composable
fun BadgesScreen() {
    val badges = listOf(
        Badge(
            id = "first_quiz",
            name = "Primer paso",
            description = "Completa tu primer quiz.",
            icon = "🔥",
            requirement = "Completa 1 quiz",
            unlocked = true
        ),
        Badge(
            id = "xp_500",
            name = "Aprendiz constante",
            description = "Has empezado a acumular experiencia.",
            icon = "⚡",
            requirement = "Consigue 500 XP",
            unlocked = false
        ),
        Badge(
            id = "streak_3",
            name = "Racha inicial",
            description = "Vuelve varios días seguidos.",
            icon = "📆",
            requirement = "Consigue una racha de 3 días",
            unlocked = false
        ),
        Badge(
            id = "quiz_master",
            name = "Mente rápida",
            description = "Demuestra dominio completando varios quizzes.",
            icon = "🏆",
            requirement = "Completa 10 quizzes",
            unlocked = false
        )
    )

    WivoScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

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
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            badges.forEach { badge ->
                BadgeItem(badge = badge)

                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    }
}

@Composable
private fun BadgeItem(
    badge: Badge
) {
    val alpha = if (badge.unlocked) 1f else 0.55f

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (badge.unlocked) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
            }
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
                    text = if (badge.unlocked) badge.icon else "🔒",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = badge.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = badge.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f * alpha)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = badge.requirement,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = {
                        if (badge.unlocked) 1f else 0.35f
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}