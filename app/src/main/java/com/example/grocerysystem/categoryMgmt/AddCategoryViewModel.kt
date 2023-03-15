package com.example.grocerysystem.categoryMgmt

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.model.CategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(private val repository: AddCategoryRepository) :
    ViewModel() {

    val userResponseLiveData: LiveData<NetworkResult<String>>
        get() = repository.responseLiveDta

    fun insertCategory(title: String) {
        viewModelScope.launch {
            repository.addCategory(title)
        }
    }

    val showCategoryLiveData: LiveData<NetworkResult<List<CategoryItem>>>
        get() = repository.responseCategoryLiveDta



    fun readCategories() {
        viewModelScope.launch {
            repository.showAllCategories()
        }
    }



    val deleteCategoryLiveData: LiveData<NetworkResult<String>>
        get() = repository.responseLiveDataDelete


    fun deleteCat(categoryItem: CategoryItem) {
        viewModelScope.launch {
            repository.deleteCategory(categoryItem)
        }
    }
}