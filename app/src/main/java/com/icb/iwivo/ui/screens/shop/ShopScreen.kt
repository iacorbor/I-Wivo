package com.icb.iwivo.ui.screens.shop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icb.iwivo.R
import com.icb.iwivo.data.repository.UserRepository
import com.icb.iwivo.ui.theme.CardDark
import com.icb.iwivo.ui.theme.GreenAccent
import com.icb.iwivo.ui.theme.TextSecondary
import com.icb.iwivo.ui.components.WivoScreen

@Composable
fun ShopScreen() {
    val context = LocalContext.current
    val userRepository = remember { UserRepository(context) }

    var coins by remember { mutableIntStateOf(userRepository.getCoins()) }
    var items by remember { mutableStateOf(userRepository.getShopItems()) }
    var message by remember { mutableStateOf<String?>(null) }

    fun refreshShop() {
        coins = userRepository.getCoins()
        items = userRepository.getShopItems()
    }

    WivoScreen{
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.shop_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = stringResource(R.string.shop_subtitle),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = CardDark),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = stringResource(R.string.wivo_coins),
                        color = TextSecondary
                    )

                    Text(
                        text = stringResource(R.string.coins_amount, coins),
                        style = MaterialTheme.typography.headlineSmall,
                        color = GreenAccent
                    )
                }
            }

            message?.let {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = it,
                    color = TextSecondary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            items.forEach { item ->
                ShopItemCard(
                    name = item.name,
                    description = item.description,
                    price = item.price,
                    unlocked = item.unlocked,
                    onBuyClick = {
                        val bought = userRepository.buyItem(item.id)

                        message = if (bought) {
                            "Objeto desbloqueado: ${item.name}"
                        } else {
                            "No tienes monedas suficientes o ya está desbloqueado"
                        }

                        refreshShop()
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun ShopItemCard(
    name: String,
    description: String,
    price: Int,
    unlocked: Boolean,
    onBuyClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardDark),
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = if (unlocked) "✅ $name" else name,
                style = MaterialTheme.typography.titleMedium,
                color = if (unlocked) GreenAccent else MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (unlocked) {
                OutlinedButton(
                    onClick = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.unlocked))
                }
            } else {
                Button(
                    onClick = onBuyClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.buy_for_coins, price)
                    )
                }
            }
        }
    }
}