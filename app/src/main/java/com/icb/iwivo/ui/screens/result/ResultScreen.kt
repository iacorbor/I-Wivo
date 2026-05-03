package com.icb.iwivo.ui.screens.result

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.ui.components.WivoButton
import com.icb.iwivo.ui.components.WivoCard
import com.icb.iwivo.ui.components.WivoScreen
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun ResultScreen(
    correct: Int,
    total: Int,
    xpEarned: Int,
    coinsEarned: Int,
    unlockedBadgeIds: List<String>,
    onBackHome: () -> Unit
) {
    val percentage = if (total > 0) (correct * 100) / total else 0

    val resultMessage = when {
        percentage >= 90 -> stringResource(R.string.result_excellent)
        percentage >= 60 -> stringResource(R.string.result_good)
        else -> stringResource(R.string.result_keep_practicing)
    }

    WivoScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
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

            WivoCard {
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
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            WivoCard {
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

            if (unlockedBadgeIds.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                WivoCard {
                    Text(
                        text = stringResource(R.string.result_badge_unlocked_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    unlockedBadgeIds.forEachIndexed { index, badgeId ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = getUnlockedBadgeIcon(badgeId),
                                style = MaterialTheme.typography.titleLarge
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = getUnlockedBadgeTitle(badgeId),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Text(
                                    text = getUnlockedBadgeSubtitle(badgeId),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        if (index != unlockedBadgeIds.lastIndex) {
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            WivoButton(
                text = stringResource(R.string.back_home),
                onClick = onBackHome
            )
        }
    }
}

private fun getUnlockedBadgeTitle(id: String): String {
    return when {
        id.startsWith("xp_") -> "XP ${id.removePrefix("xp_")}"
        id.startsWith("level_") -> "Nivel ${id.removePrefix("level_")}"
        id.startsWith("streak_") -> "Racha ${id.removePrefix("streak_")}"
        id.startsWith("coins_") -> "Wivo Coins ${id.removePrefix("coins_")}"

        id == "first_steps" -> "Primeros pasos"
        id == "warm_up" -> "Calentando motores"
        id == "rookie_dev" -> "Dev Novato"
        id == "junior_dev" -> "Junior en marcha"
        id == "daily_player" -> "Primer día activo"
        id == "week_warrior" -> "Guerrero semanal"
        id == "month_grinder" -> "Modo constancia"
        id == "xp_machine" -> "Máquina de XP"
        id == "serious_student" -> "Estudiante serio"
        id == "discipline_mode" -> "Disciplina total"
        id == "legend" -> "Leyenda I-Wivo"

        else -> "Nuevo logro"
    }
}

private fun getUnlockedBadgeSubtitle(id: String): String {
    return when {
        id.startsWith("xp_") -> "Has alcanzado ${id.removePrefix("xp_")} XP."
        id.startsWith("level_") -> "Has alcanzado el nivel ${id.removePrefix("level_")}."
        id.startsWith("streak_") -> "Has conseguido una racha de ${id.removePrefix("streak_")} días."
        id.startsWith("coins_") -> "Has acumulado monedas suficientes."

        id == "first_steps" -> "Has conseguido tus primeros 50 XP."
        id == "warm_up" -> "Has superado los 100 XP."
        id == "rookie_dev" -> "Has llegado al nivel 2."
        id == "junior_dev" -> "Has llegado al nivel 5."
        id == "daily_player" -> "Has completado actividad hoy."
        id == "week_warrior" -> "Has mantenido una racha de 7 días."
        id == "month_grinder" -> "Has mantenido una racha de 30 días."
        id == "xp_machine" -> "Has superado los 5000 XP."
        id == "serious_student" -> "Nivel 10 y racha de 7 días."
        id == "discipline_mode" -> "Nivel 20 y racha de 30 días."
        id == "legend" -> "Has alcanzado el estatus de leyenda."

        else -> "Nuevo logro desbloqueado."
    }
}

private fun getUnlockedBadgeIcon(id: String): String {
    return when {
        id.startsWith("xp_") -> "⚡"
        id.startsWith("level_") -> "🎓"
        id.startsWith("streak_") -> "🔥"
        id.startsWith("coins_") -> "🪙"

        id == "first_steps" -> "🚀"
        id == "warm_up" -> "🔥"
        id == "rookie_dev" -> "💻"
        id == "junior_dev" -> "🧠"
        id == "daily_player" -> "📅"
        id == "week_warrior" -> "🛡️"
        id == "month_grinder" -> "🏋️"
        id == "xp_machine" -> "⚙️"
        id == "serious_student" -> "📚"
        id == "discipline_mode" -> "🎯"
        id == "legend" -> "👑"

        else -> "🏆"
    }
}