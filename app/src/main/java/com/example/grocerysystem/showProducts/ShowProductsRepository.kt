package com.example.grocerysystem.showProducts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.model.Items
import com.example.grocerysystem.util.FirebaseUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject


class ShowProductsRepository @Inject constructor(private val firebaseUtil: FirebaseUtil) {


    private val _readResponseLiveData = MutableLiveData<NetworkResult<List<Items>>>()
    val readResponseLiveData get() = _readResponseLiveData

    suspend fun readDataFromFirebase() {
        _readResponseLiveData.postValue(NetworkResult.Loading())
        firebaseUtil.database.child("Products").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: MutableList<Items> = mutableListOf<Items>()
                for (snap in snapshot.children) {
                    list.add(snap.getValue<Items>()!!)
                }
                _readResponseLiveData.postValue(NetworkResult.Success(list))
            }

            override fun onCancelled(error: DatabaseError) {
                _readResponseLiveData.postValue(NetworkResult.Error("Failed to read value"))
            }
        })
    }

}