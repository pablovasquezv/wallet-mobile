package com.alkemy.alkewallet.di

import android.content.Context
import com.alkemy.alkewallet.data.source.remote.service.AccountService
import com.alkemy.alkewallet.data.source.remote.service.AuthService
import com.alkemy.alkewallet.data.source.remote.service.TransactionService
import com.alkemy.alkewallet.data.source.remote.service.UserService
import com.alkemy.alkewallet.login.AuthInterceptor
import com.alkemy.alkewallet.login.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object HiltModule {

    private const val BASE_URL =
        "http://wallet-main.eba-ccwdurgr.us-east-1.elasticbeanstalk.com/"

    @Singleton
    @Provides
    fun providesSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(sessionManager: SessionManager): AuthInterceptor =
        AuthInterceptor(sessionManager)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())

    @Provides
    fun providesAuthService(retrofit: Retrofit.Builder): AuthService {
        return retrofit.build().create(AuthService::class.java)
    }

    @Provides
    fun providesUserService(okHttpClient: OkHttpClient, retrofit: Retrofit.Builder): UserService {
        return retrofit.client(okHttpClient).build().create(UserService::class.java)
    }

    @Provides
    fun providesTransactionService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): TransactionService {
        return retrofit.client(okHttpClient).build().create(TransactionService::class.java)
    }

    @Provides
    fun providesAccountService(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): AccountService {
        return retrofit.client(okHttpClient).build().create(AccountService::class.java)
    }

}