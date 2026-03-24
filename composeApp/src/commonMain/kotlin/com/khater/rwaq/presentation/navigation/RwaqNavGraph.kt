package com.khater.rwaq.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.khater.rwaq.presentation.screens.orderScreen.orderScreenRoute
import com.khater.rwaq.presentation.screens.branchScreen.branchScreenRoute
import com.khater.rwaq.presentation.screens.branchesScreen.branchesScreenRoute
import com.khater.rwaq.presentation.screens.cartScreen.cartScreenRoute
import com.khater.rwaq.presentation.screens.contactUsScreen.contactUiScreenRoute
import com.khater.rwaq.presentation.screens.forgetPasswordScreen.forgetPasswordScreenRoute
import com.khater.rwaq.presentation.screens.homeScreen.homeScreenRoute
import com.khater.rwaq.presentation.screens.loginScreen.loginScreenRoute
import com.khater.rwaq.presentation.screens.moreScreen.moreScreenRoute
import com.khater.rwaq.presentation.screens.onBoardingScreen.onBoardingScreenRoute
import com.khater.rwaq.presentation.screens.otpScreen.registerOtpScreenRoute
import com.khater.rwaq.presentation.screens.privacyPolicyScreen.privacyPolicyScreenRoute
import com.khater.rwaq.presentation.screens.productScreen.productScreenRoute
import com.khater.rwaq.presentation.screens.profileScreen.profileScreenRoute
import com.khater.rwaq.presentation.screens.registerScreen.registerScreenRoute
import com.khater.rwaq.presentation.screens.resetPasswordScreen.resetPasswordScreenRoute
import com.khater.rwaq.presentation.screens.rewardScreen.rewardScreenRoute
import com.khater.rwaq.presentation.screens.splashScreen.splashScreenRoute
import com.khater.rwaq.presentation.screens.updateUserScreen.updateUserScreenRoute
import com.khater.rwaq.presentation.util.LocalNavigationProvider

@Composable
fun RwaqNavGraph(
    modifier: Modifier = Modifier
    ){
    val navController = LocalNavigationProvider.current
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.SplashScreen
    ){
        onBoardingScreenRoute()
        splashScreenRoute()
        privacyPolicyScreenRoute()
        homeScreenRoute()
        loginScreenRoute()
        orderScreenRoute()
        forgetPasswordScreenRoute()
        registerOtpScreenRoute()
        registerScreenRoute()
        updateUserScreenRoute()
        resetPasswordScreenRoute()
        rewardScreenRoute()
        cartScreenRoute()
        contactUiScreenRoute()
        profileScreenRoute()
        moreScreenRoute()
        branchScreenRoute()
        branchesScreenRoute()
        productScreenRoute()
    }
}