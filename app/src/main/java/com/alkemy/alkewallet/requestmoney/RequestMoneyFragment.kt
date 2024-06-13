package com.alkemy.alkewallet.requestmoney

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.alkemy.alkewallet.MainViewModel
import com.alkemy.alkewallet.R
import com.alkemy.alkewallet.databinding.FragmentRequestMoneyBinding

class RequestMoneyFragment : Fragment() {

    private var _binding: FragmentRequestMoneyBinding? = null

    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRequestMoneyBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val amountEditText = binding.inputAmount.editText!!
        val descriptionEditText = binding.inputDescription.editText!!
        val requestButton = binding.buttonRequest
        val loadingProgressBar = binding.loading

        viewModel.transactionForm.observe(viewLifecycleOwner, Observer { transactionFormState ->
            if (transactionFormState == null) {
                return@Observer
            }
            requestButton.isEnabled = transactionFormState.isDataValid
            transactionFormState.amountError?.let {
                amountEditText.error = getString(it)
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { result ->
            loadingProgressBar.visibility = if (result.loading) View.VISIBLE else View.GONE
            result.success?.let {
                if (it) {
                    findNavController().navigate(R.id.action_requestMoneyFragment_to_homeFragment)
                }
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
                viewModel.transactionDataChanged(amountEditText.text.toString())
            }
        }
        amountEditText.addTextChangedListener(afterTextChangedListener)

        requestButton.setOnClickListener {
            viewModel.requestMoney(
                amount = amountEditText.text.toString(),
                description = descriptionEditText.text.toString()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}