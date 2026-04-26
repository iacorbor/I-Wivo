package com.icb.iwivo.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.TextSecondary

@Composable
fun BadgeCard(
    name: String,
    description: String,
    unlocked: Boolean
) {
    WivoCard {
        Text(
            text = if (unlocked) "🏆 $name" else "🔒 $name",
            color = if (unlocked) GreenAccent else TextSecondary,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            color = TextSecondary
        )
    }
}