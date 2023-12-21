package com.example.grocerysystem.categoryMgmt

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.R
import com.example.grocerysystem.databinding.ActivityShowCategoriesBinding
import com.example.grocerysystem.model.CategoryItem
import com.example.grocerysystem.util.Helper
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShowCategoriesActivity : AppCompatActivity() {

    private var _binding: ActivityShowCategoriesBinding? = null
    private val binding get() = _binding!!
    private var _context: Context? = null
    private val context get() = _context!!

    private val addCategoryViewModel: AddCategoryViewModel by viewModels<AddCategoryViewModel>()
    lateinit var adapterRecycler: AdapterShowCategory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = DataBindingUtil.setContentView(this, R.layout.activity_show_categories)
        _context = this

        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        showCategoryLists()
        observeCategoryAddition()
        obserDeletedOrNot()


        binding.btnAddCat.setOnClickListener {
         openDialog();
        }
    }

    private fun openDialog() {
        val dialog = BottomSheetDialog(context)
        val view = layoutInflater.inflate(R.layout.category_layout_bottom_sheet, null)
        val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
        val etCategory = view.findViewById<EditText>(R.id.title)
        val cancelDialog = view.findViewById<ImageView>(R.id.cancelDialog)
        btnClose.setOnClickListener {
            if (etCategory.text.toString().isBlank()) {
                Helper.showToast(context, "Category can't be empty")
            } else {
                val cat = etCategory.text.toString()
                addCategoryViewModel.insertCategory(cat)
                dialog.dismiss()

            }

        }
        cancelDialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun observeCategoryAddition() {
        addCategoryViewModel.userResponseLiveData.observe(this, Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    Log.e("asdfghjkl", it.data.toString() + " Activity")
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

    private fun showCategoryLists() {
        addCategoryViewModel.readCategories()
        observeAlreadyAddedCategories()
    }

    private fun observeAlreadyAddedCategories() {
        addCategoryViewModel.showCategoryLiveData.observe(this, Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    adapterRecycler =
                        AdapterShowCategory(it.data as ArrayList<CategoryItem>, ::onDeleteClick)
                    binding.recyclerView.adapter = adapterRecycler
                    adapterRecycler.notifyDataSetChanged()

                    if (it.data.isNotEmpty()) {
                        binding.noRecordExists.visibility = View.GONE
                    } else {
                        binding.noRecordExists.visibility = View.VISIBLE
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

    private fun onDeleteClick(categoryItem: CategoryItem) {
        addCategoryViewModel.deleteCat(categoryItem)
    }

    private fun obserDeletedOrNot() {
        addCategoryViewModel.deleteCategoryLiveData.observe(this, Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    Log.e("asdfghjkl", it.data.toString() + " Activity")
//                    Toast.makeText(context, it.data.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    Helper.showLoadingDialog(context)
                }
            }
        })
//        deleteCategoryLiveData
    }
}