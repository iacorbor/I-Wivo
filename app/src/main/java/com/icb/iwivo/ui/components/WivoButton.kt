package com.icb.iwivo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.icb.iwivo.ui.theme.BluePrimary
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary

@Composable
fun WivoButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonStyleItemId: String? = null,
    themeItemId: String? = null
) {
    val gradient = getWivoButtonBrush(
        buttonStyleItemId = buttonStyleItemId,
        themeItemId = themeItemId
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(gradient)
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

private fun getWivoButtonBrush(
    buttonStyleItemId: String?,
    themeItemId: String?
): Brush {
    return when {
        buttonStyleItemId == "button_green_pulse" -> Brush.horizontalGradient(
            colors = listOf(
                GreenAccent,
                Color(0xFF00C853)
            )
        )

        buttonStyleItemId == "button_purple_core" -> Brush.horizontalGradient(
            colors = listOf(
                PurplePrimary,
                Color(0xFF7C4DFF)
            )
        )

        buttonStyleItemId == "button_blue_flash" -> Brush.horizontalGradient(
            colors = listOf(
                BluePrimary,
                Color(0xFF00B0FF)
            )
        )

        buttonStyleItemId == "button_neon_outline" -> Brush.horizontalGradient(
            colors = listOf(
                PurplePrimary,
                GreenAccent
            )
        )

        buttonStyleItemId == "button_terminal_green" -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF00C853),
                Color(0xFF1B5E20)
            )
        )

        buttonStyleItemId == "button_gold_focus" -> Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFFFD54F),
                Color(0xFFFFA000)
            )
        )

        themeItemId == "theme_matrix_wivo" -> Brush.horizontalGradient(
            colors = listOf(
                GreenAccent,
                Color(0xFF1B5E20)
            )
        )

        themeItemId == "theme_blue_terminal" -> Brush.horizontalGradient(
            colors = listOf(
                BluePrimary,
                Color(0xFF2962FF)
            )
        )

        themeItemId == "theme_purple_neon" -> Brush.horizontalGradient(
            colors = listOf(
                PurplePrimary,
                Color(0xFFB388FF)
            )
        )

        else -> Brush.horizontalGradient(
            colors = listOf(PurplePrimary, BluePrimary)
        )
    }
}