package com.example.grocerysystem.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.security.InvalidParameterException


class LoginVMFactory(private val loginRepository: LoginRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(loginRepository) as T
        }
        throw InvalidParameterException("Unable to construct LoginViewModel")
    }

}