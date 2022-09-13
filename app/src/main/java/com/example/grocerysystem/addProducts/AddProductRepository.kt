package com.example.grocerysystem.addProducts

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.grocerysystem.Helper
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.model.Items
import com.example.grocerysystem.util.FirebaseUtil
import java.util.*
import javax.inject.Inject

class AddProductRepository @Inject constructor(private val firebaseUtil: FirebaseUtil) {

    private val _responseLiveData = MutableLiveData<NetworkResult<String>>()
    val responseLiveDta: LiveData<NetworkResult<String>>
        get() = _responseLiveData

    fun uploadImageAndData(
        title: String,
        description: String,
        price: String,
        category: String,
        filePath: Uri?
    ) {
        _responseLiveData.postValue(NetworkResult.Loading())
        val ref = firebaseUtil.storageReference.child("myImages/" + UUID.randomUUID().toString())
        val uploadTask = ref.putFile(filePath!!)
        uploadTask.addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                val items =
                    Items(
                        firebaseUtil.database.child("Products").push().key,
                        title,
                        description,
                        price,
                        category,
                        it.toString()
                    )
                firebaseUtil.database.child("Products").push().setValue(items)
                    .addOnSuccessListener {
                        _responseLiveData.postValue(NetworkResult.Success("Uploaded Successfully"))
                    }.addOnFailureListener {
                        _responseLiveData.postValue(NetworkResult.Error("Something went Wrong"))

                    }
            }.addOnFailureListener {
                _responseLiveData.postValue(NetworkResult.Error("Something went wrong in image upload"))

            }
        }.addOnFailureListener {
            Helper.hideLoadingDialog()
            _responseLiveData.postValue(NetworkResult.Error("Couldn't upload image"))

        }


    }
}