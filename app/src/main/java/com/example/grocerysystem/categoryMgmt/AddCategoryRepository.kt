package com.example.grocerysystem.categoryMgmt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.model.CategoryItem
import com.example.grocerysystem.util.Const
import com.example.grocerysystem.util.FirebaseUtil
import com.example.grocerysystem.util.Helper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class AddCategoryRepository @Inject constructor(private val firebaseUtil: FirebaseUtil) {
    private val _responseLiveData = MutableLiveData<NetworkResult<String>>()
    val responseLiveDta: LiveData<NetworkResult<String>>
        get() = _responseLiveData

    fun addCategory(title: String) {
        _responseLiveData.postValue(NetworkResult.Loading())
        val cat = CategoryItem(firebaseUtil.database.child(Const.CATEGORY).push().key, title)

        firebaseUtil.database.child(Const.CATEGORY).push().setValue(cat).addOnSuccessListener {
            Log.e("asdfghjkl", "Added Successfully Repo" )

            _responseLiveData.postValue(NetworkResult.Success("Added Successfully"))
        }.addOnFailureListener {
            Helper.hideLoadingDialog()
            _responseLiveData.postValue(NetworkResult.Error("Couldn't add"))
        }
    }


    private val _responseCategoryLiveData = MutableLiveData<NetworkResult<List<CategoryItem>>>()
    val responseCategoryLiveDta: LiveData<NetworkResult<List<CategoryItem>>>
        get() = _responseCategoryLiveData

    fun showAllCategories() {
        _responseCategoryLiveData.postValue(NetworkResult.Loading())
        firebaseUtil.database.child(Const.CATEGORY)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list: MutableList<CategoryItem> = mutableListOf<CategoryItem>()
                    if (snapshot.childrenCount > 0) {
                        for (snap in snapshot.children) {
                            val catItem1 = snap.getValue<CategoryItem>()
                            catItem1?.id = snap.key
                            catItem1?.let { list.add(it) }
                        }
                        _responseCategoryLiveData.postValue(NetworkResult.Success(list))
                    } else {
                        // supposing last item is deleted and so passing empty list
                        _responseCategoryLiveData.postValue(NetworkResult.Success(list))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _responseCategoryLiveData.postValue(NetworkResult.Error("Failed to read value"))
                }
            })
    }


    private val _responseLiveDataDelete = MutableLiveData<NetworkResult<String>>()
    val responseLiveDataDelete: LiveData<NetworkResult<String>>
        get() = _responseLiveDataDelete

    fun deleteCategory(categoryItem: CategoryItem) {
        _responseLiveDataDelete.postValue(NetworkResult.Loading())
        firebaseUtil.database.child(Const.CATEGORY).child(categoryItem.id.toString()).removeValue()
            .addOnSuccessListener {
                Log.e("asdfghjkl", "Deleted Successfully Repo" )

                _responseLiveDataDelete.postValue(NetworkResult.Success("Deleted Successfully"))
            }.addOnFailureListener {
                Helper.hideLoadingDialog()
                _responseLiveDataDelete.postValue(NetworkResult.Error("Something went Wrong"))
            }
    }
}