package com.ilkeryildirim.cryptracker.ui.coins

import com.ilkeryildirim.cryptracker.api.CryptoTrackerApi
import com.ilkeryildirim.cryptracker.api.CryptrackerApiResult
import com.ilkeryildirim.cryptracker.api.CryptrackerApiResult.Success
import com.ilkeryildirim.cryptracker.api.model.coin.CoinDetail
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem


import javax.inject.Inject

class CoinsDataRepositoryImpl @Inject constructor(private val cryptoTrackerApi: CryptoTrackerApi) : CoinsDataRepository {

    override suspend fun getCoinList(): CryptrackerApiResult<ArrayList<CoinItem>> {
        return try {
            val response = cryptoTrackerApi.getCoinList()
            Success(response)
        } catch (e: Exception) {
            CryptrackerApiResult.Error(e.localizedMessage)
        }
    }

    override suspend fun getCoinDetail(id:String): CryptrackerApiResult<CoinDetail> {
        return try {
            val response = cryptoTrackerApi.getCoinDetailById(id)
            Success(response)
        } catch (e: Exception) {
            CryptrackerApiResult.Error(e.localizedMessage)
        }
    }
}