package com.alkemy.alkewallet.ui.view.fragment.login

import android.content.Intent
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
import com.alkemy.alkewallet.ui.view.activity.MainActivity
import com.alkemy.alkewallet.R
import com.alkemy.alkewallet.databinding.FragmentLoginBinding
import com.alkemy.alkewallet.utils.SessionManager
import com.alkemy.alkewallet.utils.showToast
import com.alkemy.alkewallet.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText = binding.inputEmail.editText!!
        val passwordEditText = binding.inputPassword.editText!!
        val loginButton = binding.buttonLogin
        val loadingProgressBar = binding.loading
        val signupButton = binding.buttonSignup
        val forgotButton = binding.buttonForgot

        viewModel.loginForm.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.emailError?.let {
                    emailEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        viewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = if (loginResult.loading) View.VISIBLE else View.GONE
                loginResult.error?.let {
                    showToast(context, it)
                }
                loginResult.success?.let { token ->
                    sessionManager.saveAuthToken(token)
                    startActivity(Intent(context, MainActivity::class.java))
                }
                loginResult.success
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.loginDataChanged(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.login(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }

        loginButton.setOnClickListener {
            viewModel.login(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        forgotButton.setOnClickListener {
            showToast(context, R.string.action_not_supported)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}