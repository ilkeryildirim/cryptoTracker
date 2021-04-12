package com.ilkeryildirim.cryptracker.utils

import android.text.TextUtils
import android.util.Patterns

fun String.isValidEmail() = TextUtils.isEmpty(this).not() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isValidPassword() = TextUtils.isEmpty(this).not() && this.length >= 8
