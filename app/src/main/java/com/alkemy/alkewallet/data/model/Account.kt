package com.alkemy.alkewallet.data.model

import java.util.Date

data class Account(
    val id: Int? = null,
    val creationDate: Date,
    val money: Double,
    val isBlocked: Boolean? = false,
    val userId: Int
)

data class BalanceView(val amount: String)