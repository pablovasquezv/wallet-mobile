package com.alkemy.alkewallet.login

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionManager: SessionManager) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            sessionManager.getToken()
        }
        val request = chain.request().newBuilder()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}