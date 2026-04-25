package com.icb.iwivo.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import com.icb.iwivo.data.repository.UserRepository

@Composable
fun HomeScreen(
    onStartClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(R.string.greeting),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        ProgressCard()

        Spacer(modifier = Modifier.height(16.dp))

        DailyMissionCard()

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.start_training))
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onProfileClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.view_profile))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.learning_paths),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        LearningPathCard(
            title = stringResource(R.string.topic_java),
            subtitle = stringResource(R.string.topic_java_subtitle),
            color = PurplePrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        LearningPathCard(
            title = stringResource(R.string.topic_kotlin),
            subtitle = stringResource(R.string.topic_kotlin_subtitle),
            color = BluePrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        LearningPathCard(
            title = stringResource(R.string.topic_sql),
            subtitle = stringResource(R.string.topic_sql_subtitle),
            color = GreenAccent
        )
    }
}

@Composable
private fun ProgressCard() {

    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }

    val xp = userRepository.getXp()
    val level = userRepository.getLevel()

    val progress = (xp % 500) / 500f

    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = stringResource(R.string.progress_title),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.level, level),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
                color = PurplePrimary,
                trackColor = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.xp, xp % 500, 500),
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun DailyMissionCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = stringResource(R.string.next_mission),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.daily_mission_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = stringResource(R.string.daily_mission_subtitle),
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun LearningPathCard(
    title: String,
    subtitle: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
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
                    style = MaterialTheme.typography.titleMedium,
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