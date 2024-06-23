package com.alkemy.alkewallet.data.model

data class LoginRequest(var email: String, var password: String)

data class LoginResponse(var accessToken: String)