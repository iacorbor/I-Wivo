package com.icb.iwivo.data.model

data class ShopItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Int,
    val unlocked: Boolean
)