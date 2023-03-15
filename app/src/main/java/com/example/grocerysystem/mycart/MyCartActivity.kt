package com.example.grocerysystem.mycart

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.R
import com.example.grocerysystem.databinding.ActivityMyCartBinding
import com.example.grocerysystem.db.AppDBViewModel
import com.example.grocerysystem.util.Helper
import com.example.grocerysystem.util.UserManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyCartActivity : AppCompatActivity() {

    private var _binding: ActivityMyCartBinding? = null
    private val binding get() = _binding!!
    private var _context: Context? = null
    private val context get() = _context!!

    lateinit var userManager: UserManager

    private val cartViewModel: AppDBViewModel by viewModels<AppDBViewModel>()
    private var userId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_cart)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_my_cart)
        _context = this
        userManager = UserManager(context)
        userManager.userIdFlow.asLiveData().observe(this) {
            userId = it.toString()
            cartViewModel.getAllCartItems(userId)
        }


        bindObservables()
    }

    private fun bindObservables() {
        cartViewModel.cartListLiveData.observe(this, Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    Log.e("asdfghjkl", it.data.toString() + " Activity")
                    val asd: StringBuilder = java.lang.StringBuilder()
                    if (it.data != null && it.data.isNotEmpty()) {
                        for (num in it.data) {
                            asd.append(num.title + " x " + num.count + " \n")
                        }
                        binding.tvCartItems.text = asd

                    } else {
                        binding.tvCartItems.text = "No record found in your cart"

                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    Helper.showLoadingDialog(context)
                }
            }

        })
    }
}