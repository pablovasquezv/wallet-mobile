package com.alkemy.alkewallet.ui.view.fragment.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.alkemy.alkewallet.R
import com.alkemy.alkewallet.databinding.FragmentSignupBinding
import com.alkemy.alkewallet.utils.showToast
import com.alkemy.alkewallet.viewmodel.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<SignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEditText = binding.inputName.editText!!
        val lastNameEditText = binding.inputLastName.editText!!
        val emailEditText = binding.inputEmail.editText!!
        val passwordEditText = binding.inputPassword.editText!!
        val passwordConfirmEditText = binding.inputPasswordConfirm.editText!!
        val loginButton = binding.buttonLogin
        val loadingProgressBar = binding.loading
        val signupButton = binding.buttonSignup

        viewModel.signupForm.observe(viewLifecycleOwner,
            Observer { signupFormState ->
                if (signupFormState == null) {
                    return@Observer
                }
                signupButton.isEnabled = signupFormState.isDataValid
                signupFormState.nameError?.let {
                    nameEditText.error = getString(it)
                }
                signupFormState.lastNameError?.let {
                    lastNameEditText.error = getString(it)
                }
                signupFormState.emailError?.let {
                    emailEditText.error = getString(it)
                }
                signupFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
                signupFormState.passwordConfirmError?.let {
                    passwordConfirmEditText.error = getString(it)
                }
            })

        viewModel.signupResult.observe(viewLifecycleOwner,
            Observer { signupResult ->
                signupResult ?: return@Observer
                loadingProgressBar.visibility =
                    if (signupResult.loading) View.VISIBLE else View.GONE
                signupResult.error?.let {
                    showToast(context, it)
                }
                signupResult.success?.let {
                    showToast(context, it)
                    navigateToLogin()
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.signupDataChanged(
                    nameEditText.text.toString(),
                    lastNameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString(),
                    passwordConfirmEditText.text.toString()
                )
            }
        }
        nameEditText.addTextChangedListener(afterTextChangedListener)
        lastNameEditText.addTextChangedListener(afterTextChangedListener)
        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordConfirmEditText.addTextChangedListener(afterTextChangedListener)
        passwordConfirmEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.signup(
                    nameEditText.text.toString(),
                    lastNameEditText.text.toString(),
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        signupButton.setOnClickListener {
            viewModel.signup(
                nameEditText.text.toString(),
                lastNameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        loginButton.setOnClickListener {
            navigateToLogin()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToLogin() {
        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
    }
}