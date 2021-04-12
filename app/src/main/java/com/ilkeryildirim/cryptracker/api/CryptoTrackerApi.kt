package com.ilkeryildirim.cryptracker.api
import com.ilkeryildirim.cryptracker.api.model.coin.CoinDetail
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem
import com.ilkeryildirim.cryptracker.utils.ApiConstants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CryptoTrackerApi {
    @GET(ApiConstants.Endpoints.COINS_MARKETS_ENDPOINT)
    suspend fun getCoinList(@Query("vs_currency") currency: String = "usd"): ArrayList<CoinItem>

    @GET(ApiConstants.Endpoints.COINS_BASE_ENDPOINT+"/{id}")
    suspend fun getCoinDetailById(@Path("id") coinId:String):CoinDetail
}

