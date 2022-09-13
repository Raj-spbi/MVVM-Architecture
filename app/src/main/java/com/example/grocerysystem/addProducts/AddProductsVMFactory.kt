package com.example.grocerysystem.addProducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.security.InvalidParameterException

class AddProductsVMFactory(val userRepository: AddProductRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddProductsViewModel::class.java)) {
            return AddProductsViewModel(userRepository) as T
        }
        throw InvalidParameterException("Unable to construct AddProductsViewModel")
    }

}
