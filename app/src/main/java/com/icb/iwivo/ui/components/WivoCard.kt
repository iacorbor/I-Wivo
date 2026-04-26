package com.icb.iwivo.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.icb.iwivo.ui.theme.BorderColor
import com.icb.iwivo.ui.theme.CardDark

@Composable
fun WivoCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(22.dp)

    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BorderColor.copy(alpha = 0.45f),
                shape = shape
            )
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            content = content
        )
    }
}