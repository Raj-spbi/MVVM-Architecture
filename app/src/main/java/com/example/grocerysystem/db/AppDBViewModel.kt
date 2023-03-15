package com.example.grocerysystem.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.db.entity.CartModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
/*class RegistrationViewModel @Inject constructor(private val registrationRepository: RegistrationRepository) :
    ViewModel() {*/
class AppDBViewModel @Inject constructor(private val appRepository: AppDBRepository) : ViewModel() {

    val cartLiveData: LiveData<NetworkResult<CartModel>>
        get() = appRepository.loginResponseLiveData

    val cartListLiveData: LiveData<NetworkResult<List<CartModel>>>
        get() = appRepository.cartListLiveData


    fun insertIntoCart(cartModel: CartModel) {
        viewModelScope.launch {
            appRepository.insert(cartModel)
        }
    }

    fun getFromCart(productId: String) {
        viewModelScope.launch {
            appRepository.getItemDetailById(productId)
        }
    }

    fun updateCart(productId: String, count: String) {
        viewModelScope.launch {
            appRepository.updateCart(productId, count)
        }
    }

    fun getAllCartItems(userId: String) {
        viewModelScope.launch {
            appRepository.getAllVocs(userId)
        }

    }

    fun deleteItemFromCart(userId: String, productId: String) {
        viewModelScope.launch {
            appRepository.delete(userId,productId)
        }
    }

}