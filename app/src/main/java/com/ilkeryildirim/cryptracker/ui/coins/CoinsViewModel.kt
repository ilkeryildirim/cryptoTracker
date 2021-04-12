package com.ilkeryildirim.cryptracker.ui.coins

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ilkeryildirim.cryptracker.R
import com.ilkeryildirim.cryptracker.api.CryptrackerApiResult
import com.ilkeryildirim.cryptracker.api.model.coin.CoinItem
import com.ilkeryildirim.cryptracker.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CoinsViewModel @Inject constructor(
        private val coinsDataRepository: CoinsDataRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow<DiscoverFragmentUIState>(DiscoverFragmentUIState.Initial)
    val uiState: StateFlow<DiscoverFragmentUIState> = _uiState
    private lateinit var coinList: ArrayList<CoinItem>
    var isLoading = MutableLiveData<Boolean>()

    fun onRefresh() {
        _uiState.value = DiscoverFragmentUIState.Loading
        isLoading.value = true
        getCoinList()
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
}


sealed class DiscoverFragmentUIState {
    object Initial : DiscoverFragmentUIState()
    object Loading : DiscoverFragmentUIState()
    data class Navigating(var destinationId: Int, var bundle: Bundle) : DiscoverFragmentUIState()
    data class Success(var discoverData: ArrayList<CoinItem>) : DiscoverFragmentUIState()
    data class Error(val message: String) : DiscoverFragmentUIState()
}