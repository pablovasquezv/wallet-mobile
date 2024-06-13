package com.alkemy.alkewallet.signup

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
class SignupViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _signupForm = MutableLiveData<UserFormState>()
    val signupForm: LiveData<UserFormState> = _signupForm

    private val _signupResult = MutableLiveData<Result<Int>>()
    val signupResult: LiveData<Result<Int>> = _signupResult

    fun signup(name: String, lastName: String, email: String, password: String) =
        viewModelScope.launch {
            _signupResult.value = Result(loading = true)
            userRepository.signup(name, lastName, email, password).collect {
                if (it.isSuccessful) {
                    _signupResult.value =
                        Result(success = R.string.signup_ok)
                } else {
                    _signupResult.value = Result(error = R.string.signup_failed)
                }
            }
        }

    fun signupDataChanged(
        name: String,
        lastName: String,
        email: String,
        password: String,
        passwordConfirm: String
    ) {
        when {
            name.isBlank() -> _signupForm.value =
                UserFormState(nameError = R.string.invalid_required)

            lastName.isBlank() -> _signupForm.value =
                UserFormState(lastNameError = R.string.invalid_required)

            !isEmailValid(email) -> _signupForm.value =
                UserFormState(emailError = R.string.invalid_user)

            !isPasswordValid(password) -> _signupForm.value =
                UserFormState(passwordError = R.string.invalid_password)

            isPasswordValid(password) && password != passwordConfirm -> _signupForm.value =
                UserFormState(passwordConfirmError = R.string.invalid_password_confirm)

            else -> _signupForm.value = UserFormState(isDataValid = true)
        }
    }
}