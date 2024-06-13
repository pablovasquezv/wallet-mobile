package com.alkemy.alkewallet.data.source.remote.service

import com.alkemy.alkewallet.data.model.LoginRequest
import com.alkemy.alkewallet.data.model.LoginResponse
import com.alkemy.alkewallet.data.model.SignupRequest
import com.alkemy.alkewallet.data.model.SignupResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/users")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>
}