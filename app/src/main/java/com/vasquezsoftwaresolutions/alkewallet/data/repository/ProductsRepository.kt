package com.alkemy.alkewallet.data.repository

import com.alkemy.alkewallet.data.model.Transaction
import com.alkemy.alkewallet.data.model.TransactionType
import com.alkemy.alkewallet.data.source.remote.ProductsDataSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class ProductsRepository @Inject constructor(private val productsDataSource: ProductsDataSource) {
    fun transactions(url: String? = null) = productsDataSource.transactions(url)

    fun accounts() = productsDataSource.accounts()

    fun sendMoney(
        amount: Double,
        description: String,
        user: Int,
        account: Int,
        destination: Int
    ): Flow<Response<Transaction>> {
        val transaction = Transaction(
            amount = amount,
            concept = description,
            type = TransactionType.PAYMENT,
            userId = user,
            accountId = account,
            accountDestinationId = destination
        )
        return productsDataSource.createTransaction(transaction)
    }

    fun requestMoney(
        amount: Double,
        description: String,
        user: Int,
        account: Int,
        destination: Int
    ): Flow<Response<Transaction>> {
        val transaction = Transaction(
            amount = amount,
            concept = description,
            type = TransactionType.TOP_UP,
            userId = user,
            accountId = account,
            accountDestinationId = destination
        )
        return productsDataSource.createTransaction(transaction)
    }
}