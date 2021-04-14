package com.ilkeryildirim.cryptracker.ui.coindetail

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ilkeryildirim.cryptracker.api.CryptrackerApiResult
import com.ilkeryildirim.cryptracker.api.model.coin.CoinDetail
import com.ilkeryildirim.cryptracker.ui.coins.CoinsDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CoinDetailViewModel @Inject constructor(
        private val coinsDataRepository: CoinsDataRepository,
        private val firebaseAuth: FirebaseAuth,
        private val firebaseFirestore: FirebaseFirestore
) : ViewModel() {


    private val _uiState = MutableStateFlow<CoinDetailFragmentUIState>(CoinDetailFragmentUIState.Initial)
    val uiState: StateFlow<CoinDetailFragmentUIState> = _uiState
    var isLoading = MutableLiveData<Boolean?>()
    private lateinit var coinId: String
    private val coinName = MutableLiveData<String>()
    private val coinImage = MutableLiveData<String>()
    private val coinSymbol = MutableLiveData<String>()
    private val coinDescription = MutableLiveData<String>()
    val isFavourite = MutableLiveData<Boolean>()
    fun onRefresh() {
        _uiState.value = CoinDetailFragmentUIState.Loading
        isLoading.value = true
        getCoinDetail(coinId)
    }

    fun getCoinDetail(id: String) {
        coinId = id
        checkIsFav()
        isLoading.value = true
        _uiState.value = CoinDetailFragmentUIState.Loading
        viewModelScope.launch {
            coinsDataRepository.getCoinDetail(id).let { result ->
                when (result) {
                    is CryptrackerApiResult.Success -> {
                        isLoading.value = false
                        result.data.apply {
                            coinName.value = name
                            coinImage.value = image?.thumb
                            coinSymbol.value = symbol
                            coinDescription.value = description.toString()
                        }

                        _uiState.value = CoinDetailFragmentUIState.Success(result.data)
                    }
                    is CryptrackerApiResult.Error -> {
                        result.message?.let {
                            _uiState.value = CoinDetailFragmentUIState.Error(result.message)
                        }
                    }
                }
            }
        }
    }


    fun getCoinName() = coinName

    fun getCoinDescription() = coinDescription

    fun getCoinSymbol() = coinSymbol

    fun getCoinImage() = coinImage

   private fun checkIsFav() {
        val docRef = firebaseFirestore.collection(firebaseAuth.currentUser!!.uid).document(coinId)
        docRef.get().addOnSuccessListener {
           if(it.data.isNullOrEmpty())  isFavourite.postValue(false) else isFavourite.postValue(true)
        }.addOnFailureListener {
            isFavourite.postValue(false)
            _uiState.value = CoinDetailFragmentUIState.Error(it.localizedMessage.toString())
        }
    }

  private fun removeFromFav() {
       val docRef = firebaseFirestore.collection(firebaseAuth.currentUser!!.uid).document(coinId)
        docRef.delete()
                .addOnSuccessListener {
                    isFavourite.postValue(false)
                    Log.d("removeFromFav", "$coinId removed from fav ", )
                }
                .addOnFailureListener { exception ->
                    Log.d("removeFromFav", "$coinId cant removed from fav ", )
                }
    }

   private fun addToFav() {
        val docRef = firebaseFirestore.collection(firebaseAuth.currentUser!!.uid).document(coinId)
        val data = hashMapOf(
                "id" to coinId,
        )
        docRef.set(data).addOnSuccessListener {
            isFavourite.value=true
            Log.d("addToFav", "$coinId added to fav ")
        }.addOnFailureListener {
            Log.d("addToFav", "$coinId cant add to fav ")
        }
    }

     fun changeFavStatusClick() {
         isFavourite.value?.let {
             if (isFavourite.value!!) {
                 removeFromFav()
             } else {
                 addToFav()
             }
         }

    }

}

sealed class CoinDetailFragmentUIState {
    object Initial : CoinDetailFragmentUIState()
    object Loading : CoinDetailFragmentUIState()
    data class Navigating(var destinationId: Int, var bundle: Bundle) : CoinDetailFragmentUIState()
    data class Success(var coinDetail: CoinDetail) : CoinDetailFragmentUIState()
    data class Error(val message: String) : CoinDetailFragmentUIState()
}