package com.ilkeryildirim.cryptracker.ui.subviews.coins


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ilkeryildirim.cryptracker.R
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem


class CoinsItemViewModel : ViewModel() {


    private val imageUrl = MutableLiveData<String?>()
    private val name = MutableLiveData<String>()
    private val volume = MutableLiveData<String>()
    private val currentPrice = MutableLiveData<String>()
    private val priceChange24Hr = MutableLiveData<String>()


    fun bind(coin: CoinItem) {
        this.imageUrl.value = coin.image
        this.name.value = coin.name
        this.volume.value = coin.totalVolume.toString()
        this.currentPrice.value = coin.currentPrice.toString()+" USD"
        this.priceChange24Hr.value = coin.priceChange24h.toString()
    }

    fun currentPrice() = currentPrice

    fun getVolume() = volume

    fun getImageUrl() = imageUrl

    fun getName() = name

    fun getPriceChange24hr() = priceChange24Hr

}
