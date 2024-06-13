package com.alkemy.alkewallet.utils

import android.util.Patterns

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    return password.length in 6..10
}