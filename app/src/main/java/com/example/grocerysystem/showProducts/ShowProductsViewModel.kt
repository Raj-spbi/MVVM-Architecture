package com.example.grocerysystem.showProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.model.Items
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowProductsViewModel @Inject constructor(private val showProductsRepository: ShowProductsRepository) :
    ViewModel() {

    val readLiveData: LiveData<NetworkResult<List<Items>>>
        get() = showProductsRepository.readResponseLiveData


    fun readAllUsers() {
        viewModelScope.launch {
            showProductsRepository.readDataFromFirebase()
        }
    }

}