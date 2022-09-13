package com.example.grocerysystem.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.model.RegisterRequest
import com.example.grocerysystem.util.FirebaseUtil
import javax.inject.Inject

class RegistrationRepository {
    @Inject
    lateinit var firebaseUtil: FirebaseUtil
    private val _userResponseLiveData = MutableLiveData<NetworkResult<String>>()
    val userResponseLiveData: LiveData<NetworkResult<String>>
        get() = _userResponseLiveData


    suspend fun register(userRequest: RegisterRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        firebaseUtil.firebaseAuth.createUserWithEmailAndPassword(
            userRequest.email!!,
            userRequest.password!!
        )
            .addOnCompleteListener {
                val key: String =
                    firebaseUtil.database.child("RegisteredUsers").push().key.toString()
                userRequest.firebaseUserId = key
                firebaseUtil.database.child("RegisteredUsers")
                    .child(firebaseUtil.firebaseAuth.currentUser!!.uid)
                    .setValue(userRequest).addOnSuccessListener {
                        _userResponseLiveData.postValue(NetworkResult.Success("Registered Successfully"))
                    }.addOnFailureListener {
                        _userResponseLiveData.postValue(NetworkResult.Error("Something went Wrong"))
                    }
            }.addOnFailureListener {
                _userResponseLiveData.postValue(NetworkResult.Error("Registration Failed"))
            }
    }

}