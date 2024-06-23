package com.alkemy.alkewallet.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun showToast(context: Context? = null, @StringRes errorString: Int) {
    val appContext = context?.applicationContext ?: return
    Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
}