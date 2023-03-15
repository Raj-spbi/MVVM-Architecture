package com.example.grocerysystem.db.dao

import androidx.room.*
import com.example.grocerysystem.db.entity.CartModel

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartModel: CartModel)


    @Query("SELECT * FROM CartModel WHERE id = :productId")
    suspend fun getItemDetailByPid(productId: String): CartModel


    /*   @Query("SELECT * FROM CartModel")
       suspend fun getAll(): List<CartModel>

       @Insert
       suspend fun insertAll(Courses: List<CartModel>)

   */
    @Query("DELETE FROM CartModel WHERE id = :productId AND userId=:userId")
    suspend fun delete(userId: String, productId: String)

    @Query("UPDATE CartModel SET count = :count WHERE id=:productId")
    suspend fun updateCart(productId: String, count: String)


    @Query("SELECT * FROM CartModel where userId=:userId")
    suspend fun getVocList(userId: String): List<CartModel>
}