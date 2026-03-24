package com.khater.rwaq.presentation.navigation

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import rwaq.composeapp.generated.resources.Res
import rwaq.composeapp.generated.resources.baseline_shopping_cart_24
import rwaq.composeapp.generated.resources.cart
import rwaq.composeapp.generated.resources.fill_more_icon
import rwaq.composeapp.generated.resources.fill_profile_icon
import rwaq.composeapp.generated.resources.gift
import rwaq.composeapp.generated.resources.home_screen
import rwaq.composeapp.generated.resources.logo
import rwaq.composeapp.generated.resources.more_screen
import rwaq.composeapp.generated.resources.profile_screen
import rwaq.composeapp.generated.resources.reward


enum class NavigationBarItems(
    val icon: DrawableResource,
    val fillIcon: DrawableResource,
    val title: StringResource,
    val route: Screen,
) {
    Home(
        icon = Res.drawable.logo,
        fillIcon = Res.drawable.logo,
        title = Res.string.home_screen,
        route = Screen.HomeScreen
    ),
    Reward(
        icon = Res.drawable.gift,
        fillIcon = Res.drawable.gift,
        title = Res.string.reward,
        route = Screen.RewardScreen
    ),
    Cart(
        icon = Res.drawable.baseline_shopping_cart_24,
        fillIcon = Res.drawable.baseline_shopping_cart_24,
        title = Res.string.cart,
        route = Screen.CartScreen
    ),
    Profile(
        icon = Res.drawable.fill_profile_icon,
        fillIcon = Res.drawable.fill_profile_icon,
        title = Res.string.profile_screen,
        route = Screen.ProfileScreen
    ),
//    More(
//        icon = Res.drawable.fill_more_icon,
//        fillIcon = Res.drawable.fill_more_icon,
//        title = Res.string.more_screen,
//        route = Screen.MoreScreen
//    ),

}