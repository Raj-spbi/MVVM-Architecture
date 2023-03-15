package com.example.grocerysystem.login

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerysystem.util.Helper
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.model.LoginRequest
import com.example.grocerysystem.model.RegisterRequest
import com.example.grocerysystem.registration.RegistrationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    val loginLiveData: LiveData<NetworkResult<RegisterRequest>>
        get() = loginRepository.loginResponseLiveData

    fun loginUser(userRequest: LoginRequest) {
        viewModelScope.launch {
            loginRepository.login(userRequest)
        }
    }

    fun validateCredentialsLogin(
        emailAddress: String, password: String
    ): Pair<Boolean, String> {
        var result = Pair(true, "")
        if (TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Helper.isValidEmail(emailAddress)) {
            result = Pair(false, "Email is invalid")
        } else if (TextUtils.isEmpty(password) && password.length <= 5) {
            result = Pair(false, "Password should not be empty!")
        }
        return result
    }
}