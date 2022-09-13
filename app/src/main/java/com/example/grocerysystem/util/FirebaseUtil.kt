package com.example.grocerysystem.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class FirebaseUtil @Inject constructor() {
    val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    val database: DatabaseReference by lazy {
        Firebase.database.reference
    }
    val firebaseStore: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    val storageReference: StorageReference by lazy {
        FirebaseStorage.getInstance().reference
    }
}