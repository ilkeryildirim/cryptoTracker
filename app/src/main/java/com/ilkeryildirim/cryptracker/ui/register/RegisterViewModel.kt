package com.ilkeryildirim.cryptracker.ui.register

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.ilkeryildirim.cryptracker.R
import com.ilkeryildirim.cryptracker.utils.isValidEmail
import com.ilkeryildirim.cryptracker.utils.isValidPassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {


    private val _uiState = MutableStateFlow<RegisterFragmentUIState>(RegisterFragmentUIState.Initial)
    val uiState: StateFlow<RegisterFragmentUIState> = _uiState

    var mail: String = ""
    var password: String = ""
    var passwordValidation = ""


    fun afterMailChange(s: CharSequence) {
        mail = s.toString()
    }

    fun afterPasswordChange(s: CharSequence) {
        password = s.toString()
    }

    fun afterPasswordValidation(s: CharSequence){
       passwordValidation = s.toString()
    }

    fun register(){
       if(checkPasswordsIsValid() && checkEmailIsValid(mail)){
           viewModelScope.launch {
               _uiState.value = RegisterFragmentUIState.Loading
               firebaseAuth.createUserWithEmailAndPassword(mail, password)
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                         _uiState.value = RegisterFragmentUIState.RegisterSuccess
                       } else {
                          _uiState.value = RegisterFragmentUIState.Error(task.exception?.localizedMessage ?: "An Error Occurred")
                       }
                   }
           }
       }

    }

    fun navigateToLogin(){
        _uiState.value = RegisterFragmentUIState.Navigation(R.id.action_registerFragment_to_loginFragment)
    }
    private fun checkPasswordsIsValid():Boolean{
        return password.isValidPassword() && password == passwordValidation
    }

    private fun checkEmailIsValid(email:String):Boolean{
        return email.isValidEmail()
    }
}


sealed class RegisterFragmentUIState {
    object Initial : RegisterFragmentUIState()
    object Loading : RegisterFragmentUIState()
    object RegisterSuccess : RegisterFragmentUIState()
    data class Navigation(val destinationId:Int,val bundle:Bundle?=null): RegisterFragmentUIState()
    data class Error(val message: String) : RegisterFragmentUIState()
}