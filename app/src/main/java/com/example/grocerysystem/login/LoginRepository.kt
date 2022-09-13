package com.example.grocerysystem.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.model.LoginRequest
import com.example.grocerysystem.model.RegisterRequest
import com.example.grocerysystem.util.FirebaseUtil
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class LoginRepository {

    @Inject lateinit var firebaseUtil: FirebaseUtil
    private val _loginResponseLiveData = MutableLiveData<NetworkResult<RegisterRequest>>()
    val loginResponseLiveData: LiveData<NetworkResult<RegisterRequest>>
        get() = _loginResponseLiveData


    suspend fun login(userRequest: LoginRequest) {
        _loginResponseLiveData.postValue(NetworkResult.Loading())
        firebaseUtil.firebaseAuth.signInWithEmailAndPassword(
            userRequest.email,
            userRequest.password
        )
            .addOnCompleteListener {
                val currentUser = firebaseUtil.firebaseAuth.currentUser
                firebaseUtil.database.child("RegisteredUsers")
                    .child(currentUser!!.uid)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val loginResponse = dataSnapshot.getValue<RegisterRequest>()
                            _loginResponseLiveData.postValue(NetworkResult.Success(loginResponse!!))
                        }

                        override fun onCancelled(error: DatabaseError) {
                            _loginResponseLiveData.postValue(NetworkResult.Error("Failed to read value"))
                        }
                    })
            }.addOnFailureListener {
                _loginResponseLiveData.postValue(NetworkResult.Error(it.message.toString()))
            }
    }

}