package com.khater.rwaq.domain.useCases.auth

import com.khater.rwaq.domain.util.BlankPhoneNumberException
import com.khater.rwaq.domain.util.WrongPhoneNumberException


class ValidatePhoneNumberUseCase {
    operator fun invoke(phoneNumber: String) {
        if (phoneNumber.isBlank() || phoneNumber.isEmpty()) {
            throw BlankPhoneNumberException()
        }

        if (!phoneNumber.matches(PHONE_NUMBER_REGEX.toRegex())) {
            throw WrongPhoneNumberException()
        }
    }

    companion object {
        private const val PHONE_NUMBER_REGEX = "^01\\d{9}$"
    }
}