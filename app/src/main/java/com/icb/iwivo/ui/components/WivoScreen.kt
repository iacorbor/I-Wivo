package com.icb.iwivo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WivoScreen(
    modifier: Modifier = Modifier,
    backgroundItemId: String? = null,
    themeItemId: String? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                getWivoScreenBackground(
                    backgroundItemId = backgroundItemId,
                    themeItemId = themeItemId
                )
            )
            .padding(24.dp),
        content = content
    )
}

private fun getWivoScreenBackground(
    backgroundItemId: String?,
    themeItemId: String?
): Brush {
    return when {
        themeItemId == "theme_blue_terminal" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF07111F),
                Color(0xFF04070D)
            )
        )

        themeItemId == "theme_matrix_wivo" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF06140D),
                Color(0xFF020604)
            )
        )

        themeItemId == "theme_purple_neon" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF140A24),
                Color(0xFF07040D)
            )
        )

        themeItemId == "theme_cyber_academy" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF10111F),
                Color(0xFF050711)
            )
        )

        themeItemId == "theme_dark_minimal" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF090A0E),
                Color(0xFF050507)
            )
        )

        themeItemId == "theme_full_stack" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0A1020),
                Color(0xFF090510),
                Color(0xFF020303)
            )
        )

        backgroundItemId == "background_deep_space" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF090D1A),
                Color(0xFF03040A)
            )
        )

        backgroundItemId == "background_neon_grid" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF101026),
                Color(0xFF050711)
            )
        )

        backgroundItemId == "background_midnight_code" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0D1117),
                Color(0xFF05070A)
            )
        )

        backgroundItemId == "background_matrix_flow" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF06190F),
                Color(0xFF020604)
            )
        )

        backgroundItemId == "background_blue_horizon" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF071A2E),
                Color(0xFF030713)
            )
        )

        backgroundItemId == "background_purple_void" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF160A25),
                Color(0xFF05030A)
            )
        )

        backgroundItemId == "background_green_terminal" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF04140A),
                Color(0xFF010402)
            )
        )

        backgroundItemId == "background_pro_black" -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF050506),
                Color(0xFF000000)
            )
        )

        else -> Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0D0F14),
                Color(0xFF07080C)
            )
        )
    }
}