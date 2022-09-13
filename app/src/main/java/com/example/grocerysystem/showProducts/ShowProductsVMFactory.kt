package com.example.grocerysystem.showProducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import java.security.InvalidParameterException

class ShowProductsVMFactory(val userRepository: ShowProductsRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowProductsViewModel::class.java)) {
            return ShowProductsViewModel(userRepository) as T
        }
        throw InvalidParameterException("Unable to construct AddProductsViewModel")
    }

}