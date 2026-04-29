package com.icb.iwivo.ui.screens.result

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
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
            modifier = Modifier.fillMaxSize(),
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

            Spacer(modifier = Modifier.weight(1f))

            WivoButton(
                text = stringResource(R.string.back_home),
                onClick = onBackHome
            )
        }
    }
}