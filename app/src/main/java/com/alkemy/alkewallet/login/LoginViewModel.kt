package com.alkemy.alkewallet.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alkemy.alkewallet.R
import com.alkemy.alkewallet.data.model.Result
import com.alkemy.alkewallet.data.model.UserFormState
import com.alkemy.alkewallet.data.source.remote.UserRepository
import com.alkemy.alkewallet.utils.isEmailValid
import com.alkemy.alkewallet.utils.isPasswordValid
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<UserFormState>()
    val loginForm: LiveData<UserFormState> = _loginForm

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> = _loginResult

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginResult.value = Result(loading = true)
        userRepository.login(email, password).collect {
            if (it.isSuccessful) {
                _loginResult.value =
                    Result(success = it.body()?.accessToken)
            } else {
                _loginResult.value = Result(error = R.string.login_failed)
            }
        }
    }

    fun loginDataChanged(email: String, password: String) {
        when {
            !isEmailValid(email) -> _loginForm.value =
                UserFormState(emailError = R.string.invalid_user)

            !isPasswordValid(password) -> _loginForm.value =
                UserFormState(passwordError = R.string.invalid_password)

            else -> _loginForm.value = UserFormState(isDataValid = true)
        }
    }
}