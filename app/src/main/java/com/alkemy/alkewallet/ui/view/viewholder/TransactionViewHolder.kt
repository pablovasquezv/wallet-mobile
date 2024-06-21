package com.alkemy.alkewallet.ui.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.alkemy.alkewallet.data.model.TransactionView
import com.alkemy.alkewallet.databinding.RecyclerTransactionItemBinding

class TransactionViewHolder(private val binding: RecyclerTransactionItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(transaction: TransactionView) {
        binding.textviewName.text = transaction.name
        binding.textviewAmount.text = transaction.amount
        binding.textviewDate.text = transaction.date
        binding.imageType.setImageResource(transaction.type)
    }
}