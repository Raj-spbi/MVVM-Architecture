package com.example.grocerysystem.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.security.InvalidParameterException


class RegisterVMFactory(private val registrationRepository: RegistrationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
            return RegistrationViewModel(registrationRepository) as T
        }
        throw InvalidParameterException("Unable to construct RegistrationViewModel")
    }

}