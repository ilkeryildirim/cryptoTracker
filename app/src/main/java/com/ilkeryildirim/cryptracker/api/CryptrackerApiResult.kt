package com.ilkeryildirim.cryptracker.api

sealed class CryptrackerApiResult<out T : Any> {
  data class Success<out T : Any>(val data: T) : CryptrackerApiResult<T>()
  data class Error(val message: String?, val statusCode: Int? = null) : CryptrackerApiResult<Nothing>()
}