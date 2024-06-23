package com.alkemy.alkewallet.data.repository

import com.alkemy.alkewallet.data.source.remote.UserDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(private val dataSource: UserDataSource) {
    fun login(email: String, password: String) = dataSource.login(email, password)

    fun signup(name: String, lastName: String, email: String, password: String) =
        dataSource.signup(name, lastName, email, password)

    fun profile() = dataSource.profile()
}