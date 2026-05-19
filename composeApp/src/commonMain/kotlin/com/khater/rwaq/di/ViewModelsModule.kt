package com.khater.rwaq.di

import com.khater.rwaq.presentation.AppViewModel
import com.khater.rwaq.presentation.screens.orderScreen.OrderViewModel
import com.khater.rwaq.presentation.screens.branchScreen.BranchViewModel
import com.khater.rwaq.presentation.screens.branchesScreen.BranchesViewModel
import com.khater.rwaq.presentation.screens.cartScreen.CartViewModel
import com.khater.rwaq.presentation.screens.contactUsScreen.ContactUsViewModel
import com.khater.rwaq.presentation.screens.forgetPasswordScreen.ForgetPasswordViewModel
import com.khater.rwaq.presentation.screens.homeScreen.HomeViewModel
import com.khater.rwaq.presentation.screens.loginScreen.LoginViewModel
import com.khater.rwaq.presentation.screens.newCartScreen.NewCartViewModel
import com.khater.rwaq.presentation.screens.otpScreen.RegisterOtpViewModel
import com.khater.rwaq.presentation.screens.productScreen.ProductViewModel
import com.khater.rwaq.presentation.screens.profileScreen.ProfileViewModel
import com.khater.rwaq.presentation.screens.registerScreen.RegisterViewModel
import com.khater.rwaq.presentation.screens.resetPasswordScreen.ResetPasswordViewModel
import com.khater.rwaq.presentation.screens.rewardScreen.NewRewardViewModel
import com.khater.rwaq.presentation.screens.rewardScreen.RewardViewModel
import com.khater.rwaq.presentation.screens.splashScreen.SplashScreenViewModel
import com.khater.rwaq.presentation.screens.updateUserScreen.UpdateUserViewModel
import com.russhwolf.settings.ExperimentalSettingsApi
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val viewModelModule = module {

    viewModelOf(::AppViewModel)
    viewModelOf(::SplashScreenViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::BranchViewModel)
    viewModelOf(::ProductViewModel)
    viewModelOf(::CartViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::BranchesViewModel)
    viewModelOf(::ContactUsViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::ForgetPasswordViewModel)
    viewModelOf(::RegisterOtpViewModel)
    viewModelOf(::ResetPasswordViewModel)
    viewModelOf(::OrderViewModel)
    viewModelOf(::UpdateUserViewModel)
    viewModelOf(::RewardViewModel)
    viewModelOf(::NewRewardViewModel)
    viewModelOf(::NewCartViewModel)
}
