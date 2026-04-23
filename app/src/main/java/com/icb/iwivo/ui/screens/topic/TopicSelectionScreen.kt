package com.icb.iwivo.ui.screens.topic

import androidx.compose.foundation.background
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
import androidx.compose.foundation.clickable
import com.icb.iwivo.R
import com.icb.iwivo.ui.theme.BluePrimary
import com.icb.iwivo.ui.theme.CardDark
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.ui.theme.TextSecondary

@Composable
fun TopicSelectionScreen(
    onTopicClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.choose_topic),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.choose_topic_subtitle),
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        TopicCard(
            title = stringResource(R.string.topic_java),
            subtitle = stringResource(R.string.topic_java_subtitle),
            color = PurplePrimary,
            onClick = { onTopicClick("java") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TopicCard(
            title = stringResource(R.string.topic_kotlin),
            subtitle = stringResource(R.string.topic_kotlin_subtitle),
            color = BluePrimary,
            onClick = { onTopicClick("kotlin")}
        )

        Spacer(modifier = Modifier.height(12.dp))

        TopicCard(
            title = stringResource(R.string.topic_sql),
            subtitle = stringResource(R.string.topic_sql_subtitle),
            color = GreenAccent,
            onClick = { onTopicClick("sql") }
        )
    }
}

@Composable
private fun TopicCard(
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
                .fillMaxWidth()
                .padding(18.dp),
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