package com.khater.rwaq.di

import com.khater.rwaq.domain.useCases.CreateOrderUseCase
import com.khater.rwaq.domain.useCases.GetAllBranchesUseCase
import com.khater.rwaq.domain.useCases.GetAllProductsUseCase
import com.khater.rwaq.domain.useCases.GetUserUseCase
import com.khater.rwaq.domain.useCases.ManageCartUseCase
import com.khater.rwaq.domain.useCases.SendContactMessageUseCase
import com.khater.rwaq.domain.useCases.auth.DeleteAccountUseCase
import com.khater.rwaq.domain.useCases.auth.LoginUseCase
import com.khater.rwaq.domain.useCases.auth.LogoutUseCase
import com.khater.rwaq.domain.useCases.auth.OtpUseCase
import com.khater.rwaq.domain.useCases.auth.RegisterUseCase
import com.khater.rwaq.domain.useCases.auth.ResetPasswordUseCase
import com.khater.rwaq.domain.useCases.auth.UpdateFcmTokenUseCase
import com.khater.rwaq.domain.useCases.auth.UpdateUserUseCase
import com.khater.rwaq.domain.useCases.auth.ValidatePhoneNumberUseCase
import com.russhwolf.settings.ExperimentalSettingsApi
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


@OptIn(ExperimentalSettingsApi::class)
val useCaseModule = module {

    factoryOf(::GetAllBranchesUseCase)
    factoryOf(::GetAllProductsUseCase)
    factoryOf(::ManageCartUseCase)
    factoryOf(::CreateOrderUseCase)
    factoryOf(::SendContactMessageUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::ValidatePhoneNumberUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::OtpUseCase)
    factoryOf(::ResetPasswordUseCase)
    factoryOf(::UpdateFcmTokenUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetUserUseCase)
    factoryOf(::UpdateUserUseCase)
    factoryOf(::DeleteAccountUseCase)
}