package com.example.grocerysystem.showProducts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.grocerysystem.Helper
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.R
import com.example.grocerysystem.addProducts.AddProductsActivity
import com.example.grocerysystem.databinding.ActivityAdminDashboardBinding
import com.example.grocerysystem.model.Items
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminDashboardActivity : AppCompatActivity() {
    private var _binding: ActivityAdminDashboardBinding? = null
    private val binding get() = _binding!!
    private var _context: Context? = null
    private val context get() = _context!!
    private val showProductsViewModel: ShowProductsViewModel by viewModels<ShowProductsViewModel>()

    lateinit var adapterRecycler: AdapterRecycler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_admin_dashboard)
        _context = this
//        showProductsViewModel =
//            ViewModelProvider(
//                this,
//                ShowProductsVMFactory(ShowProductsRepository())
//            ).get(
//                ShowProductsViewModel::class.java
//            )

        binding.itemViewModel = showProductsViewModel
        binding.showProductsRecycler.layoutManager = GridLayoutManager(this, 2)
        binding.lifecycleOwner = this
        binding.mContext = this

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddProductsActivity::class.java)
            startActivity(intent)
        }
        showProductsViewModel.readAllUsers()
        bindingObservables();


    }

    private fun bindingObservables() {
        showProductsViewModel.readLiveData.observe(this, androidx.lifecycle.Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    adapterRecycler =
                        AdapterRecycler(it.data as ArrayList<Items>)
                    binding.showProductsRecycler.adapter = adapterRecycler
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.data.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    Helper.showLoadingDialog(context)
                }
            }
        })
    }
}