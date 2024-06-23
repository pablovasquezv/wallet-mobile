package com.alkemy.alkewallet.data.model

data class Result<T>(
    val success: T? = null,
    val error: Int? = null,
    val loading: Boolean = false
)