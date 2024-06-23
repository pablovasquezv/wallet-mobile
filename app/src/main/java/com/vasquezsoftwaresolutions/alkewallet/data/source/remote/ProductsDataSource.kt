package com.alkemy.alkewallet.data.source.remote

import com.alkemy.alkewallet.data.model.Transaction
import com.alkemy.alkewallet.data.source.remote.service.AccountService
import com.alkemy.alkewallet.data.source.remote.service.TransactionService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductsDataSource @Inject constructor(
    private val transactionService: TransactionService,
    private val accountService: AccountService
) {
    fun transactions(url: String? = "/transactions") = flow {
        val page = url ?: "/transactions"
        emit(transactionService.getTransactions(page))
    }.flowOn(Dispatchers.IO)

    fun accounts() = flow {
        emit(accountService.getAccounts())
    }.flowOn(Dispatchers.IO)

    fun createTransaction(transaction: Transaction) = flow {
        emit(transactionService.createTransaction(transaction))
    }.flowOn(Dispatchers.IO)
}