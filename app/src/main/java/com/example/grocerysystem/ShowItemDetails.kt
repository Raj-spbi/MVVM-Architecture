package com.example.grocerysystem

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.grocerysystem.databinding.ActivityShowItemDetailsBinding
import com.example.grocerysystem.db.AppDBViewModel
import com.example.grocerysystem.db.entity.CartModel
import com.example.grocerysystem.model.Items
import com.example.grocerysystem.util.Helper
import com.example.grocerysystem.util.UserManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowItemDetails : AppCompatActivity() {
    private var _binding: ActivityShowItemDetailsBinding? = null
    private val binding get() = _binding!!
    private var _context: Context? = null
    private val context get() = _context!!

    lateinit var userManager: UserManager
    lateinit var itemDetails: Items
    private val cartViewModel: AppDBViewModel by viewModels<AppDBViewModel>()
    lateinit var cartModelLocal: CartModel
    private var userid = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_show_item_details)
        _context = this
        userManager = UserManager(context)

        itemDetails = intent.getSerializableExtra("itemDetails") as Items
        cartModelLocal = CartModel()
        userManager.userIdFlow.asLiveData().observe(this) {
            userid = it.toString()
            cartModelLocal.userId = userid
        }
        cartModelLocal.id = itemDetails.id
        cartModelLocal.category = itemDetails.category
        cartModelLocal.description = itemDetails.description
        cartModelLocal.image = itemDetails.image
        cartModelLocal.price = itemDetails.price
        cartModelLocal.title = itemDetails.title
        cartModelLocal.count = 1
        cartModelLocal.userId = userid
        binding.title.text = cartModelLocal.title
        binding.description.text = cartModelLocal.description
        binding.amt.text = "Rs. ${cartModelLocal.price}"
        binding.countTxt.text = cartModelLocal.count.toString()


        cartViewModel.getFromCart(itemDetails.id!!)
        bindObservables()

        binding.addToCart.setOnClickListener {
            binding.addToCart.visibility = View.GONE
            binding.cvrAddSub.visibility = View.VISIBLE
//            val cartModel = getCartModel(cartModelLocal)
            cartViewModel.insertIntoCart(cartModelLocal)
            Helper.showToast(context, "Added into Cart")
        }

        binding.subBtn.setOnClickListener {
            if (binding.countTxt.text.toString().toInt() <= 1) {
                binding.cvrAddSub.visibility = View.GONE
                binding.addToCart.visibility = View.VISIBLE
                cartViewModel.deleteItemFromCart(userid, cartModelLocal.id.toString())
            } else {
                var num: Int = binding.countTxt.text.toString().toInt()
                num = num.minus(1)
                cartModelLocal.id?.let { it1 -> cartViewModel.updateCart(it1, num.toString()) }
            }
        }

        binding.addBtn.setOnClickListener {
            var num: Int = binding.countTxt.text.toString().toInt()
            num = num.plus(1)
            cartModelLocal.id?.let { it1 -> cartViewModel.updateCart(it1, num.toString()) }
        }


    }

    private fun bindObservables() {
        cartViewModel.cartLiveData.observe(this, Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    if (it.data != null) {
                        cartModelLocal = it.data
                        binding.addToCart.visibility = View.GONE
                        binding.cvrAddSub.visibility = View.VISIBLE
                        binding.countTxt.text = cartModelLocal!!.count.toString()
                    }
                }
                is NetworkResult.Error -> {
                    Log.wtf("error: ", it.message.toString())
//                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    Helper.showLoadingDialog(context)
                }
            }
        })
    }

    private fun getCartModel(item: CartModel): CartModel {
        val cartModel = CartModel()
        cartModel.id = item.id
        cartModel.category = item.category
        cartModel.description = item.description
        cartModel.image = item.image
        cartModel.price = item.price
        cartModel.title = item.title
        cartModel.count = 1
        userManager.userIdFlow.asLiveData().observe(this) {
            cartModel.userId = it.toString()
        }
        return cartModel
    }
}