package com.example.grocerysystem.db

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.db.dao.CartDao
import com.example.grocerysystem.db.database.AppDatabase
import com.example.grocerysystem.db.entity.CartModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class AppDBRepository @Inject constructor(application: Application) {

    private val _cartResponseLiveData = MutableLiveData<NetworkResult<CartModel>>()
    val loginResponseLiveData: LiveData<NetworkResult<CartModel>>
        get() = _cartResponseLiveData

    private val _cartListLiveData = MutableLiveData<NetworkResult<List<CartModel>>>()
    val cartListLiveData: LiveData<NetworkResult<List<CartModel>>>
        get() = _cartListLiveData


    private val cartDao: CartDao

    init {
        val db = AppDatabase.createInstance(application)
        cartDao = db.cartDao
    }

    suspend fun insert(voc: CartModel) {
        withContext(Dispatchers.IO)
        {
            cartDao.insert(voc)
        }
    }

    suspend fun delete(userId: String, productId: String) {
        withContext(Dispatchers.IO)
        {
            cartDao.delete(userId, productId)
        }
    }

    suspend fun getItemDetailById(vocId: String) {
        var cartModel: CartModel? = null
        withContext(Dispatchers.IO)
        {
            cartModel = cartDao.getItemDetailByPid(vocId)
        }
        if (cartModel != null) {
            _cartResponseLiveData.postValue(NetworkResult.Success(cartModel!!))
        } else {
            _cartResponseLiveData.postValue(NetworkResult.Error("No record exists"))
        }
    }

    suspend fun updateCart(productId: String, count: String) {

        withContext(Dispatchers.IO)
        {
            cartDao.updateCart(productId, count)
        }

        getItemDetailById(productId)

    }

    suspend fun getAllVocs(userId: String) {
        _cartListLiveData.postValue(NetworkResult.Loading())
        var cartItems: List<CartModel>? = null
        withContext(Dispatchers.IO)
        {
            cartItems = cartDao.getVocList(userId)
        }
        if (cartItems != null) {
            _cartListLiveData.postValue(NetworkResult.Success(cartItems!!))
        } else {
            _cartListLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}