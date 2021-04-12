package com.ilkeryildirim.cryptracker.api.model.coin

import com.google.gson.annotations.SerializedName

data class CoinDetail(
    var symbol: String? = "",
    var name: String? = "",
    var hashing_algorithm: String? = "",
    var description: Description? = null,
    var market_data: MarketData? = null,
    var image: Image? = null,
    @SerializedName("price_change_percentage_24h")
    var priceChangePercentageForDayHour: Double? = 0.0
)
