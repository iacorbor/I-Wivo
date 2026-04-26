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
import com.icb.iwivo.ui.theme.PurplePrimary

@Composable
fun WivoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(22.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        PurplePrimary.copy(alpha = 0.15f),
                        Color.Transparent
                    )
                ),
                shape = shape
            )
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CardDark),
            shape = shape,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = BorderColor.copy(alpha = 0.4f),
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