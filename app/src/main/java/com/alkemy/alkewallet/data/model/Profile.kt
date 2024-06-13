package com.alkemy.alkewallet.data.model

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    val id: Int,
    @SerializedName("first_name")
    val name: String,
    @SerializedName("last_name")
    val lastName: String,
    val email: String,
    val password: String,
    val roleId: Int,
    val points: Int
)

data class UserView(
    val name: String? = null,
    val displayName: String? = null,
    val email: String? = null
)