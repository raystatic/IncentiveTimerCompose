package com.raystatic.incentivetimer.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class IconKey(val rewardIcon:ImageVector) {
    CAKE(Icons.Default.Cake),
    BATH_TUB(Icons.Default.Bathtub),
    TV(Icons.Default.Tv),
    STAR(Icons.Default.Star),
    FAVORITE(Icons.Default.Favorite),
    PETS(Icons.Default.Pets),
    PHONE(Icons.Default.Phone),
    CARD_GIFT_CARD(Icons.Default.CardGiftcard),
    GAME_PAD(Icons.Default.Gamepad),
    MONEY(Icons.Default.Money),
    COMPUTER(Icons.Default.Computer),
    GROUP(Icons.Default.Group),
    ICE_SKATING(Icons.Default.IceSkating),
    EMOJI_FOOD_BEVERAGE(Icons.Default.EmojiFoodBeverage),
    SPORTS_MOTOR_SPORTS(Icons.Default.SportsMotorsports),
    SPORTS_FOOTBALL(Icons.Default.SportsFootball),
    HEADPHONES(Icons.Default.Headphones),
    SHOPPING_CART(Icons.Default.ShoppingCart),
    DIRECTIONS_BIKE(Icons.Default.DirectionsBike),
    LOCAL_PIZZA(Icons.Default.LocalPizza)
}


val defaultRrewardIcon = IconKey.STAR