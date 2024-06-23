package com.alkemy.alkewallet.ui.view.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alkemy.alkewallet.data.model.TransactionView
import com.alkemy.alkewallet.databinding.RecyclerTransactionItemBinding
import com.alkemy.alkewallet.ui.view.viewholder.TransactionViewHolder

class TransactionsAdapter : RecyclerView.Adapter<TransactionViewHolder>() {

    private val transactions: MutableList<TransactionView> = mutableListOf()

    fun addData(list: List<TransactionView>) {
        val size = transactions.size
        transactions.addAll(list)
        notifyItemRangeInserted(size, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = RecyclerTransactionItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return transactions.count()
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }
}