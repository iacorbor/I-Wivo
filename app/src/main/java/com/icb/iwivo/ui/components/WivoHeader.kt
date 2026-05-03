package com.icb.iwivo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icb.iwivo.utils.base64ToBitmap

@Composable
fun WivoHeader(
    level: Int,
    xp: Int,
    username: String = "",
    avatarBase64: String = "",
    onAvatarClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        WivoHeaderAvatar(
            username = username,
            avatarBase64 = avatarBase64,
            onClick = onAvatarClick
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = "Nivel $level",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "$xp XP",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
            )
        }
    }
}

@Composable
private fun WivoHeaderAvatar(
    username: String,
    avatarBase64: String,
    onClick: () -> Unit
) {
    val avatarBitmap = base64ToBitmap(avatarBase64)

    val fallbackText = when {
        username.isNotBlank() -> username.first().uppercase()
        else -> "IW"
    }

    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .size(54.dp)
            .clip(CircleShape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (avatarBitmap != null) {
            Image(
                bitmap = avatarBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(54.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = fallbackText,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}