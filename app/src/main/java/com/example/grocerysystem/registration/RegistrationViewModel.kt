package com.example.grocerysystem.registration

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerysystem.Helper
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.model.RegisterRequest
import kotlinx.coroutines.launch

class RegistrationViewModel(private val registrationRepository: RegistrationRepository) :
    ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<String>>
        get() = registrationRepository.userResponseLiveData

    fun registerUser(userRequest: RegisterRequest) {
        viewModelScope.launch {
            registrationRepository.register(userRequest)
        }
    }

    fun validateCredentials(
        userName: String, emailAddress: String, password: String, cnfPass: String
    ): Pair<Boolean, String> {

        var result = Pair(true, "")
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Helper.isValidEmail(emailAddress)) {
            result = Pair(false, "Email is invalid")
        } else if (TextUtils.isEmpty(password) && password.length <= 5) {
            result = Pair(false, "Password should not be empty!")
        }
        return result
    }

}