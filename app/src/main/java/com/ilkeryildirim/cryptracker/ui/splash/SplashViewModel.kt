package com.ilkeryildirim.cryptracker.ui.splash


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.ilkeryildirim.cryptracker.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {

    val destination = MutableLiveData<Int>()

    init {
        if(firebaseAuth.currentUser==null) destination.postValue(R.id.action_splashFragment_to_loginFragment) else  destination.postValue(R.id.action_splashFragment_to_coinsFragment)
    }


}
