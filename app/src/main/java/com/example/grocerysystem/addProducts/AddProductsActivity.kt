package com.example.grocerysystem.addProducts

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.grocerysystem.NetworkResult
import com.example.grocerysystem.R
import com.example.grocerysystem.categoryMgmt.AddCategoryViewModel
import com.example.grocerysystem.databinding.ActivityAddProductsAcctivityBinding
import com.example.grocerysystem.util.Helper
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


@AndroidEntryPoint
class AddProductsActivity : AppCompatActivity() {


    private val PICK_IMAGE_REQUEST: Int = 1011
    private var _binding: ActivityAddProductsAcctivityBinding? = null
    private val binding get() = _binding!!
    private var _context: Context? = null
    private val context get() = _context!!
    private var filePath: Uri? = null
    private var title: String? = null
    private var description: String? = null
    private var price: String? = null
    private var category: String? = null
    private val addProductsViewModel: AddProductsViewModel by viewModels<AddProductsViewModel>()
    private val showCategoryViewModel: AddCategoryViewModel by viewModels<AddCategoryViewModel>()
    private var selectedChip = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_add_products_acctivity)
        _context = this

        /*  addProductsViewModel =
              ViewModelProvider(this, AddProductsVMFactory(AddProductRepository())).get(
                  AddProductsViewModel::class.java
              )
  */


        binding.addProduct.setOnClickListener {
            title = binding.title.text.toString()
            description = binding.description.text.toString()
            price = binding.priceTxt.text.toString()
//            category = binding.categoryTxt.text.toString()

            val validationType: Pair<Boolean, String> =
                addProductsViewModel.validateDataEnteredByUser(
                    title!!,
                    description!!, price!!, selectedChip, filePath
                )
            if (validationType.first) {
                addProductsViewModel.updateImageAndRecords(
                    title!!, description!!, price!!, selectedChip, filePath
                )
            } else {
                Toast.makeText(context, validationType.second, Toast.LENGTH_SHORT).show()
            }

        }

        bindingObservers()
        //load Category
        getCategories()
    }


    private fun bindingObservers() {
        addProductsViewModel.userResponseLiveData.observe(this, androidx.lifecycle.Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    Toast.makeText(context, it.data.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(context, it.data.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    Helper.showLoadingDialog(context)
//                    binding.progressBar.isVisible = true
                }
            }
        })
    }


    fun browseGallery(view: View) {
        SelectImage()
        /*  val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
          requestCode = PIC_ID
          resultLauncher.launch(cameraIntent)*/

    }


    private fun SelectImage() {

        // Defining Implicit Intent to mobile gallery
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }


    // Override onActivityResult method
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            filePath = data.data
            Log.e("ssdfgh", "onActivityResult:$filePath ")
            try {

                // Setting image on image view using Bitmap
                val bitmap = MediaStore.Images.Media
                    .getBitmap(
                        contentResolver, filePath
                    )
                binding.imageToUpload.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
    }

    private fun createChip(label: String, id: String): Chip {
        val chip = Chip(
            this,
            null,
            com.google.android.material.R.style.Widget_MaterialComponents_Chip_Entry
        )
        chip.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        chip.text = label
//        chip.isCloseIconVisible = true
        chip.isChipIconVisible = true
        chip.isCheckable = true
        chip.isClickable = true
//        chip.id=id.toInt()
//        chip.setOnCloseIconClickListener {
//            Toast.makeText(this, "Chip deleted successfully", Toast.LENGTH_SHORT).show()
//        }
        return chip
    }

    private fun getCategories() {
        showCategoryViewModel.readCategories()
        showCategoryViewModel.showCategoryLiveData.observe(this, Observer {
            Helper.hideLoadingDialog()
            when (it) {
                is NetworkResult.Success -> {
                    val chiGu: ChipGroup = binding.chipGroupFilter

                    if (it.data!!.isNotEmpty()) {
//                        binding.categoryTxt.isVisible = true

                        chiGu.isSingleSelection = true
                        chiGu.isSingleLine = false

                        for (i in it.data) {
                            chiGu.addView(i.title?.let { it1 ->
                                i.id?.let { it2 ->
                                    createChip(
                                        it1,
                                        it2
                                    )
                                }
                            })

                        }

                        chiGu.setOnCheckedChangeListener { group, checkedId ->
                            val chip: Chip? = group.findViewById(checkedId)
                            selectedChip = chip?.text.toString()
                        }
                    } else {
                        chiGu.visibility = View.GONE
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