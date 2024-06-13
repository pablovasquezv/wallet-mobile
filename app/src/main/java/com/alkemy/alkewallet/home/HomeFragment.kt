package com.alkemy.alkewallet.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alkemy.alkewallet.MainViewModel
import com.alkemy.alkewallet.R
import com.alkemy.alkewallet.databinding.FragmentHomeBinding
import com.alkemy.alkewallet.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hiTextView = binding.textviewHi
        val balanceTextView = binding.textviewBalance
        val loadingProgressBar = binding.loading
        val transactionsRecycler = binding.rvTransactions
        val emptyTransactionsTextView = binding.textviewEmpty

        val adapter = TransactionsAdapter()
        transactionsRecycler.adapter = adapter

        transactionsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager: LinearLayoutManager =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstVisibleItemPosition: Int = layoutManager.findFirstVisibleItemPosition()

                if (!viewModel.isLastPage()) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        viewModel.loadTransactions()
                    }
                }
            }
        })

        viewModel.profileResult.observe(viewLifecycleOwner, Observer { profileResult ->
            profileResult ?: return@Observer
            loadingProgressBar.visibility = if (profileResult.loading) View.VISIBLE else View.GONE
            profileResult.error?.let {
                showToast(context, it)
            }
            profileResult.success?.let { user ->
                val text = user.name?.let {
                    getString(R.string.home_hi, it)
                } ?: getString(R.string.home_hi_default)
                hiTextView.text = text
            }
        })

        viewModel.transactionsResult.observe(viewLifecycleOwner, Observer { transactionsResult ->
            transactionsResult ?: return@Observer
            loadingProgressBar.visibility =
                if (transactionsResult.loading) View.VISIBLE else View.GONE
            transactionsResult.error?.let {
                showToast(context, it)
            }
            transactionsResult.success?.let { transactions ->
                emptyTransactionsTextView.visibility =
                    if (transactions.isEmpty()) View.VISIBLE else View.GONE
                transactionsRecycler.visibility =
                    if (transactions.isEmpty()) View.GONE else View.VISIBLE
                adapter.addData(transactions)
            }
        })

        viewModel.balance.observe(viewLifecycleOwner, Observer {
            balanceTextView.text = it.amount
        })

        binding.buttonRequestMoney.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_requestMoneyFragment)
        }

        binding.buttonSendMoney.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_sendMoneyFragment)
        }
    }
}