package com.alkemy.alkewallet.data.source.remote.service

import com.alkemy.alkewallet.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET

interface UserService {
    @GET("/auth/me")
    suspend fun getUser(): Response<ProfileResponse>
}