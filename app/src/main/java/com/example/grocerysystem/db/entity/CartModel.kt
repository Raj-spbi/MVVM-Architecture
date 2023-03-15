package com.example.grocerysystem.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CartModel")
data class CartModel(
    @PrimaryKey(autoGenerate = true)
    var auto_id: Int = 0,
    var id: String? = null,
    var userId: String? = null,
    var title: String? = null,
    var description: String? = null,
    var price: String? = null,
    var category: String? = null,
    var image: String? = null,
    var count: Int = 0
)