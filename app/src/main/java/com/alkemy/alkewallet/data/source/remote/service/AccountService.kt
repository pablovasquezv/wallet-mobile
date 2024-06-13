package com.alkemy.alkewallet.data.source.remote.service

import com.alkemy.alkewallet.data.model.Account
import retrofit2.Response
import retrofit2.http.GET

interface AccountService {
    @GET("/accounts/me")
    suspend fun getAccounts(): Response<List<Account>>
}