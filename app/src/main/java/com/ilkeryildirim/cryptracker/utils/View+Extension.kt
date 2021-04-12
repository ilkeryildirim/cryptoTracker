package com.ilkeryildirim.cryptracker.utils

import android.view.View
import android.widget.EditText
import android.widget.SearchView
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}
