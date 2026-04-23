package com.icb.iwivo.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.ui.theme.BluePrimary
import com.icb.iwivo.ui.theme.CardDark
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary

@Composable
fun GameTypeScreen(
    topic: String,
    onGameTypeClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.choose_game_type),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.selected_topic, topic.uppercase()),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        GameTypeCard(
            title = stringResource(R.string.game_type_test),
            subtitle = stringResource(R.string.game_type_test_subtitle),
            color = PurplePrimary,
            onClick = { onGameTypeClick("test") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        GameTypeCard(
            title = stringResource(R.string.game_type_true_false),
            subtitle = stringResource(R.string.game_type_true_false_subtitle),
            color = BluePrimary,
            onClick = { onGameTypeClick("true_false") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        GameTypeCard(
            title = stringResource(R.string.game_type_complete_code),
            subtitle = stringResource(R.string.game_type_complete_code_subtitle),
            color = GreenAccent,
            onClick = { onGameTypeClick("complete_code") }
        )
    }
}

@Composable
private fun GameTypeCard(
    title: String,
    subtitle: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(18.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = subtitle,
                    color = TextSecondary
                )
            }

            Text(
                text = stringResource(R.string.arrow),
                style = MaterialTheme.typography.headlineSmall,
                color = color
            )
        }
    }
}