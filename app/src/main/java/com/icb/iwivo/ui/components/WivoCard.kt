package com.icb.iwivo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.icb.iwivo.ui.theme.BorderColor
import com.icb.iwivo.ui.theme.CardDark
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.PurplePrimary

@Composable
fun WivoCard(
    modifier: Modifier = Modifier,
    themeItemId: String? = null,
    effectItemId: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(22.dp)

    val glowColor = getWivoCardGlowColor(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    )

    val borderColor = getWivoCardBorderColor(
        themeItemId = themeItemId,
        effectItemId = effectItemId
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = 0.18f),
                        Color.Transparent
                    )
                ),
                shape = shape
            )
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = getWivoCardContainerColor(themeItemId)
            ),
            shape = shape,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = borderColor.copy(alpha = 0.45f),
                    shape = shape
                )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                content = content
            )
        }
    }
}

private fun getWivoCardGlowColor(
    themeItemId: String?,
    effectItemId: String?
): Color {
    return when {
        effectItemId == "effect_soft_glow" -> PurplePrimary
        effectItemId == "effect_xp_spark" -> GreenAccent
        effectItemId == "effect_coin_flash" -> Color(0xFFFFD54F)
        effectItemId == "effect_streak_fire" -> Color(0xFFFF7043)
        effectItemId == "effect_code_scan" -> Color(0xFF00E5FF)

        themeItemId == "theme_matrix_wivo" -> GreenAccent
        themeItemId == "theme_blue_terminal" -> Color(0xFF40C4FF)
        themeItemId == "theme_purple_neon" -> PurplePrimary

        else -> PurplePrimary
    }
}

private fun getWivoCardBorderColor(
    themeItemId: String?,
    effectItemId: String?
): Color {
    return when {
        effectItemId == "effect_coin_flash" -> Color(0xFFFFD54F)
        effectItemId == "effect_streak_fire" -> Color(0xFFFF7043)
        effectItemId == "effect_code_scan" -> Color(0xFF00E5FF)

        themeItemId == "theme_matrix_wivo" -> GreenAccent
        themeItemId == "theme_blue_terminal" -> Color(0xFF40C4FF)
        themeItemId == "theme_purple_neon" -> PurplePrimary

        else -> BorderColor
    }
}

private fun getWivoCardContainerColor(
    themeItemId: String?
): Color {
    return when (themeItemId) {
        "theme_dark_minimal" -> Color(0xFF101114)
        "theme_matrix_wivo" -> Color(0xFF07130C)
        "theme_blue_terminal" -> Color(0xFF09111D)
        "theme_purple_neon" -> Color(0xFF12091F)
        "theme_cyber_academy" -> Color(0xFF10111F)
        "theme_full_stack" -> Color(0xFF0D1018)
        else -> CardDark
    }
}