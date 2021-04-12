package com.ilkeryildirim.cryptracker.ui.coins

import com.ilkeryildirim.cryptracker.api.CryptrackerApiResult
import com.ilkeryildirim.cryptracker.api.model.coin.CoinDetail
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem

interface CoinsDataRepository {
    suspend fun getCoinList() : CryptrackerApiResult<ArrayList<CoinItem>>
    suspend fun getCoinDetail(id:String) : CryptrackerApiResult<CoinDetail>
}