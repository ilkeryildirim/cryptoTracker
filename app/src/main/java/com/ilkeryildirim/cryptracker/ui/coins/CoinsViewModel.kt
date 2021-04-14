package com.ilkeryildirim.cryptracker.ui.coins

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ilkeryildirim.cryptracker.api.CryptrackerApiResult
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val coinsDataRepository: CoinsDataRepository,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {


    private val _uiState = MutableStateFlow<DiscoverFragmentUIState>(DiscoverFragmentUIState.Initial)
    val uiState: StateFlow<DiscoverFragmentUIState> = _uiState
    private lateinit var coinList: ArrayList<CoinItem>
    var isLoading = MutableLiveData<Boolean>()
    var favouriteCoins =MutableLiveData<List<String>>()
    var isFavouritesFiltering = MutableLiveData<Boolean>()

    init {
        isFavouritesFiltering.value = false
        getFavouriteCoins()
    }

    fun onRefresh() {
        _uiState.value = DiscoverFragmentUIState.Loading
        isLoading.value = true
        getFavouriteCoins()
        getCoinList()
    }

    fun onFilterFavStateChange(){
        isFavouritesFiltering.value = isFavouritesFiltering.value!!.not()
    }

    fun getCoinList() {
        isLoading.value = true
        _uiState.value = DiscoverFragmentUIState.Loading
        viewModelScope.launch {
            coinsDataRepository.getCoinList().let { result ->
                when (result) {
                    is CryptrackerApiResult.Success -> {
                        coinList = result.data
                        isLoading.value = false
                        _uiState.value = DiscoverFragmentUIState.Success(result.data)
                    }
                    is CryptrackerApiResult.Error -> {
                        result.message?.let {
                            _uiState.value = DiscoverFragmentUIState.Error(result.message)
                        }
                    }
                }
            }
        }
    }

   private fun getFavouriteCoins(){
        val favouriteCoins= arrayListOf<String>()
        val collectionRef = firebaseFirestore.collection(firebaseAuth.currentUser!!.uid)
        collectionRef.get().addOnSuccessListener {userCollection->
            userCollection.forEach {
                favouriteCoins.add(it.data["id"].toString())
            }

            this.favouriteCoins.value=favouriteCoins

        }.addOnFailureListener {

        }

    }
}

sealed class DiscoverFragmentUIState {
    object Initial : DiscoverFragmentUIState()
    object Loading : DiscoverFragmentUIState()
    data class Navigating(var destinationId: Int, var bundle: Bundle) : DiscoverFragmentUIState()
    data class Success(var coinList: ArrayList<CoinItem>) : DiscoverFragmentUIState()
    data class Error(val message: String) : DiscoverFragmentUIState()
}