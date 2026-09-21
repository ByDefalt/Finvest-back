package com.example.finvest.modules.auth.domain.exception

class UserAlreadyExistsException(email: String) :
    RuntimeException("User with email $email already exists")

class UserCreationException(email: String) :
    RuntimeException("Failed to create user with email $email")

class UserNotExistsException(email: String) :
    RuntimeException("User with email $email does not exist")

class PasswordMismatchException(email: String) :
    RuntimeException("Password mismatch for user with email $email")
