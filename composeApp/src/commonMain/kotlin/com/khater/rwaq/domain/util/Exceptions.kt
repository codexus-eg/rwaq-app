package com.khater.rwaq.domain.util

abstract class RwaqException(message: String= "Aqua Exception") : Exception(message)


class NoNetworkException : RwaqException("No Internet Connection")
class ServerErrorException : RwaqException("Server error occurred")
class UnknownException : RwaqException("Unknown Exception")

// Resource-related exceptions (Common for all features)

class InvalidRequestException : RwaqException("Invalid request")
class UnAuthorizedException : RwaqException("User has no access to application")
class TooManyRequestsException : RwaqException("Too many requests")


open class ValidationException(message: String) : RwaqException(message)
class BlankPhoneNumberException() : ValidationException("Phone number cannot be blank")
class WrongPhoneNumberException() : ValidationException("Wrong phone number")


class NoInternetException(message: String = "No Internet") : RwaqException(message)

class UnknownNetworkException(message: String = "Unknown error") : RwaqException(message)


abstract class AuthenticationException(message: String) : Exception(message)

class InvalidMobileNumberException(mobileNumber: String) : AuthenticationException("mobile number: $mobileNumber doesn't match validation")
class UserIsBlockedException : AuthenticationException("user with mobile number: has many login retries")
class InvalidPasswordException : AuthenticationException("password doesn't match validations")
class InvalidCredentialsException : AuthenticationException("user with mobile number, doesn't exist or password is incorrect")
class PhoneNumberAlreadyExistsException : AuthenticationException("Phone number already exists")

