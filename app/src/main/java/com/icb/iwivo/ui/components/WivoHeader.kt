package com.icb.iwivo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary
import com.icb.iwivo.utils.base64ToBitmap

@Composable
fun WivoHeader(
    level: Int,
    xp: Int,
    username: String = "",
    avatarBase64: String = "",
    equippedShopItemId: String? = null,
    onAvatarClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        WivoHeaderAvatar(
            username = username,
            avatarBase64 = avatarBase64,
            equippedShopItemId = equippedShopItemId,
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
    equippedShopItemId: String?,
    onClick: () -> Unit
) {
    val avatarBitmap = base64ToBitmap(avatarBase64)

    val fallbackText = when {
        username.isNotBlank() -> username.first().uppercase()
        else -> "IW"
    }

    val hasDecoration = equippedShopItemId != null

    val outerSize = if (hasDecoration) 62.dp else 54.dp
    val innerSize = 54.dp

    Box(
        modifier = Modifier
            .size(outerSize)
            .clip(CircleShape)
            .background(
                if (hasDecoration) {
                    getHeaderAvatarDecorationBrush(equippedShopItemId)
                } else {
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                }
            )
            .padding(
                if (hasDecoration) {
                    getHeaderAvatarBorderPadding(equippedShopItemId)
                } else {
                    0.dp
                }
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (hasDecoration) {
            Box(
                modifier = Modifier
                    .size(innerSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                HeaderAvatarContent(
                    username = fallbackText,
                    avatarBase64 = avatarBase64,
                    textColor = MaterialTheme.colorScheme.primary,
                    size = innerSize
                )
            }
        } else {
            HeaderAvatarContent(
                username = fallbackText,
                avatarBase64 = avatarBase64,
                textColor = MaterialTheme.colorScheme.onPrimary,
                size = innerSize
            )
        }
    }
}

@Composable
private fun HeaderAvatarContent(
    username: String,
    avatarBase64: String,
    textColor: androidx.compose.ui.graphics.Color,
    size: androidx.compose.ui.unit.Dp
) {
    val avatarBitmap = base64ToBitmap(avatarBase64)

    if (avatarBitmap != null) {
        Image(
            bitmap = avatarBitmap.asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        Text(
            text = username,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
@Composable
private fun getHeaderAvatarDecorationBrush(
    equippedShopItemId: String?
): Brush {
    return when (equippedShopItemId) {
        "purple_border" -> Brush.linearGradient(
            colors = listOf(
                PurplePrimary,
                MaterialTheme.colorScheme.primary
            )
        )

        "blue_border" -> Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.primary
            )
        )

        "green_glow" -> Brush.linearGradient(
            colors = listOf(
                GreenAccent,
                MaterialTheme.colorScheme.primary
            )
        )

        "cyber_pink_border" -> Brush.linearGradient(
            colors = listOf(
                PurplePrimary,
                GreenAccent
            )
        )

        "gold_rank_border" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFFFFD54F),
                Color(0xFFFFA000)
            )
        )

        "ice_blue_border" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFF80D8FF),
                MaterialTheme.colorScheme.secondary
            )
        )

        "fire_orange_border" -> Brush.linearGradient(
            colors = listOf(
                Color(0xFFFF7043),
                Color(0xFFFF1744)
            )
        )

        "rainbow_dev_border" -> Brush.linearGradient(
            colors = listOf(
                PurplePrimary,
                MaterialTheme.colorScheme.secondary,
                GreenAccent
            )
        )

        else -> Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            )
        )
    }
}

private fun getHeaderAvatarBorderPadding(
    equippedShopItemId: String?
): Dp {
    return when (equippedShopItemId) {
        "green_glow" -> 5.dp
        "purple_border" -> 4.dp
        "blue_border" -> 4.dp
        else -> 0.dp
    }
}