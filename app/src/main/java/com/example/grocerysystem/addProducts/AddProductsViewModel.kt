package com.example.grocerysystem.addProducts

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerysystem.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductsViewModel @Inject constructor(private val addProductRepository: AddProductRepository) : ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<String>>
        get() = addProductRepository.responseLiveDta


    fun updateImageAndRecords(
        title: String,
        description: String,
        price: String,
        category: String,
        filePath: Uri?
    ) {
        viewModelScope.launch {
            addProductRepository.uploadImageAndData(title, description, price, category, filePath)
        }
    }

    fun validateDataEnteredByUser(
        title: String,
        description: String,
        price: String,
        category: String,
        filePath: Uri?
    ): Pair<Boolean, String> {

        var result = Pair(true, "")
        if (title.isNotBlank()) {
            if (description.isNotBlank()) {
                if (price.isNotBlank()) {
                    if (category.isNotBlank()) {
                        if (filePath.toString().isNotBlank()) {
                            //Here I'm not pasing anything so that Pair can be true
                        } else {
                            result = Pair(false, "Select Image")

                        }
                    } else {
                        result = Pair(false, "Enter Category")

                    }
                } else {
                    result = Pair(false, "Enter Price")
                }
            } else {
                result = Pair(false, "Enter Description")
            }
        } else {
            result = Pair(false, "Enter title")
        }
        return result;
    }
}