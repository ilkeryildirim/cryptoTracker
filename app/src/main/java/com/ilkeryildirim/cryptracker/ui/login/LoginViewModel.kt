package com.ilkeryildirim.cryptracker.ui.login

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ilkeryildirim.cryptracker.R
import com.ilkeryildirim.cryptracker.ui.register.RegisterFragmentUIState
import com.ilkeryildirim.cryptracker.utils.isValidEmail
import com.ilkeryildirim.cryptracker.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {


    private val _uiState = MutableStateFlow<LoginFragmentUIState>(LoginFragmentUIState.Initial)
    val uiState: StateFlow<LoginFragmentUIState> = _uiState

    var mail: String = ""
    var password: String = ""


    fun afterMailChange(s: CharSequence) {
        mail = s.toString()
    }

    fun afterPasswordChange(s: CharSequence) {
        password = s.toString()
    }

    fun login(){
       if(checkPasswordsIsValid() && checkEmailIsValid(mail)){
           viewModelScope.launch {
               _uiState.value = LoginFragmentUIState.Loading
               firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
                   if (task.isSuccessful) {
                       _uiState.value = LoginFragmentUIState.LoginSuccess
                   } else {
                     _uiState.value = LoginFragmentUIState.Error(task.exception?.localizedMessage ?: "An Error Occurred")
                   }
               }
           }
       }

    }

    private fun checkPasswordsIsValid():Boolean{
        return password.isValidPassword()
    }

    private fun checkEmailIsValid(email:String):Boolean{
        return email.isValidEmail()
    }

    fun navigateToRegister(){
        _uiState.value = LoginFragmentUIState.Navigation(R.id.action_loginFragment_to_registerFragment)
    }
}


sealed class LoginFragmentUIState {
    object Initial : LoginFragmentUIState()
    object Loading : LoginFragmentUIState()
    object LoginSuccess : LoginFragmentUIState()
    data class Navigation(val destinationId:Int,val bundle: Bundle?=null): LoginFragmentUIState()
    data class Error(val message: String) : LoginFragmentUIState()
}