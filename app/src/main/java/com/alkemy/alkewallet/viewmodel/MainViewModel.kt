package com.alkemy.alkewallet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alkemy.alkewallet.R
import com.alkemy.alkewallet.data.model.BalanceView
import com.alkemy.alkewallet.data.model.Result
import com.alkemy.alkewallet.data.model.TransactionFormState
import com.alkemy.alkewallet.data.model.TransactionType
import com.alkemy.alkewallet.data.model.TransactionView
import com.alkemy.alkewallet.data.model.UserView
import com.alkemy.alkewallet.data.repository.ProductsRepository
import com.alkemy.alkewallet.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.util.Objects
import javax.inject.Inject
import kotlin.properties.Delegates

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private var userId by Delegates.notNull<Int>()
    private var accountId by Delegates.notNull<Int>()
    private var destinationAccountId = 2081

    private val _profileResult = MutableLiveData<Result<UserView>>()
    val profileResult: LiveData<Result<UserView>> = _profileResult

    private val _transactionsResult = MutableLiveData<Result<List<TransactionView>>>()
    val transactionsResult: LiveData<Result<List<TransactionView>>> = _transactionsResult

    private val _formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
    private var _lastPageTransactions: Boolean = false
    private var _nextPage: String? = null

    private val _balance = MutableLiveData<BalanceView>()
    val balance: LiveData<BalanceView> = _balance

    private val _transactionForm = MutableLiveData<TransactionFormState>()
    val transactionForm: LiveData<TransactionFormState> = _transactionForm

    private val _loading = MutableLiveData<Result<Boolean>>()
    val loading: LiveData<Result<Boolean>> = _loading

    init {
        profile()
        loadTransactions()
        accounts()
    }

    private fun profile() = viewModelScope.launch {
        _profileResult.value = Result(loading = true)
        userRepository.profile().collect {
            if (it.isSuccessful) {
                val user = it.body()?.let { r ->
                    userId = r.id
                    UserView(
                        name = r.name,
                        displayName = "${r.name} ${r.lastName}",
                        email = r.email
                    )
                } ?: UserView()
                _profileResult.value = Result(user)
            } else {
                _profileResult.value = Result(error = R.string.login_failed)
            }
        }
    }

    fun loadTransactions() = viewModelScope.launch {
        if (!_lastPageTransactions) {
            _transactionsResult.value = Result(loading = true)
            productsRepository.transactions(_nextPage).collect {
                if (it.isSuccessful) {
                    _lastPageTransactions = it.body()?.nextPage?.isNotEmpty() ?: true
                    _nextPage = it.body()?.nextPage

                    val transactions = it.body()?.data?.map { transaction ->
                        val isPayment = transaction.type == TransactionType.PAYMENT
                        TransactionView(
                            name = "Falta buscar el nombre",
                            amount = if (isPayment) "-$${transaction.amount}" else "+$${transaction.amount}",
                            date = transaction.date?.let { date -> _formatter.format(date) } ?: "",
                            type = if (isPayment) R.drawable.icon_send else R.drawable.icon_request
                        )
                    }
                    _transactionsResult.value = Result(transactions)
                } else {
                    _transactionsResult.value = Result(error = R.string.home_transactions_error)
                }
            }
        }
    }

    private fun accounts() = viewModelScope.launch {
        productsRepository.accounts().collect {
            if (it.isSuccessful) {
                val amount = it.body()?.sumOf { account -> account.money }
                _balance.value = BalanceView(String.format("$%.2f", amount))
                accountId = it.body()?.first()?.id ?: 0
            }
        }
    }

    fun isLastPage() = _lastPageTransactions

    fun transactionDataChanged(amount: String) {
        when {
            Objects.isNull(amount.toDoubleOrNull()) -> _transactionForm.value =
                TransactionFormState(amountError = R.string.invalid_amount)

            else -> _transactionForm.value = TransactionFormState(isDataValid = true)
        }
    }

    fun sendMoney(
        amount: String,
        description: String
    ) = viewModelScope.launch {
        _loading.value = Result(loading = true)
        productsRepository.sendMoney(
            amount = amount.toDouble(),
            description = description,
            user = userId,
            account = accountId,
            destination = destinationAccountId
        ).collect {
            if (it.isSuccessful) {
                refresh()
                _loading.value = Result(success = true)
            }
        }
    }

    fun requestMoney(
        amount: String,
        description: String
    ) = viewModelScope.launch {
        _loading.value = Result(loading = true)
        productsRepository.requestMoney(
            amount = amount.toDouble(),
            description = description,
            user = userId,
            account = accountId,
            destination = destinationAccountId
        ).collect {
            if (it.isSuccessful) {
                refresh()
                _loading.value = Result(success = true)
            }
        }
    }

    private fun refresh() {
        _lastPageTransactions = false
        _nextPage = null
        loadTransactions()
        accounts()
    }
}